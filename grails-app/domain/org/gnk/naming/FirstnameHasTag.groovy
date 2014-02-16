package org.gnk.naming

import org.gnk.tag.Tag

class FirstnameHasTag {

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated

    Integer weight

    static belongsTo = [ firstname: Firstname, tag: Tag ]

    static constraints = {
        id type:'integer'
        version type: 'integer'
    }

}
