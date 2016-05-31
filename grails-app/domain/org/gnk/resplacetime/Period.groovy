package org.gnk.resplacetime

import org.apache.commons.lang3.time.DateUtils
import org.gnk.gn.Gn

/**
 * A period is defined from its beginning and its duration
 */
class Period {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated

    String name
    String description
    String location
    /**
     * Whether the period is visible only for the orga or also for the players
     */
    Boolean isPublic
    /**
     * The start of the period
     */
    Date beginning
    /**
     * The period will not allow other period or event to be planned within its duration in the Times Substitution algorithm
     */
    Boolean isBlocking
    /**
     * The duration of the period in minutes
     */
    Boolean isGamePeriod

    Long duration
    /**
     * mapping composition N-1 to Gn
     */
    static belongsTo = [gn: Gn]

    Period periodPredecessor

    /**
     * The interval in minutes between the predecessor and the period
     */
    Long predInterval

    static transients = ["absoluteYear", "absoluteMonth", "absoluteDay", "absoluteHour", "absoluteMinute"]

    Integer absoluteYear
    Integer absoluteMonth
    Integer absoluteDay
    Integer absoluteHour
    Integer absoluteMinute


    static constraints = {
        duration(min: (long)1)
    }
    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'

//      periodPredecessor (nullable:true)
    }

    /**
     * This method check if it is possible to set the attribute isBlocking to true without conflict with the other
     * persisted period linked to the same GN
     * @return
     */
    Boolean checkBlocking() {
        if (this.gn == null || this.beginning == null || this.duration == null) {
            return false
        }

        def blockingPeriods = Period.where {gn == this.gn && isBlocking == true}.list(sort: beginning)

        Boolean allowed = true

        blockingPeriods.each { Period testedPeriod ->
            //Return is the closure equivalent for continue
            if(testedPeriod == null) { return }
            allowed = allowed && !this.isDuring(testedPeriod.getBeginning(), testedPeriod.getEnd())

            //A closure ending with true is equivalent as break statement in a loop
            this.getEnd().before(testedPeriod.getBeginning())
        }
        return allowed
    }

    /**
     * Return the timestamp corresponding to the End of the period
     * @return Date end
     */
    Date getEnd() {
        if (this.beginning == null || this.duration == null) {
            return null
        }
        return DateUtils.addMinutes(this.beginning, (Integer)this.duration)
    }


    /**
     * This method test if an interval of time is intersect with the period
     * @param testedBeginning
     * @param testedDuration
     * @return
     */
    Boolean isDuring(Date testedBeginning, Date testedEnd) {
        if (this.beginning == null || this.duration == null) {
            return false
        }
        if (testedBeginning == null || testedBeginning == null) {
            return false
        }

        Date end = this.getEnd()

        // tested interval : []
        // this period interval : {}

        // { [ }   ]
        Boolean beginsDuringPeriod = ((this.beginning.before(testedBeginning) || beginning.equals(testedBeginning)) &&
                end.after(testedBeginning))
        // [   { ] }
        Boolean endsDuringPeriod = (this.beginning.before(testedEnd) && (end.after(testedEnd) || end.equals(testedEnd)))
        // [  { }  ]
        Boolean periodInsideTested = ((testedBeginning.before(this.beginning) || testedBeginning.equals(this.beginning))
                && (testedEnd.after(end) || testedEnd.equals(end)))
        // {  [ ]  }
        Boolean testedInsidePeriod = ((this.beginning.before(testedBeginning) || this.beginning.equals(testedBeginning))
                && (end.after(testedEnd) || end.equals(testedEnd)))

        return (beginsDuringPeriod && endsDuringPeriod && periodInsideTested && testedInsidePeriod)
    }


    /**
     *
     * This function set the beginning of the period from the integers Absolute times attributes.
     * Those integer values are a legacy carried because of the XML dtd translation
     * This function won't work with null in absoluteX (Time) parameters but will handle negative values
     */
    private void setBeginingFromXML() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(absoluteYear, absoluteMonth, absoluteDay, absoluteHour, absoluteMinute);
        Date beginning = calendar.getTime();
    }

    //enrichir le contructeur pour les dates de début relatives
    //toJSON
    //fromJSON
}