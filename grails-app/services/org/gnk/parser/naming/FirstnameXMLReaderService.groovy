package org.gnk.parser.naming

import org.gnk.naming.Firstname
import org.gnk.naming.FirstnameHasTag
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.tag.TagXMLReaderService

class FirstnameXMLReaderService {

    def serviceMethod() {
    }

    /* Exposed Methods */
    def Firstname getFirstnameFromNode(Node FIRSTNAME, GNKDataContainerService dataContainer) {
        // FIRSTNAME reader
        Firstname firstnameRes = ReadFirstnameRootNode(FIRSTNAME, dataContainer)
        // DESCRIPTION reader
        ReadDescriptionNode(FIRSTNAME, firstnameRes)

        // TAGS reader
        ReadTagsNode(FIRSTNAME, firstnameRes, dataContainer)

        return firstnameRes
    }
/* !Exposed Methods */

    /* Construction Methods */
    private Firstname ReadFirstnameRootNode (Node FIRSTNAME, GNKDataContainerService dataContainer) {
        Firstname firstnameRes = null
        String firstnameValue = null

        if (FIRSTNAME.attribute("value") != "null") {
            firstnameValue = FIRSTNAME.attribute("value") as String
            //firstnameRes = Firstname.findByName(firstnameValue)
        }
        if (firstnameRes == null)
            firstnameRes = new Firstname(name: firstnameValue)

        if (FIRSTNAME.attribute("id") != "null")
            firstnameRes.DTDId = FIRSTNAME.attribute("id") as Integer
        if (FIRSTNAME.attribute("gender") != "null")
            firstnameRes.gender = FIRSTNAME.attribute("gender") as String

        return firstnameRes
    }

    private void ReadTagsNode(Node FIRSTNAME, Firstname firstnameRes, GNKDataContainerService dataContainer) {
        assert (FIRSTNAME.TAGS.size() == 1)
        Node TAGS = FIRSTNAME.TAGS[0]
        NodeList TAGLIST = TAGS.TAG

        TagXMLReaderService tagReader = new TagXMLReaderService()
        for (int i = 0; i < TAGLIST.size(); i++)
        {
            Node TAG = TAGLIST.get(i)

            FirstnameHasTag firstnameHasTag = new FirstnameHasTag()
            firstnameHasTag.tag = tagReader.getTagFromNode(TAG, dataContainer)
            firstnameHasTag.firstname = firstnameRes
            firstnameHasTag.weight = tagReader.getTagWeight(TAG)

           firstnameRes.addToExtTags(firstnameHasTag)
        }
    }

    private void ReadDescriptionNode (Node FIRSTNAME, Firstname firstnameRes) {
        assert (FIRSTNAME.DESCRIPTION.size() <= 1)
        if (FIRSTNAME.DESCRIPTION.size() > 0) {
            firstnameRes.description =  FIRSTNAME.DESCRIPTION[0].value
        }
    }
    /* !Construction Methods */
}
