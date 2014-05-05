package org.gnk.parser.resource

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.tag.TagXMLReaderService
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag

class GenericResourceXMLReaderService {

    def serviceMethod() {
    }

    /* Exposed Methods */
    def GenericResource getGenericResourceFromNode(Node GENERIC_RESOURCE, GNKDataContainerService dataContainer) {
        GenericResource genericResourceRes = null

        // GENERIC_RESOURCE reader
        genericResourceRes = ReadGenericResourceRootNode(GENERIC_RESOURCE, dataContainer)

        // COMMENT reader
        ReadCommentNode(GENERIC_RESOURCE, genericResourceRes, dataContainer)

        // TAGS reader
        ReadTagsNode(GENERIC_RESOURCE, genericResourceRes, dataContainer)

        // TITLE (INGAME_CLUE) reader
        ReadTitleNode(GENERIC_RESOURCE, genericResourceRes, dataContainer)

        // DESCRIPTION (INGAME_CLUE) reader
        ReadDescriptionNode(GENERIC_RESOURCE, genericResourceRes, dataContainer)

        return genericResourceRes
    }
/* !Exposed Methods */

    /* Construction Methods */
    private GenericResource ReadGenericResourceRootNode(Node GENERIC_RESOURCE, GNKDataContainerService dataContainer) {
        String code = null
        GenericResource genericResourceRes = null

        if (GENERIC_RESOURCE.attribute("code") != "null") {
            code = GENERIC_RESOURCE.attribute("code") as String
            //genericResourceRes = GenericResource.findByCode(code)
        }

        if (genericResourceRes == null)
            genericResourceRes = new GenericResource(code: code)

        if (GENERIC_RESOURCE.attribute("id") != "null")
            genericResourceRes.DTDId = GENERIC_RESOURCE.attribute("id") as Integer

        // FIXME: @Boycotted: owner_role_id (already exist with role=>generic_resource)
        // if (GENERIC_RESOURCE.attribute("owner_role_id") != "null")
        //  genericResourceRes.DTDId = GENERIC_RESOURCE.attribute("id") as Integer

        return genericResourceRes
    }

    private void ReadTitleNode (Node RESOURCE, GenericResource resourceRes, GNKDataContainerService dataContainer) {
        assert (RESOURCE.TITLE.size() <= 1)
        if (RESOURCE.TITLE.size() > 0) {
            resourceRes.title=  RESOURCE.TITLE[0].text()
        }
    }

    private void ReadDescriptionNode (Node RESOURCE, GenericResource resourceRes, GNKDataContainerService dataContainer) {
        assert (RESOURCE.DESCRIPTION.size() <= 1)
        if (RESOURCE.DESCRIPTION.size() > 0) {
            resourceRes.description=  RESOURCE.DESCRIPTION[0].text()
        }
    }

    private void ReadTagsNode(Node GENERIC_RESOURCE, GenericResource genericResourceRes, GNKDataContainerService dataContainer) {
        assert (GENERIC_RESOURCE.TAGS.size() <= 1)
        Node TAGS = GENERIC_RESOURCE.TAGS[0]
        NodeList TAGLIST = TAGS.TAG

        TagXMLReaderService tagReader = new TagXMLReaderService()
        TAGLIST.each {TAG ->
            GenericResourceHasTag genericResourceHasTag = new GenericResourceHasTag(genericResourceRes, tagReader.getTagFromNode(TAG, dataContainer), tagReader.getTagWeight(TAG))
            genericResourceRes.addToExtTags(genericResourceHasTag)
        }
    }

    private void ReadCommentNode (Node RESOURCE, GenericResource resourceRes, GNKDataContainerService dataContainer) {
        assert (RESOURCE.COMMENT.size() <= 1)
        if (RESOURCE.COMMENT.size() > 0) {
            resourceRes.comment=  RESOURCE.COMMENT[0].text()
        }
    }
    /* !Construction Methods */
}
