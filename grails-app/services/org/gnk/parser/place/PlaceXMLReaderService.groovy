package org.gnk.parser.place

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.pastscene.PastSceneXMLReaderService
import org.gnk.parser.role.RoleXMLReaderService
import org.gnk.parser.tag.TagXMLReaderService
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceHasTag
import org.gnk.roletoperso.Role

class PlaceXMLReaderService {

    def serviceMethod() {
    }

    /* Exposed Methods */
    def Place getPlaceFromNode(Node PLACE, GNKDataContainerService dataContainer) {
        Place placeRes = new Place()

        // PLACE reader
        ReadPlaceRootNode(PLACE, placeRes, dataContainer)
        // DESCRIPTION reader
        ReadDescriptionNode(PLACE, placeRes)

        // TAGS reader
        ReadTagsNode(PLACE, placeRes, dataContainer)

        return placeRes
    }
/* !Exposed Methods */

    /* Construction Methods */
    private void ReadPlaceRootNode (Node PLACE, Place placeRes, GNKDataContainerService dataContainer) {
        if (PLACE.attribute("id") != "null")
            placeRes.DTDId = PLACE.attribute("id") as Integer
        if (PLACE.attribute("value") != "null")
            placeRes.name = PLACE.attribute("value") as String
        if (PLACE.attribute("gender") != "null")
            placeRes.gender = PLACE.attribute("gender") as String
    }

    private void ReadTagsNode(Node PLACE, Place placeRes, GNKDataContainerService dataContainer) {
        assert (PLACE.TAGS.size() <= 1)
        if (PLACE.TAGS.size() <= 0)
            return
        Node TAGS = PLACE.TAGS[0]
        NodeList TAGLIST = TAGS.TAG

        TagXMLReaderService tagReader = new TagXMLReaderService()
        for (int i = 0; i < TAGLIST.size(); i++)
        {
            Node TAG = TAGLIST.get(i)

            PlaceHasTag placeHasTag = new PlaceHasTag(place: placeRes, tag: tagReader.getTagFromNode(TAG, dataContainer), weight: tagReader.getTagWeight(TAG))
            placeRes.addToExtTags(placeHasTag)
        }
    }

    private void ReadDescriptionNode (Node PLACE, Place placeRes) {
        assert (PLACE.DESCRIPTION.size() <= 1)
        if (PLACE.DESCRIPTION.size() > 0) {
            placeRes.description =  PLACE.DESCRIPTION[0].text()
        }
    }
    /* !Construction Methods */
}
