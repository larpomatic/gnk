package org.gnk.tag

import grails.orm.PagedResultList
import org.springframework.dao.DataIntegrityViolationException

class TagRelationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max, Integer offset, String sort) {
        max = max ?: 10
        offset = offset ?: 0
        sort = sort ?: 'name'
        params.order = params.order ?: 'asc'

        def resultList = TagRelation.createCriteria().list(max: max, offset: offset) {
            tag1
            {
                order(sort, params.order as String)
            }
        }

        [tagRelationInstanceList: resultList]
    }

    def create() {
        [tagRelationInstance: new TagRelation(params)]
    }
	
	def addRelation()
	{
		print params
		Boolean bijective = true
		if (!params.bijective)
			bijective = false
		TagRelation tagRelationInstance = new TagRelation()
		
		if (params.Tag_select.equals("") || params.Tag2_select.equals(""))
		{
			print "Invalid params"
			flash.message = message(code: 'Erreur : Les tags selectionn�s sont invalides !')
			redirect(action: "list")
			return
		}
		
		Tag tag1Instance = Tag.get(params.Tag_select)
		Tag tag2Instance = Tag.get(params.Tag2_select)
	
		tagRelationInstance.tag1 = tag1Instance
		tagRelationInstance.tag2 = tag2Instance
		tagRelationInstance.weight = params.weight.toInteger()
		tagRelationInstance.isBijective = bijective
		tagRelationInstance.save()
		
		flash.messageInfo = message(code: 'adminRef.tagRelation.info.create', args: [tag1Instance.name, tag2Instance.name])
		redirect(action: "list")
	}

    def save() {
        def tagRelationInstance = new TagRelation(params)
        if (!tagRelationInstance.save(flush: true)) {
            render(view: "create", model: [tagRelationInstance: tagRelationInstance])
            return
        }

        flash.messageInfo = message(code: 'adminRef.tagRelation.info.create', args: [tagRelationInstance.tag1.name, tagRelationInstance.tag2.name])
        redirect(action: "show", id: tagRelationInstance.id)
    }

    def show(Long id) {
        def tagRelationInstance = TagRelation.get(id)
        if (!tagRelationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagRelation.label', default: 'TagRelation'), id])
            redirect(action: "list")
            return
        }

        [tagRelationInstance: tagRelationInstance]
    }

    def edit(Long id) {
        def tagRelationInstance = TagRelation.get(id)
        if (!tagRelationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagRelation.label', default: 'TagRelation'), id])
            redirect(action: "list")
            return
        }

        [tagRelationInstance: tagRelationInstance]
    }

    def update(Long id, Long version) {
        def tagRelationInstance = TagRelation.get(id)
        if (!tagRelationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagRelation.label', default: 'TagRelation'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tagRelationInstance.version > version) {
                tagRelationInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'tagRelation.label', default: 'TagRelation')] as Object[],
                          "Another user has updated this TagRelation while you were editing")
                render(view: "edit", model: [tagRelationInstance: tagRelationInstance])
                return
            }
        }

        tagRelationInstance.properties = params

        if (!tagRelationInstance.save(flush: true)) {
            render(view: "edit", model: [tagRelationInstance: tagRelationInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tagRelation.label', default: 'TagRelation'), tagRelationInstance.id])
        redirect(action: "show", id: tagRelationInstance.id)
    }

    def delete(Long id) {
        def tagRelationInstance = TagRelation.get(id)
        if (!tagRelationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tagRelation.label', default: 'TagRelation'), id])
            redirect(action: "list")
            return
        }

        try {
            tagRelationInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tagRelation.label', default: 'TagRelation'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tagRelation.label', default: 'TagRelation'), id])
            redirect(action: "show", id: id)
        }
    }
	
	def deleteRelation()
	{
		print "delete relation : " + params
		
		TagRelation tagRelationInstance = TagRelation.get(params.idRelation)
		flash.messageInfo = message(code: 'adminRef.tagRelation.info.delete', args: [tagRelationInstance.tag1.name, tagRelationInstance.tag2.name])
		tagRelationInstance.delete()
		redirect(action: "list")
	}
}
