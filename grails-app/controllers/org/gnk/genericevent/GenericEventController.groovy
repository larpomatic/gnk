package org.gnk.genericevent

import org.gnk.tag.Tag
import org.gnk.tag.TagService

class GenericEventController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
//        redirect(action: "list", params: params)
        redirect(action: "list")
    }

    def list(Integer max) {
//        params.max = Math.min(max ?: 10, 100)
        def list = GenericEvent.list()
        [genericEventInstanceList: list, genericEventInstanceTotal: GenericEvent.count()]
    }

    def create() {
        def list = GenericEvent.list()
        TagService tagService = new TagService();
        def listTag = tagService.getPlotTagQuery()
        [genericEventInstance: new GenericEvent(params), genericEventInstanceList: list, TagInstanceList: listTag]
    }

    def save() {
        def genericEventInstance = new GenericEvent(params)
        genericEventInstance.genericEventHasTag.first().tag.name
        if (!genericEventInstance.save(flush: true)) {
            return render(view: "create", model: [genericEventInstance: genericEventInstance])
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), genericEventInstance.id])
        redirect(action: "show", id: genericEventInstance.id)
    }

    def show(Long id) {
//        def genericEventInstance = GenericEvent.get(id)
//        if (!genericEventInstance) {
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), id])
//            redirect(action: "list")
//            return
//        }
//
//
        redirect(action: "list", params: params)
    }

    def edit(Long id) {
        GenericEvent genericEventInstance;
        if (params.genericEventHasTagAdd == "true") {
            genericEventInstance = addGenericEventHasTag()
        } else {
            genericEventInstance = GenericEvent.get(id)

            if (!genericEventInstance) {

                flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), id])
                redirect(action: "list")
                return
            }
        }
        def list = GenericEvent.list()
        TagService tagService = new TagService();
        def listTag = tagService.getPlotTagQuery()

        List<GenericEventHasTag> eventHasTagList = new ArrayList<>()
        List<GenericEventCanImplyTag> canImplyTagList = new ArrayList<>()
        List<GenericEventCanImplyGenericEvent> canImplyGenericEventList = new ArrayList<>()

        [genericEventInstance: genericEventInstance,
         genericEventInstanceList: list,
         TagInstanceList: listTag,
         eventHasTagList : eventHasTagList,
         canImplyTagList : canImplyTagList,
         canImplyGenericEventList : canImplyGenericEventList]
    }

    def update(Long genericEventId, Long genericEventVersion) {
//        def genericEventInstance = GenericEvent.get(genericEventId)
//        def genericEventInstance = addGenericEventHasTag()

        List<GenericEventHasTag> eventHasTagList = new ArrayList<>()
        List<GenericEventCanImplyTag> canImplyTagList = new ArrayList<>()
        List<GenericEventCanImplyGenericEvent> canImplyGenericEventeList = new ArrayList<>()

        def genericEventInstance = addGenericEventHasTag(eventHasTagList, canImplyTagList,
                canImplyGenericEventeList)

//        eventHasTagList.each {genericEventInstance.addToGenericEventHasTag(it)}

        //genericEventHasTag
        for(GenericEventHasTag prev : genericEventInstance.genericEventHasTag){
            if (eventHasTagList.find {it.tag.id == prev.tag.id && it.value == prev.value} == null){
                genericEventInstance.removeFromGenericEventHasTag(prev)
            }
        }

        for (GenericEventHasTag future : eventHasTagList){
            if (genericEventInstance.genericEventHasTag.find {it.tag.id == future.tag.id && it.value == future.value} == null){
                genericEventInstance.addToGenericEventHasTag(future)
            }
        }

        //genericEventCanImplyTag
        for(GenericEventCanImplyTag prev : genericEventInstance.genericEventCanImplyTag){
            if (canImplyTagList.find {it.tag.id == prev.tag.id && it.value == prev.value} == null){
                genericEventInstance.removeFromGenericEventCanImplyTag(prev)
            }
        }

        for (GenericEventHasTag future : eventHasTagList){
            if (genericEventInstance.genericEventCanImplyTag.find {it.tag.id == future.tag.id && it.value == future.value} == null){
                genericEventInstance.addToGenericEventCanImplyGenericEvent(future)
            }
        }

        if (!genericEventInstance.save(flush: true)) {
            return render(view: "edit", model: [genericEventInstance: genericEventInstance])
        }

        flash.messageInfo = message(code: 'default.updated.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), genericEventInstance.title])
        redirect(action: "list")
    }

    GenericEvent addGenericEventHasTag(List<GenericEventHasTag> eventHasTagList,
                                       List<GenericEventCanImplyTag> canImplyTagList,
                                       List<GenericEventCanImplyGenericEvent> canImplyGenericEventeList) {

        GenericEvent genericEventInstance

        genericEventInstance = getGenericEvent()

        genericEventInstance.genericEventHasTag.clear()
        genericEventInstance.genericEventCanImplyTag.clear()
        genericEventInstance.genericEventCanImplyGenericEvent.clear()

        params.each {
            if (it.key.toString().contains("eventGenericTagsWeight") && it.value != null && it.value != "") {
                GenericEventHasTag genericEventHasTag = new GenericEventHasTag();
                genericEventHasTag.id = 0
                genericEventHasTag.tag = Tag.findById(it.key.toString().tokenize("_")[1].toInteger());
                genericEventHasTag.value = it.value.toString().toInteger();
                genericEventHasTag.dateCreated = new Date();
                genericEventHasTag.lastUpdated = new Date();
                genericEventHasTag.version = 1

//                genericEventInstance.addToGenericEventHasTag(genericEventHasTag);
//                genericEventInstance.genericEventHasTag.add(genericEventHasTag)
                eventHasTagList.add(genericEventHasTag)
            }

            if (it.key.toString().contains("eventGenericImplyTagsWeight") && it.value != null && it.value != "") {
                GenericEventCanImplyTag temp = new GenericEventCanImplyTag();
//                temp.id = 0
                temp.tag = Tag.findById(it.key.toString().tokenize("_")[1].toInteger());
                temp.value = it.value.toString().toInteger();
                temp.dateCreated = new Date();
                temp.lastUpdated = new Date();
                temp.version = 1

//                genericEventInstance.addToGenericEventCanImplyTag(temp);
//                genericEventInstance.genericEventCanImplyTag.add(temp);
                canImplyTagList.add(temp)
            }
        };
        return genericEventInstance
    }

    private GenericEvent getGenericEvent() {
        GenericEvent genericEventInstance
        genericEventInstance = GenericEvent.findById(params.genericEventId as Integer)

        if (genericEventInstance == null) {
            genericEventInstance = new GenericEvent(params)
            genericEventInstance.id = ((String) params.genericEventId)?.toInteger();
            genericEventInstance.version = 1//((String)params.genericEventVersion).toInteger();
            genericEventInstance.lastUpdated = new Date();
            genericEventInstance.dateCreated = new Date();

            genericEventInstance.genericEventHasTag = new HashSet<>()
            genericEventInstance.genericEventCanImplyTag = new HashSet<>()
            genericEventInstance.genericEventCanImplyGenericEvent = new HashSet<>()
        } else {
            genericEventInstance.properties = params
        }
        return genericEventInstance
    }

//    def delete(Long id) {
//        //TODO faire une liste local et supprimer dans la liste local, renvoyer la liste pour refaire le tableau
//        def genericEventInstance = GenericEvent.get(id)
//        String title = genericEventInstance.title
//
//        if (!genericEventInstance) {
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
//            redirect(action: "list")
//            return
//        }
//
//        try {
////            genericEventInstance.delete(flush: true)
//            flash.message = message(code: 'default.deleted.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
//            redirect(action: "list")
//        }
//        catch (DataIntegrityViolationException e) {
//            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
//            redirect(action: "show", id: id)
//        }
//    }
//
//    def deleteGenericEventHasTag(Long id) {
//        GenericEventHasTag eventHasTag = GenericEventHasTag.findById(id)
//        eventHasTag.delete()
////        flash.messageInfo = message(code: 'adminRef.genericPlace.info.deleteTag', args: [placeHasTagInstance.place.name, placeHasTagInstance.tag.name])
////        redirect(action: "list")
//    }
//
//    def deleteGenericEventImplyTag(Long id) {
//        GenericEventCanImplyTag canImplyGenericEvent = GenericEventCanImplyTag.findById(id)
//        canImplyGenericEvent.delete()
////        flash.messageInfo = message(code: 'adminRef.genericPlace.info.deleteTag', args: [placeHasTagInstance.place.name, placeHasTagInstance.tag.name])
////        redirect(action: "list")
//    }
//
//    def deleteGenericEventImplyGenericEvent(Long id) {
//        GenericEventCanImplyGenericEvent canImplyGenericEvent = GenericEventCanImplyGenericEvent.findById(id)
//        canImplyGenericEvent.delete()
////        flash.messageInfo = message(code: 'adminRef.genericPlace.info.deleteTag', args: [placeHasTagInstance.place.name, placeHasTagInstance.tag.name])
////        redirect(action: "list")
//    }

}
