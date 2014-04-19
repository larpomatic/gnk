package org.gnk.resplacetime

class GenericRessourceHasIngameClue {

    Integer id
    Integer version

    Date lastUpdated
	Date dateCreated
    String title
	String descritpion

	static belongsTo = [ genericRessource: GenericResource ]

    static constraints = {
        title (maxSize: 75)
//        description (nullable: true)
    }

    static mapping = {
        descritpion type: 'text'
        id type:'integer'
        version type: 'integer'
    }

}
