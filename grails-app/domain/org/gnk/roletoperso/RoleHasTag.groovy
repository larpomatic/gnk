package org.gnk.roletoperso

import org.gnk.tag.Tag
import org.gnk.tag.TagService

class RoleHasTag {

    Integer id
    Integer version

    Date dateCreated
    Date lastUpdated

    Integer weight

    public RoleHasTag(Role rolePar, Tag tagPar, Integer weightPar) {
        role = rolePar
        tag = tagPar
        weight = weightPar
        version = 1
        Date now = new Date()
        dateCreated = now
        lastUpdated = now
    }

    static belongsTo = [ role: Role, tag: Tag ]

    static constraints = {
    }

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

    public Integer getterWeight() {
        return weight
    }

    public Tag getterTag() {
        return tag;
    }

    public boolean tagIsLocked() {
        return weight == TagService.LOCKED;
    }

    public boolean tagIsBanned() {
        return weight == TagService.BANNED;
    }
}
