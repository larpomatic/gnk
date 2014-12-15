package org.gnk.tag

import grails.orm.PagedResultList


class TagSearchService {

    public List<TagRelation> manageReturnSearchTagRealtion(int type, Tag tag1, Tag tag2, Integer max, Integer offset, String sort) {
        PagedResultList list;
        if (tag1 && tag2) {
            list = returnSearchDuoTag(type, tag1, tag2, max, offset, sort);
        } else {
            if (tag1) {
                list = returnSearchOneTag(type, tag1, max, offset, sort);
            } else {
                if (tag2) {
                    list = returnSearchOneTag(type, tag2, max, offset, sort);

                } else {
                    Tag falseTag = Tag.findById(0)
                    list = returnSearchOneTag(type, falseTag, max, offset, sort);
                }
            }

        }
        return list
    }

    public List<TagRelation> returnSearchOneTag(int type, Tag tag1, Integer max, Integer offset, String sort) {
        PagedResultList tagRelations;
        if (type == 1) {
            tagRelations = TagRelation.createCriteria().list(max: max, offset: offset) {

                or
                        {
                            like("tag1", tag1)
                            like("tag2", tag1)
                        }
            }
        }
        if (type == 2) {
            tagRelations = TagRelation.createCriteria().list(max: max, offset: offset) {
                or
                        {
                            and
                                    {
                                        like("tag1", tag1)
                                        like("isBijective", true)
                                    }
                            and
                                    {
                                        like("tag2", tag1)
                                        like("isBijective", true)
                                    }
                        }
            }
        }
        if (type == 3) {
            tagRelations = TagRelation.createCriteria().list(max: max, offset: offset) {
                or
                        {
                            and
                                    {
                                        like("tag1", tag1)
                                        like("isBijective", false)
                                    }
                            and
                                    {
                                        like("tag2", tag1)
                                        like("isBijective", false)
                                    }
                        }
            }
        }

        return tagRelations
    }


    public List<TagRelation> returnSearchDuoTag(int type, Tag tag1, Tag tag2, Integer max, Integer offset, String sort) {
        PagedResultList tagRelations;
        if (type == 1) {
            tagRelations = TagRelation.createCriteria().list(max: max, offset: offset) {
                or {
                    and
                            {
                                like("tag1", tag2)
                                like("tag2", tag1)
                            }

                    and
                            {
                                like("tag1", tag1)
                                like("tag2", tag2)
                            }
                }
            }
        }
        if (type == 2) {
            tagRelations = TagRelation.createCriteria().list(max: max, offset: offset) {
                or {
                    and
                            {
                                like("tag1", tag2)
                                like("tag2", tag1)
                            }

                    and
                            {
                                like("tag1", tag1)
                                like("tag2", tag2)
                            }
                }
            }
        }
        if (type == 3) {
            tagRelations = TagRelation.createCriteria().list(max: max, offset: offset) {
                or {
                    and
                            {
                                like("tag1", tag2)
                                like("tag2", tag1)
                            }

                    and
                            {
                                like("tag1", tag1)
                                like("tag2", tag2)
                            }
                }
            }
        }
        return tagRelations
    }

    public List<Tag> searchTag(String name) {
        List<Tag> tags = null;
        if (name.indexOf("%") != -1)
            tags = Tag.createCriteria().list {
                like("name", name)
            }
        return tags;
    }

    public List<TagRelation> searchTagandListTag(int type, List<Tag> tags, Tag t, Integer max, Integer offset, String sort) {
        PagedResultList tr = null;

        if (type == 1) {
            for (Tag tag : tags) {
                List<TagRelation> tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                    or
                            {
                                and
                                        {
                                            like("tag1", t)
                                            like("tag2", tag)
                                        }
                                and
                                        {
                                            like("tag1", tag)
                                            like("tag2", t)
                                        }
                            }

                }
                if (tmp2) {
                    if (tr) {
                        tr.add(tmp2.get(0))
                    } else {
                        tr = tmp2
                    }
                }
            }
        }
        if (type == 2) {
            for (Tag tag : tags) {
                List<TagRelation> tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                    or
                            {
                                and
                                        {
                                            like("tag1", tag)
                                            like("tag2", t)
                                            like("isBijective", true)
                                        }
                                and
                                        {
                                            like("tag1", t)
                                            like("tag2", tag)
                                            like("isBijective", true)
                                        }
                            }
                }
                if (tmp2) {
                    if (tr) {
                        tr.add(tmp2.get(0))
                    } else {
                        tr = tmp2
                    }
                }
            }
        }
        if (type == 3) {
            for (Tag tag : tags) {
                List<TagRelation> tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                    or
                            {
                                and
                                        {
                                            like("tag1", tag)
                                            like("tag2", t)
                                            like("isBijective", false)
                                        }
                                and
                                        {
                                            like("tag1", t)
                                            like("tag2", tag)
                                            like("isBijective", false)
                                        }
                            }
                }
                if (tmp2) {
                    if (tr) {
                        tr.add(tmp2.get(0))
                    } else {
                        tr = tmp2
                    }
                }
            }
        }
        Set<TagRelation> s = new HashSet<TagRelation>();
        for (int i = 0; i < tr.size(); i++) {
            if (!s.add(tr.get(i))) {
                tr.remove(i)
            }
        }
        return tr;
    }

    public List<TagRelation> searchtwoListTag(int type, List<Tag> tags1, List<Tag> tags2, Integer max, Integer offset, String sort) {
        PagedResultList tr = null
        if (tags1.size() == 0 && tags2.size() == 0) {
            Tag falseTag = Tag.findById(0)
            tr = returnSearchOneTag(type, falseTag, max, offset, sort);
        } else {
            if (type == 1) {
                for (Tag tag1 : tags1) {
                    for (Tag tag2 : tags2) {
                        if (!tag1.name.equals(tag2.name)) {
                            PagedResultList tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                                and
                                        {
                                            like("tag1", tag1)
                                            like("tag2", tag2)
                                        }
                            }
                            try {
                                if (tmp2) {
                                    if (tr) {
                                        tr.add(tmp2.get(0))
                                    } else {
                                        tr = tmp2
                                    }
                                }
                            }
                            catch (NullPointerException e) {
                            }
                        }
                    }
                }
            }
            if (type == 2) {
                for (Tag tag1 : tags1) {
                    for (Tag tag2 : tags2) {
                        if (!tag1.name.equals(tag2.name)) {
                            PagedResultList tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                                and
                                        {
                                            like("tag1", tag1)
                                            like("tag2", tag2)
                                            like("isBijective", true)
                                        }
                            }
                            if (tmp2) {
                                if (tr) {
                                    tr.add(tmp2.get(0))
                                } else {
                                    tr = tmp2
                                }
                            }
                        }
                    }
                }
            }
            if (type == 3) {
                for (Tag tag1 : tags1) {
                    for (Tag tag2 : tags2) {
                        if (!tag1.name.equals(tag2.name)) {
                            PagedResultList tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                                and
                                        {
                                            like("tag1", tag1)
                                            like("tag2", tag2)
                                            like("isBijective", false)
                                        }
                            }
                            if (tmp2) {
                                if (tr) {
                                    tr.add(tmp2.get(0))
                                } else {

                                    tr = tmp2
                                }
                            }
                        }
                    }
                }
            }
            ArrayList<TagRelation> common = new ArrayList<TagRelation>()
            for (TagRelation tgr : tr) {
                Tag t1 = tgr.tag1
                Tag t2 = tgr.tag2
                TagRelation tmp = TagRelation.findByTag1AndTag2(t2, t1)
                if (tmp) {
                    common.add(tmp)
                }
            }

            for (TagRelation tgr : common) {
                tr.remove(tgr)
            }
        }
        return tr;
    }

    public List<TagRelation> searchoneListTag(int type, List<Tag> tags1, Integer max, Integer offset, String sort) {
        PagedResultList tr = null
        if (tags1.size() == 0) {
            Tag falseTag = Tag.findById(0)
            tr = returnSearchOneTag(type, falseTag, max, offset, sort);
        } else {
            if (type == 1) {
                for (Tag tag1 : tags1) {
                    PagedResultList tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                        or {
                            like("tag1", tag1)
                            like("tag2", tag1)
                        }

                    }
                    try {
                        if (tmp2) {
                            if (tr) {
                                tr.add(tmp2.get(0))
                            } else {
                                tr = tmp2
                            }
                        }
                    }
                    catch (Exception e) {

                    }

                }
            }
            if (type == 2) {

                for (Tag tag1 : tags1) {
                    PagedResultList tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                        or {
                            and
                                    {
                                        like("tag1", tag1)
                                        like("isBijective", true)
                                    }
                            and
                                    {
                                        like("tag2", tag1)
                                        like("isBijective", true)
                                    }
                        }
                    }
                    if (tmp2) {
                        if (tr) {
                            tr.add(tmp2.get(0))
                        } else {
                            tr = tmp2
                        }
                    }

                }
            }
            if (type == 3) {
                for (Tag tag1 : tags1) {

                    PagedResultList tmp2 = TagRelation.createCriteria().list(max: max, offset: offset) {
                        or {
                            and
                                    {
                                        like("tag1", tag1)
                                        like("isBijective", false)
                                    }
                            and
                                    {
                                        like("tag2", tag1)
                                        like("isBijective", false)
                                    }
                        }
                    }
                    if (tmp2) {
                        if (tr) {
                            tr.add(tmp2.get(0))
                        } else {

                            tr = tmp2
                        }
                    }
                }
            }
            ArrayList<TagRelation> common = new ArrayList<TagRelation>()
            Set<TagRelation> s = new HashSet<TagRelation>()
            for (TagRelation tgr : tr) {
                Tag t1 = tgr.tag1
                Tag t2 = tgr.tag2
                TagRelation tmp = TagRelation.findByTag1AndTag2(t2, t1)
                if (tmp) {
                    common.add(tmp)
                }
                boolean test = s.add(tgr)
                if (!test) {
                    common.add(tgr)
                }
            }

            for (TagRelation tgr : common) {
                tr.remove(tgr)
            }
        }
        return tr;
    }
}