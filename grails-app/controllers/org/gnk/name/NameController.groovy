package org.gnk.name


import org.gnk.naming.Name
import org.gnk.naming.NameHasTag
import org.gnk.tag.Tag
import org.gnk.tag.TagService

class NameController {
        static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
        def index() {
            redirect(action: "list")
        }
        def list() {
            params.max = Math.min(params.max ? params.int('max') : 10, 100)
            def ls = Name.createCriteria().list (params) {
                if ( params.query ) {
                    ilike("name", "%${params.query}%")
                }
            }
            ls.sort()
            def totalCount = ls.size()
            [NameInstanceList: ls, nameTotal: totalCount, params: params]
        }
def create() {
    List<NameHasTag> NameHasTagList = new ArrayList<>()
    def list = Name.list()
    TagService tagService = new TagService();
    def listTag = tagService.getNameTagQuery()

    Name NameInstance = new Name()
    NameInstance.id = -1
    NameInstance.version = 1
    NameInstance.dateCreated = new Date ()

    [NameInstance: NameInstance,
     NameInstanceList: list,
     TagInstanceList: listTag,
     NameHasTagList : NameHasTagList]
}
    def dupplicate(Long id) {
        List<NameHasTag> NameHasTagList = new ArrayList<>()
        addNameChoice(NameHasTagList)
        addNameSelected(NameHasTagList)

        long idName = id ?:
                params.NameId == null || params.NameId == "" ? -1 : params.NameId as long;
        Name NameInstanceold = getName(idName)

        def list = NameInstanceold.list()
        TagService tagService = new TagService();
        def listTag = tagService.getNameTagQuery()
        for(NameHasTag nht : NameInstanceold.extTags) {
            NameHasTagList.add(nht)
        }
        Name NameInstance = new Name()
        NameInstance.id = -1
        NameInstance.version = 1
        NameInstance.dateCreated =new Date()
        NameInstance.name = NameInstanceold.name
        NameInstance.extTags = NameInstanceold.extTags
        [NameInstance: NameInstance,
         NameInstanceList: list,
         TagInstanceList: listTag,
         NameHasTagList : NameHasTagList,]
    }

def save() {
    Name NameInstance = new Name(params)
    NameInstance.nameHasTag.first().tag.name
    if (!NameInstance.save(flush: true)) {
       return render(view: "create", model: [NameInstance: NameInstance])
    }

    flash.message = message(code: 'default.created.message', args: [message(code: 'Name.label', default: 'Name'), NameInstance.id])
   redirect(action: "list", id: NameInstance.id)
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

    def edit(Long id) {

        List<NameHasTag> NameHasTagList = new ArrayList<>()
        addNameChoice(NameHasTagList)
        addNameSelected(NameHasTagList)

        long idName = id ?:
                params.NameId == null || params.NameId == "" ? -1 : params.NameId as long;
        Name NameInstance = getName(idName)
        def list = NameInstance.list()
        TagService tagService = new TagService();
        def listTag = tagService.getNameTagQuery()
        for(NameHasTag nht : NameInstance.extTags) {
            NameHasTagList.add(nht)
        }

        [NameInstance: NameInstance,
         NameInstanceList: list,
         TagInstanceList: listTag,
         NameHasTagList : NameHasTagList,]
    }

    def update(Long NameId, Long NameVersion) {

        List<NameHasTag> NameHasTagList = new ArrayList<>()

        addNameSelected(NameHasTagList)
        def var = params.NameId ?: "-1"
        Name n = getName(var as long)

        ArrayList<NameHasTag> temp = new ArrayList<>()
        for(NameHasTag prev : n.extTags){
            if (NameHasTagList.find {it.tag.id == prev.tag.id && it.weight == prev.weight} == null){
                temp.add(prev)
            }
        }

        for(NameHasTag prev : temp){
            n.removeFromExtTags(prev)
        }

        for (NameHasTag future : NameHasTagList){
            n.addToExtTags(future)
        }
        n.gender="n"
        if (!n.save(flush: true)) {
            return render(view: "edit", model: [NameInstance: n])
        }
        redirect(action: "list")
    }
    void addNameChoice(List<NameHasTag> NameHasTagList)
    {
        NameHasTagList.clear()

        params.each {
            if (it.key.toString().contains("NameTagsWeight") && it.value != null && it.value != "") {
                NameHasTag NameHasTag = new NameHasTag();
                NameHasTag.id = 0
                NameHasTag.tag = Tag.findById(it.key.toString().tokenize("_")[1].toInteger());
                NameHasTag.weight = it.value.toString().toInteger();
                NameHasTag.dateCreated = new Date();
                NameHasTag.lastUpdated = new Date();
                NameHasTag.version = 1

                NameHasTagList.add(NameHasTag)
            }
        };
    }

    void addNameSelected(List<NameHasTag> NameHasTagList)
    {

        params.each {
            if (it.key.toString().contains("tableTag_") && it.value != null && it.value != "") {
                NameHasTag nameHasTag = new NameHasTag();
                def tokenize = it.value.toString().tokenize("_")
                nameHasTag.tag = Tag.findById(tokenize[0].toInteger());
                nameHasTag.weight = tokenize[1].toInteger();
                nameHasTag.lastUpdated = new Date();
                nameHasTag.version = 1

                if (!NameHasTagList.find {it.tag.id == nameHasTag.tag.id}){
                    NameHasTagList.add(nameHasTag)
                }
            }

        };
    }
    private Name getName(long id) {

        Name NameInstance = null;

        if (id != -1) {
            NameInstance = Name.get(id)
        }
        if (NameInstance == null) {
            NameInstance = new Name(params)
            if (params.NameId != null && params.NameId != ""){
                NameInstance.id = ((String) params.NameId)?.toInteger();
            }
             NameInstance.version = 1
            NameInstance.lastUpdated = new Date();
            NameInstance.extTags = new HashSet<>()

        } else {
            NameInstance.properties = params
        }
        return NameInstance
    }

    }