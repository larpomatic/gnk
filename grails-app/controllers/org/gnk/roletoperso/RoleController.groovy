package org.gnk.roletoperso

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.selectintrigue.Plot;
import org.gnk.tag.Tag
import org.gnk.tag.TagService;
import org.gnk.user.User;

class RoleController {

	def index() {
	}

	def save () {
        Role role = new Role(params);
        Boolean res = saveOrUpdate(role, true);
        role = Role.findAllWhere("code": role.getCode()).first();
        def roleTagList = new TagService().getRoleTagQuery();
        def jsonTagList = buildTagList(roleTagList);
        def jsonRole = buildJson(role, roleTagList);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("role", jsonRole);
        object.put("roleTagList", jsonTagList);
        render(contentType: "application/json") {
            object
        }
	}

    def buildTagList(def roleTagList) {
        JSONArray jsonTagList = new JSONArray();
        for (roleTag in roleTagList) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", roleTag.getId());
            jsonTag.put("name", roleTag.getName());
            jsonTagList.add(jsonTag);
        }
        return jsonTagList;
    }

    def buildJson(Role role, roleTagList) {
        JSONObject jsonRole = new JSONObject();
        jsonRole.put("code", role.getCode());
        jsonRole.put("id", role.getId());
        jsonRole.put("plotId", role.getPlot().getId());
        jsonRole.put("pipi", role.getPipi());
        jsonRole.put("pipr", role.getPipr());
        jsonRole.put("type", role.getType());
        jsonRole.put("description", role.getDescription());
        JSONArray jsonTagList = new JSONArray();
        for (Tag roleTag in roleTagList) {
            if (role.hasRoleTag(roleTag)) {
//                JSONObject jsonTag = new JSONObject();
//                jsonTag.put("tag", roleTag.getId());
                jsonTagList.add(roleTag.getId());
            }
        }
        jsonRole.put("tagList", jsonTagList);
        return jsonRole;
    }

	def update(Long id) {
		Role role = Role.get(id)
		if (role) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(role, false))
            }
		}
	}

	def saveOrUpdate(Role newRole, boolean isNew) {
		if (params.containsKey("plotId")) {
			Plot plot = Plot.get(params.plotId as Integer)
			newRole.plot = plot
		} else {
			return false
		}
		if (params.containsKey("roleCode")) {
			newRole.code = params.roleCode
		} else {
			return false
		}
		if (params.containsKey("rolePipi")) {
			newRole.pipi = params.rolePipi as Integer
		} else {
			return false
		}
		if (params.containsKey("rolePipr")) {
			newRole.pipr = params.rolePipr as Integer
		} else {
			return false
		}
		if (params.containsKey("roleDescription")) {
			newRole.description = params.roleDescription
		} else {
			return false
		}
		if (params.containsKey("roleType") && (params.roleType == "PJ" || params.roleType == "PNJ" || params.roleType == "PHJ")) {
			newRole.type = params.roleType
		} else {
			return false
		}

		//deleteRoleHasRoleTag(newRole)
		if(newRole.roleHasTags) {
			newRole.roleHasTags.clear();
		} else {
			newRole.roleHasTags = new HashSet<RoleHasTag>()
		}
		params.each {
			if (it.key.startsWith("roleTags_")) {
				RoleHasTag roleHasTag = new RoleHasTag()
				Tag roleTag = Tag.get((it.key - "roleTags_") as Integer);
				roleHasTag.tag = roleTag
				roleHasTag.weight = TagService.LOCKED
				roleHasTag.role = newRole
				newRole.roleHasTags.add(roleHasTag)
			}
		}
		if (isNew)
			params.updateRoleResult = newRole.myInsert();
		else
			params.updateRoleResult = !!(newRole.save(flush: true))
//		redirect(controller: "redactIntrigue", action: "edit", id: params.plotId, params: [screenStep: 1])
        return true;
	}

	def deleteRoleHasRoleTag (Role role) {
		if (!role.roleHasRoleTags)
		{
			return
		}
		/*while ()
		 Set<RoleHasRoleTag> toRemove = new HashSet<RoleHasRoleTag>(role.roleHasRoleTags)
		 while (!(toRemove.empty)) {
		 RoleHasRoleTag roleHasRoleTag = toRemove.findAll().first()
		 toRemove.remove(roleHasRoleTag)
		 role.roleHasRoleTags.remove(roleHasRoleTag);
		 roleHasRoleTag.roleTag.roleHasRoleTags.remove(roleHasRoleTag)
		 roleHasRoleTag.delete(flush: true)
		 }*/
	}

	def delete (Long id) {
		Role role = Role.get(id)
        boolean isDelete = false;
        if (role) {
            //deleteRoleHasRoleTag(role)

            role.delete(flush: true)
            isDelete = true;
        }
        render(contentType: "application/json") {
            object(isdelete: isDelete)
        }
//		redirect(controller: "redactIntrigue", action: "edit", id: params.plotId, params: [screenStep: 1])
	}
}
