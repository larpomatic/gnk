package org.gnk.naming

import org.gnk.tag.Tag

class NameHasTag {

    Integer id
    Integer version

    Date dateCreated
    Date lastUpdated

    Integer weight

    static belongsTo = [ name: Name, tag: Tag ]

    static constraints = {
    }

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}
