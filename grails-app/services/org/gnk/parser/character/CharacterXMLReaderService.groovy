package org.gnk.parser.character

import org.gnk.parser.GNKDataContainerService
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Role

class CharacterXMLReaderService {

    /* Exposed Methods */

    def Character getCharacterFromNode(Node CHARACTER, GNKDataContainerService dataContainer) {
        Character characterRes = new Character()
        ReadCharacterRootNode(CHARACTER, characterRes)
        ReadRolesNode(CHARACTER, characterRes, dataContainer)
        return characterRes
    }
    /* !Exposed Methods */

    /* Construction Methods */

    private void ReadCharacterRootNode(Node CHARACTER, Character characterRes) {
        final String id = CHARACTER.attribute("id")
        if (id != "null" && (id as String).isInteger())
            characterRes.DTDId = id as Integer
        characterRes.setGender(CHARACTER.attribute("gender"));
        characterRes.setType(CHARACTER.attribute("type"));
    }

    private void ReadRolesNode(Node CHARACTER, Character characterRes, GNKDataContainerService dataContainer) {
        final NodeList ROLESLIST = CHARACTER.ROLES
        assert (ROLESLIST.size() > 0)
        if (ROLESLIST.size() <= 0) {
            return
        }
        Node ROLES = ROLESLIST[0]
        NodeList ROLELIST = ROLES.ROLE
        for (int i = 0; i < ROLELIST.size(); i++) {
            Node ROLE = ROLELIST.get(i)
            final String roleId = ROLE.attribute("role_id")
            assert (roleId != "null" && (roleId as String).isInteger())
            if (!(roleId != "null" && (roleId as String).isInteger())) {
                continue;
            }
            final String plotId = ROLE.attribute("plot_id")
            assert (plotId != "null" && (plotId as String).isInteger())
            if (!(plotId != "null" && (plotId as String).isInteger())) {
                continue;
            }

            Role role = dataContainer.getRole(roleId as Integer, plotId as Integer)
            assert (role != null)
            if (role == null)
                continue
            final String status = ROLE.attribute("status").toString().toLowerCase()
            final boolean statusIsValid = (status != "null" && (status.equals("locked") || status.equals("selected") || status.equals("banned")))
            assert (statusIsValid)
            if (!statusIsValid) {
                continue;
            }
            if (status.equals("locked"))
                characterRes.lockRole(role)
            else if (status.equals("selected"))
                characterRes.addRole(role)
            else
                characterRes.banRole(role)
        }
    }
    /* !Construction Methods */
}
