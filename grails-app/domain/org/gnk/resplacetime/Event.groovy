package org.gnk.resplacetime

import org.gnk.roletoperso.RoleHasEvent
import org.gnk.selectintrigue.Plot

class Event {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String name
	Integer timing
	Integer duration
	Boolean isPublic
	Boolean isPlanned
	String description

    // Id referenced into DTD
    static transients = ["DTDId", "absoluteYear", "absoluteMonth", "absoluteDay", "absoluteHour", "absoluteMinute"]
    Integer DTDId

    Integer absoluteYear
    Integer absoluteMonth
    Integer absoluteDay
    Integer absoluteHour
    Integer absoluteMinute

	static hasMany = [ roleHasEvents: RoleHasEvent ]

	static belongsTo = [ eventPredecessor: Event, genericPlace: GenericPlace, plot: Plot ]

	static constraints = {
		name (blank: false, maxSize: 45)
		timing ()
		duration (min: 1)
		isPublic ()
		isPlanned ()
		description ()
	}

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }
}
