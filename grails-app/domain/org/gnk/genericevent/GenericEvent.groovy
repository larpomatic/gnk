package org.gnk.genericevent


class GenericEvent {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated
    String description
    String title
    Integer ageMin
    Integer ageMax


    static constraints = {
        title{nullable:false}
        description(nullable:false)
    }
    static mapping = {
        title type:'text'
        description type:'text'
    }
    static hasMany = [ genericEventHasTag : GenericEventHasTag ]
}
