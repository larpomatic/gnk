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
        jsonPastScene.put("Year", pastscene.getDateYear());
        jsonPastScene.put("Month", pastscene.getDateMonth());
        jsonPastScene.put("MonthLetters", g.timeMonth(month: pastscene.dateMonth));
        jsonPastScene.put("Day", pastscene.getDateDay());
        jsonPastScene.put("Hour", pastscene.getDateHour());
        jsonPastScene.put("Minute", pastscene.getDateMinute());
        jsonPastScene.put("isAbsoluteYear", pastscene.getIsAbsoluteYear());
        jsonPastScene.put("isAbsoluteMonth", pastscene.getIsAbsoluteMonth());
        jsonPastScene.put("isAbsoluteDay", pastscene.getIsAbsoluteDay());
        jsonPastScene.put("isAbsoluteHour", pastscene.getIsAbsoluteHour());
        jsonPastScene.put("isAbsoluteMinute", pastscene.getIsAbsoluteMinute());
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
                        name: pastscene.title,
                        Year: pastscene.dateYear,
                        MonthLetters: g.timeMonth(month: pastscene.dateMonth),
                        Month: pastscene.dateMonth,
                        Day: pastscene.dateDay,
                        Hour: pastscene.dateHour,
                        Minute: pastscene.dateMinute,
                        isAbsoluteYear: pastscene.isAbsoluteYear,
                        isAbsoluteMonth: pastscene.isAbsoluteMonth,
                        isAbsoluteDay: pastscene.isAbsoluteDay,
                        isAbsoluteHour: pastscene.isAbsoluteHour,
                        isAbsoluteMinute: pastscene.isAbsoluteMinute
                        )
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
        } else {
            newPastscene.dateYear = null;
        }
        if (params.containsKey("month") && params.month != "") {
            newPastscene.dateMonth = params.month as Integer;
        } else {
            newPastscene.dateMonth = null;
        }
        if (params.containsKey("day") && params.day != "") {
            newPastscene.dateDay = params.day as Integer;
        } else {
            newPastscene.dateDay = null;
        }
        if (params.containsKey("hour") && params.hour != "") {
            newPastscene.dateHour = params.hour as Integer;
        } else {
            newPastscene.dateHour = null;
        }
        if (params.containsKey("minute") && params.minute != "") {
            newPastscene.dateMinute = params.minute as Integer;
        } else {
            newPastscene.dateMinute = null;
        }
        if (params.containsKey("yearIsAbsolute")) {
            newPastscene.isAbsoluteYear = true;
        } else {
            newPastscene.isAbsoluteYear = false;
        }
        if (params.containsKey("monthIsAbsolute")) {
            newPastscene.isAbsoluteMonth = true;
        } else {
            newPastscene.isAbsoluteMonth = false;
        }
        if (params.containsKey("dayIsAbsolute")) {
            newPastscene.isAbsoluteDay = true;
        } else {
            newPastscene.isAbsoluteDay = false;
        }
        if (params.containsKey("hourIsAbsolute")) {
            newPastscene.isAbsoluteHour = true;
        } else {
            newPastscene.isAbsoluteHour = false;
        }
        if (params.containsKey("minuteIsAbsolute")) {
            newPastscene.isAbsoluteMinute = true;
        } else {
            newPastscene.isAbsoluteMinute = false;
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
        params.each {
            if (it.key.startsWith("roleHasPastSceneTitle")) {
                Role role = Role.get((it.key - "roleHasPastSceneTitle") as Integer);
                RoleHasPastscene roleHasPastscene = createRoleHasPastscene(role, newPastscene);
            }
        }
        newPastscene.save(flush: true);
        return true;
    }

    def createRoleHasPastscene(Role role, Pastscene pastscene) {
        RoleHasPastscene roleHasPastscene = pastscene.getRoleHasPastscene(role);
        if (!roleHasPastscene && (params.get("roleHasPastSceneTitle" + role.id) == "")) {
            return null;
        }
        else if (!roleHasPastscene) {
            roleHasPastscene = new RoleHasPastscene();
            roleHasPastscene.dateCreated = new Date();
            roleHasPastscene.lastUpdated = new Date();
            roleHasPastscene.version = 1;
            roleHasPastscene.pastscene = pastscene;
            roleHasPastscene.role = role;
        }
        else if ((params.get("roleHasPastSceneTitle" + role.id) == "")) {
            pastscene.removeFromRoleHasPastscenes(roleHasPastscene);
            role.removeFromRoleHasPastscenes(roleHasPastscene);
            pastscene.save();
            role.save(flush: true);
            return null;
        }
        roleHasPastscene.title = params.get("roleHasPastSceneTitle" + role.id).toString().trim();
        roleHasPastscene.description = params.get("roleHasPastSceneDescription" + role.id).toString().trim();
        roleHasPastscene.save(flush: true);
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
          org.gnk.administration.ErrorHandlerController.ParseErrorHandler();
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
