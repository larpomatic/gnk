package org.gnk.admin

class SqlRequestSubcategory {
    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated

    String name

    static belongsTo = [ sqlRequestCategory: SqlRequestCategory ]
    static hasMany = [ sqlRequest : SqlRequest]
    static constraints = {
    }
}
