package org.gnk.resplacetime

class GnConstant {

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    String name
    String constantType

    //a Gn constant can either be a place or a resource
    static constantTypes = [PLACE: "place", RESOURCE: "resource"]


    static constraints = {
        name maxSize: 45
        constantType (nullable: true)
    }

    static mapping = {
        name type: 'text'
        id type:'integer'
        constantType type: 'text'
    }
}
