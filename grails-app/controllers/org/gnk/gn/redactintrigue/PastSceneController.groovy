package org.gnk.gn.redactintrigue

import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.GenericPlace
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
        pastscene = Pastscene.findAllWhere("title": params.pastSceneTitle).first();
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
        jsonPastScene.put("absoluteYear", pastscene.getAbsoluteYear());
        jsonPastScene.put("absoluteMonth", pastscene.getAbsoluteMonth());
        jsonPastScene.put("absoluteDay", pastscene.getAbsoluteDay());
        jsonPastScene.put("absoluteHour", pastscene.getAbsoluteHour());
        jsonPastScene.put("absoluteMinute", pastscene.getAbsoluteMinute());
        if (pastscene.getPastscenePredecessor()) {
            jsonPastScene.put("pastscenePredecessor", pastscene.getPastscenePredecessor().getTitle());
            jsonPastScene.put("pastscenePredecessorId", pastscene.getPastscenePredecessor().getId());
        }
        if (pastscene.getGenericPlace()) {
            jsonPastScene.put("pastscenePlaceId", pastscene.getGenericPlace().getId());
        }
        return jsonPastScene;
    }

    def update(Long id) {
        Pastscene pastscene = Pastscene.get(id)
        if (pastscene) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(pastscene))
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
        newPastscene.dateYear = 0;
        newPastscene.dateMonth = 0;
        newPastscene.dateDay = 0;
        newPastscene.dateHour = 0;
        newPastscene.dateMinute = 0;
        if (params.containsKey("pastSceneRelative")) {
            newPastscene.timingRelative = params.pastSceneRelative as Integer
        } else {
            return false
        }
        if (params.containsKey("pastSceneRelativeUnit")) {
            newPastscene.unitTimingRelative = params.pastSceneRelativeUnit
            switch (params.pastSceneRelativeUnit) {
                case "Y":
                    newPastscene.dateYear = newPastscene.timingRelative;
                    break;
                case "y":
                    newPastscene.dateYear = newPastscene.timingRelative;
                    break;
                case "M":
                    newPastscene.dateMonth = newPastscene.timingRelative;
                    break;
                case "d":
                    newPastscene.dateDay = newPastscene.timingRelative;
                    break;
                case "D":
                    newPastscene.dateDay = newPastscene.timingRelative;
                    break;
                case "h":
                    newPastscene.dateHour = newPastscene.timingRelative;
                    break;
                case "H":
                    newPastscene.dateHour = newPastscene.timingRelative;
                    break;
                case "m":
                    newPastscene.dateMinute = newPastscene.timingRelative;
                    break;
                default :
                    newPastscene.unitTimingRelative = "Y";
                    newPastscene.dateYear = newPastscene.timingRelative;
                    break;
            }
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
        Calendar calendar = isValidDate(params.pastSceneDatetime as String, "dd/MM/yyyy HH:mm");
        if (calendar) { // TODO !, données non persistante donc à traiter autrement
//            newPastscene.absoluteYear = calendar.get(Calendar.YEAR);
//            newPastscene.absoluteMonth = calendar.get(Calendar.MONTH);
//            newPastscene.absoluteDay = calendar.get(Calendar.DAY_OF_MONTH);
//            newPastscene.absoluteHour = calendar.get(Calendar.HOUR);
//            newPastscene.absoluteMinute = calendar.get(Calendar.MINUTE);
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
        newPastscene.version = 1;
        newPastscene.dateCreated = new Date();
        newPastscene.lastUpdated = new Date();
        newPastscene.save(flush: true);
        return true;
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
            pastscene.delete();
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
