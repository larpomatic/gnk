package org.gnk.parser.event

import org.gnk.resplacetime.Event
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.w3c.dom.CDATASection
import org.w3c.dom.Document
import org.w3c.dom.Element

class EventXMLWriterService {

    /* Exposed Methods */
    def Element getEventElementForPlot(Document doc, Event event) {
        Element plotEventElt = getEventRootElementForPlot(doc, event);

        plotEventElt.appendChild(getEventTitleElementForPlot(doc, event));
        plotEventElt.appendChild(getEventDescriptionElementForPlot(doc, event));
        plotEventElt.appendChild(getEventPredecessorsElementForPlot(doc, event));
        plotEventElt.appendChild(getEventGenericPlaceElementForPlot(doc, event));

        return plotEventElt;
    }

    def Element getEventElementForRole(Document doc, RoleHasEvent roleHasEvent) {
        Element roleEventElt = getEventRootElementForRole(doc, roleHasEvent);

        roleEventElt.appendChild(getEventTitleElementForRole(doc, roleHasEvent));
        roleEventElt.appendChild(getEventDescriptionElementForRole(doc, roleHasEvent));
        roleEventElt.appendChild(getEventCommentElementForRole(doc, roleHasEvent));
        roleEventElt.appendChild(getEventGenericResourcesElementForRole(doc, roleHasEvent));

        return roleEventElt;
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Element getEventRootElementForPlot(Document doc, Event event)
    {
        Element rootElt = doc.createElement("EVENT")

        if (!event.DTDId)
            event.DTDId = event.id
        rootElt.setAttribute("id", event.DTDId.toString())
        rootElt.setAttribute("timing", event.timing.toString())
        if (event.absoluteYear)
        {
            String date = event.absoluteDay + "/" + event.absoluteMonth + "/" + event.absoluteYear + " " + event.absoluteHour + ":" + event.absoluteMinute
            Date d = Date.parse("dd/MM/yyyy HH:mm", date)
            String absoluteTiming = d.format("dd/MM/yyyy HH:mm")
            rootElt.setAttribute("absolute_timing", absoluteTiming)
        }
        else
            rootElt.setAttribute("absolute_timing", "Timing absolu pas encore calculé")
        rootElt.setAttribute("duration", event.duration.toString())
        rootElt.setAttribute("is_public", event.isPublic.toString())
        rootElt.setAttribute("is_planned", event.isPlanned.toString())

        return rootElt
    }

    private Element getEventTitleElementForPlot(Document doc, Event event) {
        Element titleElt = doc.createElement("TITLE")

        if (event.name) {
            CDATASection titleData = doc.createCDATASection("TITLE")
            titleData.setData(event.name)
            titleElt.appendChild(titleData)
        }

        return titleElt
    }

    private Element getEventDescriptionElementForPlot(Document doc, Event event) {
        Element descriptionElt = doc.createElement("DESCRIPTION")

        if (event.description) {
            CDATASection descriptionData = doc.createCDATASection("DESCRIPTION")
            descriptionData.setData(event.description)
            descriptionElt.appendChild(descriptionData)
        }

        return descriptionElt
    }

    private Element getEventPredecessorsElementForPlot(Document doc, Event event) {

        Element predecessorsElt = doc.createElement("PREDECESSORS")
        if (event.eventPredecessor) {
            Element predecessorElt = doc.createElement("PREDECESSOR")
            if (!event.eventPredecessor.DTDId)
                event.eventPredecessor.DTDId = event.eventPredecessor.id
            predecessorElt.setAttribute("id", event.eventPredecessor.DTDId.toString())

            predecessorsElt.appendChild(predecessorElt)
        }

        return predecessorsElt
    }

    private Element getEventGenericPlaceElementForPlot(Document doc, Event event) {
        Element genericPlacesElt = doc.createElement("GENERIC_PLACES")
        if (event.genericPlace) {
            Element genericPlaceElt = doc.createElement("GENERIC_PLACE");
            if (!event.genericPlace.DTDId)
                event.genericPlace.DTDId = event.genericPlace.id
            genericPlaceElt.setAttribute("generic_place_id", event.genericPlace.DTDId.toString())

            genericPlacesElt.appendChild(genericPlaceElt)
        }

        return genericPlacesElt
    }

    private Element getEventRootElementForRole(Document doc, RoleHasEvent roleHasEvent)
    {
        Element rootElt = doc.createElement("EVENT")

        if (!roleHasEvent.event.DTDId)
            roleHasEvent.event.DTDId = roleHasEvent.event.id
        rootElt.setAttribute("event_id", roleHasEvent.event.DTDId.toString())
        rootElt.setAttribute("is_announced", roleHasEvent.isAnnounced.toString())

        return rootElt
    }



    private Element getEventTitleElementForRole(Document doc, RoleHasEvent roleHasEvent)
    {
        Element titleElt = doc.createElement("TITLE")

        if (roleHasEvent.title) {
            CDATASection titleData = doc.createCDATASection("TITLE")
            titleData.setData(roleHasEvent.title)
            titleElt.appendChild(titleData)
        }

        return titleElt
    }


    private Element getEventDescriptionElementForRole(Document doc, RoleHasEvent roleHasEvent)
    {
        Element descriptionElt = doc.createElement("DESCRIPTION")

        if (roleHasEvent.description) {
            CDATASection descriptionData = doc.createCDATASection("DESCRIPTION")
            descriptionData.setData(roleHasEvent.description)
            descriptionElt.appendChild(descriptionData)
        }

        return descriptionElt
    }

    private Element getEventCommentElementForRole(Document doc, RoleHasEvent roleHasEvent)
    {
        Element commentElt = doc.createElement("COMMENT")

        if (roleHasEvent.comment) {
            CDATASection commentData = doc.createCDATASection("COMMENT")
            commentData.setData(roleHasEvent.comment)
            commentElt.appendChild(commentData)
        }

        return commentElt
    }

    private Element getEventGenericResourcesElementForRole(Document doc, RoleHasEvent roleHasEvent)
    {
        Element genericResourcesElt = doc.createElement("GENERIC_RESOURCES")

        if (roleHasEvent.roleHasEventHasGenericResources) {
            for (RoleHasEventHasGenericResource roleHasEventHasGenericResource : roleHasEvent.roleHasEventHasGenericResources) {
                Element genericResourceElt = doc.createElement("GENERIC_RESOURCE");
                if (!roleHasEventHasGenericResource.genericResource.DTDId)
                    roleHasEventHasGenericResource.genericResource.DTDId = roleHasEventHasGenericResource.genericResource.id
                genericResourceElt.setAttribute("generic_resource_id", roleHasEventHasGenericResource.genericResource.DTDId.toString())
                genericResourceElt.setAttribute("quantity", roleHasEventHasGenericResource.quantity.toString())

                genericResourcesElt.appendChild(genericResourceElt)
            }
        }

        return genericResourcesElt
    }
}