package org.gnk.tag

import grails.orm.PagedResultList
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.dao.DataIntegrityViolationException

class TagRelationController {

    TagSearchService tagSearchService
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max, Integer offset, String sort) {
        max = max ?: 10
        offset = offset ?: 0
        sort = sort ?: 'name'
        params.order = params.order ?: 'asc'
        int isResult = -1;
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
        PagedResultList trs = session.getAttribute("trs")
        if (session.getAttribute("tmpsearch") == 0)
            isResult = session.getAttribute("tmpsearch");

        if (trs != null){
            isResult = 1;
        }

        for (TagRelation tagR : trs){
            tagR = tagR.merge()
            tagR.save(flush: true)
            if (tagR.isAttached()) {
                tagR.attach()
            }
        }
        session.removeAttribute("trs");
        if (params.search) {
            if (!"".equals(params.msg1) && "".equals(params.msg2))
                flash.message = message(code: 'adminRef.info.tagRelation.Nexist.one', args: [params.msg1])
            if ("".equals(params.msg1) && !"".equals(params.msg2))
                flash.message = message(code: 'adminRef.info.tagRelation.Nexist.one', args: [params.msg2])
            if (!"".equals(params.msg1) && !"".equals(params.msg2))
                flash.message = message(code: 'adminRef.info.tagRelation.Nexist.two', args: [params.msg1, params.msg2])
            if (isResult == 0)
                flash.message = message(code: 'adminRef.info.tagRelation.Nexist')
            params.search = false;
        }
        [tagRelationInstanceList: resultList, listTagParent: mapTagParent, trs: trs]
    }

    def create() {
        [tagRelationInstance: new TagRelation(params)]
    }

    def addRelation() {
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
        if (newWeight != tagR.weight) {
            tagR.weight = newWeight
        }
        tagR.save()
        redirect(action: "list")
    }

    def search(Integer max, Integer offset, String sort) {
        String t1name = params.tag1Rel;
        String t2name = params.tag2Rel;
        String message1 = "";
        String message2 = "";
        int type = Integer.parseInt(params.selectTypeRelation)
        max = max ?: 10
        offset = offset ?: 0
        sort = sort ?: 'name'
        params.order = params.order ?: 'asc'
        Tag t1 = null
        Tag t2 = null

        session.setAttribute("tmpsearch", 0)
        boolean error = false;
        List<Tag> tags1 = null
        List<Tag> tags2 = null
        PagedResultList tagRelations = null;
        if ("".equals(t1name) && "".equals(t2name)) {
            session.setAttribute("tmpsearch", 1)
            tagRelations = null
        } else {
            if (t1name.indexOf("%") == -1 && t1name.indexOf("?") == -1) {
                t1 = Tag.findByName(t1name);
                if (!t1 && !"".equals(t1name)) {
                    message1 = t1name
                    error = true;
                }
            } else {
                tags1 = tagSearchService.searchTag(t1name)
                if (tags1.size() == 0){
                    message1 = t1name
                    error = true;
                }
            }
            if (t2name.indexOf("%") == -1 && t2name.indexOf("?") == -1) {
                t2 = Tag.findByName(t2name);
                if (!t2 && !"".equals(t2name)) {
                    message2 = t2name
                    error = true;
                }
            } else {
                tags2 = tagSearchService.searchTag(t2name)
                if (tags2.size() == 0){
                    message2 = t2name
                    error = true;
                }
            }
            if (!error) {
                if (tags1 == null && tags2 == null) {
                    tagRelations = tagSearchService.manageReturnSearchTagRealtion(type, t1, t2, max, offset, sort);
                } else {
                    if ((t1 && t2 == null) && (tags2 && tags1 == null)) {
                        tagRelations = tagSearchService.searchTagandListTag(type, tags2, t1, max, offset, sort)
                    }
                    if ((t2 && t1 == null) && (tags1 && tags2 == null)) {
                        tagRelations = tagSearchService.searchTagandListTag(type, tags1, t2, max, offset, sort)
                    }
                    if (tags1 != null && tags2 != null) {
                        tagRelations = tagSearchService.searchtwoListTag(type, tags1, tags2, max, offset, sort)
                    }
                    if (tags1 != null && t2 == null && t1 == null && tags2 == null) {
                        tagRelations = tagSearchService.searchoneListTag(type, tags1, max, offset, sort)
                    }
                    if (tags2 != null && t2 == null && t1 == null && tags1 == null) {
                        tagRelations = tagSearchService.searchoneListTag(type, tags2, max, offset, sort)
                    }
                }
            }
        }

        session.setAttribute("trs", tagRelations);
        redirect(action: "list", params: [msg1: message1, msg2: message2, search: true]);
    }
}
