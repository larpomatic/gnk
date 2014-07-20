package org.gnk.genericevent

import org.gnk.tag.Tag

class GenericEvent {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated
    String description
    Integer averageAge


    static constraints = {
        description(nullable:false)
    }
    static mapping = {
        description type:'text'
    }
    static belongsTo = [tag : Tag]
}
