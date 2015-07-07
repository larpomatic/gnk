package org.gnk.genericevent

import org.springframework.dao.DataIntegrityViolationException

class GenericEventController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
//        params.max = Math.min(max ?: 10, 100)
        def list = GenericEvent.list()
        [genericEventInstanceList: list, genericEventInstanceTotal: GenericEvent.count()]
    }

    def create() {
        [genericEventInstance: new GenericEvent(params)]
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

        [genericEventInstance: genericEventInstance]
    }

    def update(Long id, Long version) {
        def genericEventInstance = GenericEvent.get(id)
        if (!genericEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (genericEventInstance.version > version) {
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
        redirect(action: "show", id: genericEventInstance.id)
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
}
