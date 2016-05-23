package org.gnk.resplacetime

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
    Integer duration
    /**
     * mapping composition N-1 to Gn
     */
    static belongsTo = [gn: Gn]
    /**
     * Currently unused
     */
    Period periodPredecessor

    static transients = ["absoluteYear", "absoluteMonth", "absoluteDay", "absoluteHour", "absoluteMinute"]

    Integer absoluteYear
    Integer absoluteMonth
    Integer absoluteDay
    Integer absoluteHour
    Integer absoluteMinute


    static constraints = {
    }
    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
//        periodPredecessor (nullable:true)
    }

    /**
     *
     * This function set the beginning of the period from the integers Absolute times attributes.
     * Those integer values are a legacy carried because of the XML dtd translation
     * This function won't work with null in absoluteX (Time) parameters but will handle negative values
     *
     * @return date
     */
    private void setBeginingFromXML() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(absoluteYear, absoluteMonth, absoluteDay, absoluteHour, absoluteMinute);
        Date beginning = calendar.getTime();
    }

}
