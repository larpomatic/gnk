package org.gnk.genericevent

import org.gnk.tag.Tag

class GenericEventCanImplyTag {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated
    Integer value
    static belongsTo = [tag : Tag, genericEvent : GenericEvent]
}
