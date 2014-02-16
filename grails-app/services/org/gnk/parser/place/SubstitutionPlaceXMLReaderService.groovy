package org.gnk.parser.place

import org.gnk.parser.GNKDataContainerService
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.Place

class SubstitutionPlaceXMLReaderService {

    /* Exposed Methods */

    def static getSubstitutedPlaceFromNode(Node PLACES, GNKDataContainerService dataContainer) {
        String selectedStatus = "selected"

        Place place;
        GenericPlace genericPlace;

        PLACES.each { genericPlaceNode ->
            Integer genericPlaceId = genericPlaceNode.attribute("generic_place_id") as Integer
            genericPlace = dataContainer.genericPlaceMap.get(genericPlaceId)

            if (dataContainer.genericPlaceMap.isEmpty()) {
                print "Warning : genericPlaceMap is empty"
                return
            }
            if (genericPlace == null)
                log.error("genericPlace is null (id : " + genericPlaceNode.attribute("generic_place_id") + ")")
            assert (genericPlace != null)
            if (genericPlace != null)
                genericPlaceNode.each { placeNode ->
                    String placeStatus = placeNode.attribute("status") as String

                    if (placeStatus.equals(selectedStatus)) {
                        Integer placeId = placeNode.attribute("place_id") as Integer
                        place = dataContainer.placeMap.get(placeId)

                        assert (place != null)
                        place.genericPlace = genericPlace
                    }
                }
        }
    }
}
