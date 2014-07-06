package org.gnk.resplacetime

import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag

class GenericPlace {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String code
	String comment

    // Id referenced into DTD
    static transients = ["DTDId", "proposedPlaces", "bannedPlaces", "selectedPlace"]
    Integer DTDId;

    List<Place> proposedPlaces
    List<Place> bannedPlaces
    Place selectedPlace

    static belongsTo = [plot: Plot]

    static hasMany = [ events: Event,
	                   extTags: GenericPlaceHasTag,
	                   pastscenes: Pastscene ]

	static constraints = {
		code (blank: false, maxSize: 45, unique: true)
		comment (nullable: true)
	}

    static mapping = {
        comment type: 'text'
        id type:'integer'
        version type: 'integer'
        extTags cascade:'all-delete-orphan'
    }

    public boolean hasGenericPlaceTag(Tag parGenericPlaceTag) {
        for (GenericPlaceHasTag genericPlaceHasPlaceTag : extTags) {
            if (genericPlaceHasPlaceTag.tag == parGenericPlaceTag) {
                return true;
            }
        }
        return false;
    }

    public getGenericPlaceHasTag(Tag tag) {
        List<GenericPlaceHasTag> genericPlaceHasTags = GenericPlaceHasTag.createCriteria().list {
            like("genericPlace", this)
            like("tag", tag)
        }
        if (genericPlaceHasTags.size() == 0) {
            return null;
        }
        return genericPlaceHasTags.first();
    }
}
