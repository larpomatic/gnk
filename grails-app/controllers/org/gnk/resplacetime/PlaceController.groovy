package org.gnk.resplacetime

import org.gnk.tag.Tag
import org.springframework.dao.DataIntegrityViolationException

class PlaceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [placeInstanceList: Place.list(params), placeInstanceTotal: Place.count()]
    }

    def create() {
        [placeInstance: new Place(params)]
    }

    def save() {
        def placeInstance = new Place(params)

        print params
        placeInstance.description = params.desc
        placeInstance.gender = params.gender_select
        placeInstance.genericPlace = GenericPlace.get(params.genericPlace_select)

        if (!placeInstance.save(flush: true)) {
            render(view: "create", model: [placeInstance: placeInstance])
            return
        }

        flash.messageInfo = message(code: 'default.created.message', args: [message(code: 'place.label', default: 'Place'), placeInstance.id])
        redirect(action: "list")
    }


    def addTagToPlace() {
        Place placeInstance = Place.get(params.place_select)
        Tag tagInstance = Tag.get(params.tag_select)

        PlaceHasTag placeHasTag = new PlaceHasTag()
        placeHasTag.tag = tagInstance
        placeHasTag.place = placeInstance
        placeHasTag.weight = Integer.parseInt(params.weight)

        if (!placeHasTag.save(flush: true)) {
            render(view: "list")
            flash.message = "Erreur lors de l'ajout du tag sur le lieu"
            return
        }

        flash.messageInfo = message(code: 'adminRef.genericPlace.info.addTag', args: [placeInstance.name, tagInstance.name])
        redirect(action: "list")

    }


    def deleteTag() {
        PlaceHasTag placeHasTagInstance = PlaceHasTag.get(params.id)
        placeHasTagInstance.delete()
        flash.messageInfo = message(code: 'adminRef.genericPlace.info.deleteTag', args: [placeHasTagInstance.place.name, placeHasTagInstance.tag.name])
        redirect(action: "list")

    }



    def show(Long id) {
        def placeInstance = Place.get(id)
        if (!placeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'place.label', default: 'Place'), id])
            redirect(action: "list")
            return
        }

        [placeInstance: placeInstance]
    }

    def showByName(String name) {
        Place p = Place.findByName(params.name)
        def placeInstance = Place.get(p.id)
        print("PIERRE ---- " + p.id + " " + p.name + " ---- PIERRE")
        if (!placeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'place.label', default: 'Place'), p.id])
            redirect(action: "list")
            return
        }

        [placeInstance: placeInstance]
    }

    def edit(Long id) {
        def placeInstance = Place.get(id)
        if (!placeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'place.label', default: 'Place'), id])
            redirect(action: "list")
            return
        }

        [placeInstance: placeInstance]
    }

    def update(Long id, Long version) {
        def placeInstance = Place.get(id)
        if (!placeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'place.label', default: 'Place'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (placeInstance.version > version) {
                placeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'place.label', default: 'Place')] as Object[],
                          "Another user has updated this Place while you were editing")
                render(view: "edit", model: [placeInstance: placeInstance])
                return
            }
        }

        placeInstance.properties = params

        if (!placeInstance.save(flush: true)) {
            render(view: "edit", model: [placeInstance: placeInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'place.label', default: 'Place'), placeInstance.id])
        redirect(action: "show", id: placeInstance.id)
    }

    def delete(Long id) {
        def placeInstance = Place.get(id)
        if (!placeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'place.label', default: 'Place'), id])
            redirect(action: "list")
            return
        }

        try {
            placeInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'place.label', default: 'Place'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'place.label', default: 'Place'), id])
            redirect(action: "show", id: id)
        }
    }
}
