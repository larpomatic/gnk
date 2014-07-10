package org.gnk.parser.place

import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceHasTag
import org.gnk.selectintrigue.Plot
import org.w3c.dom.CDATASection
import org.w3c.dom.Document
import org.w3c.dom.Element

class SubstitutionPlaceXMLWriterService {

    /* Exposed Methods */
    public Element getPlacesElement(Document doc, Set<Plot> selectedPlotSet) {
        Element placesE = doc.createElement("PLACES")
        placesE.setAttribute("step_id", "3")

        // Iterate generic places
        for(plot in selectedPlotSet) {
            for(pastScene in plot.pastescenes) {
                if (pastScene.genericPlace != null) {
                    placesE.appendChild(getGenericPlaceElement(doc, pastScene.genericPlace, plot))
                }
            }
            for(event in plot.events) {
                if (event.genericPlace != null) {
                    placesE.appendChild(getGenericPlaceElement(doc, event.genericPlace, plot))
                }
            }
        }

        return placesE
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Element getGenericPlaceElement(Document doc, GenericPlace genericPlace, Plot plot){
        Element genericPlaceE = doc.createElement("GENERIC_PLACE")

        if (!genericPlace.DTDId)
            genericPlace.DTDId = genericPlace.id
        genericPlaceE.setAttribute("generic_place_id", genericPlace.DTDId as String)
        genericPlaceE.setAttribute("plot_id", plot.DTDId as String)

        if (genericPlace.selectedPlace != null) {
            genericPlaceE.appendChild(getPlaceElement(doc, genericPlace.selectedPlace, "selected"))
        }

        for(place in genericPlace.proposedPlaces) {
            genericPlaceE.appendChild(getPlaceElement(doc, place, "proposed"))
        }

        for(place in genericPlace.bannedPlaces) {
            genericPlaceE.appendChild(getPlaceElement(doc, place, "banned"))
        }

        return genericPlaceE
    }

    private Element getPlaceElement(Document doc, Place place, String status){
        Element placeE = doc.createElement("PLACE")

        if (!place.DTDId)
            place.DTDId = place.id
        placeE.setAttribute("place_id", place.DTDId as String)
        placeE.setAttribute("status", status)

        return placeE
    }
    /* !Construction Methods */

    /* Exposed Methods */
    public Element getPlacesDataElement(Document doc, Set<Place> placeSet) {
        Element placesE = doc.createElement("PLACES")

        if (placeSet != null && !placeSet.isEmpty()) {
            placesE.setAttribute("step_id", "3")
            for (Place place : placeSet) {
                Element placeE = doc.createElement("PLACE")
                placeE.setAttribute("id", place.DTDId as String)
                placeE.setAttribute("value", place.name)
                placeE.setAttribute("gender", place.gender)

                Element descriptionE = doc.createElement("DESCRIPTION")
                Element tagsE = doc.createElement("TAGS")

                // Description
                if (place.description) {
                    CDATASection descriptionData = doc.createCDATASection("DESCRIPTION")
                    descriptionData.setData(place.description)
                    descriptionE.appendChild(descriptionData)
                }

                // Tags
                for(PlaceHasTag placeHasTag : place.extTags) {
                    Element tagE = doc.createElement("TAG")
                    tagE.setAttribute("value", placeHasTag.tag.name)
                    tagE.setAttribute("type", placeHasTag.tag.parent.name)
                    tagE.setAttribute("weight", placeHasTag.weight as String)

                    tagsE.appendChild(tagE)
                }

                placeE.appendChild(descriptionE)
                placeE.appendChild(tagsE)
                placesE.appendChild(placeE)
            }
        }
        return placesE
    }
    /* !Exposed Methods */
}
