package org.gnk.resplacetime

class ObjectType {
    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    String type

    static hasMany = [ genericPlaces: GenericPlace,
            genericResources: GenericResource]

    static constraints = {
        type (blank: false, maxSize: 45, unique: false)
    }

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}


