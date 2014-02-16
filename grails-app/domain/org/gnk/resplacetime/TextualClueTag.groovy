package org.gnk.resplacetime
import org.gnk.tag.Tag

class TextualClueTag {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated

//	static hasMany = [ genericTextualClues: GenericTextualClue ] // Dump20131114-version 2.1

	static belongsTo = [ tag: Tag ]

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

}
