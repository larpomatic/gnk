package org.gnk.resplacetime

class Resource {

    Integer id
    Integer version

    Date lastUpdated
	Date dateCreated
	String name
	String description
	String gender
	static def genders = ["M", "F", "MP", "FP"]

    // Id referenced into DTD
    static transients = ["DTDId"]
    Integer DTDId;

//    static belongsTo = [ genericResource: GenericResource] // Dump20131114-version 2.1

	static hasMany = [ extTags: ResourceHasTag,
	                   resourceHasUniverses: ResourceHasUnivers ]

	static constraints = {
		name (maxSize: 45)
		description (nullable: true)
		gender (maxSize: 2)
	}

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }
}
