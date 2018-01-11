package org.gnk.naming

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.PlaceResourceService
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Graph
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasTag
import org.gnk.selectintrigue.Plot
import org.gnk.substitution.InputHandler
import org.gnk.substitution.IntegrationHandler
import org.gnk.substitution.OutputHandler
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.Resource
import org.gnk.tag.Tag
import org.gnk.utils.Pair

class NamingController {

    NamingService namingService;
    JSONObject json;

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [NameInstanceList: Name.list(params), NameInstanceTotal: Name.count()]
    }

    def create() {
        [NameInstance: new Name(params)]
    }

    static private OutputHandler outputHandler

    def bestFirstName()
    {
        Firstname nam = new Firstname();
        Set<RoleHasTag> tags = new ArrayList<>();

        params.each {
            if (it.key.startsWith("firstnameTags_")) {
                FirstnameHasTag subtag = new Firstname();
                Tag tag = Tag.get((it.key - "firstnameTags_") as Integer);
                if (tag.parent != null) {
                    subtag.tag = tag;
                    subtag.weight = params.get("firstnameTagsWeight_" + tag.id) as Integer;
                    //subtag.type = tag.parent.name;
                    tags.add(subtag);
                }
            }
        }
        //gp.setTagList(tags);

        NamingService namingservice = new NamingService();
        Tag tagUnivers = new Tag();
        tagUnivers = Tag.findById("33089" as Integer);
        ArrayList<Tag> universList = Tag.findAllByParent(tagUnivers);

        json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        if (params.containsKey("plotId")) {
            Plot plot = Plot.get(params.plotId as Integer)
            nam.plotId = plot.id
            nam.resultsAllUniverses = namingservice.findBestObjectsForAllUnivers(rol, plot)
            if (gp.resultsAllUniverses.empty)
                throw (NullPointerException)
            for (Pair<Tag, ArrayList<Pair<ReferentialObject, Integer>>> ref in gp.resultsAllUniverses) {
                int i = 0;
                while (i != 3) {
                    jsonArray.add(ref.left.getName());
                    json.put(ref.left.name, jsonArray)
                    jsonArray = [];
                    i++;
                }
            }
        }

        render(contentType: "application/json") {
            object([json: json])
        }
    }

    def bestName()
    {
        Name naming = new Name();
        Set<RoleHasTag> tags = new ArrayList<>();

        params.each {
            if (it.key.startsWith("nameTags_")) {
                NameHasTag subtag = new Name();
                Tag tag = Tag.get((it.key - "nameTags_") as Integer);
                if (tag.parent != null) {
                    subtag.tag = tag;
                    subtag.weight = params.get("nameTagsWeight_" + tag.id) as Integer;
                    //subtag.type = tag.parent.name;
                    tags.add(subtag);
                }
            }
        }
        //gp.setTagList(tags);

        NamingService namingservice = new NamingService();
        Tag tagUnivers = new Tag();
        tagUnivers = Tag.findById("33089" as Integer);
        ArrayList<Tag> universList = Tag.findAllByParent(tagUnivers);

        json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        if (params.containsKey("plotId")) {
            Plot plot = Plot.get(params.plotId as Integer)
            naming.plotId = plot.id
            naming.resultsAllUniverses = namingservice.findBestObjectsForAllUnivers(naming, plot)
            if (gp.resultsAllUniverses.empty)
                throw (NullPointerException)
            for (Pair<Tag, ArrayList<Pair<ReferentialObject, Integer>>> ref in gp.resultsAllUniverses) {
                int i = 0;
                while (i != 3) {
                    jsonArray.add(ref.left.getName());
                    json.put(ref.left.name, jsonArray)
                    jsonArray = [];
                    i++;
                }
            }
        }

        render(contentType: "application/json") {
            object([json: json])
        }
    }

    def JSONArray buildTagList(def roleTagList) {
        JSONArray jsonTagList = new JSONArray();
        for (Tag roleTag in roleTagList) {
            JSONObject jsonTag = new JSONObject();
            jsonTag.put("id", roleTag.getId());
            jsonTag.put("name", roleTag.getName());
            if (roleTag.children && roleTag.children.size() != 0) {
                JSONArray jsonTagChildren = buildTagList(roleTag.children);
                jsonTag.put("children", jsonTagChildren);
            }
            jsonTagList.add(jsonTag);
        }
        return jsonTagList;
    }

    def index() {

        InputHandler inputHandler = new InputHandler()
        final gnIdStr = params.gnId
        if (gnIdStr == null) {
            gnIdStr = session.getAttribute("gnId")
        }
        if (gnIdStr == null || !(gnIdStr as String).isInteger()) {
            //redirect(action: "list", controller: "selectIntrigue", params: params)
            //return
            String fileContent = new File(xmlGnTestPath).text
            inputHandler.parseGN(fileContent)
        } else {
            Integer gnDbId = gnIdStr as Integer;
            List<String> sexes = params.sexe;
            //Gn gn = changeCharSex(gnDbId, sexes);
            Gn gn = Gn.get(gnDbId)
            //gn = changeCharSex(gn, sexes);
            inputHandler.parseGN(gn, sexes);

            /*render(text: gn.getDtd(), contentType: "text/xml", encoding: "UTF-8")
            return*/
        }

        Gn gn = Gn.get(gnIdStr as Integer)

        final gnData = new GNKDataContainerService()
        gnData.ReadDTD(gn)

        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "naming";
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)

        gn.save(flush: true);
        //RelationshipGraphService graphservice = new RelationshipGraphService();
        //String json = graphservice.create_graph(gn);

        Graph graph = new Graph(gn)
        String json = graph.buildGlobalGraphJSON();

        session.setAttribute("placeList", inputHandler.placeList)
        //test
        Gn gnInfo = inputHandler.gnInfo
        List<Character> characterList = inputHandler.characterList
        List<Resource> resourceList = inputHandler.resourceList
        List<Place> placeList = inputHandler.placeList
        List<Pastscene> pastsceneList = inputHandler.pastsceneList
        List<Event> eventList = inputHandler.eventList
        Map<String, Place> gnPlaceConstantMap = inputHandler.gnPlaceConstantMap

        [gnInfo: gnInfo,
         characterList: characterList,
         resourceList: resourceList,
         placeList: placeList,
         pastsceneList: pastsceneList,
         eventList: eventList,
         relationjson: json,
         gnId: gnIdStr,
         sexe: params.sexe
        ]
    }

    def getBack(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()

        if (gn.isLife)
            gn.step = "life";
        else
            gn.step = "role2perso";

        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
        Integer evenementialId = 0;
        Integer mainstreamId = 0;
        for (Plot plot in gn.selectedPlotSet) {
            if (plot.isEvenemential) {
                evenementialId = Plot.findByName(plot.name).id;
            } else if (plot.isMainstream && gn.isMainstream) {
                mainstreamId = Plot.findByName(plot.name).id; ;
            }
        }
        if (gn.isLife)
            redirect(controller: 'life', action: 'life', params: [gnId: id as String]);
        else
            redirect(controller: 'roleToPerso', action: 'roleToPerso', params: [gnId: id as String,
                                                                                selectedMainstream: mainstreamId as String,
                                                                                selectedEvenemential: evenementialId as String]);
    }

    def getSubCharacters() {
        JSONObject charJSONObject = request.JSON

        IntegrationHandler integrationHandler = new IntegrationHandler()
        charJSONObject = integrationHandler.namingIntegration(charJSONObject)

        render charJSONObject
    }

    def validateNaming() {
        JSONObject subJSON = JSON.parse(params.subJSON)

        Integer gnDbId = subJSON.gnDbId as Integer

        // Reader
        GNKDataContainerService gnkDataContainerService = new GNKDataContainerService()
        if (gnDbId == -1) {
            String fileContent = new File(xmlGnTestPath).text
            gnkDataContainerService.ReadDTD(fileContent)
        } else {
            gnkDataContainerService.ReadDTD(Gn.get(gnDbId))
        }

        // Output Substitution
        outputHandler = new OutputHandler()
        // Characters
        JSONArray charsJSONArray = subJSON.subCharacter
        Gn gn = Gn.get(gnDbId)
        gn.setCharArray(charsJSONArray)
        gn.save(flush: true, failOnError: true);

        // Writer
        GnXMLWriterService gnXMLWriter = new GnXMLWriterService()
//        gnkDataContainerService.gn.selectedPlotSet.each {
//            it.pastescenes.each {
//                it.isAbsoluteHour = true
//                it.isAbsoluteMinute = true
//                it.isAbsoluteDay = true
//                it.isAbsoluteMonth = true
//                it.isAbsoluteYear = true
//            }
//        }
        String xmlGN = gnXMLWriter.getGNKDTDString(gnkDataContainerService.gn)

        if (gnDbId == -1) {
            render(text: xmlGN, contentType: "text/xml", encoding: "UTF-8")
        } else {
            //render(text: xmlGN, contentType: "text/xml", encoding: "UTF-8")
            //return
            // Save in DataBase
            gnkDataContainerService.gn.dtd = xmlGN;
            //gnkDataContainerService.SaveDTD(gnkDataContainerService.gn)
            //line below can be commented to go back to substitution step when leaving the GN creation during publication
            gnkDataContainerService.gn.dtd = gnkDataContainerService.gn.dtd.replace("<STEPS last_step_id=\"substitution\">", "<STEPS last_step_id=\"publication\">");
            if (!gnkDataContainerService.gn.save(flush: true)) {
                redirect(action: "list", controller: "selectIntrigue", params: [gnId: gnDbId])
                return
            }
            // Go to Ressource
            redirect(controller: "ressource", action: "index", params: [gnId: gnDbId])

        }
    }

    static OutputHandler getOutputHandler()
    {
        return outputHandler;
    }
}
