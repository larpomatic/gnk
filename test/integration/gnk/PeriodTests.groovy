package gnk

import org.gnk.gn.Gn
import org.gnk.gn.GnHasConvention
import org.gnk.naming.Convention
import org.gnk.resplacetime.Period

import static org.junit.Assert.*
import org.junit.*

class PeriodTests {

    Integer gnId = null;
    String gnName = "integrationTestGn"
    Integer conventionId = null;

    @Before
    void setUp() {
        // Setup logic here
//        Convention convention = new Convention(description: "IntegrationTestingConvention")
//        convention.save(failOnError: true)
//        conventionId = convention.getId()
        Convention convention = Convention.findByDescription("Occidentale")
        GnHasConvention gnHasConvention = new GnHasConvention(convention: convention)
        Gn gn = new Gn(name: gnName, gnHasConvention: gnHasConvention)
        gnHasConvention.setGn(gn)
        gn.save(failOnError: true)
        gnHasConvention.save(failOnError: true)
        gnId = gn.getId()
    }

    @After
    void tearDown() {
        // Tear down logic here
        if(conventionId != null)
            Convention.findById(conventionId).delete()
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
}
