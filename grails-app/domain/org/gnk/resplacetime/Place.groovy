package org.gnk.resplacetime

import javafx.util.Pair
import org.gnk.tag.Tag

class Place extends ReferentialObject{



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
        return null;
    }

    Map<Tag, Integer> getTagsAndWeights() {
        return null;
    }

}
