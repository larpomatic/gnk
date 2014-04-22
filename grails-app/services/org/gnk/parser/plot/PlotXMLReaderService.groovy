package org.gnk.parser.plot

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.event.EventXMLReaderService
import org.gnk.parser.pastscene.PastSceneXMLReaderService
import org.gnk.parser.place.GenericPlaceXMLReaderService
import org.gnk.parser.resource.GenericResourceXMLReaderService
import org.gnk.parser.textualclue.GenericTextualClueXMLReaderService
import org.gnk.parser.role.RoleXMLReaderService
import org.gnk.parser.tag.TagXMLReaderService
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericTextualClue
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.Role
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.gnk.user.User

import java.sql.Timestamp

class PlotXMLReaderService {

    def serviceMethod() {
    }

    /* Exposed Methods */
    def Plot getPlotFromNode(Node PLOT, GNKDataContainerService dataContainer) {
        // PLOT reader
        Plot plotRes = ReadPlotRootNode(PLOT, dataContainer)

        plotRes.version = dataContainer.gn.version
        plotRes.isDraft = true

        // DESCRIPTION reader
        if (PLOT.DESCRIPTION.size() > 0) {
            plotRes.description =  PLOT.DESCRIPTION[0].text()
        }

        // GENERIC_PLACES reader
        ReadGenericPlacesNode(PLOT, plotRes, dataContainer)
        // PAST_SCENES reader
        ReadPastScenesNode(PLOT, plotRes, dataContainer)
        // GENERIC_RESOURCES reader
        ReadGenericResourcesNode(PLOT, plotRes, dataContainer)
        // TODO: TEXTUAL_CLUES reader
        // ReadTextualCluesNode(PLOT, plotRes, dataContainer)
        // EVENTS reader
        ReadEventsNode(PLOT, plotRes, dataContainer)

        // TAGS reader
        ReadTagsNode(PLOT, plotRes, dataContainer)

        // ROLES reader
        ReadRolesNode(PLOT, plotRes, dataContainer)

        return plotRes
    }

/* !Exposed Methods */

    /* Construction Methods */
    private Plot ReadPlotRootNode (Node PLOT, GNKDataContainerService dataContainer) {
        Plot plotRes = null
        String plotTitle = null

        if (PLOT.attribute("title") != "null") {
            plotTitle = PLOT.attribute("title")
            //Plot.findByName(plotTitle)
        }

        if (plotRes == null)
            plotRes = new Plot(name: plotTitle)

        if (PLOT.attribute("id") != "null" && (PLOT.attribute("id") as String).isInteger())
            plotRes.DTDId = PLOT.attribute("id") as Integer
        if (PLOT.attribute("is_evenemential") != "null")
            plotRes.isEvenemential = PLOT.attribute("is_evenemential") as Boolean
        if (PLOT.attribute("is_mainstream") != "null")
            plotRes.isMainstream = PLOT.attribute("is_mainstream") as Boolean
        if (PLOT.attribute("is_public") != "null")
            plotRes.isPublic = PLOT.attribute("is_public") as Boolean
        if (PLOT.attribute("author") != "null")
            plotRes.user = User.findWhere(id: PLOT.attribute("author") as Integer)
        if (PLOT.attribute("creation_date") != "null")
            plotRes.creationDate = new Date(new Timestamp(PLOT.attribute("creation_date") as Long).getTime())
        if (PLOT.attribute("last_update_date") != "null")
            plotRes.updatedDate = new Date(new Timestamp(PLOT.attribute("last_update_date") as Long).getTime())

        return plotRes
    }

    private void ReadTagsNode(Node PLOT, Plot plotRes, GNKDataContainerService dataContainer) {
        assert (PLOT.TAGS.size() == 1)
        Node TAGS = PLOT.TAGS[0]
        NodeList TAGLIST = TAGS.TAG

        TagXMLReaderService tagReader = new TagXMLReaderService()
        for (int i = 0; i < TAGLIST.size(); i++)
        {
            Node TAG = TAGLIST.get(i)

            PlotHasTag plotHasTag = new PlotHasTag(plotRes, tagReader.getTagFromNode(TAG, dataContainer), tagReader.getTagWeight(TAG, dataContainer))
            plotRes.addToExtTags(plotHasTag)
        }
    }

    private void ReadRolesNode(Node PLOT, Plot plotRes, GNKDataContainerService dataContainer) {
        assert (PLOT.ROLES.size() <= 1)
        if (PLOT.ROLES.size() <= 0)
            return

        Node ROLES = PLOT.ROLES[0]
        NodeList ROLELIST = ROLES.ROLE

        RoleXMLReaderService roleReader = new RoleXMLReaderService()
        // add each role to plot roles.
        roleReader.getRolesFromNode(ROLES, plotRes, dataContainer).each {Role role ->
            plotRes.addARole(role)
        };

    }

    private void ReadGenericPlacesNode(Node PLOT, Plot plotRes, GNKDataContainerService dataContainer) {
        assert (PLOT.GENERIC_PLACES.size() <= 1)
        if (PLOT.GENERIC_PLACES.size() <= 0)
            return

        GenericPlaceXMLReaderService genericPlaceReader = new GenericPlaceXMLReaderService()

        NodeList genericPlaceList = PLOT.GENERIC_PLACES[0].GENERIC_PLACE

        genericPlaceList.each { Node GENERIC_PLACE ->
            GenericPlace genericPlace = genericPlaceReader.getGenericPlaceFromNode(GENERIC_PLACE, dataContainer)
            if (genericPlace.getDTDId() >= 0)
                dataContainer.genericPlaceMap.put(genericPlace.getDTDId(), genericPlace)
        }
    }

    private void ReadPastScenesNode(Node PLOT, Plot plotRes, GNKDataContainerService dataContainer) {
            assert (PLOT.PAST_SCENES.size() <= 1)
        if (PLOT.PAST_SCENES.size() <= 0)
            return

        PastSceneXMLReaderService pastReader = new PastSceneXMLReaderService()

        // add each pastscenes to plotres pastscenes.
        pastReader.getPastScenesFromNode(PLOT.PAST_SCENES[0], plotRes, dataContainer).each {Pastscene pastRes ->
            plotRes.addToPastescenes(pastRes)
        };
    }

    private void ReadGenericResourcesNode(Node PLOT, Plot plotRes, GNKDataContainerService dataContainer) {
        assert (PLOT.GENERIC_RESOURCES.size() <= 1)
        if (PLOT.GENERIC_RESOURCES.size() <= 0)
            return

        GenericResourceXMLReaderService genericResourceReader = new GenericResourceXMLReaderService()

        NodeList genericResourceList = PLOT.GENERIC_RESOURCES[0].GENERIC_RESOURCE

        genericResourceList.each { Node GENERIC_RESOURCE ->
            GenericResource genericResource = genericResourceReader.getGenericResourceFromNode(GENERIC_RESOURCE, dataContainer)
            genericResource.plot = plotRes;
            if (genericResource.getDTDId() >= 0)
                dataContainer.genericResourceMap.put(genericResource.getDTDId(), genericResource)
        }
    }

    private void ReadTextualCluesNode(Node PLOT, Plot plotRes, GNKDataContainerService dataContainer) {
        assert (PLOT.TEXTUAL_CLUES.size() <= 1)
        if (PLOT.TEXTUAL_CLUES.size() <= 0)
            return

        GenericTextualClueXMLReaderService textualClueReader = new GenericTextualClueXMLReaderService()

        if (!PLOT.GENERIC_RESOURCES[0])
            return
        NodeList textualClueList = PLOT.GENERIC_RESOURCES[0].GENERIC_RESOURCE

        textualClueList.each { Node GENERIC_RESOURCE ->
            GenericTextualClue textualClue = textualClueReader.getTextualClueFromNode(GENERIC_RESOURCE, dataContainer)
            if (textualClue.getDTDId() >= 0)
                dataContainer.genericTextualClueMap.put(textualClue.getDTDId(), textualClue)
        }
    }

    private void ReadEventsNode(Node PLOT, Plot plotRes, GNKDataContainerService dataContainer) {
        assert (PLOT.EVENTS.size() <= 1)
        if (PLOT.EVENTS.size() <= 0)
            return

        EventXMLReaderService eventXMLReader = new EventXMLReaderService()

        eventXMLReader.getEventsFromNode(PLOT.EVENTS[0], plotRes, dataContainer).each { Event eventRes ->
            plotRes.addToEvents(eventRes)
        }
    }
    /* !Construction Methods */
}
