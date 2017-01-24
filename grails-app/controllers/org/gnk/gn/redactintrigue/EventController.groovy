package org.gnk.gn.redactintrigue

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.ErrorHandlerService
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.selectintrigue.Plot
import org.springframework.security.access.annotation.Secured
import java.text.ParseException
import java.text.SimpleDateFormat

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class EventController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
//   ErrorHandlerService errorHandlerService
    def index() {}
    def save () {

        Event event = new Event();
        Boolean res = saveOrUpdate(event);
        //Boolean error_event = isValidDate();
        def jsonEvent = buildJson(event);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("event", jsonEvent);
        object.put("isvalidDate", error_event);
        render(contentType: "application/json") {
            object
        }
    }
    def buildJson(Event event) {
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put("name", event.getName());
        jsonEvent.put("id", event.getId());
        jsonEvent.put("plotId", event.getPlot().getId());
        jsonEvent.put("timing", event.getTiming());
        jsonEvent.put("duration", event.getDuration());
        jsonEvent.put("isPublic", event.getIsPublic());
        jsonEvent.put("isPlanned", event.getIsPlanned());
        jsonEvent.put("description", event.getDescription());
        jsonEvent.put("absoluteYear", event.getAbsoluteYear());
        jsonEvent.put("absoluteMonth", event.getAbsoluteMonth());
        jsonEvent.put("absoluteDay", event.getAbsoluteDay());
        jsonEvent.put("absoluteHour", event.getAbsoluteHour());
        jsonEvent.put("absoluteMinute", event.getAbsoluteMinute());
        if (event.getEventPredecessor()) {
            jsonEvent.put("eventPredecessor", event.getEventPredecessor().getName());
            jsonEvent.put("eventPredecessorId", event.getEventPredecessor().getId());
        }
        if (event.getGenericPlace()) {
            jsonEvent.put("eventPlaceId", event.getGenericPlace().getId());
        }
        JSONArray jsonRoleList = new JSONArray();
        for (Role role in event.plot.roles) {
            RoleHasEvent roleHasEvent = role.getRoleHasEvent(event);
            JSONObject jsonRole = new JSONObject();
            if (roleHasEvent) {
                jsonRole.put("title", roleHasEvent.title);
                jsonRole.put("isAnnounced", roleHasEvent.isAnnounced);
                jsonRole.put("description", roleHasEvent.description);
                jsonRole.put("comment", roleHasEvent.comment);
                jsonRole.put("evenemential", roleHasEvent.evenementialDescription);
            }
            else {
                jsonRole.put("title", "");
                jsonRole.put("isAnnounced", "");
                jsonRole.put("description", "");
                jsonRole.put("comment", "");
                jsonRole.put("evenemential", "");
            }
            JSONArray jsonResourceList = new JSONArray();
            for (GenericResource resource in event.plot.genericResources) {
                JSONObject jsonResource = new JSONObject();
                jsonResource.put("id", resource.id);
                jsonResource.put("code", resource.code);
                if (resource.getGenericResourceHasRoleHasEvent(roleHasEvent)) {
                    jsonResource.put("quantity", resource.getGenericResourceHasRoleHasEvent(roleHasEvent).quantity);
                }
                else {
                    jsonResource.put("quantity", "");
                }
                jsonResourceList.add(jsonResource);
            }
            jsonRole.put("resourceList", jsonResourceList);
            jsonRole.put("roleId", role.id);
            jsonRole.put("roleCode", role.code);
            jsonRoleList.add(jsonRole);
        }
        jsonEvent.put("roleList", jsonRoleList);
        return jsonEvent;
    }
    Boolean checkevent(Plot plot)
    {
        ArrayList<String> title = plot.events.name;
        ArrayList<String> eventdescr = plot.events.description;
        ArrayList<String> roles = plot.roles.code;
        ArrayList<String> pastscenes = plot.pastescenes.description;
        ArrayList<String> gplace = plot.genericPlaces.code;
        //print(pastscenes);
        //print(roles);
        //print(title);
        //String description = params.description
        //Pastscene ps = new Pastscene();
        JSONObject jsonEvent = new JSONObject();
        //Je parse ma description et mon titre avec les roles... et je valide

        for (String r : roles)
        {
            for (String desc : eventdescr)
                {

                        if (!desc.contains(r))
                        {
                            System.out.print("warning");
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                        }

                }
        for (String desc: eventdescr)
        {
            for (String gp:gplace)
            {
                return desc.contains(gp)

            }
        }

        for (String titre : title)
        {
            for (String ps: pastscenes)
            {
                if (!ps.contains(title))
                {
                    System.out.print("warning");
                    return false;
                }
                else
                {
                    return true;
                }
              //  return ps.contains(titre);
            }
        }
        /*for  (int m = 0; m < title.size(); m++)
        {
            return pastscenes.contains(title[m]);
        }*/


        boolean isChecked = true;
        jsonEvent.put("isChecked", isChecked);


    }
    def update (Long id) {
        Event event = Event.get(id)
        if (event) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(event),
                        id: event.id,
                        timing: event.timing,
                        name: event.name)
            }
        }
    }
    def saveOrUpdate(Event newEvent) {
        if (params.containsKey("plotId")) {
            Plot plot = Plot.get(params.plotId as Integer)
            newEvent.plot = plot
            if (!checkevent(plot))
                return false;
        } else {
            return false
        }
        if (params.containsKey("eventName")) {
            newEvent.name = params.eventName
        } else {
            return false
        }
        if (params.containsKey("eventDuration")) {
            newEvent.duration = params.eventDuration as Integer
        } else {
            return false
        }
        if (params.containsKey("eventPublic")) {
            newEvent.isPublic = true;
        } else {
            newEvent.isPublic = false;
        }
        if (params.containsKey("eventPlanned")) {
            newEvent.isPlanned = true;
        } else {
            newEvent.isPlanned = false;
        }
        if (params.containsKey("eventDescription")) {
            newEvent.description = params.eventDescription
        } else {
            return false
        }
        if (params.containsKey("eventTiming")) {
            newEvent.timing = params.eventTiming as Integer;
        }
        if (params.containsKey("eventPlace") && params.eventPlace != null && params.eventPlace != "" && params.eventPlace != "null") {
            GenericPlace genericPlace = GenericPlace.findById(params.eventPlace as Integer);
            if (genericPlace) {
                newEvent.genericPlace = genericPlace;
            }
        }
        else {
            newEvent.genericPlace = null;
        }
        if (params.containsKey("eventPredecessor") && params.eventPredecessor != null && params.eventPredecessor != "" && params.eventPredecessor != "null") {
            Event eventPredecessor = Event.findById(params.eventPredecessor as Integer);
            if (eventPredecessor) {
                newEvent.eventPredecessor = eventPredecessor;
            }
        }
        else {
            newEvent.eventPredecessor = null;
        }
        newEvent.version = 1;
        newEvent.dateCreated = new Date();
        newEvent.lastUpdated = new Date();
        newEvent.save(flush: true);
        params.each {
            if (it.key.startsWith("roleHasEventTitle")) {
                Role role = Role.get((it.key - "roleHasEventTitle") as Integer);
                RoleHasEvent roleHasEvent = createRoleHasEvent(role, newEvent);
//                if (roleHasEvent) {
//                    newEvent.addToRoleHasEvents(roleHasEvent);
//                }
            }
        }
        newEvent.save(flush: true);
        return true;
    }

    def createRoleHasEvent(Role role, Event event) {
        RoleHasEvent roleHasEvent = role.getRoleHasEvent(event);
        if (!roleHasEvent && (params.get("roleHasEventTitle" + role.id) == "")) {
            return null;
        }
        else if (!roleHasEvent) {
            roleHasEvent = new RoleHasEvent();
            roleHasEvent.dateCreated = new Date();
            roleHasEvent.lastUpdated = new Date();
            roleHasEvent.version = 1;
            roleHasEvent.event = event;
            roleHasEvent.role = role;
        }
        else if ((params.get("roleHasEventTitle" + role.id) == "")) {
            roleHasEvent.delete(flush: true);
            return null;
        }
        roleHasEvent.title = params.get("roleHasEventTitle" + role.id);
        roleHasEvent.isAnnounced = params.get("roleHasEventannounced" + role.id) != null;
        roleHasEvent.description = params.get("roleHasEventDescription" + role.id);
        roleHasEvent.comment = params.get("roleHasEventComment" + role.id);
        roleHasEvent.evenementialDescription = params.get("roleHasEventEvenemential" + role.id);
        roleHasEvent.save(flush: true);
//        role.addToRoleHasEvents(roleHasEvent);
//        role.save(flush: true);
        params.each {
            if (it.key.startsWith("quantity" + role.id + "_")) {
                GenericResource genericResource = GenericResource.get((it.key - ("quantity" + role.id + "_")) as Integer);
                RoleHasEventHasGenericResource roleHasEventHasGenericResource = createRoleHasEventHasGenericResource(genericResource, roleHasEvent);
                if (roleHasEventHasGenericResource != null) {
                    roleHasEvent.addToRoleHasEventHasGenericResources(roleHasEventHasGenericResource);
                }
            }
        }
        roleHasEvent.save(flush: true);
        return roleHasEvent;
    }

    public RoleHasEventHasGenericResource createRoleHasEventHasGenericResource(GenericResource genericResource, roleHasEvent) {
        RoleHasEventHasGenericResource roleHasEventHasGenericResource = genericResource.getGenericResourceHasRoleHasEvent(roleHasEvent);
        String quantity = params.get("quantity" + roleHasEvent.role.id + "_" + genericResource.id);
        if (!roleHasEventHasGenericResource && (quantity.isInteger())) {
            roleHasEventHasGenericResource = new RoleHasEventHasGenericResource();
            roleHasEventHasGenericResource.roleHasEvent = roleHasEvent;
            roleHasEventHasGenericResource.genericResource = genericResource;
            roleHasEventHasGenericResource.quantity = params.get("quantity" + roleHasEvent.role.id + "_" + genericResource.id) as Integer;
            roleHasEventHasGenericResource.save(flush: true);
            genericResource.addToRoleHasEventHasRessources(roleHasEventHasGenericResource);
            genericResource.save();
        }
        else {
            if (!quantity.isInteger()) {
                if (roleHasEventHasGenericResource) {
                    roleHasEventHasGenericResource.delete(flush: true);
                }
                return null;
            }
            else {
                roleHasEventHasGenericResource.quantity = quantity as Integer;
                roleHasEventHasGenericResource.save(flush: true);
                genericResource.addToRoleHasEventHasRessources(roleHasEventHasGenericResource);
                genericResource.save();
            }
        }
        return roleHasEventHasGenericResource;
    }

    public Calendar isValidDate(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            Calendar cal = Calendar.getInstance();
            Date date = sdf.parse(dateToValidate);
            cal.setTime(date)
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
  //          errorHandlerService.ParseErrorHandler();
            return null;
        }
    }

    def delete (Long id) {
        Event event = Event.get(id)
        boolean isDelete = false;
        if (event) {
            event.delete(flush: true);
            isDelete = true;
        }
        final JSONObject object = new JSONObject();
        object.put("isDelete", isDelete);
        object.put("eventId", id);
        render(contentType: "application/json") {
            object
        }
    }
}
