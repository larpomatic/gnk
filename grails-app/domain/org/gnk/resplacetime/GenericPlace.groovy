package org.gnk.resplacetime

import org.gnk.selectintrigue.Plot

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
    }

}
