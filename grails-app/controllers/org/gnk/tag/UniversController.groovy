package org.gnk.tag

import org.springframework.dao.DataIntegrityViolationException

class UniversController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [universInstanceList: Univers.list(params), universInstanceTotal: Univers.count()]
    }

    def create() {
        [universInstance: new Univers(params)]
    }
	
	def deleteUnivers(Long idUnivers) {
		def universInstance = Univers.get(idUnivers)
		universInstance.delete()
		flash.messageInfo = message(code: 'adminRef.univers.info.delete', args: [universInstance.name])
		redirect(action: "list")
	}

    def save() {
        def universInstance = new Univers(params)
        if (!universInstance.save(flush: true)) {
            render(view: "create", model: [universInstance: universInstance])
            return
        }

        flash.messageInfo = message(code: 'adminRef.univers.info.create', args: [universInstance.name])
        redirect(action: "list", id: universInstance.id)
    }
	
	def getUniverse(String uName) {
		Univers.createCriteria().get {
			eq('name', uName)
		}
	}
	
	def addTagToUnivers()
	{
		if (params.Univers_select.equals("") || params.Tag_select.equals(""))
		{
				print "Invalid params"
				flash.message = message(code: 'Erreur : Il faut choisir un tag et un univers !')
				redirect(action: "list")
				return
		}

		Univers universInstance = null;
		Tag tagInstance = null;
		for (Univers univers : Univers.list())
		{
			if (univers.id == params.Univers_select.toInteger())
			{
				universInstance = univers
				break
			}
		}
		
		for (Tag tag : Tag.list())
		{
			if (tag.id == params.Tag_select.toInteger())
			{
				tagInstance = tag
				break
			}
		}
		
		if (universInstance.equals(null) || tagInstance.equals(null))
		{
			print "Universe or Tag not found"
			flash.message = message(code: 'Erreur : Tag ou univers invalide !')
			redirect(action: "list")
			return
		}
		/* TODO : Check if relation already exists + create relation
		for (TagHasTagFamily tagHasTagFamily : tagInstance.tagFamilies)
		{
			if ((tagHasTagFamily.tag.name.equals(tagInstance.name))
				&& (tagHasTagFamily.tagFamily.value.equals(tagFamilyInstance.value)))
				{
					print "This relation already exists"
					flash.message = message(code: 'Erreur : Cette relation existe deja !')
					redirect(action: "list")
					return
				}
		}
		
		TagHasTagFamily tagHasTagFamilyInstance = new TagHasTagFamily()
		tagHasTagFamilyInstance.tag = tagInstance
		tagHasTagFamilyInstance.tagFamily = tagFamilyInstance
		tagHasTagFamilyInstance.save()
		flash.messageInfo = message(code: 'adminRef.tagIntoTagFamily.info.create', args: [tagInstance.name, tagFamilyInstance.value])
		*/
		redirect(action: "list")
	}

    def show(Long id) {
        def universInstance = Univers.get(id)
        if (!universInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'univers.label', default: 'Univers'), id])
            redirect(action: "list")
            return
        }

        [universInstance: universInstance]
    }

    def edit(Long id) {
        def universInstance = Univers.get(id)
        if (!universInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'univers.label', default: 'Univers'), id])
            redirect(action: "list")
            return
        }

        [universInstance: universInstance]
    }

    def update(Long id, Long version) {
        def universInstance = Univers.get(id)
        if (!universInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'univers.label', default: 'Univers'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (universInstance.version > version) {
                universInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'univers.label', default: 'Univers')] as Object[],
                          "Another user has updated this Univers while you were editing")
                render(view: "edit", model: [universInstance: universInstance])
                return
            }
        }

        universInstance.properties = params

        if (!universInstance.save(flush: true)) {
            render(view: "edit", model: [universInstance: universInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'univers.label', default: 'Univers'), universInstance.id])
        redirect(action: "show", id: universInstance.id)
    }

    def delete(Long id) {
        def universInstance = Univers.get(id)
        if (!universInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'univers.label', default: 'Univers'), id])
            redirect(action: "list")
            return
        }

        try {
            universInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'univers.label', default: 'Univers'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'univers.label', default: 'Univers'), id])
            redirect(action: "show", id: id)
        }
    }
}
