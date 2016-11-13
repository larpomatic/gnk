package org.gnk.parser.place

import org.gnk.parser.GNKDataContainerService
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.Place

class SubstitutionPlaceXMLReaderService {

    /* Exposed Methods */

    def static getSubstitutedPlaceFromNode(Node PLACES, GNKDataContainerService dataContainer) {

        Place place;
        GenericPlace genericPlace;
        ArrayList<GenericPlace> processedGenericPlaces = new ArrayList<GenericPlace>();

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
            //if genericPlace is not null and if this genericPlace has not been processed yet
            if (genericPlace != null && !processedGenericPlaces.contains(genericPlace)) {
                genericPlaceNode.each { placeNode ->

                    String placeStatus = placeNode.attribute("status") as String
                    Integer placeId = placeNode.attribute("place_id") as Integer
                    place = dataContainer.placeMap.get(placeId)
                    assert (place != null)

                    if (placeStatus.equals("selected")) {
                        if (place.genericPlace != null) {
                            //if we need to associate another genericPlace to a place that already has one, we create a copy of this place on work on it
                            place = dataContainer.copyAndAddPlaceToPlaceMap(place);
                            place.genericPlace = genericPlace
                            genericPlace.selectedPlace = place
                        } else {
                            place.genericPlace = genericPlace
                            genericPlace.selectedPlace = place
                        }
                    } else if (placeStatus.equals("locked")) {
                        genericPlace.lockedPlace = place
                    } else if (placeStatus.equals("proposed")) {
                        if (genericPlace.proposedPlaces == null)
                            genericPlace.proposedPlaces = new ArrayList<Place>()
                        genericPlace.proposedPlaces.add(place)
                    } else if (placeStatus.equals("banned")) {
                        if (genericPlace.bannedPlaces == null)
                            genericPlace.bannedPlaces = new ArrayList<Place>()
                        genericPlace.bannedPlaces.add(place);
                    }

                }
                //we processed the genericPlace so we add it to the list
                processedGenericPlaces.add(genericPlace);

            }
        }
    }
}
