package org.gnk.resplacetime

import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag

class GenericResource {

    Integer id
    Integer version

    Date dateCreated
    Date lastUpdated

	String code
	String comment

    // Ingame Clue :
    String title
    String description

 static belongsTo = [plot: Plot]

    // Id referenced into DTD
    static transients = ["DTDId", "proposedResources", "bannedResources", "selectedResource"]

    Integer DTDId

    List<Resource> proposedResources
    List<Resource> bannedResources
    Resource selectedResource

	static hasMany = [ extTags: GenericResourceHasTag,
	                   roleHasEventHasRessources: RoleHasEventHasGenericResource]

    static constraints(def it = null) {
        title(maxSize: 75)
        code(blank: false, maxSize: 45, unique: true)
        comment(nullable: true)
        title(nullable: true)
        descritpion(nullable: true)
    }

    static mapping = {
        comment type: 'text'
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }

    public boolean hasGenericResourceTag(Tag parGenericResourceTag) {
        for (GenericResourceHasTag genericResourceHasPlaceTag : extTags) {
            if (genericResourceHasPlaceTag.tag == parGenericResourceTag) {
                return true;
            }
        }
        return false;
    }

    boolean isIngameClue()
    {
        return ((this.title != null) || (this.description != null)) && ((!this.title.isEmpty()) || (!this.description.isEmpty()))
    }
}


/*
GenericRessourceHasIngameClue.groovy :


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

    GenericResourceHasIngameClue(GenericResource genericResource, String title, String description) {
        this.genericResource = genericResource
        this.title = title
        this.description = description
    }
}

 */