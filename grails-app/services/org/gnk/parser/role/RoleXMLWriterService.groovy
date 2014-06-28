package org.gnk.parser.role

import org.gnk.parser.event.EventXMLWriterService
import org.gnk.roletoperso.*
import org.w3c.dom.CDATASection
import org.w3c.dom.Document
import org.w3c.dom.Element

class RoleXMLWriterService {

    /* Exposed Methods */

    def Element getRoleElement(Document doc, Role role) {
        Element roleElt = getRoleRootElement(doc, role);

        Element roleDescription = doc.createElement("DESCRIPTION");
        CDATASection descriptionData = doc.createCDATASection("DESCRIPTION")
        descriptionData.setData(role.description)
        roleDescription.appendChild(descriptionData)
        roleElt.appendChild(roleDescription);

        roleElt.appendChild(getTagsElement(doc, role));
        roleElt.appendChild(getRelationsElement(doc, role));
        roleElt.appendChild(getPastscenesRoleElement(doc, role));
        roleElt.appendChild(getEventsRoleElement(doc, role));


        return roleElt;
    }
    /* !Exposed Methods */

    /* Construction Methods */

    private Element getRoleRootElement(Document doc, Role role) {
        Element rootElement = doc.createElement("ROLE");

        if (role.DTDId == null || role.DTDId < 0)
            role.DTDId = role.id
        assert (role.DTDId != null && role.DTDId >= 0)
        rootElement.setAttribute("id", role.DTDId.toString());
        rootElement.setAttribute("code", role.code);
        rootElement.setAttribute("pipr", role.pipr?.toString());
        rootElement.setAttribute("pipi", role.pipi?.toString());
        rootElement.setAttribute("type", role.type);

        return rootElement;
    }

    private Element getTagsElement(Document doc, Role role) {
        Element tagsElt = doc.createElement("TAGS");

        if (role.roleHasTags) {
            for (RoleHasTag roleHasTag : role.roleHasTags) {
                Element tag = doc.createElement("TAG");
                tag.setAttribute("value", roleHasTag.tag.name)
//                tag.setAttribute("type", roleHasTag.tag.tagFamily.value)
                tag.setAttribute("weight", roleHasTag.weight.toString())
                tagsElt.appendChild(tag)
            }
        }

        return tagsElt;
    }

    private Element getPastscenesRoleElement(Document doc, Role role) {
        Element pastscenesElt = doc.createElement("PAST_SCENES");

        if (role.roleHasPastscenes) {


            for (RoleHasPastscene roleHasPastscene : role.roleHasPastscenes) {
                Element pastsceneElt = doc.createElement("PAST_SCENE")

                if (roleHasPastscene.pastscene.DTDId == null || roleHasPastscene.pastscene.DTDId < 0)
                    roleHasPastscene.pastscene.DTDId = roleHasPastscene.pastscene.id;
                assert (roleHasPastscene.pastscene.DTDId != null);
                pastsceneElt.setAttribute("past_scene_id", roleHasPastscene.pastscene.DTDId.toString())
                pastsceneElt.setAttribute("title", roleHasPastscene.title)

                Element descriptionElt = doc.createElement("DESCRIPTION")
                if (roleHasPastscene.description) {
                    CDATASection descriptionData = doc.createCDATASection("DESCRIPTION")
                    descriptionData.setData(roleHasPastscene.description)
                    descriptionElt.appendChild(descriptionData)
                }
                pastsceneElt.appendChild(descriptionElt)

                pastscenesElt.appendChild(pastsceneElt);
            }
        }

        return pastscenesElt;
    }

    private Element getEventsRoleElement(Document doc, Role role) {
        Element eventsElt = doc.createElement("EVENTS");

        if (role.roleHasEvents) {
            final EventXMLWriterService eventWriter = new EventXMLWriterService()
            for (RoleHasEvent roleHasEvent : role.roleHasEvents) {
                eventsElt.appendChild(eventWriter.getEventElementForRole(doc, roleHasEvent))
            }
        }
        return eventsElt;
    }

    private Element getRelationsElement(Document doc, Role role) {
        Element relationsElt = doc.createElement("RELATIONS");
        for (RoleHasRelationWithRole roleRelation : role.getRoleHasRelationWithRolesForRole1Id()) {
            Element relationElt = doc.createElement("RELATION")
            final Role receiver = roleRelation.getRole2()
            assert (receiver != null)
            Integer roleId = receiver.getDTDId()
            if (roleId == null || roleId < 0)
                roleId = receiver.getId()
            relationElt.setAttribute("role_id", roleId.toString())
            relationElt.setAttribute("type", roleRelation.getRoleRelationType().getName())
            relationElt.setAttribute("is_bijective", roleRelation.getIsBijective().toString())
            relationElt.setAttribute("weight", roleRelation.getWeight().toString())
            relationsElt.appendChild(relationElt)
        }
        return relationsElt;
    }
    /* !Construction Methods */
}
