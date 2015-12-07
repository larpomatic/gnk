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
        List<GenericEventHasTag> eventHasTagList = new ArrayList<>()
        List<GenericEventCanImplyTag> canImplyTagList = new ArrayList<>()
        List<GenericEventCanImplyGenericEvent> canImplyGenericEventList = new ArrayList<>()

        def list = GenericEvent.list()
        TagService tagService = new TagService();
        def listTag = tagService.getPlotTagQuery()

        GenericEvent genericEventInstance = new GenericEvent()
        genericEventInstance.id = -1
        genericEventInstance.version = 1

        [genericEventInstance: genericEventInstance,
         genericEventInstanceList: list,
         TagInstanceList: listTag,
         eventHasTagList : eventHasTagList,
         canImplyTagList : canImplyTagList,
         canImplyGenericEventList : canImplyGenericEventList]
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

        List<GenericEventHasTag> eventHasTagList = new ArrayList<>()
        List<GenericEventCanImplyTag> canImplyTagList = new ArrayList<>()
        List<GenericEventCanImplyGenericEvent> canImplyGenericEventList = new ArrayList<>()

        addGenericEventChoice(eventHasTagList, canImplyTagList,
                canImplyGenericEventList)

        addGenericEventSelected(eventHasTagList, canImplyTagList, canImplyGenericEventList)

        long idGenericEvent = id ?:
                params.genericEventId == null || params.genericEventId == "" ? -1 : params.genericEventId as long;
        GenericEvent genericEventInstance = getGenericEvent(idGenericEvent)

        def list = GenericEvent.list()
        TagService tagService = new TagService();
        def listTag = tagService.getPlotTagQuery()

        if (params.first == null){
            eventHasTagList.addAll(genericEventInstance.genericEventHasTag)
            canImplyTagList.addAll(genericEventInstance.genericEventCanImplyTag)
        }

        [genericEventInstance: genericEventInstance,
         genericEventInstanceList: list,
         TagInstanceList: listTag,
         eventHasTagList : eventHasTagList,
         canImplyTagList : canImplyTagList,
         canImplyGenericEventList : canImplyGenericEventList]
    }

    def update(Long genericEventId, Long genericEventVersion) {

        List<GenericEventHasTag> eventHasTagList = new ArrayList<>()
        List<GenericEventCanImplyTag> canImplyTagList = new ArrayList<>()
        List<GenericEventCanImplyGenericEvent> canImplyGenericEventeList = new ArrayList<>()

        addGenericEventSelected(eventHasTagList, canImplyTagList, canImplyGenericEventeList)


        def var = params.genericEventId ?: "-1"
        GenericEvent ge = getGenericEvent(var as long)

        //============ genericEventHasTag ==================
        ArrayList<GenericEventHasTag> temp = new ArrayList<>()
        for(GenericEventHasTag prev : ge.genericEventHasTag){
            if (eventHasTagList.find {it.tag.id == prev.tag.id && it.value == prev.value} == null){
                temp.add(prev)
            }
        }

        for(GenericEventHasTag prev : temp){
            ge.removeFromGenericEventHasTag(prev)
        }

        for (GenericEventHasTag future : eventHasTagList){
//            future.value = 100
            ge.addToGenericEventHasTag(future)
        }

        //============ genericEventCanImplyTag ==================

        ArrayList<GenericEventCanImplyTag> temp2 = new ArrayList<>()
        for(GenericEventCanImplyTag prev : ge.genericEventCanImplyTag){
            if (canImplyTagList.find {it.tag.id == prev.tag.id && it.value == prev.value} == null){
                temp2.add(prev)

            }
        }

        for(GenericEventCanImplyTag prev : temp2){
            ge.removeFromGenericEventCanImplyTag(prev)
        }

        for (GenericEventCanImplyTag future : canImplyTagList){
//            future.value = 100
            ge.addToGenericEventCanImplyTag(future)
        }

        if (!ge.save(flush: true)) {
            return render(view: "edit", model: [genericEventInstance: ge])
        }

        flash.messageInfo = message(code: 'default.updated.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), ge.title])
        redirect(action: "list")
    }

    void addGenericEventChoice(List<GenericEventHasTag> eventHasTagList,
                                       List<GenericEventCanImplyTag> canImplyTagList,
                                       List<GenericEventCanImplyGenericEvent> canImplyGenericEventList) {

        eventHasTagList.clear()
        canImplyTagList.clear()
        canImplyGenericEventList.clear()

        params.each {
            if (it.key.toString().contains("eventGenericTagsWeight") && it.value != null && it.value != "") {
                GenericEventHasTag genericEventHasTag = new GenericEventHasTag();
                genericEventHasTag.id = 0
                genericEventHasTag.tag = Tag.findById(it.key.toString().tokenize("_")[1].toInteger());
                genericEventHasTag.value = it.value.toString().toInteger();
                genericEventHasTag.dateCreated = new Date();
                genericEventHasTag.lastUpdated = new Date();
                genericEventHasTag.version = 1

                eventHasTagList.add(genericEventHasTag)
            }

            if (it.key.toString().contains("eventGenericImplyTagsWeight") && it.value != null && it.value != "") {
                GenericEventCanImplyTag temp = new GenericEventCanImplyTag();
                temp.id = 0
                temp.tag = Tag.findById(it.key.toString().tokenize("_")[1].toInteger());
                temp.value = it.value.toString().toInteger();
                temp.dateCreated = new Date();
                temp.lastUpdated = new Date();
                temp.version = 1

                canImplyTagList.add(temp)
            }
        };
    }

    void addGenericEventSelected(List<GenericEventHasTag> eventHasTagList,
                               List<GenericEventCanImplyTag> canImplyTagList,
                               List<GenericEventCanImplyGenericEvent> canImplyGenericEventList) {

        params.each {
            if (it.key.toString().contains("tableTag_") && it.value != null && it.value != "") {
                GenericEventHasTag genericEventHasTag = new GenericEventHasTag();
//                genericEventHasTag.id = 0
                def tokenize = it.value.toString().tokenize("_")
                genericEventHasTag.tag = Tag.findById(tokenize[0].toInteger());
                genericEventHasTag.value = tokenize[1].toInteger();
                genericEventHasTag.dateCreated = new Date();
                genericEventHasTag.lastUpdated = new Date();
                genericEventHasTag.version = 1

                eventHasTagList.add(genericEventHasTag)
            }

            if (it.key.toString().contains("tableImplyTag_") && it.value != null && it.value != "") {
                GenericEventCanImplyTag temp = new GenericEventCanImplyTag();
//                temp.id = 0
                def tokenize = it.value.toString().tokenize("_")
                temp.tag = Tag.findById(tokenize[0].toInteger());
                temp.value = tokenize[1].toInteger();

                temp.dateCreated = new Date();
                temp.lastUpdated = new Date();
                temp.version = 1

                canImplyTagList.add(temp)
            }
        };

    }
    private GenericEvent getGenericEvent(long id) {

        GenericEvent genericEventInstance = null;

    if (id != -1) {
        genericEventInstance = GenericEvent.get(id)
    }
//        else{
//        genericEventInstance = new GenericEvent()
//        genericEventInstance.version = 1
//    }
//
//        if (!genericEventInstance) {
//
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), id])
//            redirect(action: "list")
//            return
//        }

//        genericEventInstance = GenericEvent.findById(params.genericEventId as Integer)

        if (genericEventInstance == null) {
            genericEventInstance = new GenericEvent(params)
            if (params.genericEventId != null && params.genericEventId != ""){
                genericEventInstance.id = ((String) params.genericEventId)?.toInteger();
            }
//            else {
//                genericEventInstance.id = -1
//            }
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

       def delete(Long id) {
            def genericEventInstance = GenericEvent.get(id)
            String title = genericEventInstance.title

            if (!genericEventInstance) {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
                redirect(action: "list")
                return
            }

//            try {
                genericEventInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
                redirect(action: "list")
//            }
//            catch (DataIntegrityViolationException e) {
//                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
//                redirect(action: "show", id: id)
//            }
        }
}
