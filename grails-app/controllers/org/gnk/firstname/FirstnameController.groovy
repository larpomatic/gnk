package org.gnk.firstname

import org.gnk.naming.Firstname
import org.gnk.naming.FirstnameHasTag
import org.gnk.tag.Tag
import org.gnk.tag.TagService

class FirstnameController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def index() {
        redirect(action: "list")
    }
    def list(String sort) {
        def firstnames = Firstname.list()
        [FirstnameInstanceList: firstnames]
    }
    def create() {
        List<FirstnameHasTag> FirstnameHasTagList = new ArrayList<>()
        def list = Firstname.list()
        TagService tagService = new TagService();
        def listTag = tagService.getFirstnameTagQuery()

        Firstname FirstnameInstance = new Firstname()
        FirstnameInstance.id = -1
        FirstnameInstance.version = 1

        [FirstnameInstance: FirstnameInstance,
         FirstnameInstanceList: list,
         TagInstanceList: listTag,
         FirstnameHasTagList : FirstnameHasTagList]
    }
    def dupplicate(Long id) {
        List<FirstnameHasTag> FirstNameHasTagList = new ArrayList<>()
        addFirstnameChoice(FirstNameHasTagList)
        addFirstnameSelected(FirstNameHasTagList)

        long idFirstname = id ?:
                params.FirstnameId == null || params.FirstnameId == "" ? -1 : params.FirstnameId as long;
        Firstname FirstnameInstanceold = getFirstname(idFirstname)

        def list = FirstnameInstanceold.list()
        TagService tagService = new TagService();
        def listTag = tagService.getFirstnameTagQuery()
        for(FirstnameHasTag nht : FirstnameInstanceold.extTags) {
            FirstNameHasTagList.add(nht)
        }
        Firstname FirstnameInstance = new Firstname()
        FirstnameInstance.id = -1
        FirstnameInstance.version = 1
        FirstnameInstance.dateCreated =new Date()

        FirstnameInstance.name = FirstnameInstanceold.name
        FirstnameInstance.extTags = FirstnameInstanceold.extTags

        [FirstnameInstance: FirstnameInstance,
         FirstnameInstanceList: list,
         TagInstanceList: listTag,
         FirstnameHasTagList : FirstNameHasTagList]
    }

    def save() {
        Firstname FirstnameInstance = new Firstname(params)
        FirstnameInstance.firstnameHasTag.first().tag.name
        if (!FirstnameInstance.save(flush: true)) {
            return render(view: "create", model: [FirstnameInstance: FirstnameInstance])
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'Firstname.label', default: 'Firstname'), FirstnameInstance.id])
        redirect(action: "list", id: FirstnameInstance.id)
    }
    def show(Long id) {
        redirect(action: "list", params: params)
    }


    def delete(Long id) {
        def FirstnameInstance = Firstname.get(id)
        String Firstname = FirstnameInstance.name

        if (!FirstnameInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'Firstname.label', default: 'Firstname'), Firstname])
            redirect(action: "list")
            return
        }

        FirstnameInstance.delete(flush: true)
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'Firstname.label', default: 'Firstname'), Firstname])
        redirect(action: "list")
    }

    def edit(Long id) {

        List<FirstnameHasTag> FirstnameHasTagList = new ArrayList<>()
        addFirstnameChoice(FirstnameHasTagList)
        addFirstnameSelected(FirstnameHasTagList)

        long idFirstname = id ?:
                params.FirstnameId == null || params.FirstnameId == "" ? -1 : params.FirstnameId as long;
        Firstname FirstnameInstance = getFirstname(idFirstname)
        def list = FirstnameInstance.list()
        TagService tagService = new TagService();
        def listTag = tagService.getFirstnameTagQuery()
        for(FirstnameHasTag nht : FirstnameInstance.extTags) {
            FirstnameHasTagList.add(nht)
        }

        [FirstnameInstance: FirstnameInstance,
         FirstnameInstanceList: list,
         TagInstanceList: listTag,
         FirstnameHasTagList : FirstnameHasTagList,]
    }

    def update(Long FirstnameId, Long FirstnameVersion) {

        List<FirstnameHasTag> FirstnameHasTagList = new ArrayList<>()

        addFirstnameSelected(FirstnameHasTagList)
        def var = params.FirstnameId ?: "-1"
        Firstname n = getFirstname(var as long)

        ArrayList<FirstnameHasTag> temp = new ArrayList<>()
        for(FirstnameHasTag prev : n.extTags){
            if (FirstnameHasTagList.find {it.tag.id == prev.tag.id && it.weight == prev.weight} == null){
                temp.add(prev)
            }
        }

        for(FirstnameHasTag prev : temp){
            n.extTags.remove(prev)
        }

        for (FirstnameHasTag future : FirstnameHasTagList){
            n.extTags.add(future)
        }
        n.gender="n"
        if (!n.save (flush: true)) {
            return render(view: "edit", model: [FirstnameInstance: n])
        }
        redirect(action: "list")
    }
    void addFirstnameChoice(List<FirstnameHasTag> FirstnameHasTagList)
    {
        FirstnameHasTagList.clear()

        params.each {
            if (it.key.toString().contains("FirstnameTagsWeight") && it.value != null && it.value != "") {
                FirstnameHasTag FirstnameHasTag = new FirstnameHasTag();
                FirstnameHasTag.id = 0
                FirstnameHasTag.tag = Tag.findById(it.key.toString().tokenize("_")[1].toInteger());
                FirstnameHasTag.weight = it.value.toString().toInteger();
                FirstnameHasTag.dateCreated = new Date();
                FirstnameHasTag.lastUpdated = new Date();
                FirstnameHasTag.version = 1

                FirstnameHasTagList.add(FirstnameHasTag)
            }
        };
    }

    void addFirstnameSelected(List<FirstnameHasTag> FirstnameHasTagList)
    {

        params.each {
            if (it.key.toString().contains("tableTag_") && it.value != null && it.value != "") {
                FirstnameHasTag FirstnameHasTag = new FirstnameHasTag();
                def tokenize = it.value.toString().tokenize("_")
                FirstnameHasTag.tag = Tag.findById(tokenize[0].toInteger());
                FirstnameHasTag.weight = tokenize[1].toInteger();
                FirstnameHasTag.dateCreated = new Date();
                FirstnameHasTag.lastUpdated = new Date();
                FirstnameHasTag.version = 1

                if (!FirstnameHasTagList.find {it.tag.id == FirstnameHasTag.tag.id}){
                    FirstnameHasTagList.add(FirstnameHasTag)
                }
            }

        };
    }
    private Firstname getFirstname(long id) {

        Firstname FirstnameInstance = null;

        if (id != -1) {
            FirstnameInstance = Firstname.get(id)
        }
        if (FirstnameInstance == null) {
            FirstnameInstance = new Firstname(params)
            if (params.FirstnameId != null && params.FirstnameId != ""){
                FirstnameInstance.id = ((String) params.FirstnameId)?.toInteger();
            }
            FirstnameInstance.version = 1
            FirstnameInstance.lastUpdated = new Date();
            FirstnameInstance.dateCreated = new Date();
            FirstnameInstance.extTags = new HashSet<>()

        } else {
            FirstnameInstance.properties = params
        }
        return FirstnameInstance
    }

}