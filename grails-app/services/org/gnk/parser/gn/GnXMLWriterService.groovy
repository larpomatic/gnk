package org.gnk.parser.gn

import org.gnk.gn.Gn
import org.gnk.gn.GnHasUser
import org.gnk.parser.plot.PlotXMLWriterService
import org.gnk.parser.character.CharacterXMLWriterService
import org.gnk.parser.naming.SubstitutionNamingXMLWriterService
import org.gnk.parser.resource.SubstitutionResourceXMLWriterService
import org.gnk.parser.place.SubstitutionPlaceXMLWriterService
import org.gnk.parser.time.SubstitutionTimeXMLWriterService
import org.gnk.roletoperso.Character
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag
import org.gnk.user.User
import org.w3c.dom.Document
import org.w3c.dom.Element

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class GnXMLWriterService {
    /* Exposed Methods */

    def String getGNKDTDString(Gn gn) {
        DOMSource domSource = new DOMSource(getGNKDTDDocument(gn));
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.newTransformer().transform(domSource, result);
        return writer.toString();
    }

    def Document getGNKDTDDocument(Gn gn) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element gnkElement = doc.createElement("GNK");
        gnkElement.setAttribute("dtd_version", "1.7");
        gnkElement.appendChild(getGnInformationElement(doc, gn));

        //GN_DEFINITION elements
        Element gnDefinition = doc.createElement("GN_DEFINITION");
        gnDefinition.appendChild(getSelectIntrigueElement(doc, gn))
        gnDefinition.appendChild(getRoleToPersoElement(doc, gn))
        gnDefinition.appendChild(getSubstitutionElement(doc, gn))
        gnkElement.appendChild(gnDefinition);

        //GN_DATA elements
        Element gnData = doc.createElement("GN_DATA");
        gnData.appendChild(getPlotsElement(doc, gn))
        // Naming
        SubstitutionNamingXMLWriterService namingXMLWriter = new SubstitutionNamingXMLWriterService()
        gnData.appendChild(namingXMLWriter.getFirstnamesDataElement(doc, gn.firstnameSet))
        gnData.appendChild(namingXMLWriter.getLastnamesDataElement(doc, gn.lastnameSet))
        // Resources
        SubstitutionResourceXMLWriterService substitutionResourceXMLWriterService = new SubstitutionResourceXMLWriterService()
        gnData.appendChild(substitutionResourceXMLWriterService.getResourcesDataElement(doc, gn.resourceSet))
        // Places
        SubstitutionPlaceXMLWriterService substitutionPlaceXMLWriterService = new SubstitutionPlaceXMLWriterService()
        gnData.appendChild(substitutionPlaceXMLWriterService.getPlacesDataElement(doc, gn.placeSet))

        gnkElement.appendChild(gnData);

        doc.appendChild(gnkElement);

        return doc;
    }
    /* !Exposed Methods */

    /* Construction Methods */

    private Element getGnInformationElement(Document doc, Gn gn) {
        Element gnElement = getGnInformationRootElement(doc, gn)

        // AUTHORS elements
        Element authors = getAuthorsElement(doc, gn);
        gnElement.appendChild(authors);

        // STEP elements
        Element steps = getStepsElement(doc, gn);
        gnElement.appendChild(steps);

        // PARTICIPANTS elements
        Element players = getPlayersElement(doc, gn);
        gnElement.appendChild(players);

        //UNIVERS elements
        Element universe = doc.createElement("UNIVERSE");
        universe.setAttribute("id", gn.univers.id.toString());
        gnElement.appendChild(universe);

        // TAGS elements
        Element tags = doc.createElement("TAGS");
        tags.appendChild(getTagsGnElement(doc, gn));
        tags.appendChild(getTagsMainstreamElement(doc, gn));
        tags.appendChild(getTagsEventElement(doc, gn));
        gnElement.appendChild(tags);

        return gnElement;
    }

    private Element getSelectIntrigueElement(Document doc, Gn gn) {
        Element selectIntrigueElement = doc.createElement("PLOTS");
        selectIntrigueElement.setAttribute("step_id", "1");

        Element bannedElt = doc.createElement("BANNED");
        for (Plot bannedPlot : gn.bannedPlotSet) {
            Element plotElt = doc.createElement("PLOT");
            Integer plotId = bannedPlot.getDTDId()
            if (plotId == null || plotId < 0)
                plotId = bannedPlot.getId()
            plotElt.setAttribute("plot_id", plotId.toString());
            bannedElt.appendChild(plotElt)
        }
        selectIntrigueElement.appendChild(bannedElt);

        Element lockedElt = doc.createElement("LOCKED");
        for (Plot lockedPlot : gn.lockedPlotSet) {
            Element plotElt = doc.createElement("PLOT");
            Integer plotId = lockedPlot.getDTDId()
            if (plotId == null || plotId < 0)
                plotId = lockedPlot.getId()
            plotElt.setAttribute("plot_id", plotId.toString());
            lockedElt.appendChild(plotElt)
        }
        selectIntrigueElement.appendChild(lockedElt);

        Element selectedElt = doc.createElement("SELECTED");
        for (Plot selectedPlot : gn.selectedPlotSet) {
            Element plotElt = doc.createElement("PLOT");
            Integer plotId = selectedPlot.getDTDId()
            if (plotId == null || plotId < 0)
                plotId = selectedPlot.getId()
            plotElt.setAttribute("plot_id", plotId.toString());
            selectedElt.appendChild(plotElt)
        }
        selectIntrigueElement.appendChild(selectedElt);

        return selectIntrigueElement;
    }

    private Element getRoleToPersoElement(Document doc, Gn gn) {
        Element roleToPersoElement = doc.createElement("CHARACTERS");
        roleToPersoElement.setAttribute("step_id", "2");


        final Set<Character> characterSet = gn.getCharacterSet()
        CharacterXMLWriterService characterWriter = new CharacterXMLWriterService()
        if (characterSet != null) {
            for (Character character : characterSet) {
                Element characterElt = characterWriter.getCharacterElement(doc, character)
                roleToPersoElement.appendChild(characterElt)
            }
        }
        for (Character character : gn.getterNonPlayerCharSet()) {
            Element characterElt = characterWriter.getCharacterElement(doc, character)
            roleToPersoElement.appendChild(characterElt)
        }

        return roleToPersoElement;
    }

    private Element getPlotsElement(Document doc, Gn gn) {
        Element plotsElement = doc.createElement("PLOTS");
        plotsElement.setAttribute("step_id", "1");
        PlotXMLWriterService plotWriter = new PlotXMLWriterService();
        for (Plot bannedPlot : gn.bannedPlotSet) {
            plotsElement.appendChild(plotWriter.getPlotElement(doc, bannedPlot));
        }
        for (Plot selectedPlot : gn.selectedPlotSet) {
            plotsElement.appendChild(plotWriter.getPlotElement(doc, selectedPlot));
        }
        //No lockedPlot because they are in selectedPlot
        return plotsElement
    }

    private Element getGnInformationRootElement(Document doc, Gn gn) {
        Element gnElement = doc.createElement("GN_INFORMATION");

        Date now = new Date();

        gnElement.setAttribute("title", gn.name);
        gnElement.setAttribute("creation_date", gn.dateCreated == null ? now.time.toString() : gn.dateCreated?.time.toString());
        gnElement.setAttribute("last_update_date", gn.lastUpdated == null ? now.time.toString() : gn.lastUpdated?.time.toString());
        gnElement.setAttribute("architecture", gn.isMainstream ? "mainstream" : "parallelized");
        gnElement.setAttribute("t0_date", gn.t0Date?.time.toString());
        gnElement.setAttribute("duration", gn.duration.toString());
        gnElement.setAttribute("pip_min", gn.pipMin.toString());
        gnElement.setAttribute("pip_max", gn.pipMax.toString());
        gnElement.setAttribute("pip_core", gn.pipCore.toString());

        return gnElement;
    }

    private Element getAuthorsElement(Document doc, Gn gn) {
        Element authors = doc.createElement("AUTHORS");

        if (gn.gnHasUsers) {
            Integer i = 0;
            for (GnHasUser authorP : gn.gnHasUsers) {
                User author = authorP.user;
                Element authorElement = doc.createElement("AUTHOR");
                authorElement.setAttribute("id", i.toString());
                authorElement.setAttribute("db_id", author.id);
                authorElement.setAttribute("last_edit_date", authorP.lastUpdated);
                authorElement.setAttribute("is_creator", authorP.isCreator);
                authors.appendChild(authorElement);
                i++;
            }
        }
        return authors;
    }

    private Element getStepsElement(Document doc, Gn gn) {
        Element steps = doc.createElement("STEPS");
        steps.setAttribute("last_step_id", gn.step);

        Element stepElement = doc.createElement("STEP");
        stepElement.setAttribute("id", "0");
        stepElement.setAttribute("id_author", "0"); // FIXME
        stepElement.setAttribute("module", "ecran_0");
        stepElement.setAttribute("version", "0.1");
        stepElement.setAttribute("date", (new Date()).time.toString());
        stepElement.setAttribute("is_active", "true");
        steps.appendChild(stepElement);

        return steps;
    }

    private Element getPlayersElement(Document doc, Gn gn) {
        Element players = doc.createElement("PLAYERS");

        players.setAttribute("men", gn.nbMen.toString());
        players.setAttribute("women", gn.nbWomen.toString());
        players.setAttribute("undefined", (gn.nbPlayers - (gn.nbMen + gn.nbWomen)).toString());

        return players
    }

    private Element getTagsGnElement(Document doc, Gn gn) {
        Element tagsGn = doc.createElement("TAGS_GN");

        if (gn.gnTags) {
            for (Tag tagK : gn.gnTags.keySet()) {
                tagsGn.appendChild(getGnTagElement(doc, tagK, gn.gnTags.get(tagK)))
            }
        }

        return tagsGn;
    }

    private Element getTagsMainstreamElement(Document doc, Gn gn) {
        Element tagsMainstream = doc.createElement("TAGS_MAINSTREAM");

        if (gn.mainstreamTags) {
            for (Tag tagK : gn.mainstreamTags.keySet()) {
                tagsMainstream.appendChild(getGnTagElement(doc, tagK, gn.mainstreamTags.get(tagK)))
            }
        }
        return tagsMainstream;
    }

    private Element getTagsEventElement(Document doc, Gn gn) {
        Element tagsEvent = doc.createElement("TAGS_EVENT");

        if (gn.evenementialTags) {
            for (Tag tagK : gn.evenementialTags.keySet()) {
                tagsEvent.appendChild(getGnTagElement(doc, tagK, gn.evenementialTags.get(tagK)))
            }
        }

        return tagsEvent;
    }

    private Element getGnTagElement(Document doc, Tag tag, Integer weight) {
        Element tagE = doc.createElement("TAG");

        tagE.setAttribute("db_id", tag.id.toString())
        tagE.setAttribute("value", tag.name)
        tagE.setAttribute("type", tag.tagFamily.value)
        tagE.setAttribute("weight", weight.toString())
        tagE.setAttribute("status", "")

        return tagE
    }

    private Element getSubstitutionElement(Document doc, Gn gn) {
        Element substitutionE = doc.createElement("SUBSTITUTION")

        // Naming
        substitutionE.setAttribute("step_id", "3")
        SubstitutionNamingXMLWriterService namingXMLWriter = new SubstitutionNamingXMLWriterService()
        substitutionE.appendChild(namingXMLWriter.getNamingElement(doc, gn.characterSet, gn.nonPlayerCharSet))

        // Resources
        SubstitutionResourceXMLWriterService substitutionResourceXMLWriterService = new SubstitutionResourceXMLWriterService()
        substitutionE.appendChild(substitutionResourceXMLWriterService.getResourcesElement(doc, gn.selectedPlotSet))

        // Places
        SubstitutionPlaceXMLWriterService substitutionPlaceXMLWriterService = new SubstitutionPlaceXMLWriterService()
        substitutionE.appendChild(substitutionPlaceXMLWriterService.getPlacesElement(doc, gn.selectedPlotSet))

        // Time
        SubstitutionTimeXMLWriterService substitutionTimeXMLWriterService = new SubstitutionTimeXMLWriterService()
        substitutionE.appendChild(substitutionTimeXMLWriterService.getTimeElement(doc, gn.selectedPlotSet))

        return substitutionE
    }
    /* Construction Methods */
}
