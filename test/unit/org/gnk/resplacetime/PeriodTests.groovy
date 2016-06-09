package org.gnk.resplacetime



import grails.test.mixin.*
import org.gnk.gn.Gn
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
//@Mock([Gn])
@TestFor(Period)
class PeriodTests {

    Period period = new Period(beginning: Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 12:00"), duration: 60)


    // tested interval : []
    // this period interval : {}

    // { }
    // [ ]
    void testPeriodIsDuringSameInterval() {
        Date testedBeginning = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 12:00")
        Date testedEnd = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 13:00")
        assert period.isDuring(testedBeginning, testedEnd)
    }

    // [    ]
    //   {}
    void testPeriodInsideTested() {
        Date testedBeginning = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 10:00")
        Date testedEnd = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 16:00")
        assert period.isDuring(testedBeginning, testedEnd)
    }

    void testTestedInsidePeriod() {
        Date testedBeginning = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 12:20")
        Date testedEnd = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 12:40")
        assert period.isDuring(testedBeginning, testedEnd)
    }

    void testAcceptedEqualPeriodEndTestedBeginning() {
        Date testedBeginning = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 13:00")
        Date testedEnd = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 14:00")
        assert !period.isDuring(testedBeginning, testedEnd)
    }

    void testAcceptedEqualPeriodBeginningTestedEnd() {
        Date testedBeginning = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 10:00")
        Date testedEnd = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 12:00")
        assert !period.isDuring(testedBeginning, testedEnd)
    }

    void testTestedEndInsidePeriod() {
        Date testedBeginning = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 11:00")
        Date testedEnd = Date.parse("yyyy/MM/dd HH:mm", "2016/05/31 12:30")
        assert period.isDuring(testedBeginning, testedEnd)
    }


/*
    void testSavePeriod() {

        Calendar calendar = new GregorianCalendar(2016, Calendar.MAY, 28)
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        //def gnInstance = [new Gn()]
        //MockDomain(Gn, gnInstance)

        def periodInstances = [new Period(name: "p1", description: "Testing period 1", location: "",
        isPublic: true, isBlocking: true, beginning: calendar.getTime(), duration: 60)]
        MockDomain(Period, periodInstances)
        assertEquals(Period.list().size(), 1)
        /*
        Period p1 = new Period()
        p1.setName("p1")
        p1.setDescription("Testing period 1")
        p1.setLocation("")
        p1.setIsPublic(true)
        p1.setBeginning(calendar.getTime())
        p1.setIsBlocking(true)
        p1.setDuration(60)
        Gn g1 = new Gn()
        g1.setId(1)
        p1.setGn(g1)
        Period p1 = createPeriod1()
        p1.save(failOnError: true)
        * /
        mockDomain(Period)
        assertEquals Period.list().size(), 0

        assertEquals Product.list().size(), 1

        def p1 = new Period(name: "Test").save()
        assert p1.name.equals("Test")

    }
*/
}
