package org.gnk.resplacetime

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.selectintrigue.Plot
import org.gnk.tag.TagService
import org.gnk.tag.Tag

class GenericPlaceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [genericPlaceInstanceList: GenericPlace.list(params), genericPlaceInstanceTotal: GenericPlace.count()]
    }

    def create() {
        [genericPlaceInstance: new GenericPlace(params)]
    }

    def save() {
        GenericPlace genericPlace = new GenericPlace(params)
        Plot plot = Plot.get(params.plotId as Integer);
        Boolean res = saveOrUpdate(genericPlace, true);
        genericPlace = GenericPlace.findAllWhere("code": genericPlace.getCode()).first();
        def placeTagList = new TagService().getPlaceTagQuery();
        def jsonTagList = buildTagList(placeTagList);
        def jsonGenericPlace = buildJson(genericPlace, placeTagList, plot);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("genericPlace", jsonGenericPlace);
        object.put("genericPlaceTagList", jsonTagList);
        render(contentType: "application/json") {
            object
        }
    }

    def buildTagList(def genericPlaceTagList) {
        JSONArray jsonTagList = new JSONArray();
        for (genericPlaceTag in genericPlaceTagList) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", genericPlaceTag.getId());
            jsonTag.put("name", genericPlaceTag.getName());
            jsonTagList.add(jsonTag);
        }
        return jsonTagList;
    }

    def buildJson(GenericPlace genericPlace, placeTagList, Plot plot) {
        JSONObject jsonGenericPlace = new JSONObject();
        jsonGenericPlace.put("code", genericPlace.getCode());
        jsonGenericPlace.put("id", genericPlace.getId());
        jsonGenericPlace.put("plotId", genericPlace.getPlot().getId());
        jsonGenericPlace.put("comment", genericPlace.getComment());
        JSONArray jsonTagList = new JSONArray();
        for (Tag genericPlaceTag in placeTagList) {
            if (genericPlace.hasGenericPlaceTag(genericPlaceTag)) {
                jsonTagList.add(genericPlaceTag.getId());
            }
        }
        jsonGenericPlace.put("tagList", jsonTagList);
        JSONArray jsonEventList = new JSONArray();
        for (Event event in plot.events) {
            JSONObject jsonEvent = new JSONObject();
            jsonEvent.put("eventId", event.id);
            jsonEvent.put("eventName", event.name);
            jsonEvent.put("eventCheck", genericPlace.events.contains(event));
            jsonEventList.add(jsonEvent);
        }
        jsonGenericPlace.put("eventList", jsonEventList);
        JSONArray jsonPastsceneList = new JSONArray();
        for (Pastscene pastscene in plot.pastescenes) {
            JSONObject jsonPastscene = new JSONObject();
            jsonPastscene.put("pastsceneId", pastscene.id);
            jsonPastscene.put("pastsceneTitle", pastscene.title);
            jsonPastscene.put("pastsceneCheck", genericPlace.pastscenes.contains(pastscene));
            jsonPastsceneList.add(jsonPastscene);
        }
        jsonGenericPlace.put("pastsceneList", jsonPastsceneList);
        return jsonGenericPlace;
    }

    def saveOrUpdate(GenericPlace newGenericPlace, boolean isNew) {
        if (params.containsKey("plotId")) {
            Plot plot = Plot.get(params.plotId as Integer)
            newGenericPlace.plot = plot
        } else {
            return false
        }
        if (params.containsKey("placeCode")) {
            newGenericPlace.code = params.placeCode
        } else {
            return false
        }
        if (params.containsKey("placeDescription")) {
            newGenericPlace.comment = params.placeDescription
        } else {
            return false
        }
        if(newGenericPlace.extTags) {
            newGenericPlace.extTags.clear();
        } else {
            newGenericPlace.extTags = new HashSet<GenericPlaceHasTag>()
        }
        if(newGenericPlace.events) {
            newGenericPlace.events.clear();
        } else {
            newGenericPlace.events = new HashSet<Event>()
        }
        if(newGenericPlace.pastscenes) {
            newGenericPlace.pastscenes.clear();
        } else {
            newGenericPlace.pastscenes = new HashSet<Pastscene>()
        }

        newGenericPlace.save(flush: true);
        newGenericPlace = GenericPlace.findAllWhere("code": newGenericPlace.getCode()).first();

        params.each {
            if (it.key.startsWith("placeTags_")) {
                GenericPlaceHasTag genericPlaceHasTag = new GenericPlaceHasTag();
                Tag genericPlaceTag = Tag.get((it.key - "placeTags_") as Integer);
                genericPlaceHasTag.tag = genericPlaceTag
                genericPlaceHasTag.weight = TagService.LOCKED
                genericPlaceHasTag.genericPlace = newGenericPlace
                newGenericPlace.extTags.add(genericPlaceHasTag)
            }
            else if (it.key.startsWith("placeEvent_")) {
                Event event = Event.get((it.key - "placeEvent_") as Integer);
                event.genericPlace = newGenericPlace;
                event.save(flush: true);
                newGenericPlace.events.add(event);
            }
            else if (it.key.startsWith("placePastScene_")) {
                Pastscene pastscene = Pastscene.get((it.key - "placePastScene_") as Integer);
                pastscene.genericPlace = newGenericPlace;
                pastscene.save(flush: true);
                newGenericPlace.pastscenes.add(pastscene);
            }
        }
        newGenericPlace.save(flush: true);
        return true;
    }

    def show(Long id) {
        def genericPlaceInstance = GenericPlace.get(id)
        if (!genericPlaceInstance) {
            redirect(action: "list")
            return
        }

        [genericPlaceInstance: genericPlaceInstance]
    }

    def edit(Long id) {
        def genericPlaceInstance = GenericPlace.get(id)
        if (!genericPlaceInstance) {
            redirect(action: "list")
            return
        }

        [genericPlaceInstance: genericPlaceInstance]
    }

    def update(Long id) {
        GenericPlace genericPlace = GenericPlace.get(id)
        if (genericPlace) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(genericPlace, false))
            }
        }
    }

    def delete(Long id) {
        def genericPlaceInstance = GenericPlace.get(id)
        boolean isDelete = false;
        if (genericPlaceInstance) {
            for (Event event in genericPlaceInstance.events) {
                event.genericPlace = null;
                event.save(flush: true);
            }
            for (Pastscene pastscene in genericPlaceInstance.pastscenes) {
                pastscene.genericPlace = null;
                pastscene.save(flush: true);
            }
            genericPlaceInstance.delete(flush: true)
            isDelete = true;
        }

        render(contentType: "application/json") {
            object(isdelete: isDelete)
        }
    }
}
