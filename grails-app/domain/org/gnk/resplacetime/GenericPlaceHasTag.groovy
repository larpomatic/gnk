package org.gnk.resplacetime

import org.gnk.tag.Tag

class GenericPlaceHasTag {

    Integer id
    Integer version

    Date dateCreated
    Date lastUpdated

    Integer weight

    static belongsTo = [ tag: Tag, genericPlace: GenericPlace ]

    static constraints = {

    }

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}
