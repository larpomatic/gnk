package org.gnk.genericevent
import org.gnk.tag.TagService
import org.springframework.dao.DataIntegrityViolationException

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
        [genericEventInstance: new GenericEvent(params), genericEventInstanceList: list, TagInstanceList : listTag]
    }

    def save() {
        def genericEventInstance = new GenericEvent(params)
        genericEventInstance.genericEventHasTag.first().tag.name
        if (!genericEventInstance.save(flush: true)) {
            render(view: "create", model: [genericEventInstance: genericEventInstance])
            return
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
        def genericEventInstance = GenericEvent.get(id)
        if (!genericEventInstance) {

            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), id])
            redirect(action: "list")
            return
        }
        def list = GenericEvent.list()
        TagService tagService = new TagService();
        def listTag = tagService.getPlotTagQuery()


        [genericEventInstance: genericEventInstance, genericEventInstanceList: list, TagInstanceList : listTag]
    }

    def update(Long genericEventId, Long genericEventVersion) {
       def genericEventInstance = GenericEvent.get(genericEventId)

        if (!genericEventInstance) {
            flash.message = message(code: 'default.not.found.message',
                    args: [message(code: 'genericEvent.label', default: 'GenericEvent'), genericEventId])
            redirect(action: "list")
            return
        }

        if (genericEventVersion != null) {
            if (genericEventInstance.version > genericEventVersion) {

                genericEventInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'genericEvent.label', default: 'GenericEvent')] as Object[],
                        "Another user has updated this GenericEvent while you were editing")
                render(view: "edit", model: [genericEventInstance: genericEventInstance])
                return
            }
        }

        genericEventInstance.properties = params

        if (!genericEventInstance.save(flush: true)) {
            render(view: "edit", model: [genericEventInstance: genericEventInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), genericEventInstance.id])
//        redirect(action: "show", id: genericEventInstance.id)
        redirect(action: "list")
    }

    def delete(Long id) {
        def genericEventInstance = GenericEvent.get(id)
        String title = genericEventInstance.title

        if (!genericEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
            redirect(action: "list")
            return
        }

        try {
//            genericEventInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), title])
            redirect(action: "show", id: id)
        }
    }

    def deleteGenericEventHasTag(Long id) {
        GenericEventHasTag eventHasTag = GenericEventHasTag.findById(id)
        eventHasTag.delete()
//        flash.messageInfo = message(code: 'adminRef.genericPlace.info.deleteTag', args: [placeHasTagInstance.place.name, placeHasTagInstance.tag.name])
//        redirect(action: "list")
    }

    def deleteGenericEventImplyTag(Long id) {
        GenericEventCanImplyTag canImplyGenericEvent = GenericEventCanImplyTag.findById(id)
        canImplyGenericEvent.delete()
//        flash.messageInfo = message(code: 'adminRef.genericPlace.info.deleteTag', args: [placeHasTagInstance.place.name, placeHasTagInstance.tag.name])
//        redirect(action: "list")
    }

    def deleteGenericEventImplyGenericEvent(Long id) {
        GenericEventCanImplyGenericEvent canImplyGenericEvent = GenericEventCanImplyGenericEvent.findById(id)
        canImplyGenericEvent.delete()
//        flash.messageInfo = message(code: 'adminRef.genericPlace.info.deleteTag', args: [placeHasTagInstance.place.name, placeHasTagInstance.tag.name])
//        redirect(action: "list")
    }
}
