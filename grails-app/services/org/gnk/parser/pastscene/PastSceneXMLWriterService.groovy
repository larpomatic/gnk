package org.gnk.parser.pastscene

import org.gnk.resplacetime.Pastscene
import org.w3c.dom.CDATASection
import org.w3c.dom.Document
import org.w3c.dom.Element

class PastSceneXMLWriterService {

    /* Exposed Methods */
    def Element getPastSceneElementForPlot(Document doc, Pastscene pastscene) {
        Element plotPastsceneElt = getPastsceneRootElementForPlot(doc, pastscene);

        plotPastsceneElt.appendChild(getPastsceneTitleElementForPlot(doc, pastscene));
        plotPastsceneElt.appendChild(getPastsceneDescriptionElementForPlot(doc, pastscene));
        plotPastsceneElt.appendChild(getPastscenePredecessorsElementForPlot(doc, pastscene));
        plotPastsceneElt.appendChild(getPastsceneGenericPlaceElementForPlot(doc, pastscene));
        plotPastsceneElt.appendChild(getPastsceneTimeElementForPlot(doc, pastscene));

        return plotPastsceneElt;
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Element getPastsceneRootElementForPlot(Document doc, Pastscene pastscene)
    {
        Element rootElt = doc.createElement("PAST_SCENE")

        if (!pastscene.DTDId)
            pastscene.DTDId = pastscene.id
        rootElt.setAttribute("id", pastscene.DTDId.toString())
        rootElt.setAttribute("is_public", pastscene.isPublic.toString())

        return rootElt
    }

    private Element getPastsceneTitleElementForPlot(Document doc, Pastscene pastscene) {
        Element titlenElt = doc.createElement("TITLE")

        if (pastscene.title) {
            CDATASection titleData = doc.createCDATASection("TITLE")
            titleData.setData(pastscene.title)
            titlenElt.appendChild(titleData)
        }

        return titlenElt
    }

    private Element getPastsceneDescriptionElementForPlot(Document doc, Pastscene pastscene) {
        Element descriptionElt = doc.createElement("DESCRIPTION")

        if (pastscene.description) {
            CDATASection descriptionData = doc.createCDATASection("DESCRIPTION")
            descriptionData.setData(pastscene.description)
            descriptionElt.appendChild(descriptionData)
        }

        return descriptionElt
    }

    private Element getPastscenePredecessorsElementForPlot(Document doc, Pastscene pastscene) {
        Element predecessorsElt = doc.createElement("PREDECESSORS")

        if (pastscene.pastscenePredecessor) {
            Element predecessorElt = doc.createElement("PREDECESSOR");
            if (!pastscene.pastscenePredecessor.DTDId)
                pastscene.pastscenePredecessor.DTDId = pastscene.pastscenePredecessor.id
            predecessorElt.setAttribute("past_scene_id", pastscene.pastscenePredecessor.DTDId.toString())

            predecessorsElt.appendChild(predecessorElt)
        }

        return predecessorsElt
    }

    private Element getPastsceneGenericPlaceElementForPlot(Document doc, Pastscene pastscene) {
        Element genericPlacesElt = doc.createElement("GENERIC_PLACES")

        if (pastscene.genericPlace) {
            Element genericPlaceElt = doc.createElement("GENERIC_PLACE");

            if (!pastscene.genericPlace.DTDId)
                pastscene.genericPlace.DTDId = pastscene.genericPlace.id
            genericPlaceElt.setAttribute("generic_place_id", pastscene.genericPlace.DTDId.toString())

            genericPlacesElt.appendChild(genericPlaceElt)
        }

        return genericPlacesElt
    }

    private Element getPastsceneTimeElementForPlot(Document doc, Pastscene pastscene) {
        Element timeElt = doc.createElement("TIME")

        /*
        Element relativeTimeElt = doc.createElement("RELATIVE")
        if (pastscene.timingRelative == null)
            relativeTimeElt.setAttribute("time", "")
        else
            relativeTimeElt.setAttribute("time", pastscene.timingRelative.toString())
        if (pastscene.unitTimingRelative == null)
            relativeTimeElt.setAttribute("time_unit", "")
        else
            relativeTimeElt.setAttribute("time_unit", pastscene.unitTimingRelative)
        timeElt.appendChild(relativeTimeElt)
        */

        Element absoluteTimeElt = doc.createElement("ABSOLUTE")
        if (pastscene.dateYear == null)
            absoluteTimeElt.setAttribute("year", "")
        else
            absoluteTimeElt.setAttribute("year", pastscene.dateYear.toString())
        if (pastscene.dateMonth == null)
            absoluteTimeElt.setAttribute("month", "")
        else
            absoluteTimeElt.setAttribute("month", pastscene.dateMonth.toString())
        if (pastscene.dateDay == null)
            absoluteTimeElt.setAttribute("day", "")
        else
            absoluteTimeElt.setAttribute("day", pastscene.dateDay.toString())
        if (pastscene.dateHour == null)
            absoluteTimeElt.setAttribute("hour", "")
        else
            absoluteTimeElt.setAttribute("hour", pastscene.dateHour.toString())
        if (pastscene.dateMinute == null)
            absoluteTimeElt.setAttribute("minute", "")
        else
            absoluteTimeElt.setAttribute("minute", pastscene.dateMinute.toString())

        // ABSOLUTE
        if (pastscene.isAbsoluteYear == null)
            absoluteTimeElt.setAttribute("isAbsoluteYear", "false")
        else
            absoluteTimeElt.setAttribute("isAbsoluteYear", pastscene.isAbsoluteYear.toString())

        if (pastscene.isAbsoluteMonth == null)
            absoluteTimeElt.setAttribute("isAbsoluteMonth", "false")
        else
            absoluteTimeElt.setAttribute("isAbsoluteMonth", pastscene.isAbsoluteMonth.toString())
        if (pastscene.isAbsoluteDay == null)
            absoluteTimeElt.setAttribute("isAbsoluteDay", "false")
        else
            absoluteTimeElt.setAttribute("isAbsoluteDay", pastscene.isAbsoluteDay.toString())
        if (pastscene.isAbsoluteHour == null)
            absoluteTimeElt.setAttribute("isAbsoluteHour", "false")
        else
            absoluteTimeElt.setAttribute("isAbsoluteHour", pastscene.isAbsoluteHour.toString())
        if (pastscene.isAbsoluteMinute == null)
            absoluteTimeElt.setAttribute("isAbsoluteMinute", "false")
        else
            absoluteTimeElt.setAttribute("isAbsoluteMinute", pastscene.isAbsoluteMinute.toString())

        timeElt.appendChild(absoluteTimeElt)

        return timeElt
    }
}
