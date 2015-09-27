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

    static belongsTo = [plot: Plot, objectType: ObjectType, gnConstant: GnConstant]

    static hasMany = [ events: Event,
	                   extTags: GenericPlaceHasTag,
	                   pastscenes: Pastscene ]

	static constraints = {
		code (blank: false, maxSize: 45, unique: false)
		comment (nullable: true)
	}

    static mapping = {
        comment type: 'text'
        id type:'integer'
        version type: 'integer'
        extTags cascade:'all-delete-orphan'
    }

    public boolean hasGenericPlaceTag(Tag parGenericPlaceTag) {
        return (GenericPlaceHasTag.findByTagAndGenericPlace(parGenericPlaceTag, this) != null)
    }

    public getGenericPlaceHasTag(Tag tag) {
        return GenericPlaceHasTag.findByTagAndGenericPlace(tag, this);
    }

//    boolean isIngameClue()
//    {
//        return ((this.code != null) || (this.comment != null)) && ((!this.code.isEmpty()) || (!this.comment.isEmpty()))
//    }
}
