package org.gnk.selectintrigue

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.gnk.gn.Gn
import org.gnk.naming.Firstname
import org.gnk.naming.FirstnameHasTag
import org.gnk.naming.Name
import org.gnk.naming.NameHasTag
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceHasTag
import org.gnk.resplacetime.Resource
import org.gnk.resplacetime.ResourceHasTag
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.roletoperso.RoleHasRelationWithRole
import org.gnk.roletoperso.RoleHasTag
import org.gnk.roletoperso.RoleRelationType
import org.gnk.tag.Tag
import org.gnk.tag.TagFamily
import org.gnk.tag.Univers
import org.gnk.user.User

import java.text.SimpleDateFormat

/**
 * Created by pico on 15/04/2014.
 */
@TestFor(SelectIntrigueController)
@Mock([Tag, Plot, Gn, Univers, User, TagFamily, GenericPlaceHasTag, GenericPlace, Pastscene, GenericPlaceHasTag, GenericResourceHasTag, GenericResource, GenericPlace,
Place, PlaceHasTag, Resource, ResourceHasTag, Firstname, FirstnameHasTag, Name, NameHasTag, PlotHasTag, Role, RoleHasTag, Event, RoleHasEvent,
RoleRelationType, RoleHasPastscene, RoleHasRelationWithRole, RoleHasEventHasGenericResource])
class SelectIntrigueControllerTests {

    Gn gn = null

    def createGn() {

        def gnInstance = new Gn()
        SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd")
        gnInstance.name = "name"
        gnInstance.dtd = new File("/Users/pico/projets/prc-gn/gnk/test/unit/org/gnk/selectintrigue/gn.xml").text
        gnInstance.date = adf.parse("2014-04-12")
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
        Calendar calHour = Calendar.getInstance();
        calHour.setTime(sdfHour.parse("11:06"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(gnInstance.date);
        cal.set(Calendar.HOUR_OF_DAY, calHour.get(Calendar.HOUR_OF_DAY))
        cal.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE))
        gnInstance.date = cal.getTime();
        gnInstance.univers = Univers.get(2) // Star Wars
        gnInstance.step = 0
        gnInstance.isMainstream = false
        gnInstance.t0Date = adf.parse("2014-04-13")
        calHour.setTime(sdfHour.parse("11:10"));
        cal.setTime(gnInstance.t0Date);
        cal.set(Calendar.HOUR_OF_DAY, calHour.get(Calendar.HOUR_OF_DAY))
        cal.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE))
        gnInstance.t0Date = cal.getTime();
        gnInstance.duration = 5 // in hour
        gnInstance.pipMin = 3
        gnInstance.pipMax = 5
        gnInstance.pipCore = 3
        gnInstance.nbPlayers = 4
        gnInstance.nbMen = 1
        gnInstance.nbWomen = 2

        Map<Tag, Integer> gnTags = gnInstance.gnTags
        Map<Tag, Integer> mainstreamTags = gnInstance.mainstreamTags
        Map<Tag, Integer> evenementialTags = gnInstance.evenementialTags
        gnTags = new HashMap<Tag, Integer>()
        gnInstance.gnTags = gnTags
        mainstreamTags = new HashMap<Tag, Integer>()
        gnInstance.mainstreamTags = mainstreamTags
        evenementialTags = new HashMap<Tag, Integer>()
        gnInstance.evenementialTags = evenementialTags
        Tag plotTag = Tag.get(3) // Tag violence
        String weight = "50"
        gnTags.put(plotTag, weight)
        Tag eventPlotTag = Tag.get(4) // Tag Gore
        evenementialTags.put(eventPlotTag, weight)
        return gnInstance
    }

    void "test show gn"() {

        given:
        Gn gn = createGn()
        gn.save(failOnError: true)

        when:
        def res = controller.show(gn.id)

        then:
        res.gnInstance.id == gn.id
    }

    void "test selectIntrigue"() {

        given:
        Gn gn = createGn()
        gn.save(failOnError: true)
        controller.params.put("ScreenStep", 1)
        controller.params.put("gnDTD", "just for test")

        when:
        def res = controller.selectIntrigue(gn.id)

        then:
        res.gnInstance == gn.id
        res.gnInstance.dtd == "just for test"

    }
}
