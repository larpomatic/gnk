package org.gnk.resplacetime

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag
import org.gnk.tag.TagService

class GenericResourceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [genericResourceInstanceList: GenericResource.list(params), genericResourceInstanceTotal: GenericResource.count()]
    }

    def create() {
        [genericResourceInstance: new GenericResource(params)]
    }

    def save() {
        GenericResource genericResource = new GenericResource(params)
        Boolean res = saveOrUpdate(genericResource, true);
        genericResource = GenericResource.findAllWhere("code": genericResource.getCode(), "plot": genericResource.plot).first();
        def resourceTagList = new TagService().getResourceTagQuery();
        def jsonTagList = buildTagList(resourceTagList);
        def jsonGenericResource = buildJson(genericResource, resourceTagList);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("genericResource", jsonGenericResource);
        object.put("genericResourceTagList", jsonTagList);
        render(contentType: "application/json") {
            object
        }
    }

    def buildTagList(def genericResourceTagList) {
        JSONArray jsonTagList = new JSONArray();
        for (genericResourceTag in genericResourceTagList) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", genericResourceTag.getId());
            jsonTag.put("name", genericResourceTag.getName());
            jsonTagList.add(jsonTag);
        }
        return jsonTagList;
    }

    def buildJson(GenericResource genericResource, resourceTagList) {
        JSONObject jsonGenericResource = new JSONObject();
        jsonGenericResource.put("code", genericResource.getCode());
        jsonGenericResource.put("id", genericResource.getId());
        jsonGenericResource.put("plotId", genericResource.getPlot().getId());
        jsonGenericResource.put("comment", genericResource.getComment());
        JSONArray jsonTagList = new JSONArray();
        for (Tag genericResourceTag in resourceTagList) {
            if (genericResource.hasGenericResourceTag(genericResourceTag)) {
                jsonTagList.add(genericResourceTag.getId());
            }
        }
        jsonGenericResource.put("tagList", jsonTagList);
        return jsonGenericResource;
    }

    def saveOrUpdate(GenericResource newGenericResource, boolean isNew) {
        if (params.containsKey("plotId")) {
            Plot plot = Plot.get(params.plotId as Integer)
            newGenericResource.plot = plot
        } else {
            return false
        }
        if (params.containsKey("resourceCode")) {
            newGenericResource.code = params.resourceCode
        } else {
            return false
        }
        if (params.containsKey("resourceDescription")) {
            newGenericResource.comment = params.resourceDescription
        } else {
            return false
        }
        if(newGenericResource.extTags) {
            HashSet<GenericResourceHasTag> genericResourceHasTags = newGenericResource.extTags;
            newGenericResource.extTags.clear();
            GenericResourceHasTag.deleteAll(genericResourceHasTags);
        } else {
            newGenericResource.extTags = new HashSet<GenericResourceHasTag>()
        }
        if(newGenericResource.roleHasEventHasRessources) {
            HashSet<RoleHasEventHasGenericResource> genericResourceHasRoleHasEvents = newGenericResource.roleHasEventHasRessources;
            newGenericResource.roleHasEventHasRessources.clear();
            RoleHasEventHasGenericResource.deleteAll(genericResourceHasRoleHasEvents);
        } else {
            newGenericResource.roleHasEventHasRessources = new HashSet<RoleHasEventHasGenericResource>()
        }
        newGenericResource.version = 1;
        newGenericResource.dateCreated = new Date();
        newGenericResource.lastUpdated = new Date();
        newGenericResource.title = "";
        newGenericResource.description = "";
        newGenericResource.save(flush: true);
        newGenericResource = GenericResource.findAllWhere("code": newGenericResource.getCode()).first();

        params.each {
            if (it.key.startsWith("resourceTags_")) {
                GenericResourceHasTag genericResourceHasTag = new GenericResourceHasTag();
                Tag genericResourceTag = Tag.get((it.key - "resourceTags_") as Integer);
                genericResourceHasTag.tag = genericResourceTag
                genericResourceHasTag.weight = TagService.LOCKED
                genericResourceHasTag.genericResource = newGenericResource
                newGenericResource.extTags.add(genericResourceHasTag)
            }
        }
        newGenericResource.save(flush: true);
        return true;
    }

    def show(Long id) {
        def genericResourceInstance = GenericResource.get(id)
        if (!genericResourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
            return
        }

        [genericResourceInstance: genericResourceInstance]
    }

    def edit(Long id) {
        def genericResourceInstance = GenericResource.get(id)
        if (!genericResourceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), id])
            redirect(action: "list")
            return
        }

        [genericResourceInstance: genericResourceInstance]
    }

    def update(Long id) {
        GenericResource genericResource = GenericResource.get(id);
        String oldname = genericResource.code;
        if (genericResource) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(genericResource, false),
                       id: genericResource.id,
                       name: genericResource.code,
                       oldname: oldname)
            }
        }
    }

    def delete(Long id) {
        GenericResource genericResource = GenericResource.get(id)
        boolean isDelete = false;
        if (genericResource) {
            genericResource.delete(flush: true)
            isDelete = true;
        }
        render(contentType: "application/json") {
            object(isdelete: isDelete)
        }
    }

//    def deleteResource()
//    {
//        def genericResourceInstance = GenericResource.get(params.id)
//
//        if (!genericResourceInstance) {
//
//            flash.message = message(code: 'Erreur : Suppression échouée de la ressource !')
//            redirect(action: "list")
//            return
//        }
//
//        try {
//            genericResourceInstance.delete(flush: true)
//            flash.messageInfo = message(code: 'adminRef.genericResource.info.delete', args: [genericResourceInstance.code])
//            redirect(action: "list")
//        }
//        catch (DataIntegrityViolationException e) {
//            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'genericResource.label', default: 'GenericResource'), params.id])
//            redirect(action: "show", id: id)
//        }
//
//
//    }
}
