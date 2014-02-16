package org.gnk.resplacetime

import org.gnk.tag.Tag

class ResourceHasTag {

    def ResourceHasTag(Resource resource, Tag tag, Integer weight)
    {
        this.resource = resource
        this.tag = tag
        this.weight = weight
    }
    Integer id
    Integer version

    Date dateCreated
    Date lastUpdated

    Integer weight

    static belongsTo = [ tag: Tag, resource: Resource ]

    static constraints = {}

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}
