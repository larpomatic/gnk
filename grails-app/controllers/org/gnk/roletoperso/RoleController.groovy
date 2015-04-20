package org.gnk.roletoperso

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Pastscene
import org.gnk.selectintrigue.Plot;
import org.gnk.tag.Tag
import org.gnk.tag.TagService;

class RoleController {

	def index() {
	}

	def save () {
        Role role = new Role(params);
        Plot plot = Plot.get(params.plotId as Integer);
        Boolean res = saveOrUpdate(role);
        def roleTagList = new TagService().getRoleTagQuery();
        def jsonTagList = buildTagList(roleTagList);
        def jsonRole = buildJson(role, plot);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("role", jsonRole);
        object.put("roleTagList", jsonTagList);
        render(contentType: "application/json") {
            object
        }
	}

    def JSONArray buildTagList(def roleTagList) {
        JSONArray jsonTagList = new JSONArray();
        for (Tag roleTag in roleTagList) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", roleTag.getId());
            jsonTag.put("name", roleTag.getName());
            if (roleTag.children && roleTag.children.size() != 0) {
                JSONArray jsonTagChildren = buildTagList(roleTag.children);
                jsonTag.put("children", jsonTagChildren);
            }
            jsonTagList.add(jsonTag);
        }
        return jsonTagList;
    }

    def buildJson(Role role, Plot plot) {
        JSONObject jsonRole = new JSONObject();
        jsonRole.put("code", role.getCode());
        jsonRole.put("id", role.getId());
        jsonRole.put("plotId", role.getPlot().getId());
        jsonRole.put("pipi", role.getPipi());
        jsonRole.put("pipr", role.getPipr());
        jsonRole.put("type", role.getType());
        jsonRole.put("pjgp", role.getPjgp());
        jsonRole.put("description", role.getDescription());
        JSONArray jsonResourceList = new JSONArray();
        for (GenericResource genericResource in plot.genericResources) {
            JSONObject jsonResource = new JSONObject();
            jsonResource.put("id", genericResource.id)
            jsonResource.put("code", genericResource.code)
            jsonResourceList.add(jsonResource);
        }
        jsonRole.put("resourceList", jsonResourceList);
        JSONArray jsonTagList = new JSONArray();
        for (RoleHasTag roleHasTag in role.roleHasTags) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", roleHasTag.tag.id)
            jsonTag.put("weight", roleHasTag.weight)
            jsonTagList.add(jsonTag);
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
            jsonEvent.put("eventTiming", event.timing);
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
            jsonPastscene.put("Year", pastscene.dateYear);
            jsonPastscene.put("Month", pastscene.getDateMonth());
            if (pastscene.dateMonth == null || pastscene.dateMonth.equals("") || (pastscene.dateMonth as Integer) > 12) {
                jsonPastscene.put("MonthLetters", "");
            }
            else {
                jsonPastscene.put("MonthLetters", g.timeMonth(month: pastscene.dateMonth));
            }
            jsonPastscene.put("Day", pastscene.dateDay);
            jsonPastscene.put("Hour", pastscene.dateHour);
            jsonPastscene.put("Minute", pastscene.dateMinute);
            jsonPastscene.put("isAbsoluteYear", pastscene.getIsAbsoluteYear());
            jsonPastscene.put("isAbsoluteMonth", pastscene.getIsAbsoluteMonth());
            jsonPastscene.put("isAbsoluteDay", pastscene.getIsAbsoluteDay());
            jsonPastscene.put("isAbsoluteHour", pastscene.getIsAbsoluteHour());
            jsonPastscene.put("isAbsoluteMinute", pastscene.getIsAbsoluteMinute());
            jsonPastsceneList.add(jsonPastscene);
        }
        jsonRole.put("pastsceneList", jsonPastsceneList);
        return jsonRole;
    }

	def update(Long id) {
		Role role = Role.get(id)
        String oldname = role.code;
		if (role) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(role),
                        id: role.id,
                        type: role.type,
                        name: role.code,
                        oldname: oldname)
            }
		}
	}

	def saveOrUpdate(Role newRole) {

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
		if (params.containsKey("roleType") && (params.roleType == "PJ" || params.roleType == "PNJ" || params.roleType == "PHJ"
        || params.roleType == "PJG" || params.roleType == "TPJ" || params.roleType == "STF")) {
			newRole.type = params.roleType
		} else {
			return false
		}
        if (params.containsKey("rolePJGP")){
            newRole.pjgp = params.rolePJGP as Integer
        } else {
            if (params.roleType == "PJG")
            return false
        }
		if(newRole.roleHasTags != null) {
            HashSet<RoleHasTag> roleHasTags = newRole.roleHasTags;
            newRole.roleHasTags.clear();
            RoleHasTag.deleteAll(roleHasTags);
		} else {
			newRole.roleHasTags = new HashSet<RoleHasTag>()
		}
        newRole.save(flush: true);

        params.each {
            if (it.key.startsWith("roleTags_")) {
                RoleHasTag roleHasTag = new RoleHasTag();
                Tag roleTag = Tag.get((it.key - "roleTags_") as Integer);
                roleHasTag.tag = roleTag
                roleHasTag.weight = params.get("roleTagsWeight_" + roleTag.id) as Integer;
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

	def delete (Long id) {
		Role role = Role.get(id)
        boolean isDelete = false;
        if (role) {
            role.delete(flush: true)
            isDelete = true;
        }
        render(contentType: "application/json") {
            object(isdelete: isDelete)
        }
	}
}
