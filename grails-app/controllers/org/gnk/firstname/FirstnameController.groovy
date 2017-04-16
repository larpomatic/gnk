package org.gnk.firstname

import org.gnk.naming.Firstname
import org.gnk.naming.FirstnameHasTag
import org.gnk.tag.TagService

class FirstnameController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def index() {
        redirect(action: "list")
    }
    def list(String sort) {
        def firstnames = Firstname.list()
        [FirstnameInstanceList: firstnames]            }
    def create() {
        List<FirstnameHasTag> FirstnameHasTagList = new ArrayList<>()
        def list = Firstname.list()
        TagService tagService = new TagService();
        def listTag = tagService.getPlotTagQuery()

        Firstname FirstnameInstance = new Firstname()
        FirstnameInstance.id = -1
        FirstnameInstance.version = 1

        [FirstnameInstance: FirstnameInstance,
         FirstnameInstanceList: list,
         TagInstanceList: listTag,
         FirstnameHasTagList : FirstnameHasTagList]
    }

    def save() {
        def FirstnameInstance= new Firstname(params)
        //NameInstance.NameHasTag.first().tag.name
        if (!FirstnameInstance.save(flush: true)) {
            // return render(view: "create", model: [NameInstance: NameInstance])
        }

        // flash.message = message(code: 'default.created.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), genericEventInstance.id])
        redirect(action: "show", id: FirstnameInstance.id)
    }
    def show(Long id) {
        redirect(action: "list", params: params)
    }


    def delete(Long id) {
        def FirstnameInstance =Firstname.get(id)
        String firstname = FirstnameInstance.name

        if (!FirstnameInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'firstname.label', default: 'Firstname'), firstname])
            redirect(action: "list")
            return
        }

        FirstnameInstance.delete(flush: true)
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'firstname.label', default: 'Name'), firstname])
        redirect(action: "list")
    }
}