package org.gnk.resplacetime
import org.gnk.tag.Univers

class ResourceHasUnivers {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	Integer weight

	static belongsTo = [ resource: Resource, univers: Univers ]

	static mapping = {
        id type:'integer'
        version type: 'integer'
	}

	static constraints = {
		weight (nullable: true)
	}

}
