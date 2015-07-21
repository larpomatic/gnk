package org.gnk.genericevent

import org.gnk.tag.Tag


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
//        title{nullable:false}
        description(nullable:false)
    }
    static mapping = {
        description type:'text'
    }
    static hasMany = [ genericEventHasTag : GenericEventHasTag,
                       genericEventCanImplyGenericEvent : GenericEventCanImplyGenericEvent,
                       genericEventCanImplyTag : GenericEventCanImplyTag]


    public boolean hasTag(Tag tag) {
        def find = this.genericEventHasTag.find { it.tag.id == tag.id }

        return (find != null)
    }

    public boolean implyTag(Tag tag) {
        def find = this.genericEventCanImplyTag.find { it.tag.id == tag.id }
        return (find != null)
    }

    public boolean implyGenericEvent(GenericEvent ge) {
        def find = this.findById(ge.id)
        return (find != null)
    }

    public GenericEventHasTag hasTagValue(Tag tag) {
        def find = this.genericEventHasTag.find { it.tag.id == tag.id }

        return find
    }

    public GenericEventCanImplyTag implyTagValue(Tag tag) {
        def find = this.genericEventCanImplyTag.find { it.tag.id == tag.id }
        return find
    }
}
