package org.gnk.resplacetime

import org.gnk.gn.Gn

/**
 * A period is defined from its beginning and its duration
 */
class Period {
    Integer id
    Integer version
    String title
    String description
    String location
    Boolean isPublic
    /**
     * The period will not allow other period or event to be planned within its duration in the Times Substitution algorithm
     */
    Boolean isBlocking

    Integer duration

    static transients = ["absoluteYear", "absoluteMonth", "absoluteDay", "absoluteHour", "absoluteMinute"]

    Integer absoluteYear
    Integer absoluteMonth
    Integer absoluteDay
    Integer absoluteHour
    Integer absoluteMinute

    static belongsTo = [ periodPredecessor: Period ]
                         //, gn: Gn]

    static constraints = {
    }
    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }

    /**
     *
     * This function return a date created from the integers Absolute times attributes.
     *
     * Those integer values are a legacy carried because of the XML dtd translation
     *
     * This function won't work with null in absoluteX (Time) parameters but will handle negative values
     *
     * @return date
     */
    Date toDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(absoluteYear, absoluteMonth, absoluteDay, absoluteHour, absoluteMinute);
        return calendar.getTime();
    }
}
