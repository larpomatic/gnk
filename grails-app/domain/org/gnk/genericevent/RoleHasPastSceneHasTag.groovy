package org.gnk.genericevent

import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.tag.Tag

class RoleHasPastSceneHasTag {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated

    Integer value
    static belongsTo = [tag : Tag, roleHasPastScene : RoleHasPastscene]
}