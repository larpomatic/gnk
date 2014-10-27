package org.gnk.resplacetime

class ObjectType {
    Integer id
    /*
        Id 0 = "À définir"
        Id 1 = "En jeu"
        Id 2 = "Simulé"
        Id 3 = "Hors jeu"
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
        if (id == 0)
            this.type = "À définir"
        else if (id == 1)
            this.type = "En jeu"
        else if (id == 2)
            this.type = "Simulé"
        else if (id == 3)
            this.type = "Hors jeu"
        else
            throw Exception("Bad Object Type ID value")
    }
}


