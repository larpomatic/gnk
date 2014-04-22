package org.gnk.resplacetime

import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.selectintrigue.Plot

class GenericResource {

    Integer id
    Integer version

    Date dateCreated
    Date lastUpdated

	String code
	String comment
 static belongsTo = [plot: Plot]
    GenericRessourceHasIngameClue genericRessourceHasIngameClue;

    // Id referenced into DTD
    static transients = ["DTDId", "proposedResources", "bannedResources", "selectedResource"]

    Integer DTDId

    List<Resource> proposedResources
    List<Resource> bannedResources
    Resource selectedResource

	static hasMany = [ extTags: GenericResourceHasTag,
	                   roleHasEventHasRessources: RoleHasEventHasGenericResource]

	static constraints = {
		code (blank: false, maxSize: 45, unique: true)
		comment (nullable: true)
	}

    static mapping = {
        comment type: 'text'
        id type:'integer'
        version type: 'integer'
    }
}
