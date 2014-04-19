package org.gnk.user

import org.gnk.gn.GnHasUser
import org.gnk.selectintrigue.Plot

class User {

	Integer id
	Integer version

	def springSecurityService
	static transients = ["springSecurityService"]

	String username
	String password
	
	boolean enabled
	
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    def rights = [0,0,0]

	Date lastUpdated
	Date dateCreated
    def lastConnection

	String firstname
	String lastname

	static hasMany = [ gnHasUsers: GnHasUser, plots: Plot ]

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
		id type:'integer'
		version type: 'integer'
	}

	Set<SecRole> getAuthorities() {
		print UserSecRole.findAllByUser(this)
		UserSecRole.findAllByUser(this).collect { it.secRole } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
