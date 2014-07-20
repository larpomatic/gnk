package org.gnk.naming

class ConventionHasRule {

    Integer id
    Integer version

    static belongsTo = [ rule:Rule, convention: Convention ]

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

}
