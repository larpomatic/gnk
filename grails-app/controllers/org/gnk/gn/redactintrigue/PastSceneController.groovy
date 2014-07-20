package org.gnk.gn.redactintrigue

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.GenericPlace
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.selectintrigue.Plot
import org.springframework.security.access.annotation.Secured

import java.text.ParseException
import java.text.SimpleDateFormat

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class PastSceneController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {}

    def save () {
        Pastscene pastscene = new Pastscene();
        Boolean res = saveOrUpdate(pastscene);
        def jsonPastScene = buildJson(pastscene);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("pastscene", jsonPastScene);
        render(contentType: "application/json") {
            object
        }
    }

    def buildJson(Pastscene pastscene) {
        JSONObject jsonPastScene = new JSONObject();
        jsonPastScene.put("title", pastscene.getTitle());
        jsonPastScene.put("id", pastscene.getId());
        jsonPastScene.put("plotId", pastscene.getPlot().getId());
        jsonPastScene.put("isPublic", pastscene.getIsPublic());
        jsonPastScene.put("description", pastscene.getDescription());
        jsonPastScene.put("timingRelative", pastscene.getTimingRelative());
        jsonPastScene.put("unitTimingRelative", pastscene.getUnitTimingRelative());
        jsonPastScene.put("absoluteYear", pastscene.getDateYear());
        jsonPastScene.put("absoluteMonth", pastscene.getDateMonth());
        jsonPastScene.put("absoluteDay", pastscene.getDateDay());
        jsonPastScene.put("absoluteHour", pastscene.getDateHour());
        jsonPastScene.put("absoluteMinute", pastscene.getDateMinute());
        if (pastscene.getPastscenePredecessor()) {
            jsonPastScene.put("pastscenePredecessor", pastscene.getPastscenePredecessor().getTitle());
            jsonPastScene.put("pastscenePredecessorId", pastscene.getPastscenePredecessor().getId());
        }
        if (pastscene.getGenericPlace()) {
            jsonPastScene.put("pastscenePlaceId", pastscene.getGenericPlace().getId());
        }
        JSONArray jsonRoleList = new JSONArray();
        for (Role role in pastscene.plot.roles) {
            RoleHasPastscene roleHasPastscene = role.getRoleHasPastScene(pastscene);
            JSONObject jsonRole = new JSONObject();
            if (roleHasPastscene) {
                jsonRole.put("title", roleHasPastscene.title);
                jsonRole.put("description", roleHasPastscene.description);
            }
            else {
                jsonRole.put("title", "");
                jsonRole.put("description", "");
            }
            jsonRole.put("roleId", role.id);
            jsonRole.put("roleCode", role.code);
            jsonRoleList.add(jsonRole);
        }
        jsonPastScene.put("roleList", jsonRoleList);
        return jsonPastScene;
    }

    def update(Long id) {
        Pastscene pastscene = Pastscene.get(id)
        if (pastscene) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(pastscene),
                        id: pastscene.id,
                        name: pastscene.title)
            }
        }
    }

    def saveOrUpdate(Pastscene newPastscene) {
        if (params.containsKey("plotId")) {
            Plot plot = Plot.get(params.plotId as Integer)
            newPastscene.plot = plot
        } else {
            return false
        }
        if (params.containsKey("pastSceneTitle")) {
            newPastscene.title = params.pastSceneTitle
        } else {
            return false
        }
        if (params.containsKey("pastSceneRelative") && params.pastSceneRelative != "") {
            newPastscene.timingRelative = params.pastSceneRelative as Integer
        }
        if (params.containsKey("pastSceneRelativeUnit")) {
            newPastscene.unitTimingRelative = params.pastSceneRelativeUnit
        }
        if (params.containsKey("pastScenePublic")) {
            newPastscene.isPublic = true;
        } else {
            newPastscene.isPublic = false;
        }
        if (params.containsKey("pastSceneDescription")) {
            newPastscene.description = params.pastSceneDescription
        } else {
            return false
        }
        if (params.containsKey("year") && params.year != "") {
            newPastscene.dateYear = params.year as Integer;
        }
        if (params.containsKey("month") && params.month != "") {
            newPastscene.dateMonth = params.month as Integer;
        }
        if (params.containsKey("day") && params.day != "") {
            newPastscene.dateDay = params.day as Integer;
        }
        if (params.containsKey("hour") && params.hour != "") {
            newPastscene.dateHour = params.hour as Integer;
        }
        if (params.containsKey("minute") && params.minute != "") {
            newPastscene.dateMinute = params.minute as Integer;
        }
        if (params.containsKey("pastScenePlace") && params.pastScenePlace != null && params.pastScenePlace != "" && params.pastScenePlace != "null") {
            GenericPlace genericPlace = GenericPlace.findById(params.pastScenePlace as Integer);
            if (genericPlace) {
                newPastscene.genericPlace = genericPlace;
            }
        }
        if (params.containsKey("pastScenePredecessor") && params.pastScenePredecessor != null && params.pastScenePredecessor != "" && params.pastScenePredecessor != "null") {
            Pastscene pastScenePredecessor = Pastscene.findById(params.pastScenePredecessor as Integer);
            if (pastScenePredecessor) {
                newPastscene.pastscenePredecessor = pastScenePredecessor;
            }
        }
        newPastscene.save(flush: true);
//        newPastscene = Pastscene.findAllWhere("title": newPastscene.getTitle()).first();
        params.each {
            if (it.key.startsWith("roleHasPastSceneTitle")) {
                Role role = Role.get((it.key - "roleHasPastSceneTitle") as Integer);
                RoleHasPastscene roleHasPastscene = createRoleHasPastscene(role, newPastscene);
                newPastscene.addToRoleHasPastscenes(roleHasPastscene);
            }
        }
        newPastscene.save(flush: true);
        return true;
    }

    def createRoleHasPastscene(Role role, Pastscene pastscene) {
        RoleHasPastscene roleHasPastscene = pastscene.getRoleHasPastscene(role);
        if (!roleHasPastscene) {
            roleHasPastscene = new RoleHasPastscene();
            roleHasPastscene.dateCreated = new Date();
            roleHasPastscene.lastUpdated = new Date();
            roleHasPastscene.version = 1;
            roleHasPastscene.pastscene = pastscene;
            roleHasPastscene.role = role;
        }
        roleHasPastscene.title = params.get("roleHasPastSceneTitle" + role.id);
        roleHasPastscene.description = params.get("roleHasPastSceneDescription" + role.id);
        roleHasPastscene.save(flush: true);
        role.addToRoleHasPastscenes(roleHasPastscene);
        role.save();
        return roleHasPastscene;
    }

    public Calendar isValidDate(String dateToValidate, String dateFromat){
        if(dateToValidate == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            Calendar cal = Calendar.getInstance();
            Date date = sdf.parse(dateToValidate);
            cal.setTime(date)
            return cal;
        } catch (ParseException e) {
            return null;
        }
    }

    def delete (Long id) {
        Pastscene pastscene = Pastscene.get(id)
        boolean isDelete = false;
        if (pastscene) {
            pastscene.delete(flush: true);
            isDelete = true;
        }
        final JSONObject object = new JSONObject();
        object.put("isDelete", isDelete);
        object.put("pastsceneId", id);
        render(contentType: "application/json") {
            object
        }
    }
}
