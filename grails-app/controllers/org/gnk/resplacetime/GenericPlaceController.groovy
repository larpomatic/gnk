package org.gnk.resplacetime

import org.springframework.dao.DataIntegrityViolationException

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
        def genericPlaceInstance = new GenericPlace(params)
        if (!genericPlaceInstance.save(flush: true)) {
            render(view: "create", model: [genericPlaceInstance: genericPlaceInstance])
            return
        }

        flash.messageInfo = message(code: 'adminRef.genericPlace.info.create', args: [genericPlaceInstance.code])
        redirect(action: "list")
    }

    def show(Long id) {
        def genericPlaceInstance = GenericPlace.get(id)
        if (!genericPlaceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericPlace.label', default: 'GenericPlace'), id])
            redirect(action: "list")
            return
        }

        [genericPlaceInstance: genericPlaceInstance]
    }

    def edit(Long id) {
        def genericPlaceInstance = GenericPlace.get(id)
        if (!genericPlaceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericPlace.label', default: 'GenericPlace'), id])
            redirect(action: "list")
            return
        }

        [genericPlaceInstance: genericPlaceInstance]
    }

    def update(Long id, Long version) {
        def genericPlaceInstance = GenericPlace.get(id)
        if (!genericPlaceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericPlace.label', default: 'GenericPlace'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (genericPlaceInstance.version > version) {
                genericPlaceInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'genericPlace.label', default: 'GenericPlace')] as Object[],
                        "Another user has updated this GenericPlace while you were editing")
                render(view: "edit", model: [genericPlaceInstance: genericPlaceInstance])
                return
            }
        }

        genericPlaceInstance.properties = params

        if (!genericPlaceInstance.save(flush: true)) {
            render(view: "edit", model: [genericPlaceInstance: genericPlaceInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'genericPlace.label', default: 'GenericPlace'), genericPlaceInstance.id])
        redirect(action: "show", id: genericPlaceInstance.id)
    }

    def delete(Long id) {
        def genericPlaceInstance = GenericPlace.get(id)
        if (!genericPlaceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericPlace.label', default: 'GenericPlace'), id])
            redirect(action: "list")
            return
        }

        try {
            genericPlaceInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'genericPlace.label', default: 'GenericPlace'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'genericPlace.label', default: 'GenericPlace'), id])
            redirect(action: "show", id: id)
        }
    }
}
