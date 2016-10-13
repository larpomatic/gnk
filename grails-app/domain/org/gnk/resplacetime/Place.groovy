package org.gnk.resplacetime

import org.gnk.ressplacetime.ReferentialObject
import org.gnk.tag.Tag

class Place extends ReferentialObject{

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    String name
    String gender
    String description
    static def genders = ["M", "F", "MP", "FP"]

    // Id referenced into DTD
    static transients = ["DTDId"]
    Integer DTDId;

    static belongsTo = [genericPlace: GenericPlace]

	static hasMany = [ extTags: PlaceHasTag]

	static constraints = {
		name maxSize: 45
		gender maxSize: 2
		description nullable: true
	}

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }

    ArrayList<Tag> getTags() {
        ArrayList<Tag> tagsList = new ArrayList<>();

        for (PlaceHasTag placeHasTag in this.extTags)
            tags.add(placeHasTag.tag)

        return tagsList;
    }

    Map<Tag, Integer> getTagsAndWeights() {
        Map<Tag, Integer> mapTagInt = new HashMap<>();

        for (PlaceHasTag placeHasTag in this.extTags)
            mapTagInt.put(placeHasTag.tag, placeHasTag.weight)

        return mapTagInt;
    }

}
