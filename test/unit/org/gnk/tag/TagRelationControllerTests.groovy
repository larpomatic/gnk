package org.gnk.tag
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(TagRelationController)
@Mock(TagRelation)
class TagRelationControllerTests {

//    def populateValidParams(params) {
//        assert params != null
//        // TODO: Populate valid properties like...
//        //params["name"] = 'someValidName'
//    }
//
//    void testIndex() {
//        controller.index()
//        assert "/tagRelation/list" == response.redirectedUrl
//    }
//
//    void testList() {
//
//        def model = controller.list()
//
//        assert model.tagRelationInstanceList.size() == 0
//        assert model.tagRelationInstanceTotal == 0
//    }
//
//    void testCreate() {
//        def model = controller.create()
//
//        assert model.tagRelationInstance != null
//    }

    void testSave() {
        /*
        controller.save()

        assert model.tagRelationInstance != null
        assert view == '/tagRelation/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/tagRelation/show/1'
        assert controller.flash.message != null
        assert TagRelation.count() == 1
        */
        // FIXME
    }

    void testShow() {
        /*
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/tagRelation/list'

        populateValidParams(params)
        def tagRelation = new TagRelation(params)

        assert tagRelation.save() != null

        params.id = tagRelation.id

        def model = controller.show()

        assert model.tagRelationInstance == tagRelation
        */
        // FIXME
    }

    void testEdit() {
        /*
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/tagRelation/list'

        populateValidParams(params)
        def tagRelation = new TagRelation(params)

        assert tagRelation.save() == true

        params.id = tagRelation.id

        def model = controller.edit()

        assert model.tagRelationInstance == tagRelation
        */
        // FIXME
    }

    void testUpdate() {
        /*
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/tagRelation/list'

        response.reset()

        populateValidParams(params)
        def tagRelation = new TagRelation(params)

        assert tagRelation.save() == true

        // CreateGnTests invalid parameters in update
        params.id = tagRelation.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/tagRelation/edit"
        assert model.tagRelationInstance != null

        tagRelation.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/tagRelation/show/$tagRelation.id"
        assert flash.message != null

        //CreateGnTests outdated version number
        response.reset()
        tagRelation.clearErrors()

        populateValidParams(params)
        params.id = tagRelation.id
        params.version = -1
        controller.update()

        assert view == "/tagRelation/edit"
        assert model.tagRelationInstance != null
        assert model.tagRelationInstance.errors.getFieldError('version')
        assert flash.message != null
        */
        // FIXME
    }

    void testDelete() {
        /*
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/tagRelation/list'

        response.reset()

        populateValidParams(params)
        def tagRelation = new TagRelation(params)

        assert tagRelation.save() == true
        assert TagRelation.count() == 1

        params.id = tagRelation.id

        controller.delete()

        assert TagRelation.count() == 0
        assert TagRelation.get(tagRelation.id) == null
        assert response.redirectedUrl == '/tagRelation/list'
        */
        // FIXME
    }
}
