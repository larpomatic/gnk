package org.gnk.roletoperso

import org.gnk.resplacetime.GenericResource

class RoleHasEventHasGenericResource {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated

	Integer quantity

	static belongsTo = [ genericResource: GenericResource, roleHasEvent: RoleHasEvent ]

	static mapping = {
		version false
        id type:'integer'
	}
}
