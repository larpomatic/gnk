package org.gnk.user
import org.apache.commons.lang.builder.HashCodeBuilder

class UserSecRole implements Serializable {

	Integer id

	User user

	SecRole secRole

	Date lastUpdated
	Date dateCreated

	boolean equals(other) {
		if (!(other instanceof UserSecRole)) {
			return false
		}

		other.user?.id == user?.id &&
			other.secRole?.id == secRole?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (secRole) builder.append(secRole.id)
		builder.toHashCode()
	}

	static UserSecRole get(long userId, long secRoleId) {
		find 'from UserSecRole where user.id=:userId and secRole.id=:secRoleId',
			[userId: userId, secRoleId: secRoleId]
	}

	static UserSecRole create(User user, SecRole secRole, boolean flush = false) {
		try {
			new UserSecRole(user: user, secRole: secRole).save(flush: flush, insert: true)
		}
		catch (Exception e){
			print "Error: " + e.toString() // TODO proper log
		}

	}

	static boolean remove(User user, SecRole secRole, boolean flush = false) {
		UserSecRole instance = findByUserAndSecRole(user, secRole)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(User user) {
		executeUpdate 'DELETE FROM UserSecRole WHERE user=:user', [user: user]
	}

	static void removeAll(SecRole secRole) {
		executeUpdate 'DELETE FROM UserSecRole WHERE secRole=:secRole', [secRole: secRole]
	}

	static mapping = {
		id composite: ['secRole', 'user']
		version false
	}
}
