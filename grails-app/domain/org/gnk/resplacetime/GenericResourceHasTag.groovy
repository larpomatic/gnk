package org.gnk.resplacetime

import org.gnk.tag.Tag

class GenericResourceHasTag {

    Integer id
    Integer version

    Date dateCreated
    Date lastUpdated

    Integer weight

    static belongsTo = [ tag: Tag, genericResource: GenericResource ]

    static transients = ["DTDId", "proposedPlaces", "bannedPlaces", "selectedPlace", "lockedPlace", "value", "family"]

    String value
    String family

    static constraints = {
    }

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

    GenericResourceHasTag(GenericResource genericResource, Tag tag, Integer weight) {
        this.genericResource = genericResource
        this.tag = tag
        this.weight = weight
    }
}
