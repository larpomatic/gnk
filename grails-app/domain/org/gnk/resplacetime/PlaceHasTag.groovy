package org.gnk.resplacetime

import org.gnk.tag.Tag

class PlaceHasTag {

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated

    Integer weight

    static belongsTo = [ place: Place, tag: Tag ]

    static constraints = {

    }

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}
