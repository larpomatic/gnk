package org.gnk.roletoperso

import org.gnk.tag.Tag
import org.gnk.tag.TagService

class RoleRelationTypeCompatibility {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	Boolean sameReceiver
	Integer weight

	static belongsTo = [ roleRelationType1: RoleRelationType, roleRelationType2: RoleRelationType ]

    static mapping = {
        id type: 'integer'
        version type: 'integer'
    }

    static public RoleRelationTypeCompatibility myFindWhere(RoleRelationType roleRelationType1Par, RoleRelationType roleRelationType2Par, boolean sameReceiverPar) {
        return RoleRelationTypeCompatibility.findWhere(roleRelationType1: roleRelationType1Par, roleRelationType2: roleRelationType2Par, sameReceiver: sameReceiverPar)
    }

    public Integer getterWeight () {
        return weight;
    }

    public boolean isBanned() {
        return weight == TagService.BANNED;
    }

}
