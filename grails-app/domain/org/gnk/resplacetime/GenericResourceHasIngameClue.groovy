package org.gnk.resplacetime

class GenericResourceHasIngameClue {

    Integer id
    Integer version

    Date lastUpdated
	Date dateCreated
    String title
	String description

	static belongsTo = [ genericResource: GenericResource ]

    static constraints = {
        title (maxSize: 75)
//        description (nullable: true)
    }

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }

}
