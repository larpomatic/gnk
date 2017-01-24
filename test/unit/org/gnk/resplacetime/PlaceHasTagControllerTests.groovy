package org.gnk.resplacetime



import org.junit.*
import grails.test.mixin.*

@TestFor(PlaceHasTagController)
@Mock(PlaceHasTag)
class PlaceHasTagControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/placeHasTag/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.placeHasTagInstanceList.size() == 0
        assert model.placeHasTagInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.placeHasTagInstance != null
    }

    void testSave() {
        controller.save()

        assert model.placeHasTagInstance != null
        assert view == '/placeHasTag/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/placeHasTag/show/1'
        assert controller.flash.message != null
        assert PlaceHasTag.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/placeHasTag/list'

        populateValidParams(params)
        def placeHasTag = new PlaceHasTag(params)

        assert placeHasTag.save() != null

        params.id = placeHasTag.id

        def model = controller.show()

        assert model.placeHasTagInstance == placeHasTag
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/placeHasTag/list'

        populateValidParams(params)
        def placeHasTag = new PlaceHasTag(params)

        assert placeHasTag.save() != null

        params.id = placeHasTag.id

        def model = controller.edit()

        assert model.placeHasTagInstance == placeHasTag
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/placeHasTag/list'

        response.reset()

        populateValidParams(params)
        def placeHasTag = new PlaceHasTag(params)

        assert placeHasTag.save() != null

        // test invalid parameters in update
        params.id = placeHasTag.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/placeHasTag/edit"
        assert model.placeHasTagInstance != null

        placeHasTag.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/placeHasTag/show/$placeHasTag.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        placeHasTag.clearErrors()

        populateValidParams(params)
        params.id = placeHasTag.id
        params.version = -1
        controller.update()

        assert view == "/placeHasTag/edit"
        assert model.placeHasTagInstance != null
        assert model.placeHasTagInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/placeHasTag/list'

        response.reset()

        populateValidParams(params)
        def placeHasTag = new PlaceHasTag(params)

        assert placeHasTag.save() != null
        assert PlaceHasTag.count() == 1

        params.id = placeHasTag.id

        controller.delete()

        assert PlaceHasTag.count() == 0
        assert PlaceHasTag.get(placeHasTag.id) == null
        assert response.redirectedUrl == '/placeHasTag/list'
    }
}
