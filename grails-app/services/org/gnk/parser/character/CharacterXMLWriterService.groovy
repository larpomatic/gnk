package org.gnk.parser.character

import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Role
import org.gnk.selectintrigue.Plot
import org.w3c.dom.Document
import org.w3c.dom.Element

class CharacterXMLWriterService {

    /* Exposed Methods */

    def Element getCharacterElement(Document doc, Character character) {
        Element characterElt = getCharacterRootElement(doc, character);

        characterElt.appendChild(getRolesElement(doc, character));

        return characterElt;
    }
    /* !Exposed Methods */

    /* Construction Methods */

    private Element getCharacterRootElement(Document doc, Character character) {
        Element rootElement = doc.createElement("CHARACTER");

        final Integer DTDId = character.DTDId
        assert (DTDId != null && DTDId > 0)
        rootElement.setAttribute("id", DTDId.toString());
        rootElement.setAttribute("gender", character.gender);
        rootElement.setAttribute("type", character.type);
        rootElement.setAttribute("age", (character.age == null ? "0" : character.age as String));
        return rootElement;
    }

    private Element getRolesElement(Document doc, Character character) {
        Element rolesElt = doc.createElement("ROLES");

        final List<Role> lockedRoleList = character.getLockedRoles()
        if (lockedRoleList) {
            for (Role role : lockedRoleList) {
                Element roleElt = getRoleElement(doc, role, "locked")
                if (roleElt != null)
                    rolesElt.appendChild(roleElt)
            }
        }

        final List<Role> selectedRoleList = character.getSelectedRoles()
        if (selectedRoleList) {
            for (Role role : selectedRoleList) {
                Element roleElt = getRoleElement(doc, role, "selected")
                if (roleElt != null)
                    rolesElt.appendChild(roleElt)
            }
        }

        final List<Role> bannedRoleList = character.getBannedRoles()
        if (bannedRoleList) {
            for (Role role : bannedRoleList) {
                Element roleElt = getRoleElement(doc, role, "banned")
                if (roleElt != null)
                    rolesElt.appendChild(roleElt)
            }
        }

        return rolesElt;
    }

    private Element getRoleElement(Document doc, Role role, String status) {
        Element roleElt = doc.createElement("ROLE");
        Integer DTDId = role.DTDId
        if (DTDId == null || DTDId < 0)
            DTDId = role.getId()
        roleElt.setAttribute("role_id", DTDId.toString());
        final Plot plot = role.getPlot()
        assert (plot != null)
        if (plot == null)
            return null;
        Integer plotId = plot.getDTDId()
        if (plotId == null || plotId < 0)
            plotId = plot.getId()
        roleElt.setAttribute("plot_id", plotId.toString())
        roleElt.setAttribute("scoring", "0") // FIXME Si c'est réellement utilisé
        roleElt.setAttribute("status", status)
        return roleElt
    }
    /* !Construction Methods */
}
