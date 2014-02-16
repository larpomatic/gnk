package org.gnk.parser.pastscene

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.PastSceneXMLNode
import org.gnk.parser.place.GenericPlaceXMLReaderService
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.selectintrigue.Plot

class PastSceneXMLReaderService {

    /* Exposed Methods */

    public List<Pastscene> getPastScenesFromNode(Node PAST_SCENES, Plot plot, GNKDataContainerService dataContainer) {
        NodeList PASTSCENELIST = PAST_SCENES.PAST_SCENE
        HashMap<Integer, PastSceneXMLNode> pastsceneNodes = new HashMap<Integer, PastSceneXMLNode>()

        for (int i = 0; i < PASTSCENELIST.size(); i++) {
            Node PAST_SCENE = PASTSCENELIST.get(i)

            PastSceneXMLNode pastRes = getPastSceneXMLNode(PAST_SCENE, plot, dataContainer)
            if (pastRes != null) {
                pastsceneNodes.put(pastRes.pastscene.DTDId, pastRes);
            }
        }

        //parse pastsceneNodes
        List<Pastscene> pastscenes = new ArrayList<>()

        pastsceneNodes.each { DTDId, pastSceneXMLNode ->
            // FIXME: add many predecessors
            // add predecessor object to pastscene
            if (pastSceneXMLNode.predecessorIds.size() > 0) {
                PastSceneXMLNode pred = pastsceneNodes.get(pastSceneXMLNode.predecessorIds[0])
                pastSceneXMLNode.pastscene.pastscenePredecessor = pred.pastscene
            }
            pastscenes.add(pastSceneXMLNode.pastscene)
        }

        return pastscenes
    }

    def PastSceneXMLNode getPastSceneXMLNode(Node PASTSCENE, Plot plot, GNKDataContainerService dataContainer) {
        PastSceneXMLNode pastSceneXMLNode = new PastSceneXMLNode();

        // PASTSCENE reader
        pastSceneXMLNode.pastscene = ReadPastsceneRootNode(PASTSCENE, plot)

        // TITLE reader
        ReadTitleNode(PASTSCENE, pastSceneXMLNode.pastscene)

        // DESCRIPTION reader
        ReadDescriptionNode(PASTSCENE, pastSceneXMLNode.pastscene)

        // PREDECESSORS reader
        ReadPredecessorsNode(PASTSCENE, pastSceneXMLNode, dataContainer)

        // GENERIC_PLACES reader
        ReadGenericplacesNode(PASTSCENE, pastSceneXMLNode.pastscene, dataContainer)

        // TIME reader
        ReadTimeNode(PASTSCENE, pastSceneXMLNode.pastscene, dataContainer)

        return pastSceneXMLNode
    }

    public RoleHasPastscene GetRoleHasPastsceneFromRootNode(Node PASTSCENE, Plot plot) {
        RoleHasPastscene roleHasPastScene = new RoleHasPastscene()

        if (PASTSCENE.attribute("past_scene_id") != "null" && (PASTSCENE.attribute("past_scene_id") as String).isInteger()) {
            Integer pastSceneDTDId = PASTSCENE.attribute("past_scene_id") as Integer

            plot.getPastescenes().each { pastscene ->
                if (pastscene.getDTDId() == pastSceneDTDId)
                    roleHasPastScene.pastscene = pastscene
            }
            assert (roleHasPastScene.pastscene != null);
            if (roleHasPastScene.pastscene == null)
                return null;
        } else {
            assert (false);
            return null;
        }

        if (PASTSCENE.TITLE.size() > 0)
            roleHasPastScene.title = PASTSCENE.TITLE[0].text()

        if (PASTSCENE.DESCRIPTION.size() > 0)
            roleHasPastScene.description = PASTSCENE.DESCRIPTION[0].text()

        return roleHasPastScene
    }
    /* !Exposed Methods */

    /* Construction Methods */

    private Pastscene ReadPastsceneRootNode(Node PASTSCENE, Plot plot) {
        Pastscene pastscene = null

        if (pastscene == null)
            pastscene = new Pastscene()

        if (PASTSCENE.attribute("id") != "null" && (PASTSCENE.attribute("id") as String).isInteger())
            pastscene.DTDId = PASTSCENE.attribute("id") as Integer
        if (PASTSCENE.attribute("is_public") != "null")
            pastscene.isPublic = PASTSCENE.attribute("is_public") as Boolean

        return pastscene
    }

    private void ReadTitleNode(Node PASTSCENE, Pastscene pastsceneRes) {
        assert (PASTSCENE.TITLE.size() <= 1)
        if (PASTSCENE.TITLE.size() > 0) {
            if (pastsceneRes.title != null)
                pastsceneRes.title += PASTSCENE.TITLE[0].text()
            else
                pastsceneRes.title = PASTSCENE.TITLE[0].text()
        }
    }

    private void ReadDescriptionNode(Node PASTSCENE, Pastscene pastsceneRes) {
        assert (PASTSCENE.DESCRIPTION.size() <= 1)
        if (PASTSCENE.DESCRIPTION.size() > 0) {
            if (pastsceneRes.description != null)
                pastsceneRes.description += PASTSCENE.DESCRIPTION[0].text()
            else
                pastsceneRes.description = PASTSCENE.DESCRIPTION[0].text()
        }
    }

    private void ReadPredecessorsNode(Node PASTSCENE, PastSceneXMLNode pastSceneXMLNode, GNKDataContainerService dataContainer) {
        if (PASTSCENE.PREDECESSORS.size() <= 0)
            return;

        assert (PASTSCENE.PREDECESSORS.size() <= 1)
        if (PASTSCENE.PREDECESSORS.size() <= 0)
            return;

        Node PREDECESSORS = PASTSCENE.PREDECESSORS[0]
        NodeList PREDECESSORLIST = PREDECESSORS.PREDECESSOR

        for (int i = 0; i < PREDECESSORLIST.size(); i++) {
            Node PREDECESSOR = PREDECESSORLIST.get(i)

            // FIXME add many predecessors
            // créé un nouveau PastScene si l'identifiant est renseigné sinon le PastScene n'existe pas.
            if (PREDECESSOR.attribute("past_scene_id") != "null")
                pastSceneXMLNode.predecessorIds.add(PREDECESSOR.attribute("past_scene_id") as Integer)
        }
    }

    private void ReadGenericplacesNode(Node PASTSCENE, Pastscene pastsceneRes, GNKDataContainerService dataContainer) {
        assert (PASTSCENE.GENERIC_PLACES.size() <= 1)
        if (PASTSCENE.GENERIC_PLACES.size() <= 0)
            return;
        Node GENERIC_PLACES = PASTSCENE.GENERIC_PLACES[0]

        assert (GENERIC_PLACES.GENERIC_PLACE.size() <= 1)
        if (GENERIC_PLACES.GENERIC_PLACE.size() <= 0)
            return;
        Node GENERIC_PLACE = GENERIC_PLACES.GENERIC_PLACE[0]

        GenericPlaceXMLReaderService genericPlaceReader = new GenericPlaceXMLReaderService()

        if (GENERIC_PLACE.attribute("generic_place_id") == null || !(GENERIC_PLACE.attribute("generic_place_id") as String).isInteger())
            return

        Integer genericPlaceDTDId = GENERIC_PLACE.attribute("generic_place_id") as Integer
        pastsceneRes.setGenericPlace(dataContainer.genericPlaceMap.get(genericPlaceDTDId))

        // assert on genericPlace not found
        //   assert (pastsceneRes.genericPlace != null)
        if (pastsceneRes.genericPlace != null)
            pastsceneRes.genericPlace.addToPastscenes(pastsceneRes)
        else
            log.error("genericPlace is not found in pastScene " + pastsceneRes.getDTDId() + (pastsceneRes.plot == null ? "" : " in plot " + pastsceneRes.plot.getDTDId()))
    }

    private void ReadTimeNode(Node PASTSCENE, Pastscene pastsceneRes, GNKDataContainerService dataContainer) {
        assert (PASTSCENE.TIME.size() <= 1)
        if (PASTSCENE.TIME.size() <= 0)
            return;
        Node TIME = PASTSCENE.TIME[0]

        assert (TIME.RELATIVE.size() <= 1)
        assert (TIME.ABSOLUTE.size() <= 1)
        if (TIME.RELATIVE.size() == 1) {
            Node RELATIVE = TIME.RELATIVE[0]
            if (RELATIVE.attribute("time") != "null" && RELATIVE.attribute("time_unit") != "null") {
                if ((RELATIVE.attribute("time") as String).isInteger())
                    pastsceneRes.timingRelative = RELATIVE.attribute("time") as Integer
                pastsceneRes.unitTimingRelative = RELATIVE.attribute("time_unit") as String
            }
        }
        if (TIME.ABSOLUTE.size() == 1) {
            Node ABSOLUTE = TIME.ABSOLUTE[0]
            if (ABSOLUTE.attribute("year") != "null" && (ABSOLUTE.attribute("year") as String).isInteger())
                pastsceneRes.dateYear = ABSOLUTE.attribute("year") as Integer
            if (ABSOLUTE.attribute("month") != "null" && (ABSOLUTE.attribute("month") as String).isInteger())
                pastsceneRes.dateMonth = ABSOLUTE.attribute("month") as Integer
            if (ABSOLUTE.attribute("day") != "null" && (ABSOLUTE.attribute("day") as String).isInteger())
                pastsceneRes.dateDay = ABSOLUTE.attribute("day") as Integer
            if (ABSOLUTE.attribute("hour") != "null" && (ABSOLUTE.attribute("hour") as String).isInteger())
                pastsceneRes.dateHour = ABSOLUTE.attribute("hour") as Integer
            if (ABSOLUTE.attribute("min") != "null" && (ABSOLUTE.attribute("min") as String) != null && (ABSOLUTE.attribute("min") as String).isInteger())
                pastsceneRes.dateMinute = ABSOLUTE.attribute("min") as Integer
        }
    }
    /* !Construction Methods */
}
