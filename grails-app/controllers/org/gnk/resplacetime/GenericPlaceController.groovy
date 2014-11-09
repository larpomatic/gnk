package org.gnk.resplacetime

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.selectintrigue.Plot
import org.gnk.tag.TagService
import org.gnk.tag.Tag
import org.gnk.tag.Univers

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
        GenericPlace genericPlace = new GenericPlace(params);
        Boolean res = saveOrUpdate(genericPlace);
//        genericPlace = GenericPlace.findAllWhere("code": genericPlace.getCode(), "plot": ).first();
        def placeTagList = new TagService().getPlaceTagQuery();
        def jsonTagList = buildTagList(placeTagList);
        def jsonGenericPlace = buildJson(genericPlace);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("genericPlace", jsonGenericPlace);
        object.put("genericPlaceTagList", jsonTagList);
        render(contentType: "application/json") {
            object
        }
    }

    def getBestPlaces() {
        org.gnk.ressplacetime.GenericPlace genericplace = new org.gnk.ressplacetime.GenericPlace();
        List<com.gnk.substitution.Tag> tags = new ArrayList<>();
        params.each {
            if (it.key.startsWith("placeTags_")) {
                // TODO create TAG into genericplace
                com.gnk.substitution.Tag tag = new com.gnk.substitution.Tag();
                Tag genericPlaceTag = Tag.get((it.key - "placeTags_") as Integer);
                tag.value = genericPlaceTag.name;
                tag.weight =  params.get("placeTagsWeight_" + genericPlaceTag.id) as Integer;
                tags.add(tag);
            }
        }
        genericplace.setTagList(tags);
        List<Univers> univers = Univers.getAll();
        PlaceService placeservice = new PlaceService();
        for (Univers u : univers)
        {
            genericplace = placeservice.findReferentialPlace(genericplace, u.name);
            genericplace.resultList;
        }
        final JSONObject object = new JSONObject();
        object.put("value", jsonTagList.size());
        render(contentType: "application/json") {
            object;
        }
    }

    def buildTagList(def genericPlaceTagList) {
        JSONArray jsonTagList = new JSONArray();
        for (genericPlaceTag in genericPlaceTagList) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", genericPlaceTag.getId());
            jsonTag.put("name", genericPlaceTag.getName());
            if (genericPlaceTag.children && genericPlaceTag.children.size() != 0) {
                JSONArray jsonTagChildren = buildTagList(genericPlaceTag.children);
                jsonTag.put("children", jsonTagChildren);
            }
            jsonTagList.add(jsonTag);
        }
        return jsonTagList;
    }

    def buildJson(GenericPlace genericPlace) {
        JSONObject jsonGenericPlace = new JSONObject();
        jsonGenericPlace.put("code", genericPlace.getCode());
        jsonGenericPlace.put("id", genericPlace.getId());
        jsonGenericPlace.put("plotId", genericPlace.getPlot().getId());
        jsonGenericPlace.put("comment", genericPlace.getComment());
        jsonGenericPlace.put("placeObject", genericPlace.getObjectType().getId());

        JSONArray jsonTagList = new JSONArray();
        for (GenericPlaceHasTag genericPlaceHasTag in genericPlace.extTags) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", genericPlaceHasTag.tag.id)
            jsonTag.put("weight", genericPlaceHasTag.weight)
            jsonTagList.add(jsonTag);
        }
        jsonGenericPlace.put("tagList", jsonTagList);
        return jsonGenericPlace;
    }

    def saveOrUpdate(GenericPlace newGenericPlace) {
//        Plot plot = null;
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
        if (params.containsKey("placeObject")) {
            ObjectType objectType = ObjectType.findById(params.placeObject as Integer);
            newGenericPlace.objectType = objectType;
        }
        else {
            ObjectType objectType = ObjectType.findById(1);
            newGenericPlace.objectType = objectType;
        }
        if (params.containsKey("placeDescription")) {
            newGenericPlace.comment = params.placeDescription
        } else {
            return false
        }
        if(newGenericPlace.extTags != null) {
            HashSet<GenericPlaceHasTag> genericPlaceHasTag = newGenericPlace.extTags;
            newGenericPlace.extTags.clear();
            GenericPlaceHasTag.deleteAll(genericPlaceHasTag);
        } else {
            newGenericPlace.extTags = new HashSet<GenericPlaceHasTag>();
        }
        newGenericPlace.save(flush: true);
//        newGenericPlace = GenericPlace.findAllWhere("code": newGenericPlace.getCode(), "plot": plot).first();

        params.each {
            if (it.key.startsWith("placeTags_")) {
                GenericPlaceHasTag genericPlaceHasTag = new GenericPlaceHasTag();
                Tag genericPlaceTag = Tag.get((it.key - "placeTags_") as Integer);
                genericPlaceHasTag.tag = genericPlaceTag
                genericPlaceHasTag.weight = params.get("placeTagsWeight_" + genericPlaceTag.id) as Integer;
                genericPlaceHasTag.genericPlace = newGenericPlace
                newGenericPlace.extTags.add(genericPlaceHasTag)
            }
        }
        newGenericPlace.save(flush: true);
        return true;
    }

    def show(Long id) {
        def genericPlaceInstance = GenericPlace.get(id)
        if (!genericPlaceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
            return
        }

        [genericPlaceInstance: genericPlaceInstance]
    }

    def edit(Long id) {
        def genericPlaceInstance = GenericPlace.get(id)
        if (!genericPlaceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
            return
        }

        [genericPlaceInstance: genericPlaceInstance]
    }

    def update(Long id) {
        GenericPlace genericPlace = GenericPlace.get(id);
        String oldname = genericPlace.code;
        if (genericPlace) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(genericPlace),
                        id: genericPlace.id,
                        name: genericPlace.code,
                        oldname: oldname)
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
