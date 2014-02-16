package org.gnk.naming

class Name {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String name
	String gender

    // Id referenced into DTD
    static transients = ["DTDId"]
    Integer DTDId;

    static hasMany = [ extTags: NameHasTag ]

	static constraints = {
		name maxSize: 45
		gender maxSize: 2
	}

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
	
}
