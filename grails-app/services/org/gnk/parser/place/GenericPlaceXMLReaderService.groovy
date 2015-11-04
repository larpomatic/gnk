package org.gnk.parser.place

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.tag.TagXMLReaderService
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GnConstant
import org.gnk.resplacetime.ObjectType
import org.gnk.selectintrigue.Plot

class GenericPlaceXMLReaderService {

    def serviceMethod() {
    }

    /* Exposed Methods */
    def GenericPlace getGenericPlaceFromNode(Node GENERICPLACE, GNKDataContainerService dataContainer) {
        GenericPlace genericPlaceRes = null

        // GENERICPLACE reader
        genericPlaceRes = ReadGenericPlaceRootNode(GENERICPLACE, dataContainer)
        // COMMENT reader
        ReadCommentNode(GENERICPLACE, genericPlaceRes)

        // TAGS reader
        ReadTagsNode(GENERICPLACE, genericPlaceRes, dataContainer)

        ReadGnConstantNode(GENERICPLACE, genericPlaceRes)

        return genericPlaceRes
    }

/* !Exposed Methods */

    /* Construction Methods */
    private GenericPlace ReadGenericPlaceRootNode(Node GENERICPLACE, GNKDataContainerService dataContainer) {
        String code = null
        GenericPlace genericPlaceRes = null

        if (GENERICPLACE.attribute("code") != "null"){
            code = GENERICPLACE.attribute("code")
            //genericPlaceRes = GenericPlace.findByCode(code)
        }

        if (genericPlaceRes == null)
            genericPlaceRes = new GenericPlace(code: code)

        if (GENERICPLACE.attribute("id") != "null")
            genericPlaceRes.DTDId = GENERICPLACE.attribute("id") as Integer

        if (GENERICPLACE.attribute("object_type_id") != "null")
            genericPlaceRes.objectType = new ObjectType(GENERICPLACE.attribute("object_type_id") as Integer)

        return genericPlaceRes
    }

    private void ReadTagsNode(Node GENERICPLACE, GenericPlace genericPlaceRes, GNKDataContainerService dataContainer) {
        assert (GENERICPLACE.TAGS.size() <= 1)
        if (GENERICPLACE.TAGS.size() <= 0)
            return
        Node TAGS = GENERICPLACE.TAGS[0]

        NodeList TAGLIST = TAGS.TAG

        TagXMLReaderService tagReader = new TagXMLReaderService()
        for (int i = 0; i < TAGLIST.size(); i++)
        {
            Node TAG = TAGLIST.get(i)

            GenericPlaceHasTag genericPlaceHasTag = new GenericPlaceHasTag(genericPlace: genericPlaceRes, tag:tagReader.getTagFromNode(TAG, dataContainer), weight: tagReader.getTagWeight(TAG))
            genericPlaceRes.addToExtTags(genericPlaceHasTag)
        }
    }

    private void ReadCommentNode(Node GENERICPLACE, GenericPlace genericPlaceRes) {
        assert (GENERICPLACE.COMMENT.size() <= 1)
        if (GENERICPLACE.COMMENT.size() > 0) {
            genericPlaceRes.comment =  GENERICPLACE.COMMENT[0].text()
        }
    }

    private void ReadGnConstantNode(Node GENERICPLACE, GenericPlace genericPlaceRes) {
        assert (GENERICPLACE.GNCONSTANT.size() <= 1)
        if (GENERICPLACE.GNCONSTANT.size() <= 0) {
            return
        } else {

            if (GENERICPLACE.GNCONSTANT[0].attribute("id") != null) {
                genericPlaceRes.gnConstant = GnConstant.findById(GENERICPLACE.GNCONSTANT[0].attribute("id"))
            }
        }
    }
    /* !Construction Methods */
}
