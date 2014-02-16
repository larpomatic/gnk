package org.gnk.roletoperso

import org.gnk.resplacetime.Event

class RoleHasEvent {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String title
	Boolean isAnnounced
	String description
	String comment
	String evenementialDescription

	static hasMany = [ roleHasEventHasGenericResources: RoleHasEventHasGenericResource ]

    static belongsTo = [ event: Event, role: Role ]

	static constraints = {
		title maxSize: 45
		comment nullable: true
	}

    static mapping = {
        description type: 'text'
        comment type: 'text'
        evenementialDescription type: 'text'
        id type:'integer'
        version type: 'integer'
    }
}
