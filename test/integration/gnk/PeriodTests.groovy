package gnk

import org.gnk.gn.Gn
import org.gnk.gn.GnHasConvention
import org.gnk.naming.Convention
import org.gnk.resplacetime.Period
import org.gnk.resplacetime.TimeService
import org.gnk.ressplacetime.EventTime
import org.junit.*

class PeriodTests {

    Integer gnId = null;
    String gnName = "integrationTestGn"
    // Integer conventionId = null;

    @Before
    void setUp() {
        // Setup logic here
//        Convention convention = new Convention(description: "IntegrationTestingConvention")
//        convention.save(failOnError: true)
//        conventionId = convention.getId()
        Calendar cal = new GregorianCalendar(2016, Calendar.MAY, 28)
        cal.set(Calendar.HOUR_OF_DAY, 9)
        Convention convention = Convention.findByDescription("Occidentale")
        GnHasConvention gnHasConvention = new GnHasConvention(convention: convention)
        Gn gn = new Gn(name: gnName, gnHasConvention: gnHasConvention, date: cal.getTime(), duration: 6)
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
    void testSetBlocking() {
        given:
        Calendar cal = new GregorianCalendar(2016, Calendar.MAY, 28)
        cal.set(Calendar.HOUR_OF_DAY, 10)
        Gn gn = Gn.findById(gnId)
        Date date1 = cal.getTime()
        Date date2 = Date.parse("yyyy-MM-dd-hh", "2016-05-28-10")
        assert date1.equals(date2)
        Period p1 = new Period(name: "IntegrationTestP1", description: "Testing period 1", location: "", gn: gn,
                isPublic: true, beginning: cal.getTime(), duration: 60, isBlocking: false)
        p1.save(failOnError: true)

        Period p2 = new Period(gn: gn, beginning: cal.getTime(), duration: 20, isBlocking: false,
                name: "IntegrationTestP2", isPublic: true, location: "", description: "")
        p2.save(failOnError: true)

        when:
        Console.println("Nb of Period : " + Period.list().size())
        Console.println(p2.name + " " + p2.beginning.toString())
        p1.setIsBlocking(true)
        p2.setIsBlocking(true)

        then:
        assert p1.getIsBlocking()
        assert !p2.getIsBlocking()

        Period fetchTest = Period.findByName("IntegrationTestP1")
        assert fetchTest.getIsBlocking()
    }

    void testTiming() {

    }

    def timeTestForEvent() {
        final TimeService timeService = new TimeService()

        // Date de pivot (choisie comme étant le début du GN)
        /*
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 4, 1, 10, 0);
        Date gnBeginDate = calendar.getTime();
        Integer gnDuration = 50;
        render("Début du GN: " + gnBeginDate.toString()
                + "<br/>Durée du GN: " + gnDuration.toString() + " heures<br/><br/>");
        EventTime result;
        */

        Gn gn = Gn.findById(gnId)

        // Event qui se passe à 20%
        //render("On teste: 20% (soit T0 + 10 heures)<br/>");
        EventTime event1 = new EventTime();
        event1.code = "EV1";
        event1.timing = 20;
        def result1 = timeService.eventRealDate(event1, gn.getDate(), gn.getDuration(), gn.getId());
        Console.println(result1)
        //render(result.code + ": " + result.absoluteDay.toString() + "." + result.absoluteMonth.toString() + "." + result.absoluteYear.toString()
        //        + " " + result.absoluteHour.toString() + ":" + result.absoluteMinute.toString() + "<br/><br/>");

        // Event qui se passe à 50%
        //render("On teste: 50% (soit T0 + 25 heures)<br/>");
        EventTime event2 = new EventTime();
        event2.code = "EV2";
        event2.timing = 50;
        def result2 = timeService.eventRealDate(event2, gn.getDate(), gn.getDuration(), gn.getId());
        Console.println(result2)

        //render(result.code + ": " + result.absoluteDay.toString() + "." + result.absoluteMonth.toString() + "." + result.absoluteYear.toString()
        //        + " " + result.absoluteHour.toString() + ":" + result.absoluteMinute.toString() + "<br/><br/>");
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
