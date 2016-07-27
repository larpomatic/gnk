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
    public PlaceHasTag(Place PlacePar, Tag tagPar, Integer weightPar) {
        place = PlacePar
        tag = tagPar
        weight = weightPar
        version = 1
        Date now = new Date()
        dateCreated = now
        lastUpdated = now
    }
    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}
