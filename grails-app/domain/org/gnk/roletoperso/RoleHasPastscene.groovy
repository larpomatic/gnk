package org.gnk.roletoperso

import org.gnk.resplacetime.Pastscene

class RoleHasPastscene {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String title
	String description

	static belongsTo = [ pastscene: Pastscene, role: Role ]

	static constraints = {
		title maxSize: 45
	}

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }
}
