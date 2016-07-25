package org.gnk.naming


class Convention {

    Integer id
    Integer version

    String description

    static constraints = {
        description maxSize: 45
    }

    static hasMany = [conventionHasRules: ConventionHasRule]

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

    @Override
    public String toString() {
        return description;
    }
}
