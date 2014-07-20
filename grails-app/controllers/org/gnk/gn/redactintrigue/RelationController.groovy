package org.gnk.gn.redactintrigue

import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasRelationWithRole
import org.gnk.roletoperso.RoleRelationType

class RelationController {

    def index() {
    }

    def save () {
        RoleHasRelationWithRole relation = new RoleHasRelationWithRole(params);
        Boolean res = saveOrUpdate(relation);
        def jsonRelation = buildJson(relation);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("relation", jsonRelation);
        object.put("isupdate", res);
        render(contentType: "application/json") {
            object
        }
    }

    def buildJson(RoleHasRelationWithRole relation) {
        JSONObject jsonRelation = new JSONObject();
        jsonRelation.put("weight", relation.getWeight());
        jsonRelation.put("id", relation.getId());
        jsonRelation.put("isBijective", relation.getIsBijective());
        jsonRelation.put("isExclusive", relation.getIsExclusive());
        jsonRelation.put("isHidden", relation.getIsHidden());
        jsonRelation.put("description", relation.getDescription());
        jsonRelation.put("RoleFromId", relation.getRole1().id);
        jsonRelation.put("RoleToId", relation.getRole2().id);
        jsonRelation.put("RoleFromCode", relation.getRole1().code);
        jsonRelation.put("RoleToCode", relation.getRole2().code);
        jsonRelation.put("RoleRelationTypeId", relation.getRoleRelationType().id);
        jsonRelation.put("RoleRelationTypeName", relation.getRoleRelationType().name);
        return jsonRelation;
    }

    def update(Long id) {
        RoleHasRelationWithRole relation = RoleHasRelationWithRole.get(id);
        if (relation) {
            Boolean isupdate = saveOrUpdate(relation);
            def jsonRelation = buildJson(relation);
            final JSONObject object = new JSONObject();
            object.put("isupdate", isupdate);
            object.put("relation", jsonRelation);
            render(contentType: "application/json") {
                object
            }
        }
    }

    def saveOrUpdate(RoleHasRelationWithRole newRelation) {
        if (params.containsKey("relationType")) {
            RoleRelationType roleRelationType = RoleRelationType.get(params.relationType as Integer);
            newRelation.roleRelationType = roleRelationType;
        } else {
            return false
        }
        if (params.containsKey("relationFrom")) {
            Role roleFrom = Role.get(params.relationFrom as Integer);
            newRelation.role1 = roleFrom;
        } else {
            return false
        }
        if (params.containsKey("relationTo")) {
            Role roleTo = Role.get(params.relationTo as Integer);
            newRelation.role2 = roleTo;
        } else {
            return false
        }
        if (params.containsKey("relationBijective")) {
            newRelation.isBijective = true;
        } else {
            newRelation.isBijective = false;
        }
        if (params.containsKey("relationExclusive")) {
            newRelation.isExclusive = true;
        } else {
            newRelation.isExclusive = false;
        }
        if (params.containsKey("relationHidden")) {
            newRelation.isHidden = true;
        } else {
            newRelation.isHidden = false;
        }
        if (params.containsKey("relationWeight")) {
            newRelation.weight = params.relationWeight as Integer;
        } else {
            return false
        }
        if (params.containsKey("relationDescription")) {
            newRelation.description = params.relationDescription;
        } else {
            return false
        }
        newRelation.save(flush: true);
        return true;
    }

    def delete (Long id) {
        RoleHasRelationWithRole relation = RoleHasRelationWithRole.get(id)
        boolean isDelete = false;
        if (relation) {
            relation.delete(flush: true)
            isDelete = true;
        }
        render(contentType: "application/json") {
            object(isdelete: isDelete, oldId: id)
        }
    }
}
