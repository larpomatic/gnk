package org.gnk.resplacetime
import org.gnk.roletoperso.Role

class GenericTextualClue {

    Integer id
    Integer version

    Date lastUpdated
	Date dateCreated
	String code
	// String gender // Dump20131114-version 2.1
	String text
	String title

    // Id referenced into DTD
    static transients = ["DTDId"]
    Integer DTDId;

//    static hasMany = [ ]

//    static belongsTo = [ possessedByRole: Role, fromRole: Role, toRole: Role, textualClueTag: TextualClueTag ] // Dump20131114-version 2.1
    static belongsTo = [ possessedByRole: Role, fromRole: Role, toRole: Role ]

	static constraints = {
		code (maxSize: 45)
		title (maxSize: 45)
	}

    static mapping = {
        id type:'integer'
        version type: 'integer'
        text type: 'text'
    }
}
