package org.gnk.admin

class SqlRequestCategory {
    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated

    String name
    static hasMany = [ sqlRequestSubcategory : SqlRequestSubcategory]
    static constraints = {
    }
}
