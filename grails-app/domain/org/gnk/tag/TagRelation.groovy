package org.gnk.tag

import org.javatuples.Triplet


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

    static def findParents(Tag tag)
    {
        List<Tag> resultList = new ArrayList<Tag>()
        Tag t = tag

        // parents of tag added in the resultList
        while (t.parent != null)
        {
            resultList.add(t)
            t = t.parent
        }
    }

    static public TagRelation myFindWhere(Tag tag1Par, Tag tag2Par) {

//        if (TagRelation.findWhere(tag1: tag1Par, tag2: tag2Par) == null)
//        {
//            List<Tag> refChildren = new TagService().findChildren(tag2Par)
//            List<Tag> chalParents = findParents(tag1Par)
//            Triplet<Tag, Tag, Integer> relation = new Triplet<Tag, Tag, Integer>(null, null, null)
//
//            relation.add(null, null, 0)
//
//            for (int i = 0; i < refChildren.size(); i++)
//            {
//                for (int j = 0; j < chalParents.size(); j++)
//                {
//                    if (TagRelation.findWhere(tag1: refChildren.get(i), tag2: chalParents.get(j)) > relation.getValue2())
//                        relation.setAt0(refChildren.get(i))
//                    relation.setAt1(chalParents.get(j))
//                    relation.setAt2(Math.abs(TagRelation.findWhere(tag1: refChildren.get(i), tag2: chalParents.get(j))))
//                }
//            }
//
//            return TagRelation.findWhere(tag1: relation.value0, tag2: relation.value1)
//        }
//        else
            return TagRelation.findWhere(tag1: tag1Par, tag2: tag2Par)

    }

    public Integer getterWeight () {
        return weight;
    }

    public boolean isLocked() {
        return weight == TagService.LOCKED;
    }

    public boolean isBanned() {
        return weight == TagService.BANNED;
    }

}
