package org.gnk.resplacetime

import org.springframework.dao.DataIntegrityViolationException

class GenericResourceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [genericResourceInstanceList: GenericResource.list(params), genericResourceInstanceTotal: GenericResource.count()]
    }

    def create() {
        [genericResourceInstance: new GenericResource(params)]
    }

    def save() {
        def genericResourceInstance = new GenericResource(params)
        if (!genericResourceInstance.save(flush: true)) {
            render(view: "create", model: [genericResourceInstance: genericResourceInstance])
            return
        }

        flash.messageInfo = message(code: 'adminRef.genericResource.info.create', args: [genericResourceInstance.code])
        redirect(action: "list")
    }

    def show(Long id) {
        def genericResourceInstance = GenericResource.get(id)
        if (!genericResourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
            return
        }

        [genericResourceInstance: genericResourceInstance]
    }

    def edit(Long id) {
        def genericResourceInstance = GenericResource.get(id)
        if (!genericResourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
            return
        }

        [genericResourceInstance: genericResourceInstance]
    }

    def update(Long id, Long version) {
        def genericResourceInstance = GenericResource.get(id)
        if (!genericResourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (genericResourceInstance.version > version) {
                genericResourceInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'genericResource.label', default: 'GenericResource')] as Object[],
                          "Another user has updated this GenericResource while you were editing")
                render(view: "edit", model: [genericResourceInstance: genericResourceInstance])
                return
            }
        }

        genericResourceInstance.properties = params

        if (!genericResourceInstance.save(flush: true)) {
            render(view: "edit", model: [genericResourceInstance: genericResourceInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), genericResourceInstance.id])
        redirect(action: "show", id: genericResourceInstance.id)
    }

    def delete(Long id) {
        def genericResourceInstance = GenericResource.get(id)
        if (!genericResourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
            return
        }

        try {
            genericResourceInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "show", id: id)
        }
    }

    def deleteResource()
    {
        def genericResourceInstance = GenericResource.get(params.id)

        if (!genericResourceInstance) {

            flash.message = message(code: 'Erreur : Suppression échouée de la ressource !')
            redirect(action: "list")
            return
        }

        try {
            genericResourceInstance.delete(flush: true)
            flash.messageInfo = message(code: 'adminRef.genericResource.info.delete', args: [genericResourceInstance.code])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), params.id])
            redirect(action: "show", id: id)
        }


    }
}
