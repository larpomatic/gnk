package org.gnk.resplacetime

import org.gnk.ressplacetime.ReferentialObject
import org.gnk.roletoperso.Character
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
    static transients = ["DTDId", "plotId", "code", "character", "tagList", "comment", "plot", "objectType", "plotName", "placeTags"]
    Integer DTDId;

    //Transient fields used in substitution

    //String id
    String plotId
    String code
    Character character
    List<Tag> tagList
    String comment
    // Plot name
    String plotName
    String plot
    String objectType

    List<Tag> placeTags;

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

    Map<Tag, Integer> getTagsAndWeights(Float ponderation) {
        Map<Tag, Integer> mapTagInt = new HashMap<>();

        for (PlaceHasTag placeHasTag in this.extTags)
            mapTagInt.put(placeHasTag.tag, new Integer((int)(placeHasTag.weight * ponderation)))

        return mapTagInt;
    }

    String getSubType() {
        return "Place";
    }

    List<ReferentialObject> getAll() {
        Place.findAll()
    }

}
