package org.gnk.gn.redactintrigue

import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.selectintrigue.Plot
import org.springframework.security.access.annotation.Secured

import java.text.ParseException
import java.text.SimpleDateFormat

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class EventController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {}

    def save () {
        Event event = new Event();
        Boolean res = saveOrUpdate(event);
//        event = Event.findAllWhere("name": params.eventName).first();
        def jsonEvent = buildJson(event);
        final JSONObject object = new JSONObject();
        object.put("iscreate", res);
        object.put("event", jsonEvent);
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
        return jsonEvent;
    }

    def update(Long id) {
        Event event = Event.get(id)
        if (event) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(event),
                        id: event.id,
                        name: event.name)
            }
        }
    }

    def saveOrUpdate(Event newEvent) {
        if (params.containsKey("plotId")) {
            Plot plot = Plot.get(params.plotId as Integer)
            newEvent.plot = plot
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
        Calendar calendar = isValidDate(params.eventDatetime as String, "dd/MM/yyyy HH:mm");
        if (calendar) { // TODO !, données non persistante donc à traiter autrement
//            newEvent.absoluteYear = calendar.get(Calendar.YEAR);
//            newEvent.absoluteMonth = calendar.get(Calendar.MONTH);
//            newEvent.absoluteDay = calendar.get(Calendar.DAY_OF_MONTH);
//            newEvent.absoluteHour = calendar.get(Calendar.HOUR);
//            newEvent.absoluteMinute = calendar.get(Calendar.MINUTE);
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
//        newEvent.roleHasEvents = new HashSet<RoleHasEvent>();
//        newEvent.DTDId = 1;
        newEvent.save(flush: true);
        return true;
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
            return null;
        }
    }

    def delete (Long id) {
        Event event = Event.get(id)
        boolean isDelete = false;
        if (event) {
            event.delete();
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
