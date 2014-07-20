package org.gnk.genericevent

class GenericEventCanImplyGenericEvent {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated
    Integer value

    static belongsTo = [genericEventFirst : GenericEvent, genericEventNext : GenericEvent]
}
