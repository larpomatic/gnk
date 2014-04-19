package org.gnk.resplacetime
import org.gnk.tag.Univers

class GenericRessourceHasIngameClue {

    Integer id
    Integer version

    Date lastUpdated
	Date dateCreated
	String adaptedText

	static belongsTo = [ genericTextualClue: GenericTextualClue, univers: Univers ]

    static mapping = {
        adaptedText type: 'text'
        id type:'integer'
        version type: 'integer'
    }

}
