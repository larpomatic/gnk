package org.gnk.user

class SecRole {

	Integer id
	Integer version

	String authority
	Date lastUpdated
	Date dateCreated
	
	static mapping = {
		cache true
		id type:'integer'
		version type: 'integer'
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
 