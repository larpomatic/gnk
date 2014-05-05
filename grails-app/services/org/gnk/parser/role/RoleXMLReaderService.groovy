package org.gnk.parser.role
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.RoleXMLNode
import org.gnk.parser.event.EventXMLReaderService
import org.gnk.parser.pastscene.PastSceneXMLReaderService
import org.gnk.parser.tag.TagXMLReaderService
import org.gnk.roletoperso.*
import org.gnk.selectintrigue.Plot

class RoleXMLReaderService {
    /* Exposed Methods */
    public List<Role> getRolesFromNode(Node ROLES, Plot plot, GNKDataContainerService dataContainer) {
        NodeList ROLELIST = ROLES.ROLE
        HashMap<Integer, RoleXMLNode> roleXMLNodes = new HashMap<Integer, RoleXMLNode>()

        // Parse the DTD and add found role to roleXMLNodes
        ROLELIST.each {Node ROLE ->
            RoleXMLNode roleRes = getRoleXMLNode(ROLE, plot, dataContainer)
            if (roleRes != null)
                roleXMLNodes.put(roleRes.role.DTDId, roleRes)
        }

        // Links the roles in roleXMLNodes
        List<Role> roles = new ArrayList<>()
        roleXMLNodes.each { DTDId, roleXMLNode ->
            // add relations to role
            roleXMLNode.getXMLrelations().each { RoleXMLNode.RoleHasRelationWithRoleXMLNode relationXML ->
                RoleHasRelationWithRole relation = null
                RoleXMLNode receiver = roleXMLNodes.get(relationXML.receiverId)

                // if assert false, the linked role is not found, the DTDId is bad
                assert (receiver != null)
                // continue each closure
                if (receiver == null)
                    return

                // check if relation already exists
                //relation = RoleHasRelationWithRole.findByRole1AndRole2(roleXMLNode.role, receiver.role)
                //if (relation == null)
                //  relation = RoleHasRelationWithRole.findByRole1AndRole2(receiver.role, roleXMLNode.role)

                // if not found, create a new one
                if (relation == null)
                    relation = new RoleHasRelationWithRole(
                        roleXMLNode.role,
                        roleXMLNodes.get(relationXML.receiverId).role,
                        relationXML.weight,
                        relationXML.isBijective,
                        relationXML.type,
                        relationXML.isExclusive,
                        relationXML.description
                )

                // add relation to role
                roleXMLNode.role.addToRoleHasRelationWithRolesForRole1Id(relation)
            }
            roles.add(roleXMLNode.role)
        }
        return roles
    }

    def RoleXMLNode getRoleXMLNode(Node ROLE, Plot plot, GNKDataContainerService dataContainer) {
        RoleXMLNode roleXMLNode = new RoleXMLNode()
        roleXMLNode.role = ReadRoleRootNode(ROLE, plot)

        // DESCRIPTION reader
        if (ROLE.DESCRIPTION.size() > 0) {
            roleXMLNode.role.description =  ROLE.DESCRIPTION[0].text()
        }

        // TAGS reader
        ReadTagsNode(ROLE, roleXMLNode.role, dataContainer)

        // RELATIONS reader
        ReadRelationsNode(ROLE, roleXMLNode, dataContainer)

        // ROLE.PAST_SCENES reader
        ReadPastScenesNode(ROLE, roleXMLNode.role, dataContainer)

        // ROLE.EVENTS reader
        ReadEventsNode(ROLE, roleXMLNode.role, dataContainer)

        return roleXMLNode
    }

    /* Has to be called AFTER all the roles of the plot have been initialized by getRoleFromNode */
    @Deprecated
    def void setRelationsOfRole(Node ROLE, Plot plot, Role role) {
        if (ROLE.RELATIONS.size() <= 0)
            return

        Node RELATIONS = ROLE.RELATIONS[0]
        NodeList RELATIONLIST = RELATIONS.RELATION
        for (int i = 0; i < RELATIONLIST.size(); i++) {
            Node RELATION = RELATIONLIST.get(i)

            final String roleIdStr = RELATION.attribute("role_id")
            Integer roleId;
            if (roleIdStr != "null" && (roleIdStr as String).isInteger())
                roleId = roleIdStr as Integer
            assert (roleId != null)
            Role receiver;
            for (Role roleIt : plot.roles) {
                if (roleIt.DTDId == roleId) {
                    receiver = roleIt
                    break
                }
            }
            if (receiver == null)
                return

            final String relationTypeStr = RELATION.attribute("type")
            RoleRelationType relationType;
            if (relationTypeStr != "null")
                relationType = RoleRelationType.findWhere(name: relationTypeStr) //FIXME TODO désolidariser DTD et BDD
            if (relationType == null)
                return
            final String isBijectiveStr = RELATION.attribute("is_bijective")
            Boolean isBijective;
            if (isBijectiveStr != "null")
                isBijective = isBijectiveStr as Boolean
            final String weightStr = RELATION.attribute("weight")
            Integer weight;
            if (weightStr != "null" && (weightStr as String).isInteger())
                weight = weightStr as Integer
            assert (weight != null)
            RoleHasRelationWithRole roleRelation = new RoleHasRelationWithRole(role, receiver, weight, isBijective, relationType);
            role.addToRoleHasRelationWithRolesForRole1Id(roleRelation)
            receiver.addToRoleHasRelationWithRolesForRole2Id(roleRelation)
        }
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Role ReadRoleRootNode (Node ROLE, Plot plot) {
        Role roleRes = null
        String code = null

        if (ROLE.attribute("code") != "null") {
            code = ROLE.attribute("code") as String
        }
        if (roleRes == null)
            roleRes = new Role(code: code, plot: plot)

        if (ROLE.attribute("id") != "null" && (ROLE.attribute("id") as String).isInteger())
            roleRes.DTDId = ROLE.attribute("id") as Integer

        if (ROLE.attribute("pipr") != "null" && (ROLE.attribute("pipr") as String).isInteger())
            roleRes.pipr = ROLE.attribute("pipr") as Integer

        if (ROLE.attribute("pipi") != "null" && (ROLE.attribute("pipi") as String).isInteger())
            roleRes.pipi = ROLE.attribute("pipi") as Integer

        if (ROLE.attribute("type") != "null")
            roleRes.type = ROLE.attribute("type")

        return roleRes
    }

    private void ReadTagsNode(Node ROLE, Role roleRes, GNKDataContainerService dataContainer) {
        assert (ROLE.TAGS.size() <= 1)
        if (ROLE.TAGS.size() <= 0)
            return
        Node TAGS = ROLE.TAGS[0]
        NodeList TAGLIST = TAGS.TAG
        TagXMLReaderService tagReader = new TagXMLReaderService()
        for (int i = 0; i < TAGLIST.size(); i++)
        {
            Node TAG = TAGLIST.get(i)

            RoleHasTag roleHasTag = new RoleHasTag(roleRes, tagReader.getTagFromNode(TAG, dataContainer), tagReader.getTagWeight(TAG, dataContainer))
            roleRes.addToRoleHasTags(roleHasTag)
        }
    }

    private void ReadRelationsNode(Node ROLE, RoleXMLNode roleXMLNode, GNKDataContainerService dataContainer) {
        assert (ROLE.RELATIONS.size() <= 1)
        if (ROLE.RELATIONS.size() <= 0)
            return

        Node RELATIONS = ROLE.RELATIONS[0]
        NodeList RELATIONLIST = RELATIONS.RELATION
        RELATIONLIST.each {Node RELATION ->
            if (RELATION.attribute("role_id") != "null" && (RELATION.attribute("role_id") as String).isInteger()) {
                Integer roleId = RELATION.attribute("role_id") as Integer
                RoleRelationType type = null
                boolean isBijective = false;
                boolean isExclusive = false
                Integer weight = 0
                String description = ""

                if (RELATION.attribute("type") != "null")
                {
                    String typeName = RELATION.attribute("type")
                    type = RoleRelationType.findWhere(name: typeName)
                    if (type == null)
                    {
                        type = new RoleRelationType(name: typeName)
                    }

                }

                if (RELATION.attribute("is_bijective") != "null")
                    isBijective = RELATION.attribute("is_bijective") as boolean
                if (RELATION.attribute("is_exclusive") != "null")
                    isExclusive = RELATION.attribute("is_exclusive") as boolean
                if (RELATION.attribute("weight") != "null")
                    weight = RELATION.attribute("weight") as Integer
                if (RELATION.DESCRIPTION.size() > 0)
                    description = RELATION.DESCRIPTION[0].text()

                roleXMLNode.addRelation(roleId, type, isBijective, isExclusive, weight, description)
            }

        }
    }

    private void ReadPastScenesNode(Node ROLE, Role roleRes, GNKDataContainerService dataContainer) {
        assert (ROLE.PAST_SCENES.size() <= 1)
        if (ROLE.PAST_SCENES.size() <= 0)
            return

        NodeList PASTSCENELIST = ROLE.PAST_SCENES[0].PAST_SCENE
        PastSceneXMLReaderService pastSceneReader = new PastSceneXMLReaderService()
        PASTSCENELIST.each { Node PAST_SCENE ->
            RoleHasPastscene roleHasPastscene = pastSceneReader.GetRoleHasPastsceneFromRootNode(PAST_SCENE, roleRes.plot)
            roleHasPastscene.setRole(roleRes)
            roleRes.addToRoleHasPastscenes(roleHasPastscene)
            roleHasPastscene.pastscene.addToRoleHasPastscenes(roleHasPastscene)
        }
    }

    private void ReadEventsNode(Node ROLE, Role roleRes, GNKDataContainerService dataContainer) {
        assert (ROLE.EVENTS.size() <= 1)
        if (ROLE.EVENTS.size() <= 0)
            return

        EventXMLReaderService eventXMLReader = new EventXMLReaderService()

        // FIXME
        NodeList EVENTLIST = ROLE.EVENTS[0].EVENT
        EVENTLIST.each { Node EVENT ->
            eventXMLReader.getRoleHasEventsFromNode(EVENT, roleRes, dataContainer).each { RoleHasEvent roleHasEvent ->
                roleRes.addToRoleHasEvents(roleHasEvent)
            }
        }

    }
    /* !Construction Methods */
}
