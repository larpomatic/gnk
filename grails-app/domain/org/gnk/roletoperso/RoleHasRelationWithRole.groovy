package org.gnk.roletoperso

class RoleHasRelationWithRole {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	Integer weight
	Boolean isBijective
    Boolean isExclusive
    String description

	static belongsTo = [role1: Role, role2:Role, roleRelationType: RoleRelationType]

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

    public RoleHasRelationWithRole(Role senderPar, Role receiverPar, Integer weightPar, Boolean isBijectivePar, RoleRelationType relationType) {
        role1 = senderPar
        role2 = receiverPar
        weight = weightPar
        isBijective = isBijectivePar
        roleRelationType = relationType
        version = 1
        Date now = new Date()

        lastUpdated = now
        dateCreated = now
    }

    public RoleHasRelationWithRole(Role senderPar, Role receiverPar, Integer weightPar, Boolean isBijectivePar, RoleRelationType relationType, Boolean isExclusivePar, String description) {
        this(senderPar, receiverPar, weightPar, isBijectivePar, relationType)

        this.isExclusive = isExclusivePar
        this.description = description
    }

    public RoleRelationType getterRoleRelationType() {
        return roleRelationType
    }

    public Integer getterWeight () {
        return weight;
    }

    public Role getterRole1() {
        return role1
    }

    public Role getterRole2() {
        return role2
    }

    public Role getterOtherRole(Role role) {
        if (role1 == role)
            return role2;
        return role1;
    }
}
