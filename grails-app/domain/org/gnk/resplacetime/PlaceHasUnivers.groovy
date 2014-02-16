package org.gnk.resplacetime

import org.gnk.tag.Univers

class PlaceHasUnivers {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	Integer weight

	static belongsTo = [ place: Place, univers: Univers ]

	static constraints = {
		weight nullable: true
	}

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}
