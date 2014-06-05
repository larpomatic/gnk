package org.gnk.roletoperso

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.Pastscene
import org.gnk.selectintrigue.Plot;
import org.gnk.tag.Tag
import org.gnk.tag.TagService;
import org.gnk.user.User;

class RoleController {

	def index() {
	}

	def save () {
        Role role = new Role(params);
        Plot plot = Plot.get(params.plotId as Integer);
        Boolean res = saveOrUpdate(role, true);
        role = Role.findAllWhere("code": role.getCode()).first();
        def roleTagList = new TagService().getRoleTagQuery();
        def jsonTagList = buildTagList(roleTagList);
        def jsonRole = buildJson(role, roleTagList, plot);
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

    def buildJson(Role role, roleTagList, Plot plot) {
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
                jsonTagList.add(roleTag.getId());
            }
        }
        jsonRole.put("tagList", jsonTagList);
        JSONArray jsonEventList = new JSONArray();
        for (Event event in plot.events) {
            RoleHasEvent roleHasEvent = role.getRoleHasEvent(event);
            JSONObject jsonEvent = new JSONObject();
            if (roleHasEvent) {
                jsonEvent.put("title", roleHasEvent.title);
                jsonEvent.put("description", roleHasEvent.description);
                jsonEvent.put("isAnnounced", roleHasEvent.isAnnounced);
            }
            else {
                jsonEvent.put("title", "");
                jsonEvent.put("description", "");
                jsonEvent.put("isAnnounced", "");
            }
            jsonEvent.put("eventId", event.id);
            jsonEvent.put("eventName", event.name);
            jsonEventList.add(jsonEvent);
        }
        jsonRole.put("eventList", jsonEventList);
        JSONArray jsonPastsceneList = new JSONArray();
        for (Pastscene pastscene in plot.pastescenes) {
            RoleHasPastscene roleHasPastscene = role.getRoleHasPastScene(pastscene);
            JSONObject jsonPastscene = new JSONObject();
            if (roleHasPastscene) {
                jsonPastscene.put("title", roleHasPastscene.title);
                jsonPastscene.put("description", roleHasPastscene.description);
            }
            else {
                jsonPastscene.put("title", "");
                jsonPastscene.put("description", "");
            }
            jsonPastscene.put("pastsceneId", pastscene.id);
            jsonPastscene.put("pastsceneTitle", pastscene.title);
            jsonPastsceneList.add(jsonPastscene);
        }
        jsonRole.put("pastsceneList", jsonPastsceneList);
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
        if(newRole.roleHasEvents) {
            newRole.roleHasEvents.clear();
        } else {
            newRole.roleHasEvents = new HashSet<RoleHasEvent>()
        }
        if(newRole.roleHasPastscenes) {
            newRole.roleHasPastscenes.clear();
        } else {
            newRole.roleHasPastscenes = new HashSet<RoleHasPastscene>()
        }
		if (isNew)
			params.updateRoleResult = newRole.myInsert();
		else
			params.updateRoleResult = !!(newRole.save(flush: true));

        newRole = Role.findAllWhere("code": newRole.getCode()).first();
        params.each {
            if (it.key.startsWith("roleTags_")) {
                RoleHasTag roleHasTag = new RoleHasTag();
                Tag roleTag = Tag.get((it.key - "roleTags_") as Integer);
                roleHasTag.tag = roleTag
                roleHasTag.weight = TagService.LOCKED
                roleHasTag.role = newRole
                newRole.roleHasTags.add(roleHasTag)
            }
            else if (it.key.startsWith("roleHasEventTitle")) {
                Event event = Event.get((it.key - "roleHasEventTitle") as Integer);
                RoleHasEvent roleHasEvent = createRoleHasEvent(newRole, event);
                newRole.addToRoleHasEvents(roleHasEvent);
            }
            else if (it.key.startsWith("roleHasPastSceneTitle")) {
                Pastscene pastscene = Pastscene.get((it.key - "roleHasPastSceneTitle") as Integer);
                RoleHasPastscene roleHasPastscene = createRoleHasPastscene(newRole, pastscene);
                newRole.addToRoleHasPastscenes(roleHasPastscene);
            }
        }
        newRole.save(flush: true);
        return true;
	}

    def createRoleHasEvent(Role newRole, Event event) {
        RoleHasEvent roleHasEvent = RoleHasEvent.createCriteria().get {
            like("role.id", newRole.id)
            like("event", event)
        };
        if (!roleHasEvent) {
            roleHasEvent = new RoleHasEvent();
        }
        roleHasEvent.title = params.get("roleHasEventTitle" + event.id);
        roleHasEvent.isAnnounced = params.get("roleHasEventannounced" + event.id) != null;
        roleHasEvent.description = params.get("roleHasEventDescription" + event.id);
        roleHasEvent.dateCreated = new Date();
        roleHasEvent.lastUpdated = new Date();
        roleHasEvent.version = 1;
        roleHasEvent.comment = "";
        roleHasEvent.event = event;
        roleHasEvent.role = newRole;
        roleHasEvent.evenementialDescription = "";
        roleHasEvent.save(flush: true);
        event.addToRoleHasEvents(roleHasEvent);
        event.save();
        return roleHasEvent;
    }

    def createRoleHasPastscene(Role newRole, Pastscene pastscene) {
        RoleHasPastscene roleHasPastscene = RoleHasPastscene.createCriteria().get {
            like("role.id", newRole.id)
            like("pastscene", pastscene)
        };
        if (!roleHasPastscene) {
            roleHasPastscene = new RoleHasPastscene();
        }
        roleHasPastscene.title = params.get("roleHasPastSceneTitle" + pastscene.id);
        roleHasPastscene.description = params.get("roleHasPastSceneDescription" + pastscene.id);
        roleHasPastscene.dateCreated = new Date();
        roleHasPastscene.lastUpdated = new Date();
        roleHasPastscene.version = 1;
        roleHasPastscene.pastscene = pastscene;
        roleHasPastscene.role = newRole;
        roleHasPastscene.save(flush: true);
        pastscene.addToRoleHasPastscenes(roleHasPastscene);
        pastscene.save();
        return roleHasPastscene;
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
	}
}
