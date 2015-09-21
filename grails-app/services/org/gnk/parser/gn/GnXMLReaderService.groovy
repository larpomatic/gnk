package org.gnk.parser.gn

import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.character.CharacterXMLReaderService
import org.gnk.roletoperso.Character
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag

import java.sql.Timestamp

class GnXMLReaderService {

    def serviceMethod() {

    }

    def ReadGnDTD(Node GNK, Gn gn, GNKDataContainerService dataContainer) {
        if (!(gn.dtd)) {
            return;
        }

        assert (GNK.GN_INFORMATION.size() <= 1)
        if (GNK.GN_INFORMATION.size() <= 0)
            return


        Node GN_INFORMATION = GNK.GN_INFORMATION[0]
        gn.name = GN_INFORMATION.attribute("title")
        if (GN_INFORMATION.attribute("creation_date") != "null")
            gn.dateCreated = new Date(new Timestamp(GN_INFORMATION.attribute("creation_date") as Long).getTime())
        if (GN_INFORMATION.attribute("last_update_date") != "null")
            gn.lastUpdated = new Date(new Timestamp(GN_INFORMATION.attribute("last_update_date") as Long).getTime())
        gn.isMainstream = (GN_INFORMATION.attribute("architecture") == "mainstream")
        if (GN_INFORMATION.attribute("t0_date") != "null")
            gn.t0Date = new Date(new Timestamp(GN_INFORMATION.attribute("t0_date") as Long).getTime())
        if (GN_INFORMATION.attribute("date") != "null")
            gn.date = new Date(new Timestamp(GN_INFORMATION.attribute("date") as Long).getTime())
        if (GN_INFORMATION.attribute("duration") != "null")
            gn.duration = GN_INFORMATION.attribute("duration") as Integer
        if (GN_INFORMATION.attribute("pip_min") != "null")
            gn.pipMin = GN_INFORMATION.attribute("pip_min") as Integer
        if (GN_INFORMATION.attribute("pip_max") != "null")
            gn.pipMax = GN_INFORMATION.attribute("pip_max") as Integer
        if (GN_INFORMATION.attribute("pip_core") != "null")
            gn.pipCore = GN_INFORMATION.attribute("pip_core") as Integer

        assert (GN_INFORMATION.AUTHORS.size() == 1)
        Node AUTHORS = GN_INFORMATION.AUTHORS[0]
        NodeList AUTHORLIST = AUTHORS.AUTHOR //FIXME

        assert (GN_INFORMATION.STEPS.size() == 1)
        Node STEPS = GN_INFORMATION.STEPS[0]
        gn.step = STEPS.attribute("last_step_id")

        if (STEPS.attribute("isLife") != null && STEPS.attribute("isLife") == "true")
            gn.isLife = true
        else
            gn.isLife = false

        assert (GN_INFORMATION.PLAYERS.size() == 1)
        Node PLAYERS = GN_INFORMATION.PLAYERS[0]
        if (PLAYERS.attribute("men") != "null")
            gn.nbMen = PLAYERS.attribute("men") as Integer
        if (PLAYERS.attribute("women") != "null")
            gn.nbWomen = PLAYERS.attribute("women") as Integer
        if (PLAYERS.attribute("undefined") != "null")
            gn.nbPlayers = (PLAYERS.attribute("undefined") as Integer) + gn.nbMen + gn.nbWomen

        assert (GN_INFORMATION.UNIVERSE.size() == 1)
        Node UNIVERSE = GN_INFORMATION.UNIVERSE[0]
        gn.univers = Tag.get(UNIVERSE.attribute("id") as Integer)

        assert (GN_INFORMATION.TAGS.size() == 1)
        Node TAGS = GN_INFORMATION.TAGS[0]
        assert (TAGS.TAGS_GN.size() == 1)
        NodeList TAGLIST = TAGS.TAGS_GN.TAG
        Map<Tag, Integer> gnTagsTmp = new HashMap<Tag, Integer>()
        for (int i = 0; i < TAGLIST.size(); i++) {
            Node TAG = TAGLIST.get(i)

            final String dbIdStr = TAG.attribute("db_id")
            Integer dbId
            if (dbIdStr != "null" && (dbIdStr as String).isInteger())
                dbId = dbIdStr as Integer
            final String weightStr = TAG.attribute("weight")
            Integer weight
            if (weightStr != "null" && (weightStr as String).isInteger())
                weight = weightStr as Integer
            assert (dbId != null)
            assert (weight != null)
            if (dbId == null || weight == null)
                continue
            Tag tag = Tag.get(dbId)
            assert (tag != null)
            if (tag == null)
                continue
            gnTagsTmp.put(tag, weight)
        }
        gn.gnTags = gnTagsTmp

        assert (TAGS.TAGS_MAINSTREAM.size() == 1)
        NodeList TAGS_MAINSTREAM = TAGS.TAGS_MAINSTREAM.TAG
        Map<Tag, Integer> mainstreamTagsTmp = new HashMap<Tag, Integer>()
        for (int i = 0; i < TAGS_MAINSTREAM.size(); i++) {
            Node TAG = TAGS_MAINSTREAM.get(i)
            mainstreamTagsTmp.put(Tag.get(TAG.attribute("db_id") as Integer), TAG.attribute("weight") as Integer)
        }
        gn.mainstreamTags = mainstreamTagsTmp

        assert (TAGS.TAGS_EVENT.size() == 1)
        NodeList TAGS_EVENT = TAGS.TAGS_EVENT.TAG
        Map<Tag, Integer> eventTagsTmp = new HashMap<Tag, Integer>()
        for (int i = 0; i < TAGS_EVENT.size(); i++) {
            Node TAG = TAGS_EVENT.get(i)
            eventTagsTmp.put(Tag.get(TAG.attribute("db_id") as Integer), TAG.attribute("weight") as Integer)
        }
        gn.evenementialTags = eventTagsTmp

        final GN_DEFINITIONLIST = GNK.GN_DEFINITION
        assert (GN_DEFINITIONLIST.size() == 1)
        if (GN_DEFINITIONLIST.size() > 0) {
            Node GN_DEFINITION = GN_DEFINITIONLIST[0]
            readGnDefinitionNode(GN_DEFINITION, dataContainer)
        }
    }

    private void readGnDefinitionNode(Node GN_DEFINITION, GNKDataContainerService dataContainer) {
        final NodeList PLOTSLIST = GN_DEFINITION.PLOTS
        assert (PLOTSLIST.size() == 1);
        if (PLOTSLIST.size() > 0) {
            Node PLOTS = PLOTSLIST[0]
            readSelectIntrigueNode(PLOTS, dataContainer)
        }

        final NodeList CHARACTERSLIST = GN_DEFINITION.CHARACTERS
        assert (CHARACTERSLIST.size() == 1)
        if (CHARACTERSLIST.size() > 0) {
            Node CHARACTERS = CHARACTERSLIST[0]
            readRoleToPersoNode(CHARACTERS, dataContainer)
        }
    }

    private void readRoleToPersoNode(Node CHARACTERS, GNKDataContainerService dataContainer) {
        final NodeList CHARACTERLIST = CHARACTERS.CHARACTER
        CharacterXMLReaderService characterReader = new CharacterXMLReaderService()
        for (int i = 0; i < CHARACTERLIST.size(); i++) {
            Node CHARACTER = CHARACTERLIST.get(i)
            Character character = characterReader.getCharacterFromNode(CHARACTER, dataContainer)

            assert (character != null)

            Set<Character> characterSet = dataContainer.gn.getCharacterSet()
            Set<Character> nonPlayerCharSet = dataContainer.gn.getterNonPlayerCharSet();
            Set<Character> stafCharSet = dataContainer.gn.getStaffCharSet();
            if (characterSet == null) {
                characterSet = new HashSet<Character>()
                dataContainer.gn.setCharacterSet(characterSet)
            }
            if (stafCharSet == null) {
                stafCharSet = new HashSet<Character>();
                dataContainer.gn.setStaffCharSet(stafCharSet)
            }
            if (character.isPJ())
                characterSet.add(character)
            else if (character.isSTF())
                stafCharSet.add(character)
            else
                nonPlayerCharSet.add(character)
        }
    }

    private void readSelectIntrigueNode(Node PLOTS, GNKDataContainerService dataContainer) {
        final NodeList BANNEDLIST = PLOTS.BANNED
        assert (BANNEDLIST.size() == 1)
        if (BANNEDLIST.size() > 0) {
            Node BANNED = BANNEDLIST[0]
            NodeList PLOTLIST = BANNED.PLOT
            for (int i = 0; i < PLOTLIST.size(); i++) {
                Node PLOT = PLOTLIST.get(i)
                final String plot_id = PLOT.attribute("plot_id")
                if (plot_id != "null" && (plot_id as String).isInteger()) {
                    Integer plotId = plot_id as Integer
                    if (dataContainer.gn.bannedPlotSet == null)
                        dataContainer.gn.bannedPlotSet = new HashSet<Plot>()
                    dataContainer.gn.bannedPlotSet.add(dataContainer.getPlot(plotId));
                }
            }
        }

        final NodeList LOCKEDLIST = PLOTS.LOCKED
        assert (LOCKEDLIST.size() == 1)
        if (LOCKEDLIST.size() > 0) {
            Node LOCKED = LOCKEDLIST[0]
            NodeList PLOTLIST = LOCKED.PLOT
            for (int i = 0; i < PLOTLIST.size(); i++) {
                Node PLOT = PLOTLIST.get(i)
                final String plot_id = PLOT.attribute("plot_id")
                if (plot_id != "null" && (plot_id as String).isInteger()) {
                    Integer plotId = plot_id as Integer
                    if (dataContainer.gn.lockedPlotSet == null)
                        dataContainer.gn.lockedPlotSet = new HashSet<Plot>()
                    dataContainer.gn.lockedPlotSet.add(dataContainer.getPlot(plotId));
                }
            }
        }

        final NodeList SELECTEDLIST = PLOTS.SELECTED
        assert (SELECTEDLIST.size() == 1)
        if (SELECTEDLIST.size() > 0) {
            Node SELECTED = SELECTEDLIST[0]
            NodeList PLOTLIST = SELECTED.PLOT
            for (int i = 0; i < PLOTLIST.size(); i++) {
                Node PLOT = PLOTLIST.get(i)
                final String plot_id = PLOT.attribute("plot_id")
                if (plot_id != "null" && (plot_id as String).isInteger()) {
                    Integer plotId = plot_id as Integer
                    if (dataContainer.gn.selectedPlotSet == null)
                        dataContainer.gn.selectedPlotSet = new HashSet<Plot>()
                    dataContainer.gn.selectedPlotSet.add(dataContainer.getPlot(plotId));
                }
            }
        }
    }
}
