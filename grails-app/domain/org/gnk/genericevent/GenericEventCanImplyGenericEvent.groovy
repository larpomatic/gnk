package org.gnk.genericevent

class GenericEventCanImplyGenericEvent {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated
    Integer value

    static belongsTo = [genericEvent : GenericEvent, genericEventFirst : GenericEvent, genericEventNext : GenericEvent]
}
