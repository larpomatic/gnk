package org.gnk.parser.event

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.EventXMLNode
import org.gnk.resplacetime.Event
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.selectintrigue.Plot

class EventXMLReaderService {

    /* Exposed Methods */
    public List<Event> getEventsFromNode(Node EVENTS, Plot plot, GNKDataContainerService dataContainer) {

        NodeList eventList = EVENTS.EVENT
        HashMap<Integer, EventXMLNode> eventNodes = new HashMap<Integer, EventXMLNode>()

        eventList.each { Node EVENT ->
            EventXMLNode eventRes = getEventXMLNode(EVENT, plot, dataContainer)
            if (eventRes != null && eventRes.event != null) {
                eventNodes.put(eventRes.event.DTDId, eventRes);
            }
        }

        //parse eventNodes
        List<Event> events = new ArrayList<>()

        eventNodes.each { Integer DTDId, EventXMLNode eventXMLNode ->
            // FIXME: add many predecessors
            // add predecessor object to event
            if (eventXMLNode.predecessorIds.size() > 0) {
                if (eventNodes.get(eventXMLNode.predecessorIds[0])) {
                    eventXMLNode.event.eventPredecessor = eventNodes.get(eventXMLNode.predecessorIds[0]).event
                } else {
                    log.error("Cannot find event with DTDid ${eventXMLNode.predecessorIds[0]}")
                }
            }
            events.add(eventXMLNode.event)
        }

        return events
    }

    def EventXMLNode getEventXMLNode(Node EVENT, Plot plot, GNKDataContainerService dataContainer) {
        EventXMLNode eventXMLNode = new EventXMLNode();

        // EVENT reader
        eventXMLNode.event = ReadEventRootNode(EVENT, plot, dataContainer)

        // TITLE reader
        ReadTitleNode(EVENT, eventXMLNode.event)

        // DESCRIPTION reader
        ReadDescriptionNode(EVENT, eventXMLNode.event)

        // PREDECESSORS reader
        ReadPredecessorsNode(EVENT, eventXMLNode, dataContainer)

        // GENERIC_PLACES reader
        ReadGenericPlacesNode(EVENT, eventXMLNode.event, dataContainer)

        return eventXMLNode
    }

    def RoleHasEvent getRoleHasEventsFromNode(Node EVENT, Role role, GNKDataContainerService dataContainer) {
        Event eventRes = null

        // Get Event
        eventRes = GetEventFromRootNode(EVENT, role)
        if (eventRes == null)
            return null

        // Link roleHasEventRes to eventRes
        RoleHasEvent roleHasEventRes = new RoleHasEvent()
        eventRes.addToRoleHasEvents(roleHasEventRes)
        roleHasEventRes.event = eventRes

        // EVENT reader : fill roleHasEvent
        ReadEventRootNodeInRole (EVENT, roleHasEventRes, role.plot)
        // TITLE reader
        ReadTitleNodeInRole(EVENT, roleHasEventRes)
        // DESCRIPTION reader
        ReadDescriptionNodeInRole(EVENT, roleHasEventRes)
        // COMMENT reader
        ReadCommentNodeInRole(EVENT, roleHasEventRes)
        // EVENEMENTIAL_DESCRIPTION reader
        ReadEvenementialDescriptionNodeInRole(EVENT, roleHasEventRes)
        // GENERIC_RESOURCES reader
        ReadGenericResourcesNode(EVENT, roleHasEventRes, dataContainer)

        return roleHasEventRes
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Event ReadEventRootNode (Node EVENT, Plot plot, GNKDataContainerService dataContainer) {
        String name = null
        Event eventRes = null

        if (eventRes == null)
            eventRes = new Event()

        if (EVENT.attribute("id") != "null")
            eventRes.DTDId = EVENT.attribute("id") as Integer
        if (EVENT.attribute("absolute_timing") != "null" && !EVENT.attribute("absolute_timing").toString().startsWith("Timing"))
        {
            // syntaxe : 25/01/2014 10:48
            String date = EVENT.attribute("absolute_timing").toString()

            eventRes.absoluteDay = Integer.parseInt(date.substring(0, 2))
            eventRes.absoluteMonth = Integer.parseInt(date.substring(3, 5))
            eventRes.absoluteYear = Integer.parseInt(date.substring(6, 10))
            eventRes.absoluteHour = Integer.parseInt(date.substring(11, 13))
            eventRes.absoluteMinute = Integer.parseInt(date.substring(14, 16))
        }
        if (EVENT.attribute("timing") != "null")
            eventRes.timing = EVENT.attribute("timing") as Integer
        if (EVENT.attribute("duration") != "null")
            eventRes.duration = EVENT.attribute("duration") as Integer
        if (EVENT.attribute("is_public") != "null")
            eventRes.isPublic = EVENT.attribute("is_public").equals("false") ? false : true
        if (EVENT.attribute("is_planned") != "null")
            eventRes.isPlanned = EVENT.attribute("is_planned").equals("false") ? false : true

        return eventRes
    }

    private Event GetEventFromRootNode (Node EVENT, Role role) {
        Event eventRes = new Event()
        if (EVENT.attribute("event_id") != "null" && (EVENT.attribute("event_id") as String).isInteger()) {
            Integer eventDTDId = EVENT.attribute("event_id") as Integer
            eventRes = role.plot.events.find{ Event event ->
                if (event.getDTDId() == eventDTDId)
                    return event
            }
        }

        return eventRes
    }

    private void ReadEventRootNodeInRole(Node EVENT, RoleHasEvent roleHasEvent, Plot plot) {
        if (EVENT.attribute("is_announced") != "null")
            roleHasEvent.isAnnounced = EVENT.attribute("is_announced")
    }

    private void ReadTitleNode (Node EVENT, Event eventRes) {
        assert (EVENT.TITLE.size() <= 1)
        if (EVENT.TITLE.size() > 0) {
            eventRes.name =  EVENT.TITLE[0].text()
        }
    }

    private void ReadDescriptionNode (Node EVENT, Event eventRes) {
        assert (EVENT.DESCRIPTION.size() <= 1)
        if (EVENT.DESCRIPTION.size() > 0) {
            eventRes.description =  EVENT.DESCRIPTION[0].text()
        }
    }

    private void ReadDescriptionNodeInRole (Node EVENT, RoleHasEvent roleHasEvent) {
        assert (EVENT.DESCRIPTION.size() <= 1)
        if (EVENT.DESCRIPTION.size() > 0)
                roleHasEvent.description =  EVENT.DESCRIPTION[0].text()
    }

    private void ReadTitleNodeInRole (Node EVENT, RoleHasEvent roleHasEvent) {
        assert (EVENT.TITLE.size() <= 1)
        if (EVENT.TITLE.size() > 0)
            roleHasEvent.title = EVENT.TITLE[0].text()
    }

    private void ReadCommentNodeInRole (Node EVENT, RoleHasEvent roleHasEvent) {
        assert (EVENT.COMMENT.size() <= 1)
        if (EVENT.COMMENT.size() > 0)
            roleHasEvent.comment = EVENT.COMMENT[0].text()
    }

    private void ReadEvenementialDescriptionNodeInRole (Node EVENT, RoleHasEvent roleHasEvent) {
        assert (EVENT.EVENEMENTIAL_DESCRIPTION.size() <= 1)
        if (EVENT.EVENEMENTIAL_DESCRIPTION.size() > 0)
            roleHasEvent.evenementialDescription = EVENT.EVENEMENTIAL_DESCRIPTION[0].text()
    }

    private void ReadPredecessorsNode(Node EVENT, EventXMLNode eventXMLNode, GNKDataContainerService dataContainer) {
        assert (EVENT.PREDECESSORS.size() <= 1)
        if (EVENT.PREDECESSORS.size() <= 0)
            return;

        Node PREDECESSORS = EVENT.PREDECESSORS[0]
        NodeList PREDECESSORLIST = PREDECESSORS.PREDECESSOR

        PREDECESSORLIST.each { Node PREDECESSOR ->
            if (PREDECESSOR.attribute("id") != "null")
                eventXMLNode.predecessorIds.add(PREDECESSOR.attribute("id") as Integer)
        }
    }

    private void ReadGenericPlacesNode(Node EVENT, Event eventRes, GNKDataContainerService dataContainer) {
        assert (EVENT.GENERIC_PLACES.size() <= 1)
        if (EVENT.GENERIC_PLACES.size() <= 0)
            return;
        Node GENERIC_PLACES = EVENT.GENERIC_PLACES[0]

        assert (GENERIC_PLACES.GENERIC_PLACE.size() <= 1)
        if (GENERIC_PLACES.GENERIC_PLACE.size() <= 0)
            return;
        Node GENERIC_PLACE = GENERIC_PLACES.GENERIC_PLACE[0]

        Integer genericPlaceDTDId
        if (GENERIC_PLACE.attribute("generic_place_id") == null || !(GENERIC_PLACE.attribute("generic_place_id") as String).isInteger())
            return

        genericPlaceDTDId = GENERIC_PLACE.attribute("generic_place_id") as Integer
        eventRes.setGenericPlace(dataContainer.genericPlaceMap.get(genericPlaceDTDId))

        // assert on genericPlace not found
//        assert (eventRes.genericPlace != null)
        if (eventRes.genericPlace != null)
            eventRes.genericPlace.addToEvents(eventRes)
        else
            log.error("genericPlace is not found in event " + eventRes.getDTDId() + (eventRes.plot == null ? "":  " in plot " + eventRes.plot.getDTDId()))
    }

    private void ReadGenericResourcesNode(Node EVENT, RoleHasEvent roleHasEvent, GNKDataContainerService dataContainer) {
        assert (EVENT.GENERIC_RESOURCES.size() <= 1)
        if (EVENT.GENERIC_RESOURCES.size() <= 0)
            return;
        Node GENERIC_RESOURCES = EVENT.GENERIC_RESOURCES[0]
        NodeList genericResourceList = GENERIC_RESOURCES.GENERIC_RESOURCE

        genericResourceList.each {Node GENERIC_RESOURCE ->
            Integer genericResourceDTDId
            if (GENERIC_RESOURCE.attribute("generic_resource_id") != null && (GENERIC_RESOURCE.attribute("generic_resource_id") as String).isInteger()) {
                RoleHasEventHasGenericResource roleHasEventHasGenericResource = new RoleHasEventHasGenericResource()
                genericResourceDTDId = GENERIC_RESOURCE.attribute("generic_resource_id") as Integer
                if (GENERIC_RESOURCE.attribute("quantity") != "null")
                    roleHasEventHasGenericResource.quantity = GENERIC_RESOURCE.attribute("quantity") as Integer
                else
                    roleHasEventHasGenericResource.quantity = 1

                roleHasEventHasGenericResource.genericResource = dataContainer.genericResourceMap.get(genericResourceDTDId)
                roleHasEvent.addToRoleHasEventHasGenericResources(roleHasEventHasGenericResource)
            }
        }
    }
    /* !Construction Methods */
}
