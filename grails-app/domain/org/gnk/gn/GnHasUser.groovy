package org.gnk.gn

import org.gnk.user.User

class GnHasUser {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated

	Boolean isCreator

	static belongsTo = [ gn: Gn, user: User ]

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

}
