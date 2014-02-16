package org.gnk.tag

class TagRelation {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	Boolean isBijective
	Integer weight

	static belongsTo = [ tag1: Tag, tag2: Tag ]

    static mapping = {
        id type: 'integer'
        version type: 'integer'
    }

    static public TagRelation myFindWhere(Tag tag1Par, Tag tag2Par) {
        return TagRelation.findWhere(tag1: tag1Par, tag2: tag2Par)
    }

    public Integer getterWeight () {
        return weight;
    }

    public boolean isLocked() {
        return weight == 100;
    }

    public boolean isBanned() {
        return weight == -100;
    }

}
