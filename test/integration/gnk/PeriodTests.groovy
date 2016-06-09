package gnk

import org.apache.commons.lang3.time.DateUtils
import org.gnk.gn.Gn
import org.gnk.gn.GnHasConvention
import org.gnk.naming.Convention
import org.gnk.resplacetime.Period
import org.gnk.resplacetime.TimeService
import org.gnk.ressplacetime.EventTime
import org.junit.*

import java.security.InvalidParameterException

class PeriodTests {

    Integer gnId = null;
    String gnName = "integrationTestGn"
    // Integer conventionId = null;
    Date gnBeginning = Date.parse("yyyy-MM-dd HH:mm", "2016-05-28 09:00")
    Integer gnDuration = 10

    @Before
    void setUp() {
        // Setup logic here
//        Convention convention = new Convention(description: "IntegrationTestingConvention")
//        convention.save(failOnError: true)
//        conventionId = convention.getId()
        Convention convention = Convention.findByDescription("Occidentale")
        GnHasConvention gnHasConvention = new GnHasConvention(convention: convention)
        Gn gn = new Gn(name: gnName, gnHasConvention: gnHasConvention, date: gnBeginning,
                duration: gnDuration, step: "selectIntrigue")
        gnHasConvention.setGn(gn)
        gn.save(failOnError: true)
        gnHasConvention.save(failOnError: true)
        gnId = gn.getId()
    }

    @After
    void tearDown() {
        // Tear down logic here
        //if(conventionId != null)
        //    Convention.findById(conventionId).delete()
        if(gnId != null)
            Gn.findById(gnId).delete()
        Gn.findAllByName(gnName).each {Gn gn -> gn.delete()}
    }

    @Test
    void testCheckBlocking() {
        given:
        Gn gn = Gn.findById(gnId)
        Period p1 = new Period(name: "IntegrationTestP1", description: "Testing period 1", location: "", gn: gn,
                isPublic: true, beginning: gnBeginning, duration: 60, isBlocking: false, isGamePeriod: false,
                predInterval: 0)
        p1.save(failOnError: true)

        Period p2 = new Period(gn: gn, beginning: gnBeginning, duration: 20, isBlocking: false, isPublic: true,
                name: "IntegrationTestP2", location: "", description: "", predInterval: 0, isGamePeriod: false)
        p2.save(failOnError: true)

        when:
        Console.println("Nb of Period : " + Period.list().size())
        Console.println(p2.name + " " + p2.beginning.toString())
        if (p1.checkCanBeBlocking()) {
            p1.setIsBlocking(true)
        }
        if (p2.checkCanBeBlocking()) {
            p2.setIsBlocking(true)
        }

        //then:
        assert p1.getIsBlocking()
        assert !p2.getIsBlocking()

        p1.delete()
        p2.delete()
    }

    @Test
    void timeTestForEvent() {
        final TimeService timeService = new TimeService()

        //Using the gn from the setUp needed to test the blocking periods
        Gn gn = Gn.findById(gnId)

        // Event qui se passe à 20%
        EventTime event1 = new EventTime();
        event1.code = "EV1";
        event1.timing = 20;
        event1.duration = 60
        def result1 = timeService.eventRealDate(event1, gn.getDate(), gnDuration, gn.getId());
        Console.println(result1)

        // Event qui se passe à 50%
        EventTime event2 = new EventTime();
        event2.code = "EV2";
        event2.timing = 50;
        event2.duration = 60
        def result2 = timeService.eventRealDate(event2, gn.getDate(), gnDuration, gn.getId());
        Console.println(result2)
        assert result2.getAbsoluteHour() == (int)((float)(event2.getTiming() * gnDuration) / 100) + gnBeginning.getHours()
        Date beginningP1 = DateUtils.addHours(gnBeginning, (int)gnDuration/2)
        Period blockingPeriod2 = new Period(gn: gn, beginning: beginningP1, duration: 60, isBlocking: true, isPublic: true,
                name: "Blocking Period 2", location: "", description: "", predInterval: 0, isGamePeriod: false)
        blockingPeriod2.save(failOnError: true)
        result2 = timeService.eventRealDate(event2, gn.getDate(), gnDuration, gn.getId());
        assert result2.absoluteHour == (int)((float)(event2.getTiming() * gnDuration) / 100) +
                gnBeginning.getHours() + (blockingPeriod2.getDuration()/60);
        Period nonBlocking = new Period(gn: gn, beginning: gnBeginning, duration: 10*60, isBlocking: false, isPublic: true,
                name: "Non Blocking Period", location: "", description: "", predInterval: 0, isGamePeriod: true)
        nonBlocking.save(failOnError: true)
        result2 = timeService.eventRealDate(event2, gn.getDate(), gnDuration, gn.getId());
        assert result2.absoluteHour == (int)((float)(event2.getTiming() * gnDuration) / 100) +
                gnBeginning.getHours() + (blockingPeriod2.getDuration()/60);

    }

    @Test
    void testNullParameters () {
        final TimeService timeService = new TimeService()
        try {
            timeService.eventRealDate(null, null, null, null)
        }
        catch (InvalidParameterException e) {

        }
    }
}

/*
        final gnIdStr = params.gnId
        if (gnIdStr == null) {
            gnIdStr = session.getAttribute("gnId")
        }
        if (gnIdStr == null || !(gnIdStr as String).isInteger()) {
            //redirect(action: "list", controller: "selectIntrigue", params: params)
            //return
            String fileContent = new File(xmlGnTestPath).text
            inputHandler.parseGN(fileContent)
        } else {
            Integer gnDbId = gnIdStr as Integer;
            List<String> sexes = params.sexe;
            //Gn gn = changeCharSex(gnDbId, sexes);
            Gn gn = Gn.get(gnDbId)
            //gn = changeCharSex(gn, sexes);
            inputHandler.parseGN(gn, sexes);

            /*render(text: gn.getDtd(), contentType: "text/xml", encoding: "UTF-8")
            return*/
/*
}

Gn gn = Gn.get(gnIdStr as Integer)

final gnData = new GNKDataContainerService()
gnData.ReadDTD(gn)

 */
