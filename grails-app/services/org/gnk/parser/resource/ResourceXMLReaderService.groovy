package org.gnk.parser.resource

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.tag.TagXMLReaderService
import org.gnk.resplacetime.Resource
import org.gnk.resplacetime.ResourceHasTag

class ResourceXMLReaderService {

    def serviceMethod() {
    }

    /* Exposed Methods */
    def Resource getResourceFromNode(Node RESOURCE, GNKDataContainerService dataContainer) {
        Resource resourceRes = new Resource()

        // RESOURCE reader
        ReadResourceRootNode(RESOURCE, resourceRes, dataContainer)

        // DESCRIPTION reader
        ReadDescriptionNode(RESOURCE, resourceRes, dataContainer)

        // TAGS reader
        ReadTagsNode(RESOURCE, resourceRes, dataContainer)

        return resourceRes
    }
/* !Exposed Methods */

    /* Construction Methods */
    private void ReadResourceRootNode (Node RESOURCE, Resource resourceRes, GNKDataContainerService dataContainer) {
        if (RESOURCE.attribute("id") != "null")
            resourceRes.DTDId = RESOURCE.attribute("id") as Integer
        if (RESOURCE.attribute("value") != "null")
            resourceRes.name = RESOURCE.attribute("value") as String
        if (RESOURCE.attribute("gender") != "null")
            resourceRes.gender = RESOURCE.attribute("gender") as String
    }

    private void ReadTagsNode(Node RESOURCE, Resource resourceRes, GNKDataContainerService dataContainer) {
        if (RESOURCE.TAGS.size() == 1)
        {
            Node TAGS = RESOURCE.TAGS[0]
            NodeList TAGLIST = TAGS.TAG

            TagXMLReaderService tagReader = new TagXMLReaderService()
            for (int i = 0; i < TAGLIST.size(); i++)
            {
                Node TAG = TAGLIST.get(i)

                ResourceHasTag resourceHasTag = new ResourceHasTag(resourceRes, tagReader.getTagFromNode(TAG, dataContainer), tagReader.getTagWeight(TAG))
                resourceRes.addToExtTags(resourceHasTag)
            }
        }
    }

    private void ReadDescriptionNode (Node RESOURCE, Resource resourceRes, GNKDataContainerService dataContainer) {
        assert (RESOURCE.DESCRIPTION.size() <= 1)
        if (RESOURCE.DESCRIPTION.size() > 0) {
            resourceRes.description =  RESOURCE.DESCRIPTION[0].text()
        }
    }
    /* !Construction Methods */
}
