package org.gnk.tag

import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray
import org.gnk.administration.DbCoherenceController
import org.gnk.naming.Firstname
import org.gnk.naming.FirstnameHasTag
import org.gnk.naming.Name
import org.gnk.naming.NameHasTag
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceHasTag
import org.gnk.resplacetime.Resource
import org.gnk.resplacetime.ResourceHasTag
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasTag
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag

//import org.json.JSONArray
import org.springframework.security.access.annotation.Secured
import org.gnk.tag.TagService
import org.springframework.web.context.request.RequestContextHolder

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class TagController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    TagService tagService

    def index() {
        redirect(action: "list", params: "params")
    }

    def list(String sort) {
        List<Tag> tags = Tag.list();
        Map<Integer, ArrayList<Tag>> mapTagParent = new HashMap<Integer, ArrayList>();
        JSONArray jsonArray = new JSONArray();
        for (Tag tag : tags) {
            JSONObject obj = new JSONObject();
            JSONObject obj2 = new JSONObject();
            String namePlot = new String()
            String namePlace = new String()
            String nameResource = new String()
            String nameRole = new String()
            String fname = new String()
            String name = new String()
            obj.put("id", tag.id);
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
            if (tagParent.size() == 0)
                obj.put("parent", "#");
            else
                obj.put("parent", tagParent.get(tagParent.size()-1).id);
            obj.put("text", tag.name);
            obj2.put("relevantFirstname", tag.relevantFirstname)
            List<FirstnameHasTag> firstnames = FirstnameHasTag.findAllByTag(tag)
            for (FirstnameHasTag  firstnameht : firstnames) {
                Firstname firstname = firstnameht.getFirstname()
                fname+=firstname.name+"\n"

            }
            if (fname != "")
                obj2.put("firstname",fname)
            obj2.put("relevantLastname", tag.relevantLastname);
            List<NameHasTag> lastnames = NameHasTag.findAllByTag(tag)
            for (NameHasTag lastnameht : lastnames) {
                Name lastname = lastnameht.getName()
                name+=lastname.name+"\n"

            }
            if (name != "")
                obj2.put("lastname",name)
            obj2.put("relevantPlace", tag.relevantPlace);
            List<PlaceHasTag> places = PlaceHasTag.findAllByTag(tag)
            for (PlaceHasTag placeht : places) {
                Place place = placeht.getPlace()
                namePlace+=place.name+"\n"

            }
            if (namePlace != "")
             obj2.put("place",namePlace)
            obj2.put("relevantPlot", tag.relevantPlot);
            List<PlotHasTag> plots = PlotHasTag.findAllByTag(tag)
            for (PlotHasTag plotht : plots) {
                Plot plot = plotht.getPlot()
                namePlot+=plot.name+"\n"
            }
             if (namePlot != "")
              obj2.put("plot",namePlot)
            obj2.put("relevantResource", tag.relevantResource);
            List<ResourceHasTag> resources = ResourceHasTag.findAllByTag(tag)
            for (ResourceHasTag resourceht : resources) {
                Resource resource = resourceht.getResource()
                nameResource+=resource.name+"\n"
            }
             if (nameResource != "")
                obj2.put("resource",nameResource)
            obj2.put("relevantRole", tag.relevantRole);
            List<RoleHasTag> roles = RoleHasTag.findAllByTag(tag)
            for (RoleHasTag roleht : roles) {
                Role role = roleht.getRole()
                nameRole+=role.code+"\n"
            }
             if (nameRole != "")
             obj2.put("role",nameRole)
            obj.put("a_attr",obj2);
            jsonArray.put(obj);
        }
        Tag parent = Tag.get(params.Tag_select as Integer)
        String parentName = parent == null ? "" : parent.name

        if (params.showChildren) {
            session.tagParent = parent.id

            List<Tag> resultList = new ArrayList<Tag>()
            resultList.addAll(parent.children)
            [tagInstanceList: resultList, genericTags: new TagService().getGenericChilds(), tagParent: parentName,json:jsonArray]
        } else {
            sort = sort ?: 'name'
            params.order = params.order ?: 'asc'

            def resultList = Tag.createCriteria().list() {
                if (sort.indexOf('tagFamily.') == 0) {
                    tagFamily {
                        order(sort.split('\\.')[1], params.order)
                    }
                } else
                    order(sort, params.order as String)
            }
            [tagInstanceList: resultList, genericTags: new TagService().getGenericChilds(), tagParent: parentName, listTagParent: mapTagParent,json:jsonArray]
        }
    }

    def childrenList(Tag t) {
        t = t ?: params.Tag_select

        def resultList = findChildren(t)

        [tagInstanceList: resultList]
    }

    def findChildren(Tag t) {

        def tags = Tag.findAllWhere(parent: t)
        def tagsTmp = new ArrayList()
        tagsTmp.addAll(tags)
        if (tagsTmp == null)
            return tags
        for (Tag tag : tagsTmp) {
            tags.addAll(findChildren(tag))
        }
        return tags
    }

//	def listFrom(String tagFamily) {
//		TagFamily family
//		List<Tag> result = []
//
//		/* Get the TagFamily */
//		family = TagFamily.createCriteria().get {
//			eq ('value', tagFamily)
//		}
//		/* Get all Tags corresponding to the TagFamily */
//		result = Tag.createCriteria().list {
//			eq ('tagFamily', TagFamily)
//		}
//
//		result
//	}

    def create() {

        [tagInstance: new Tag(params)]
    }

    def save() {
        int idparent = Integer.parseInt(params.idTag);
        Tag tagParent = Tag.findById(idparent);
        Tag newTag = new Tag();
        String name = params.nameTag
        newTag.name = name;
        for (Tag tag : Tag.list()) {
            if (newTag.name.toLowerCase().equals(tag.name.toLowerCase())) {
                print "Tag already exist"
                flash.message = message(code: 'Erreur : Un tag avec le meme nom existe deja !')
                redirect(action: "list")
                return
            }
        }
        newTag.children = new HashSet<>();
        newTag.extPlaceTags = new HashSet<>();
        newTag.extPlotTags = new HashSet<>();
        newTag.extResourceTags = new HashSet<>();
        newTag.extRoleTags = new HashSet<>();
        if (params.relevantPlaceson) {
            newTag.relevantPlace = true;
        } else {
            newTag.relevantPlace = false;
        }
        if (params.relevantFirstnameson) {
            newTag.relevantFirstname = true;
        } else {
            newTag.relevantFirstname = false;
        }
        if (params.relevantLastnameson) {
            newTag.relevantLastname = true;
        } else {
            newTag.relevantLastname = false;
        }
        if (params.relevantPlotson) {
            newTag.relevantPlot = true;
        } else {
            newTag.relevantPlot = false;
        }
        if (params.relevantResourceson) {
            newTag.relevantResource = true;
        } else {
            newTag.relevantResource = false;
        }
        if (params.relevantRoleson) {
            newTag.relevantRole = true;
        } else {
            newTag.relevantRole = false;
        }
        newTag.parent = tagParent;

//		TagFamily tagFamilyInstance = null;
//		tagFamilyInstance = TagFamily.get(tagFamilyId)
//
//		if (tagFamilyInstance.equals(null))
//		{
//		print "Family not found"
//		flash.message = message(code: 'Erreur : Cette famille n\'existe pas !')
//           redirect(action: "list")
//           return
//		}
//
//		// Creates the relation between the tag and the family
//		tagInstance.tagFamily = tagFamilyInstance

        if (!newTag.save(flush: true)) {
            render(view: "create", model: [tagInstance: newTag])
            return
        }

        flash.messageInfo = message(code: 'adminRef.tag.info.create', args: [newTag.name, tagParent.name])
        redirect(action: "list")
    }

    private void isTagApplicable(Tag tagInstance) {
        // FIXME Changement de Base
        /*
        if (params.isPlotTag) {
            PlotTag plotTag = new PlotTag()
            plotTag.tag = tagInstance
            plotTag.save()
        }
        if (params.isRoleTag) {
            RoleTag roleTag = new RoleTag()
            roleTag.tag = tagInstance
            roleTag.save()
        }
        if (params.isResourceTag) {
            ResourceTag resourceTag = new ResourceTag()
            resourceTag.tag = tagInstance
            resourceTag.save()
        }
        */
    }

//    def addTagIntoFamily()
//	{
//		if (params.TagFamily_select.equals("") || params.Tag_select.equals(""))
//		{
//				print "Invalid params"
//				flash.message = message(code: 'Erreur : Il faut choisir un tag et une famille !')
//				redirect(action: "list")
//				return
//		}
//
////		TagFamily tagFamilyInstance = null;
//		Tag tagInstance = null;
////		for (TagFamily tagFamily : TagFamily.list())
////		{
////			if (tagFamily.id == params.TagFamily_select.toInteger())
////			{
////				tagFamilyInstance = tagFamily
////				break
////			}
////		}
//
//		for (Tag tag : Tag.list())
//		{
//			if (tag.id == params.Tag_select.toInteger())
//			{
//				tagInstance = tag
//				break
//			}
//		}
//
//		if (tagFamilyInstance.equals(null) || tagInstance.equals(null))
//		{
//			print "Family or Tag not found"
//			flash.message = message(code: 'Erreur : Tag ou famille invalide !')
//			redirect(action: "list")
//			return
//		}
//
//		tagInstance.tagFamily = tagFamilyInstance
//		tagInstance.save()
//		flash.messageInfo = message(code: 'adminRef.tagIntoTagFamily.info.create', args: [tagInstance.name, tagFamilyInstance.value])
//
//		redirect(action: "list")
//	}

//    def viewChildren() {
//
//        Tag tagInstance = null;
//
//        if (params.Tag_select.equals("")) {
//            print "Invalid params"
//            flash.message = message(code: 'Erreur : Il faut choisir un tag et son parent!')
//            redirect(action: "list")
//            return
//        }
//
//        tagInstance = Tag.findWhere(id: (int) params.Tag_select)
//
//        List<Tag> children = tagInstance.children
//        list(children)
//
//    }

    def show(Long id) {
        def tagInstance = Tag.get(id)
        if (!tagInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
            return
        }

        [tagInstance: tagInstance]
    }

    def edit(Long id) {
        def tagInstance = Tag.get(id)
        if (!tagInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
            return
        }

        [tagInstance: tagInstance]
    }

    def update(Long id, Long version) {
        def tagInstance = Tag.get(id)
        if (!tagInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (tagInstance.version > version) {
                tagInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'tag.label', default: 'Tag')] as Object[],
                        "Another user has updated this Tag while you were editing")
                render(view: "edit", model: [tagInstance: tagInstance])
                return
            }
        }

        tagInstance.properties = params

        if (!tagInstance.save(flush: true)) {
            render(view: "edit", model: [tagInstance: tagInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'tag.label', default: 'Tag'), tagInstance.id])
        redirect(action: "show", id: tagInstance.id)
    }

    def delete(Long id) {
        def tagInstance = Tag.get(id)
        if (!tagInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
            return
        }
        // FIXME Changement de Base
        /*
        try {
            tagInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tag.label', default: 'Tag'), id])
            redirect(action: "show", id: id)
        }*/
    }

//	def deleteFamily(Long idTag, Long idFamily) {
//		TagFamily tagFamilyInstance = null;
//		Tag tagInstance = null;
//		for (TagFamily tagFamily : TagFamily.list())
//		{
//			if (tagFamily.id == idFamily)
//			{
//				tagFamilyInstance = tagFamily
//				break
//			}
//		}
//
//		for (Tag tag : Tag.list())
//		{
//			if (tag.id == idTag)
//			{
//				tagInstance = tag
//				break
//			}
//		}
//
//		if (tagFamilyInstance.equals(null) || tagInstance.equals(null))
//		{
//			print "Family or Tag not found"
//			flash.message = message(code: 'Erreur : Famille ou tag incorrecte !')
//			redirect(action: "list")
//			return
//		}
//
//		if (tagInstance.tagFamily.id.equals(idFamily) )
//		{
//			tagInstance.tagFamily = null
//            tagFamilyInstance.removeFromTags(tagInstance)
//            tagFamilyInstance.save(flush: true)
//			tagInstance.save()
//			flash.messageInfo = message(code: 'adminRef.tagIntoTagFamily.info.delete', args: [tagInstance.name, tagFamilyInstance.value])
//
//			redirect(action: "list")
//			return
//		}
//
//		print("The family was not deleted. Possible error ?")
//		redirect(action: "list")
//	}

    def deleteTag() {
        int idTag = Integer.parseInt(params.idEditRelTagson);
        def tagInstance = Tag.findById(idTag)
        def tagInstanceName = tagInstance.name
        if (tagInstance.children.size() > 0) {
            flash.message = message(code: 'adminRef.tag.info.delete.fail', args: [tagInstance.name])
        } else {
            for (int i = 0; tagInstance.extPlaceTags; i++) {
                ((PlaceHasTag) tagInstance.extPlaceTags.toArray()[i]).delete()
            }
            for (int i = 0; tagInstance.extPlotTags; i++) {
                ((PlaceHasTag) tagInstance.extPlotTags.toArray()[i]).delete()
            }
            for (int i = 0; tagInstance.extResourceTags; i++) {
                ((PlaceHasTag) tagInstance.extResourceTags.toArray()[i]).delete()
            }
            for (int i = 0; tagInstance.extRoleTags; i++) {
                ((PlaceHasTag) tagInstance.extRoleTags.toArray()[i]).delete()
            }
            tagInstance.parent = null;
            tagInstance.delete(flush: true)
            flash.message = message(code: 'adminRef.tag.info.delete', args: [tagInstanceName])

            render(action: "list")
        }
    }

    def stats() {
        Tag tagUniverParent = Tag.findByName("Tag Univers");
        Set<Tag> tagUnivers = tagUniverParent.children
        Map<Integer, ArrayList<Tag>> mapTagParent = new HashMap<Integer, ArrayList>();
        List<Tag> tags = Tag.list();
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


        render(view: "statistics", model: [listTagParent: mapTagParent, tagUniverse: tagUnivers])
    }

    def editRelevantTag() {
        int id = Integer.parseInt(params.idEditRelTag)
        Tag tag = Tag.findById(id)
        tag.name = params.NameEditRelTag
        if (params.relevantPlace) {
            tag.relevantPlace = true;
        } else {
            tag.relevantPlace = false;
        }
        if (params.relevantFirstname) {
            tag.relevantFirstname = true;
        } else {
            tag.relevantFirstname = false;
        }
        if (params.relevantLastname) {
            tag.relevantLastname = true;
        } else {
            tag.relevantLastname = false;
        }
        if (params.relevantPlot) {
            tag.relevantPlot = true;
        } else {
            tag.relevantPlot = false;
        }
        if (params.relevantResource) {
            tag.relevantResource = true;
        } else {
            tag.relevantResource = false;
        }
        if (params.relevantRole) {
            tag.relevantRole = true;
        } else {
            tag.relevantRole = false;
        }

        int idparent = Integer.parseInt(params.idParentSave);
        Tag tagParent = Tag.findById(idparent)
        if (idparent != -1) {
            tag.parent = tagParent;
            if (tagParent.children.size() == 1) {
                Tag tagRelevant1 = new Tag();
                tagRelevant1.relevantFirstname = false;
                tagRelevant1.relevantLastname = false;
                tagRelevant1.relevantPlace = false;
                tagRelevant1.relevantPlot = false;
                tagRelevant1.relevantResource = false;
                tagRelevant1.relevantRole = false;
                // tagRelevant1.tag = tagParent;
                tagRelevant1.save(failOnError: true);
            }
        }
        flash.messageInfo = message(code: 'default.updated.message', args: [tag.name])

        redirect(action: "list")
    }

    def showInformation() {
        int idTag = Integer.parseInt(params.idTag);
        Tag tag = Tag.findById(idTag)
        ArrayList<Long> parentid = new ArrayList<Long>()
        JSONObject jsonMain = new JSONObject()
        JSONArray jsonParent = new JSONArray()
        JSONArray jsonRess = new JSONArray()
        JSONArray jsonRole = new JSONArray()
        JSONArray jsonPlot = new JSONArray()
        JSONArray jsonRelation = new JSONArray()
        Tag tmp = tag
        while (tmp.parent != null) {
            parentid.add(tmp.parentId)

            tmp = tmp.parent
        }
        parentid = parentid.reverse()
        for (Long idparent : parentid){
            Tag tagParent = Tag.findById(idparent.toInteger())
            JSONObject parent = new JSONObject();
            parent.put("idparent", tagParent.id);
            parent.put("nameparent", tagParent.name.encodeAsHTML())
            jsonParent.put(parent)
        }
        int tmpnb = 1
        for (ResourceHasTag res : tag.extResourceTags) {

            JSONObject resjson = new JSONObject();
            resjson.put("index", tmpnb);
            resjson.put("name", res.resource.name)
            resjson.put("gender", res.resource.gender)
            resjson.put("description", res.resource.description)
            resjson.put("weight", res.weight)
            jsonRess.put(resjson)
            tmpnb++;
        }
        tmpnb = 1
        for (Role role : Role.list()) {
            for (RoleHasTag rolehasTag : role.roleHasTags) {
                if (rolehasTag.tag.equals(tag)) {
                    JSONObject rolejson = new JSONObject();
                    rolejson.put("index", tmpnb);
                    rolejson.put("code", role.code)
                    rolejson.put("description", role.description)
                    rolejson.put("weight", rolehasTag.weight)
                    jsonRole.put(rolejson)
                    tmpnb++;
                }
            }
        }
        tmpnb = 1
        for (PlotHasTag plotTag : tag.extPlotTags) {

            JSONObject plotjson = new JSONObject();
            plotjson.put("index", tmpnb);
            plotjson.put("name", plotTag.plot.name)
            plotjson.put("description", plotTag.plot.description)
            plotjson.put("username", plotTag.plot.user.username)
            plotjson.put("weight", plotTag.weight)
            jsonPlot.put(plotjson)
            tmpnb++;
        }
        tmpnb = 1
        for (TagRelation tagR : TagRelation.findByTag1(tag)) {

            JSONObject tagRjson = new JSONObject();
            tagRjson.put("index", tmpnb);
            tagRjson.put("nameOtherTag", tagR.tag2.name)
            tagRjson.put("description", tagR.weight)
            jsonRelation.put(tagRjson)
            tmpnb++;
        }
        for (TagRelation tagR : TagRelation.findByTag2(tag)) {
            JSONObject tagRjson = new JSONObject();
            tagRjson.put("index", tmpnb);
            tagRjson.put("nameOtherTag", tagR.tag1.name)
            tagRjson.put("weight", tagR.weight)
            jsonRelation.put(tagRjson)
            tmpnb++;
        }
        jsonMain.put("name", tag.name)
        jsonMain.put("id", tag.id)
        jsonMain.put("parents", jsonParent)
        jsonMain.put("nbRelations", TagRelation.findAllByTag1(tag).size() + TagRelation.findAllByTag2(tag).size())
        jsonMain.put("nbRess", tag.extResourceTags.size())
        jsonMain.put("nbRoles", tag.extRoleTags.size())
        jsonMain.put("nbPlots", tag.extPlotTags.size())
        jsonMain.put("relations", jsonRelation)
        jsonMain.put("ress", jsonRess)
        jsonMain.put("plots", jsonPlot)
        jsonMain.put("roles", jsonRole)
        jsonMain.put("tagRelevant", tag.tagRelevant)
        if (tag.tagRelevant) {
            jsonMain.put("tagRelevantFn", tag.tagRelevant.relevantFirstname)
            jsonMain.put("tagRelevantLn", tag.tagRelevant.relevantLastname)
            jsonMain.put("tagRelevantRs", tag.tagRelevant.relevantResource)
            jsonMain.put("tagRelevantPlot", tag.tagRelevant.relevantPlot)
            jsonMain.put("tagRelevantPlace", tag.tagRelevant.relevantPlace)
            jsonMain.put("tagRelevantRole", tag.tagRelevant.relevantRole)
        }
        render(contentType: "application/json") {
            jsonMain
        }
    }

    def editTag(){
        int idTag = Integer.parseInt(params.idTag);
        Tag tag = Tag.findById(idTag)
        JSONArray jsonTags = new JSONArray()
        JSONObject jsonMain = new JSONObject()
        jsonMain.put("tagRelevant", tag.tagRelevant)
        if (tag.tagRelevant) {
            jsonMain.put("tagRelevantFn", tag.tagRelevant.relevantFirstname.toString())
            jsonMain.put("tagRelevantLn", tag.tagRelevant.relevantLastname.toString())
            jsonMain.put("tagRelevantRs", tag.tagRelevant.relevantResource.toString())
            jsonMain.put("tagRelevantPlot", tag.tagRelevant.relevantPlot.toString())
            jsonMain.put("tagRelevantPlace", tag.tagRelevant.relevantPlace.toString())
            jsonMain.put("tagRelevantRole", tag.tagRelevant.relevantRole.toString())
        }
        for (Tag tag1 : Tag.list()){
            if (tag.id != tag1.id){
                JSONObject tagjson = new JSONObject();
                tagjson.put("name" , tag1.name)
                tagjson.put("id" , tag1.id)
                jsonTags.add(tagjson)
            }
        }
        jsonMain.put("id", tag.id)
        jsonMain.put("tags", jsonTags)
        render(contentType: "application/json") {
            jsonMain
        }
    }
}
