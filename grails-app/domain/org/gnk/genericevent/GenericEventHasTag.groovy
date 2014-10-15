package org.gnk.genericevent

import org.gnk.tag.Tag

class GenericEventHasTag {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated

    static belongsTo = [tag : Tag, genericEvent : GenericEvent]
}
