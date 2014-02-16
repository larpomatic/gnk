package org.gnk.parser.naming

import org.gnk.naming.Name
import org.gnk.naming.NameHasTag
import org.gnk.parser.tag.TagXMLReaderService

class NameXMLReaderService {

    def serviceMethod() {
    }

    /* Exposed Methods */
    def Name getNameFromNode(Node NAME) {
        // NAME reader
        Name nameRes = ReadNameRootNode(NAME)
        // DESCRIPTION reader
        ReadDescriptionNode(NAME, nameRes)

        // TAGS reader
        ReadTagsNode(NAME, nameRes)

        return nameRes
    }
/* !Exposed Methods */

    /* Construction Methods */
    private Name ReadNameRootNode (Node NAME) {
        Name nameRes = null
        String nameValue = null

        if (NAME.attribute("value") != "null") {
            nameValue = NAME.attribute("value") as String
            //nameRes = Name.findByName(nameValue)
        }
        if (nameRes == null)
            nameRes = new Name(name: nameValue)

        if (NAME.attribute("id") != "null")
            nameRes.DTDId = NAME.attribute("id") as Integer
        if (NAME.attribute("gender") != "null")
            nameRes.gender = NAME.attribute("gender") as String

        return nameRes
    }

    private void ReadTagsNode(Node NAME, Name nameRes) {
        assert (NAME.TAGS.size() == 1)
        Node TAGS = NAME.TAGS[0]
        NodeList TAGLIST = TAGS.TAG

        TagXMLReaderService tagReader = new TagXMLReaderService()
        for (int i = 0; i < TAGLIST.size(); i++)
        {
            Node TAG = TAGLIST.get(i)

            NameHasTag nameHasTag = new NameHasTag()
            nameHasTag.name = nameRes
            nameHasTag.tag = tagReader.getTagFromNode(TAG)
            nameHasTag.weight = tagReader.getTagWeight(TAG)
            nameRes.addToExtTags(nameHasTag)
        }
    }

    private void ReadDescriptionNode (Node NAME, Name nameRes) {
        assert (NAME.DESCRIPTION.size() <= 1)
        if (NAME.DESCRIPTION.size() > 0) {
            nameRes.description =  NAME.DESCRIPTION[0].value
        }
    }
    /* !Construction Methods */
}
