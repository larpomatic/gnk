package org.gnk.naming

class Rule {

    Integer id
    Integer version

    String description

    static constraints = {
        description maxSize: 90
    }

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

    static hasMany = [ conventionHasRules: ConventionHasRule ]

    @Override
    public String toString() {
        return description;
    }
}
