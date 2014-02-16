package org.gnk.roletoperso

class RoleRelationType {

    Integer id
    Integer version
	Date lastUpdated
	Date dateCreated
	String name
    Boolean sexDiffers

	static hasMany = [roleHasRelationWithRoles: RoleHasRelationWithRole]

	static constraints = {
		name maxSize: 45
	}

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

    boolean getterSexDiffers() {
        return sexDiffers == null ? false : sexDiffers;
    }

    String getterName() {
        return name;
    }
}
