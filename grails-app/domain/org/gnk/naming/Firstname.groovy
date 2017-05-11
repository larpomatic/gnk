package org.gnk.naming

import org.gnk.tag.Tag

class Firstname {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String name
	String gender

    // Id referenced into DTD
    static transients = ["DTDId"]
    Integer DTDId;

    static hasMany = [ extTags : FirstnameHasTag ]

	static constraints = {
		name maxSize: 45
		gender maxSize: 2
	}

    static mapping = {
        id type:'integer'
        version type: 'integer'
        extTags cascade: "all-delete-orphan"
    }


    @Override
    public java.lang.String toString() {
        return "Firstname{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", DTDId=" + DTDId +
                '}';
    }

    public getFirsnameHasTag(Tag tag) {
        return FirstnameHasTag.findByTagAndFirstname(tag, this);
    }

    public getFirstnameHasTag() {
        return FirstnameHasTag.findAllByFirstname(this).sort { -it.weight };
    }
    public FirstnameHasTag hasTagValue(Tag tag) {
        def find = this.extTags.find { it.tag.id == tag.id }

        return find
    }
    public boolean hasTag(Tag tag) {
        def find = this.extTags.find { it.tag.id == tag.id }

        return (find != null)
    }

}