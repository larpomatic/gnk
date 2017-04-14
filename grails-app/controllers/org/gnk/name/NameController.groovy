package org.gnk.name

import org.gnk.genericevent.GenericEvent
import org.gnk.naming.Name
import org.gnk.naming.NameHasTag
import org.gnk.tag.TagService

class NameController {
        static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
        def index() {
            redirect(action: "list")
        }
        def list(String sort) {
            def names = Name.list()
            [NameInstanceList: names]            }
def create() {
    List<NameHasTag> NameHasTagList = new ArrayList<>()
    def list = Name.list()
    TagService tagService = new TagService();
    def listTag = tagService.getPlotTagQuery()

    Name NameInstance = new Name()
    NameInstance.id = -1
    NameInstance.version = 1

    [NameInstance: NameInstance,
     NameInstanceList: list,
     TagInstanceList: listTag,
     NameHasTagList : NameHasTagList]
}

def save() {
    def NameInstance = new Name(params)
    //NameInstance.NameHasTag.first().tag.name
    if (!NameInstance.save(flush: true)) {
       // return render(view: "create", model: [NameInstance: NameInstance])
    }

   // flash.message = message(code: 'default.created.message', args: [message(code: 'genericEvent.label', default: 'GenericEvent'), genericEventInstance.id])
   redirect(action: "show", id: NameInstance.id)
}
    def show(Long id) {
        redirect(action: "list", params: params)
    }


    def delete(Long id) {
        def NameInstance = Name.get(id)
        String name = NameInstance.name

        if (!NameInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'name.label', default: 'Name'), name])
            redirect(action: "list")
            return
        }

        NameInstance.delete(flush: true)
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'name.label', default: 'Name'), name])
        redirect(action: "list")
          }
    }