package org.gnk.gn.redactintrigue

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasRelationWithRole
import org.gnk.roletoperso.RoleRelationType
import org.gnk.selectintrigue.Plot;

class RelationController {

    def index() {
    }

    def save () {
        RoleHasRelationWithRole relation = new RoleHasRelationWithRole(params);
        Boolean res = saveOrUpdate(relation, true);
//        relation = RoleHasRelationWithRole.findAllWhere("code": role.getCode()).first();
        def jsonRelation = buildJson(relation);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("relation", jsonRelation);
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
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(relation, false),
                        id: relation.id)
            }
        }
    }

    def saveOrUpdate(RoleHasRelationWithRole newRelation, boolean isNew) {
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
            newRelation.weight = params.relationWeight;
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
            object(isdelete: isDelete)
        }
    }
}
