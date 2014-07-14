package org.gnk.parser.tag

import org.gnk.parser.GNKDataContainerService
import org.gnk.tag.Tag

class TagXMLReaderService {

    def Tag getTagFromNode(Node TAG, GNKDataContainerService dataContainer) {
        return getTagFromNode(TAG)
    }

    def Tag getTagFromNode(Node TAG) {
        String tagFamilyValue = null
        Tag tagFamily = null
        String tagName = null
        Tag tagRes = null

        if (TAG.attribute("type") != "null") {
            tagFamilyValue = TAG.attribute("type")
            tagFamily = Tag.findByName(tagFamilyValue)
        }

        if (tagFamily == null)
            tagFamily = new Tag(name: tagFamilyValue)

        if (TAG.attribute("value") != "null") {
            tagName = TAG.attribute("value")
            /*if (tagFamily != null)
                tagRes = Tag.findByNameAndTagFamily(tagName, tagFamily)
            else*/
                tagRes = Tag.findByName(tagName)
        }

        if (tagRes == null)
            tagRes = new Tag(name: tagName, tagFamily: tagFamily)

        return tagRes
    }

    def Integer getTagWeight(Node TAG, GNKDataContainerService dataContainer) {
        return getTagWeight(TAG)
    }

    def Integer getTagWeight(Node TAG) {
        Integer tagWeight = 0
        if (TAG.attribute("weight") != "null" && (TAG.attribute("weight") as String).isInteger())
            tagWeight = TAG.attribute("weight") as Integer
        return tagWeight
    }
}
