package org.gnk.parser.time

import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.Place
import org.gnk.selectintrigue.Plot
import org.w3c.dom.Document
import org.w3c.dom.Element

class SubstitutionTimeXMLWriterService {

    /* Exposed Methods */
    public Element getTimeElement(Document doc, Set<Plot> selectedPlotSet) {

        Element timeE = doc.createElement("TIME")
        timeE.setAttribute("step_id", "3")

        // Iterate pastscene
        for(plot in selectedPlotSet) {
            for(pastScene in plot.pastescenes) {
                timeE.appendChild(getPastsceneElement(doc, pastScene, plot))
            }
        }

        // Iterate event
        for(plot in selectedPlotSet) {
            for(event in plot.events) {
                timeE.appendChild(getEventElement(doc, event, plot))
            }
        }

        return timeE
    }
/* !Exposed Methods */

/* Construction Methods */
    private Element getPastsceneElement(Document doc, Pastscene pastscene, Plot plot){
        Element pastsceneE = doc.createElement("PASTSCENE")

        pastsceneE.setAttribute("pastscene_id", pastscene.DTDId as String)
        pastsceneE.setAttribute("plot_id", plot.DTDId as String)

        pastsceneE.appendChild(getAbsoluteElement(doc, pastscene))

        return pastsceneE
    }

    private Element getAbsoluteElement(Document doc, Pastscene pastscene) {
        Element absoluteE = doc.createElement("ABSOLUTE")

        absoluteE.setAttribute("year", pastscene.absoluteYear as String)
        absoluteE.setAttribute("month", pastscene.absoluteMonth as String)
        absoluteE.setAttribute("day", pastscene.absoluteDay as String)
        absoluteE.setAttribute("hour", pastscene.absoluteHour as String)
        absoluteE.setAttribute("min", pastscene.absoluteMinute as String)

        return absoluteE
    }

    private Element getEventElement(Document doc, Event event, Plot plot){
        Element eventE = doc.createElement("EVENT")

        eventE.setAttribute("event_id", event.DTDId as String)
        eventE.setAttribute("plot_id", plot.DTDId as String)

        eventE.appendChild(getAbsoluteElement(doc, event))

        return eventE
    }

    private Element getAbsoluteElement(Document doc, Event event) {
        Element absoluteE = doc.createElement("ABSOLUTE")

        absoluteE.setAttribute("year", event.absoluteYear as String)
        absoluteE.setAttribute("month", event.absoluteMonth as String)
        absoluteE.setAttribute("day", event.absoluteDay as String)
        absoluteE.setAttribute("hour", event.absoluteHour as String)
        absoluteE.setAttribute("min", event.absoluteMinute as String)

        return absoluteE
    }
/* !Construction Methods */
}
