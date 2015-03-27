package org.gnk.admin

class SqlRequest {
    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated

    String name

    String sqlrequest
    static belongsTo = [ sqlRequestSubcategory : SqlRequestSubcategory]
    static constraints = {
        sqlrequest (nullable: true)
    }
    static mapping = {
        sqlrequest type: 'text'
    }
}