package org.gnk.naming

import org.gnk.tag.Tag

class Name {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String name
	String gender

    // Id referenced into DTD
    static transients = ["DTDId"]
    Integer DTDId;

    static hasMany = [ extTags: NameHasTag ]

	static constraints = {
		name maxSize: 45
		gender maxSize: 2
	}

    static mapping = {
        id type:'integer'
        version type: 'integer'
        extTags cascade: "all-delete-orphan"
    }

    public getNameHasTag(Tag tag) {
        return NameHasTag.findByTagAndName(tag, this);
    }

    public getNameHasTag() {
        return NameHasTag.findAllByName(this).sort { -it.weight };
    }
    public NameHasTag hasTagValue(Tag tag) {
        def find = this.extTags.find { it.tag.id == tag.id }

        return find
    }
    public boolean hasTag(Tag tag) {
        def find = this.extTags.find { it.tag.id == tag.id }

        return (find != null)
    }

}
