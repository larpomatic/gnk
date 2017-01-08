package org.gnk.resplacetime

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.ressplacetime.ReferentialResource
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag
import org.gnk.tag.TagService

class GenericResourceController {

    //ResourceService resourceService;

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
        Boolean res = saveOrUpdate(genericResource);
      //  Boolean res_ = checkresource(genericResource);
//        genericResource = GenericResource.findAllWhere("code": genericResource.getCode(), "plot": genericResource.plot).first();
        def resourceTagList = new TagService().getResourceTagQuery();
        def jsonTagList = buildTagList(resourceTagList);
        def jsonGenericResource = buildJson(genericResource);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
     //   object.put("ischecked", res_);
        object.put("genericResource", jsonGenericResource);
        object.put("genericResourceTagList", jsonTagList);
        render(contentType: "application/json") {
            object
        }
    }
Boolean checkresource(Plot plot)
{

    ArrayList<String> title = plot.genericResources.comment;
    ArrayList<String> event = plot.events.name;
    ArrayList<String> roles = plot.roles.code;
    ArrayList<String> pastscenes = plot.pastescenes.description;
    ArrayList<String> gplace = plot.genericPlaces.code;
   print(title);
    for  (int i = 0; i < roles.size(); i++)
    {
        return title.contains(roles[i]);
    }
    for  (int i = 0; i < gplace.size(); i++)
    {
        return title.contains(gplace[i]);
    }
    for  (int i = 0; i < title.size(); i++)
    {
        return pastscenes.contains(title[i]);
    }
    for (int i = 0; i < event.size(); i++)
    {
        return event.contains(title[i]);
    }
return checkresource(plot);
}
    def buildTagList(def genericResourceTagList) {
        JSONArray jsonTagList = new JSONArray();
        for (genericResourceTag in genericResourceTagList) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", genericResourceTag.getId());
            jsonTag.put("name", genericResourceTag.getName());
            if (genericResourceTag.children && genericResourceTag.children.size() != 0) {
                JSONArray jsonTagChildren = buildTagList(genericResourceTag.children);
                jsonTag.put("children", jsonTagChildren);
            }
            jsonTagList.add(jsonTag);
        }
        return jsonTagList;
    }

    def buildJson(GenericResource genericResource) {
        JSONObject jsonGenericResource = new JSONObject();
        jsonGenericResource.put("code", genericResource.getCode());
        jsonGenericResource.put("id", genericResource.getId());
        jsonGenericResource.put("plotId", genericResource.getPlot().getId());
        jsonGenericResource.put("comment", genericResource.getComment());
        jsonGenericResource.put("resourceObject", genericResource.getObjectType().getId());
        if (genericResource.gnConstant != null)
            jsonGenericResource.put("gnConstant", genericResource.getGnConstant().getId());

        if (genericResource.title) {
            jsonGenericResource.put("clue", true);
            jsonGenericResource.put("title", genericResource.getTitle());
        }
        if (genericResource.possessedByRole) {
            jsonGenericResource.put("possessedByRoleId", genericResource.getPossessedByRole().id);
            jsonGenericResource.put("possessedByRoleName", genericResource.getPossessedByRole().code);
        }
        //
//        if (genericResource.fromRole) {
//            jsonGenericResource.put("fromRoleId", genericResource.getFromRole().id);
//            jsonGenericResource.put("fromRoleName", genericResource.getFromRole().code);
//        }
//        if (genericResource.toRole) {
//            jsonGenericResource.put("toRoleId", genericResource.getToRole().id);
//            jsonGenericResource.put("toRoleName", genericResource.getToRole().code);
//        }
        if (genericResource.fromRoleText) {
            jsonGenericResource.put("fromRoleText", genericResource.getFromRoleText());
        }
        if (genericResource.toRoleText) {
            jsonGenericResource.put("toRoleText", genericResource.getToRoleText());
        }
        if (genericResource.description) {
            jsonGenericResource.put("description", genericResource.getDescription());
        }
        JSONArray jsonTagList = new JSONArray();
        for (GenericResourceHasTag genericResourceHasTag in genericResource.extTags) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", genericResourceHasTag.tag.id)
            jsonTag.put("weight", genericResourceHasTag.weight)
            jsonTagList.add(jsonTag);
        }
        jsonGenericResource.put("tagList", jsonTagList);
        return jsonGenericResource;
    }

    def saveOrUpdate(GenericResource newGenericResource) {
        if (params.containsKey("plotId")) {
            Plot plot = Plot.get(params.plotId as Integer)
            newGenericResource.plot = plot
            if (!checkresource(plot))
            return false;
        } else {
            return false
        }
        if (params.containsKey("resourceCode")) {
            newGenericResource.code = params.resourceCode
        } else {
            return false
        }
        if (params.containsKey("resourceComment")) {
            newGenericResource.comment = params.resourceComment
        } else {
            return false
        }
        if (params.containsKey("resourceConstantForm")){
            if (params.resourceConstantForm != "null")
                newGenericResource.gnConstant = GnConstant.get(params.resourceConstantForm as Integer);
            else
                newGenericResource.gnConstant = null;
        } else {
            return false
        }
        if (params.containsKey("resourceRolePossessor")) {
            if (params.resourceRolePossessor != "") {
                Integer possessorId = params.resourceRolePossessor as Integer;
                Role possessor = Role.get(possessorId);
                newGenericResource.possessedByRole = possessor;
            }
        }
        if (params.containsKey("resourceObject")) {
            ObjectType objectType = ObjectType.findById(params.resourceObject as Integer);
            newGenericResource.objectType = objectType;
        }
        else {
            ObjectType objectType = ObjectType.findById(1);
            newGenericResource.objectType = objectType;
        }
        if(newGenericResource.extTags != null) {
            HashSet<GenericResourceHasTag> genericResourceHasTags = newGenericResource.extTags;
            newGenericResource.extTags.clear();
            GenericResourceHasTag.deleteAll(genericResourceHasTags);
        } else {
            newGenericResource.extTags = new HashSet<GenericResourceHasTag>();
        }
        if(newGenericResource.roleHasEventHasRessources != null) {
            HashSet<RoleHasEventHasGenericResource> genericResourceHasRoleHasEvents = newGenericResource.roleHasEventHasRessources;
            newGenericResource.roleHasEventHasRessources.clear();
            RoleHasEventHasGenericResource.deleteAll(genericResourceHasRoleHasEvents);
        } else {
            newGenericResource.roleHasEventHasRessources = new HashSet<RoleHasEventHasGenericResource>()
        }
        newGenericResource.version = 1;
        newGenericResource.dateCreated = new Date();
        newGenericResource.lastUpdated = new Date();
        if (params.containsKey("resourceIsClue")) {
            newGenericResource.title = params.resourceTitle;
            if (params.containsKey("resourceRolePossessor")) {
                Integer possessorId = params.resourceRolePossessor as Integer;
                Role possessor = Role.get(possessorId);
                newGenericResource.possessedByRole = possessor;
            }


//            if (params.containsKey("resourceRoleFrom")) {
//                Integer roleFromId = params.resourceRoleFrom as Integer;
//                Role roleFrom = Role.get(roleFromId);
//                newGenericResource.fromRole = roleFrom;
//            }
//            if (params.containsKey("resourceRoleTo")) {
//                Integer roleToId = params.resourceRoleTo as Integer;
//                Role roleTo = Role.get(roleToId);
//                newGenericResource.toRole = roleTo;
//            }

            // from & to role text version

            if (params.containsKey("resourceRoleFrom")) {
                newGenericResource.fromRoleText = params.resourceRoleFrom;
                print(params.resourceRoleFrom);
            }
            if (params.containsKey("resourceRoleTo")) {
                newGenericResource.toRoleText = params.resourceRoleTo;
                print(params.resourceRoleTo);
            }
            if (params.containsKey("resourceDescription")) {
                newGenericResource.description = params.resourceDescription;
                print(params.resourceDescription);
            }
        }
        else if (newGenericResource.title != null) {
            newGenericResource.title = null;
            newGenericResource.possessedByRole = null;
            newGenericResource.fromRoleText = null;
            newGenericResource.toRoleText = null;
            newGenericResource.description = null;
        }
        newGenericResource.save(flush: true);

        params.each {
            if (it.key.startsWith("resourceTags_")) {
                GenericResourceHasTag genericResourceHasTag = new GenericResourceHasTag();
                Tag genericResourceTag = Tag.get((it.key - "resourceTags_") as Integer);
                genericResourceHasTag.tag = genericResourceTag
                genericResourceHasTag.weight = params.get("resourceTagsWeight_" + genericResourceTag.id) as Integer;
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
                object(isupdate: saveOrUpdate(genericResource),
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

    def getBestResources() {
        org.gnk.ressplacetime.GenericResource genericressource = new org.gnk.ressplacetime.GenericResource()
        List<com.gnk.substitution.Tag> tags = new ArrayList<>();
        params.each {
            if (it.key.startsWith("resourceTags_")) {
                com.gnk.substitution.Tag subtag = new com.gnk.substitution.Tag();
                Tag tag = Tag.get((it.key - "resourceTags_") as Integer);
                if (tag.parent != null) {
                    subtag.value = tag.name;
                    subtag.weight = params.get("resourceTagsWeight_" + tag.id) as Integer;
                    subtag.type = tag.parent.name;
                    if (tag.parent.id == 33096)
                    {
                        tags.add(tags.size(), subtag);
                    }
                    else {
                        tags.add(0, subtag);
                    }
                }
            }
        }
        ResourceService resourceService = new ResourceService();
        Tag tagUnivers = new Tag();
        tagUnivers = Tag.findById("33089");
        ArrayList<Tag> universList = Tag.findAllByParent(tagUnivers);
        genericressource.setTagList(tags);


        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < universList.size() ; i++) {
            genericressource = resourceService.findReferentialResource(genericressource, universList[i].name);
            jsonArray.add(universList[i].name);
            for(ReferentialResource ref in genericressource.resultList) {
                jsonArray.add(ref.name);
            }
            json.put(universList[i].name,jsonArray);
            jsonArray = [];
        }

        render(contentType: "application/json") {
            object(json: json)

            return json.toString();
        }
    }
}
