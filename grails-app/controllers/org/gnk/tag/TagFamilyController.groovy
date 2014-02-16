package org.gnk.tag

import org.springframework.dao.DataIntegrityViolationException

class TagFamilyController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        print params
        if (params.sort == null) {
            params.sort = "name"
        }

        def resultList = TagFamily.list().sort{
            a, b ->
                if (params.order == 'asc') {
                    b.value.toLowerCase() <=> a.value.toLowerCase()
                } else {
                    a.value.toLowerCase() <=> b.value.toLowerCase()
                }
        }
        [tagFamilyInstanceList: resultList, tagFamilyInstanceTotal: TagFamily.count()]
    }

    def create() {
        [tagFamilyInstance: new TagFamily(params)]
    }

    def save() {
		print params
		if (params.value.equals(""))
		{
			print "empty tagFamily name"
			flash.message = message(code: 'Erreur : Le nom de la famille ne peut etre vide !')
			redirect(action: "list")
			return
		}
		
		for (TagFamily tagFamily : TagFamily.list())
		{
			if (params.value.equals(tagFamily.value))
			{
				print "TagFamily already exist"
				flash.message = message(code: 'Erreur : Une famille portant ce nom existe d�j� !')
				redirect(action: "list")
				return
			}
		}



        def tagFamilyInstance = checkIfRelevant()

        if (!tagFamilyInstance.save(flush: true)) {
            render(view: "create", model: [tagFamilyInstance: tagFamilyInstance])
            return
        }

        flash.messageInfo = message(code: 'adminRef.tagFamily.info.create', args: [tagFamilyInstance.value])
        redirect(action: "list")
    }

    def TagFamily checkIfRelevant()
    {
        def tagFamilyInstance = new TagFamily()
        tagFamilyInstance.value = params.name

        if (params.isRoleTag.equals("on"))
            tagFamilyInstance.relevantRole = true
        if (params.isPlotTag.equals("on"))
            tagFamilyInstance.relevantPlot = true
        if (params.isResourceTag.equals("on"))
            tagFamilyInstance.relevantResource = true

        return tagFamilyInstance
    }

    def show(Long id) {
        def tagFamilyInstance = TagFamily.get(id)
        if (!tagFamilyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagFamily.label', default: 'TagFamily'), id])
            redirect(action: "list")
            return
        }

        [tagFamilyInstance: tagFamilyInstance]
    }

    def edit(Long id) {
        def tagFamilyInstance = TagFamily.get(id)
        if (!tagFamilyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagFamily.label', default: 'TagFamily'), id])
            redirect(action: "list")
            return
        }

        [tagFamilyInstance: tagFamilyInstance]
    }

    def update(Long id, Long version) {
        def tagFamilyInstance = TagFamily.get(id)
        if (!tagFamilyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagFamily.label', default: 'TagFamily'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tagFamilyInstance.version > version) {
                tagFamilyInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tagFamily.label', default: 'TagFamily')] as Object[],
                          "Another user has updated this TagFamily while you were editing")
                render(view: "edit", model: [tagFamilyInstance: tagFamilyInstance])
                return
            }
        }

        tagFamilyInstance.properties = params

        if (!tagFamilyInstance.save(flush: true)) {
            render(view: "edit", model: [tagFamilyInstance: tagFamilyInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tagFamily.label', default: 'TagFamily'), tagFamilyInstance.id])
        redirect(action: "show", id: tagFamilyInstance.id)
    }

    def delete(Long id) {
        def tagFamilyInstance = TagFamily.get(id)
        if (!tagFamilyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagFamily.label', default: 'TagFamily'), id])
            redirect(action: "list")
            return
        }

        try {
            tagFamilyInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tagFamily.label', default: 'TagFamily'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tagFamily.label', default: 'TagFamily'), id])
            redirect(action: "show", id: id)
        }
    }
	
	def deleteFamily(Long idFamily) {
		def tagFamilyInstance = TagFamily.get(idFamily)
		tagFamilyInstance.delete()
		flash.messageInfo = message(code: 'adminRef.tagFamily.info.delete', args: [tagFamilyInstance.value])
		redirect(action: "list")
	}
}
