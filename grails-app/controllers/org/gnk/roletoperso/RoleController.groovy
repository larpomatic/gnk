package org.gnk.roletoperso

import org.gnk.selectintrigue.Plot;
import org.gnk.tag.Tag
import org.gnk.tag.TagService;
import org.gnk.user.User;

class RoleController {

	def index() {
	}

	def save () {
		saveOrUpdate(new Role(params), true)
	}

	def update(Long id) {
		Role role = Role.get(id)
		if (role) {
			saveOrUpdate(role, false)
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
		redirect(controller: "redactIntrigue", action: "edit", id: params.plotId, params: [screenStep: 1])
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
