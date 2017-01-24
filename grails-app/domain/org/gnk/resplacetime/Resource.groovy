package org.gnk.resplacetime

import org.gnk.ressplacetime.ReferentialObject
import org.gnk.tag.Tag

class Resource extends ReferentialObject {


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


//    static belongsTo = [ genericResource: GenericResource] // Dump20131114-version 2.1

	static hasMany = [ extTags: ResourceHasTag]

	static constraints = {
		name (maxSize: 45)
		description (nullable: true)
		gender (maxSize: 2)
	}

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }

	ArrayList<Tag> getTags() {
		ArrayList<Tag> tagsList = new ArrayList<>();

		for (ResourceHasTag resourceHasTag in this.extTags)
			tags.add(resourceHasTag.tag)

		return tagsList;
	}

	Map<Tag, Integer> getTagsAndWeights(Float ponderation) {
		Map<Tag, Integer> mapTagInt = new HashMap<>();

		for (ResourceHasTag resourceHasTag in this.extTags)
			mapTagInt.put(resourceHasTag.tag, new Integer((int)(resourceHasTag.weight * ponderation)))

		return mapTagInt;
	}

	String getSubType() {
		return "Resource";
	}

	List<ReferentialObject> getAll() {
		Resource.findAll()
	}

}
