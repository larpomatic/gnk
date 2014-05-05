package org.gnk.parser.textualclue

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.tag.TagXMLReaderService

class GenericTextualClueXMLReaderService {

    def serviceMethod() {
    }

    /* Exposed Methods */
//    def GenericTextualClue getTextualClueFromNode(Node TEXTUAL_CLUE, GNKDataContainerService dataContainer) {
//        GenericTextualClue textualClueRes = new GenericTextualClue()
//
//        // TEXTUAL_CLUE reader
//        ReadTextualClueRootNode(TEXTUAL_CLUE, textualClueRes, dataContainer)
//
//        // TEXT reader
//        ReadTextNode(TEXTUAL_CLUE, textualClueRes, dataContainer)
//
//        // TAGS reader
//        ReadTagsNode(TEXTUAL_CLUE, textualClueRes, dataContainer)
//
//        return textualClueRes
//    }
/* !Exposed Methods */

    /* Construction Methods */
//    private void ReadTextualClueRootNode(Node TEXTUAL_CLUE, GenericTextualClue textualClueRes, GNKDataContainerService dataContainer) {
//        if (TEXTUAL_CLUE.attribute("id") != "null")
//            textualClueRes.DTDId = TEXTUAL_CLUE.attribute("id") as Integer
//        if (TEXTUAL_CLUE.attribute("code") != "null")
//            textualClueRes.code = TEXTUAL_CLUE.attribute("code") as String
//        if (TEXTUAL_CLUE.attribute("title") != "null")
//            textualClueRes.title = TEXTUAL_CLUE.attribute("title") as String
//        /*if (TEXTUAL_CLUE.attribute("gender") != "null")
//            textualClueRes.gender = TEXTUAL_CLUE.attribute("gender") as String*/
//        // FIXME: type ???
//        // FIXME: owner_role_id from_role_id to_role_id
//    }
//
//    private void ReadTagsNode(Node TEXTUAL_CLUE, GenericTextualClue textualClueRes, GNKDataContainerService dataContainer) {
//        assert (TEXTUAL_CLUE.TAGS.size() <= 1)
//        if (TEXTUAL_CLUE.TAGS.size() <= 0)
//            return
//        assert (TEXTUAL_CLUE.TAGS.TAG.size() <= 1)
//        if (TEXTUAL_CLUE.TAGS.TAG.size() <= 0)
//            return
//
//        Node TAG = TEXTUAL_CLUE.TAGS[0].TAG[0]
//
//        // FIXME: add many tags
//        // FIXME: weight not used
//        TagXMLReaderService tagReader = new TagXMLReaderService()
//        TextualClueTag tag = new TextualClueTag(tag: tagReader.getTagFromNode(TAG, dataContainer))
//        tag.addToGenericTextualClues(textualClueRes)
//    }
//
//    private void ReadTextNode (Node RESOURCE, GenericTextualClue textualClueRes, GNKDataContainerService dataContainer) {
//        assert (RESOURCE.TEXT.size() <= 1)
//        if (RESOURCE.TEXT.size() > 0) {
//            textualClueRes.text = RESOURCE.TEXT[0].value
//        }
//    }
    /* !Construction Methods */
}
