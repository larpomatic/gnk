package org.gnk.utils

import org.gnk.roletoperso.RoleHasTag
import org.gnk.tag.Tag
import org.gnk.tag.TagFamily
import org.gnk.tag.TagRelevant

/**
 * Created by pico on 26/06/2014.
 */
class Base {

//    def transfertTagToNewTag()
//    {
//            for (Tag t : Tag.findAll())
//            {
//                Tag nt = new Tag()
//                Tag newtag = new Tag()
//                int id = t.id
//                newtag.setId(id)
//                newtag.setName(t.name)
//                newtag.setDateCreated(t.getDateCreated())
//                newtag.setLastUpdated(t.getLastUpdated())
//                newtag.setVersion(t.getVersion())
//                RoleHasTag.findAllWhere(tag: t)
//
//                newtag.save()
//            }
//        }
//
//    def transfertTagFamilyToTag()
//    {
//        HashMap<TagFamily, Tag> tab = new HashMap<TagFamily, Tag>()
//        List<TagFamily> l = TagFamily.findAll()
//
//        for(TagFamily i: l) {
//            Tag nt = new Tag()
//            nt.setName(i.getValue())
//            nt.setVersion(i.getVersion())
//            nt.setDateCreated(i.getDateCreated())
////            nt.parent(null)
//            nt.setLastUpdated(i.getLastUpdated())
//            nt.save()
//
//            for (Tag t: i.tags)
//            {
//                Tag children = Tag.findWhere(id: t.id)
//                children.setParent(nt)
//                children.save()
//            }
//
//            tab.put(i, nt)
//
//            TagRelevant tr = new TagRelevant()
//            tr.setTag(nt)
//            tr.setRelevantFirstname(i.getRelevantFirstname())
//            tr.setRelevantLastname(i.getRelevantLastname())
//            tr.setRelevantPlace(i.getRelevantPlace())
//            tr.setRelevantPlot(i.getRelevantPlot())
//            tr.setRelevantResource(i.getRelevantResource())
//            tr.setRelevantRole(i.getRelevantRole())
//
//            tr.save()
//        }
//
//        for (Map.Entry<TagFamily, Tag> i: tab.entrySet())
//        {
//            i.getValue().setParent(tab.get(i.getKey().getTagFamilyParent()))
//            i.getValue().save()
//        }
//    }
}
