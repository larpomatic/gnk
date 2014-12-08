package org.gnk.tag

import grails.orm.PagedResultList
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
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
        List<Tag> tags = Tag.list();
        Map<Integer, ArrayList<Tag>> mapTagParent = new HashMap<Integer, ArrayList>();
        for (Tag tag : tags) {
            ArrayList<Tag> tagParent = new ArrayList<Tag>();
            Tag t = tag.parent;
            while (t != null) {
                if (!"".equals(tag.name)) {
                    tagParent.add(t);
                }
                t = t.parent;
            }
            tagParent = tagParent.reverse();
            mapTagParent.put(tag.id, tagParent);
        }
        [tagRelationInstanceList: resultList, listTagParent : mapTagParent]
    }

    def create() {
        [tagRelationInstance: new TagRelation(params)]
    }

    def addRelation() {
        print params
        Boolean bijective = true
        if (!params.bijective)
            bijective = false
        TagRelation tagRelationInstance = new TagRelation()

        if (params.Tag_select.equals("") || params.Tag2_select.equals("")) {
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

    def deleteRelation() {
        print "delete relation : " + params

        TagRelation tagRelationInstance = TagRelation.get(params.idRelation)
        flash.messageInfo = message(code: 'adminRef.tagRelation.info.delete', args: [tagRelationInstance.tag1.name, tagRelationInstance.tag2.name])
        tagRelationInstance.delete()
        redirect(action: "list")
    }

    def updateParam() {
        GrailsParameterMap param = getParams()
        int tagRelationId = Integer.parseInt(param.get("tagRelation"))
        TagRelation tagRelation = TagRelation.findById(tagRelationId);
        int weight = tagRelation.weight
        boolean isBijectiv = tagRelation.isBijective
        int weightUpdate = Integer.parseInt(param.get("weightTag"))
        boolean isBijectivUpdate = (boolean) param.get("bijectivTag")
        if (weight != weightUpdate) {
            tagRelation.weight = weightUpdate;
        }
        if (("true".equals(isBijectivUpdate) || "false".equals(isBijectivUpdate)) && isBijectiv != isBijectivUpdate) {
            tagRelation.isBijective = isBijectivUpdate
        }
        tagRelation.save();
        redirect(action: "list")
    }

    def updateRelation(long id) {
        GrailsParameterMap param = getParams()
        String isbijective = params.("isBijective" + id)
        String tagWeightInput = params.("weightRelationTag" + id)
        TagRelation tagR = TagRelation.findById(id)
        if (isbijective) {
            tagR.isBijective = true;
        } else {
            tagR.isBijective = false;
        }
        int newWeight = Integer.parseInt(tagWeightInput)
        if (newWeight != tagR.weight){
            tagR.weight = newWeight
        }
        tagR.save()
        redirect (action: "list")
    }
}
