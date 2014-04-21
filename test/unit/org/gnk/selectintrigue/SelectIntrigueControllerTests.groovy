package org.gnk.selectintrigue

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.gnk.gn.Gn
import org.gnk.tag.Tag
import org.gnk.tag.Univers

import java.text.SimpleDateFormat

/**
 * Created by pico on 15/04/2014.
 */
@TestFor(SelectIntrigueController)
@Mock([Tag, Plot, Gn, Univers])
class SelectIntrigueControllerTests {

    Gn gn = null

    def createGn() {

        def gnInstance = new Gn()
        SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd")

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

    void testToto() {

        assert true == controller.toto()
        //Assert.assertEquals(true, SelectIntrigueController.toto());
    }

    void "test toto return true"() {

        given:
        SelectIntrigueController s = new SelectIntrigueController()

        when:
        def res = s.toto()

        then:
        res == true
    }

    void "test selectIntrigue"() {

//        Long testId = 2
//        Gn i = createGn()
//        Long id = i.getId()
//        controller.selectIntrigue(id)

        given:
        Gn gn = createGn().save()
        SelectIntrigueController s = new SelectIntrigueController()
        params.put("ScreenStep", 1)

        when:
        s.selectIntrigue((Long) gn.getId())

    }
}
