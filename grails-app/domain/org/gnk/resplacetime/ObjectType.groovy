package org.gnk.resplacetime

class ObjectType {
    Integer id
    /*
        Id 1 = "en jeu"
        Id 2 = "simulé"
        Id 3 = "hors jeu"
     */
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

    ObjectType(int id)
    {
        this.id = id
        if (id == 1)
            this.type = "en jeu"
        else if (id == 2)
            this.type = "simulé"
        else if (id == 3)
            this.type = "hors jeu"
        else
            throw Exception("Bad Object Type ID value")
    }
}


