package org.gnk.utils

class Tag {

    def findChildren(org.gnk.tag.Tag t) {
        def tags = org.gnk.tag.Tag.findAllWhere(parent: t)
        def tagsTmp = new ArrayList()
        tagsTmp.addAll(tags)
        if (tagsTmp == null)
            return tags
        for (org.gnk.tag.Tag tag : tagsTmp) {
            tags.addAll(findChildren(tag))
        }
        return tags
    }
}
