package org.gnk.resplacetime

import org.gnk.roletoperso.RoleHasEvent
import org.gnk.selectintrigue.Plot

class Event {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String name
	/**
	 * Position of the event during GN duration in %
	 */
	Integer timing
	Integer duration
	Boolean isPublic
	Boolean isPlanned
	String description
	Event eventPredecessor
	String plotId
	String plotName

    // Id referenced into DTD
    static transients = ["DTDId", "absoluteYear", "absoluteMonth", "absoluteDay", "absoluteHour", "absoluteMinute", "plotId", "plotName"]

    Integer DTDId

    Integer absoluteYear
    Integer absoluteMonth
    Integer absoluteDay
    Integer absoluteHour
    Integer absoluteMinute

	static hasMany = [ roleHasEvents: RoleHasEvent ]

//BelongTo means cascading delete
	static belongsTo = [ genericPlace: GenericPlace, plot: Plot ]

	static constraints = {
		name (blank: false, maxSize: 256)
        eventPredecessor (nullable: true)
        genericPlace (nullable: true)
		timing ()//min: 0, max: 100)
		duration (min: 1)
		isPublic ()
		isPlanned ()
		description ()
	}

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
        roleHasEvents cascade: 'all-delete-orphan'
    }
}
