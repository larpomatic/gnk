package org.gnk.resplacetime

import org.gnk.tag.Tag

class ResourceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [resourceInstanceList: Resource.list(), resourceInstanceTotal: Resource.count()]
    }

	def addResourceToUnivers()
	{
		if (params.Univers_select.equals("") || params.Resource_select.equals(""))
		{
				print "Invalid params"
				flash.message = message(code: 'Erreur : Il faut choisir un univers et une ressource !')
				redirect(action: "list")
				return
		}

		Resource resourceInstance = null;
		Tag universInstance = null;
		for (Resource resource : Resource.list())
		{
			if (resource.id == params.Resource_select.toInteger())
			{
				resourceInstance = resource
				break
			}
		}
		
		for (Tag univers : Tag.list())
		{
			if (univers.id == params.Univers_select.toInteger())
			{
				universInstance = univers
				break
			}
		}
		
		if (resourceInstance.equals(null) || universInstance.equals(null))
		{
			print "Plot or Tag not found"
			flash.message = message(code: 'Erreur : Univers ou ressource invalide !')
			redirect(action: "list")
			return
		}
		
		for (ResourceHasTag resourceHasTags : resourceInstance.extTags)
		{
			if ((resourceHasTags.resource.id.equals(universInstance.id)
				&& (resourceHasTags.resource.id.equals(resourceInstance.id))))
				{
					print "This relation already exists"
					flash.message = message(code: 'Erreur : Cette relation existe deja !')
					redirect(action: "list")
					return
				}
		}
		
		ResourceHasTag resourceHasUniversInstance = new ResourceHasTag();
		resourceHasUniversInstance.resource = resourceInstance
		resourceHasUniversInstance.univers = universInstance
		resourceHasUniversInstance.weight = params.weight.toInteger()
		resourceHasUniversInstance.save()
		redirect(action: "list")
	}


    def deleteResource()
	{
		Resource resourceInstance
		for (Resource res : Resource.list()) {
			if (res.id == params.id.toInteger())
			{
				resourceInstance = res
				break;
			}
		}
		
		if (resourceInstance != null)
		{
			resourceInstance.delete()
			redirect(action: "list")
			return
		}
		
		print "Error : resource not deleted"
	}
	
    def create() {
		resourceInstance = new Resource()
		
        [resourceInstance: new Resource(params)]
    }

    def save() {
		if (params.name[0].equals(""))
		{
			print "Invalid params"
			flash.message = message(code: 'Erreur : Le nom de la ressource ne peut etre vide !')
			redirect(action: "list")
			return
		}
		
		for (Resource res : Resource.list())
		{
			if (res.name.equals(params.name[0]))
			{
				flash.message = message(code: 'Erreur : Une ressource de ce nom existe d�j� !')
				redirect(action: "list")
				return
			}
		}
		
        def resourceInstance = new Resource()
		resourceInstance.name = params.name
		resourceInstance.description = params.desc
		resourceInstance.gender = params.gender_select
        // voir dump 2013 (domainclass Resource resourceInstance.genericResource = GenericResource.get(params.genericResource_select)

        if (!resourceInstance.save(flush: true)) {
            render(view: "create", model: [resourceInstance: resourceInstance])
            return
        }
		flash.messageInfo = message(code: 'adminRef.resource.info.create', args: [resourceInstance.name])
        redirect(action: "list", id: resourceInstance.id)
    }

    def show(Long id) {
        def resourceInstance = Resource.get(id)
        if (!resourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), id])
            redirect(action: "list")
            return
        }

        [resourceInstance: resourceInstance]
    }
	
	def addTagToResource()
	{

		if (params.Resource_select.equals("") || params.Tag_select.equals(""))
		{
				print "Invalid params"
				flash.message = message(code: 'Erreur : Il faut choisir un tag et une ressource !')
				redirect(action: "list")
				return
		}

		Resource resourceInstance = null;
		Tag tagInstance = null;
		for (Resource resource : Resource.list())
		{
			if (resource.id == params.Resource_select.toInteger())
			{
				resourceInstance = resource
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
		
		if (resourceInstance.equals(null) || tagInstance.equals(null))
		{
			print "Resource or Tag not found"
			flash.message = message(code: 'Erreur : Tag ou ressource invalide !')
			redirect(action: "list")
			return
		}
		
		ResourceHasTag resourceHasTagInstance = new ResourceHasTag()
        resourceHasTagInstance.tag = tagInstance
        resourceHasTagInstance.resource = resourceInstance
        resourceHasTagInstance.weight = params.weight.toInteger()


        print "test : " + resourceHasTagInstance.tagId

     //   resourceHasTagInstance.tagId = resourceHasTagInstance.tag.id
		
		for (ResourceHasTag resourceHasResourceTag : resourceInstance.extTags)
		{
			if ((resourceHasResourceTag.tag.name.equals(resourceHasTagInstance.tag.name))
				&& (resourceHasResourceTag.resource.name.equals(resourceHasTagInstance.resource.name)))
				{
					print "This relation already exists"
					flash.message = message(code: 'Erreur : Cette relation existe deja !')
					redirect(action: "list")
					return
				}
		}

		if (!resourceHasTagInstance.save())
			print "error : plotHasPlotTagInstance was not saved."
		resourceInstance.extTags.add(resourceHasTagInstance)
		resourceInstance.save()
		flash.messageInfo = message(code: 'adminRef.tagToPlot.info.create', args: [tagInstance.name, resourceInstance.name])
		
		redirect(action: "list")
	}

    def edit(Long id) {
        def resourceInstance = Resource.get(id)
        if (!resourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), id])
            redirect(action: "list")
            return
        }

        [resourceInstance: resourceInstance]
    }
	
	def deleteTag()
	{

		ResourceHasTag resourceHasTag = ResourceHasTag.get(params.id.toInteger())
		
		if (resourceHasTag.equals(null))
		{
			print "Error : resourceHasResourceTag not found."
			flash.message = message(code: 'Erreur : la relation est invalide.')
			redirect(action: "list")
			return
		}


        resourceHasTag.delete(flush: true)
        flash.messageInfo = message(code: 'adminRef.resource.info.deleteTag', args: [resourceHasTag.tag.name, resourceHasTag.resource.name])
				
		redirect(action: "list")
	}

    def update(Long id, Long version) {
        def resourceInstance = Resource.get(id)
        if (!resourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (resourceInstance.version > version) {
                resourceInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'resource.label', default: 'Resource')] as Object[],
                          "Another user has updated this Resource while you were editing")
                render(view: "edit", model: [resourceInstance: resourceInstance])
                return
            }
        }

        resourceInstance.properties = params

        if (!resourceInstance.save(flush: true)) {
            render(view: "edit", model: [resourceInstance: resourceInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'resource.label', default: 'Resource'), resourceInstance.id])
        redirect(action: "show", id: resourceInstance.id)
    }

    def delete(Long id) {
        def resourceInstance = Resource.get(id)
        if (!resourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), id])
            redirect(action: "list")
            return
        }
		//FIXME Changement de Base
		/*
        try {
            resourceInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'resource.label', default: 'Resource'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'resource.label', default: 'Resource'), id])
            redirect(action: "show", id: id)
        }*/

    }
}
