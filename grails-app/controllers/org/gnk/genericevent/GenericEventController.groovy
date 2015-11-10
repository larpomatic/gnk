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


        [genericEventInstance: genericEventInstance, genericEventInstanceList: list, TagInstanceList: listTag]
    }

    def update(Long genericEventId, Long genericEventVersion) {
//        def genericEventInstance = GenericEvent.get(genericEventId)
//        def genericEventInstance = addGenericEventHasTag()

        List<GenericEventHasTag> eventHasTagList = new ArrayList<>()
        List<GenericEventCanImplyTag> canImplyTagList = new ArrayList<>()
        List<GenericEventCanImplyGenericEvent> canImplyGenericEventeList = new ArrayList<>()

        addGenericEventHasTag(eventHasTagList, canImplyTagList, canImplyGenericEventeList)

//        if (genericEventInstance == null) {
//            flash.message = message(code: 'default.not.found.message',
//                    args: [message(code: 'genericEvent.label', default: 'GenericEvent'), genericEventId])
//            redirect(action: "list")
//            return
//        }
//
//        if (genericEventVersion != null) {
//            if (genericEventInstance.version > genericEventVersion) {
//
//                genericEventInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
//                        [message(code: 'genericEvent.label', default: 'GenericEvent')] as Object[],
//                        "Another user has updated this GenericEvent while you were editing")
//                render(view: "edit", model: [genericEventInstance: genericEventInstance])
//                return
//            }
//        }
//
//        if (genericEventInstance.ageMax < genericEventInstance.ageMin){
//            flash.message = "Age mininimum < Age maximum";
//            return render(view: "edit", model: [genericEventInstance: genericEventInstance])
//        }

//        GenericEvent ge = GenericEvent.findById(genericEventInstance.id)
//
//        if (ge == null) {
//            ge = getGenericEvent()
//        }
//
//        ge.ageMax = genericEventInstance.ageMax
//        ge.ageMin = genericEventInstance.ageMin
//        ge.description = genericEventInstance.description
//        ge.title = genericEventInstance.title
//
//        //genericEventHasTag
//        if (ge != null){
//            for(def tmp : ge.genericEventHasTag) {
//                if (ge != null && genericEventInstance.genericEventHasTag.find {it.id == tmp.id} == null) {
//                    ge.removeFromGenericEventHasTag(tmp)
//                }
//            }
//        }
//
//        for(def tmp :genericEventInstance.genericEventHasTag) {
//            if (ge != null && !ge.genericEventHasTag.contains(tmp)) {
//                ge.addToGenericEventHasTag(tmp)
//            }
//            else {
////                if (ge == null){
//                    ge.addToGenericEventHasTag(tmp)
////                }
//            }
//        }
//
//        //genericEventCanImplyTag
//        if (ge != null){
//            for(def tmp : ge.genericEventCanImplyTag) {
//                if (ge != null && !genericEventInstance.genericEventCanImplyTag.find {it.id == tmp.id} == null) {
//                    ge.removeFromGenericEventCanImplyGenericEvent(tmp)
//                }
//            }
//        }
//        for(def tmp :genericEventInstance.genericEventCanImplyTag) {
//            if (ge != null && !ge.genericEventCanImplyTag.contains(tmp)) {
//                ge.addToGenericEventCanImplyTag(tmp)
//            }
//            else {
//                if (ge == null){
//                    ge.addToGenericEventCanImplyTag(tmp)
//                }
//            }
//        }
//
//        //genericEventCanImplyGenericEvent
//        if (ge != null){
//            for(def tmp : ge.genericEventCanImplyGenericEvent) {
//                if (ge != null && !genericEventInstance.genericEventCanImplyGenericEvent.find {it.id == tmp.id} == null) {
//                    ge.removeFromGenericEventCanImplyGenericEvent(tmp)
//                }
//            }
//        }
//        for(def tmp : genericEventInstance.genericEventCanImplyGenericEvent) {
//            if (ge != null && !ge.genericEventCanImplyGenericEvent.contains(tmp)) {
//                ge.addToGenericEventCanImplyGenericEvent(tmp)
//            }
//            else {
//                if (ge == null){
//                    ge.addToGenericEventCanImplyGenericEvent(tmp)
//                }
//            }
//        }

        GenericEvent ge = getGenericEvent()
        ge.genericEventHasTag.each {ge.removeFromGenericEventHasTag(it)}


        eventHasTagList.each {ge.addToGenericEventHasTag(it)}


        if (!ge.save(flush: true)) {
            return render(view: "edit", model: [genericEventInstance: ge])
        }

        flash.messageInfo = message(code: 'default.updated.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), genericEventInstance.title])
        redirect(action: "list")
    }

    GenericEvent addGenericEventHasTag() {

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
                genericEventInstance.genericEventHasTag.add(genericEventHasTag)
            }

//            if (it.key.toString().contains("eventGenericImplyTagsWeight") && it.value != null && it.value != "") {
//                GenericEventCanImplyTag temp = new GenericEventCanImplyTag();
//                temp.id = 0
//                temp.tag = Tag.findById(it.key.toString().tokenize("_")[1].toInteger());
//                temp.value = it.value.toString().toInteger();
//                temp.dateCreated = new Date();
//                temp.lastUpdated = new Date();
//                temp.version = 1
//
//                genericEventInstance.addToGenericEventCanImplyTag(temp);
////                genericEventInstance.genericEventCanImplyTag.add(temp);
//            }
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
