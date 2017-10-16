package org.gnk.time

import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.gn.Gn
import org.gnk.naming.Convention
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Graph
import org.gnk.substitution.InputHandler
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.Resource
import org.json.*;

class GanttController {

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
        gn.step = "time";
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
        JSONObject ganttData = loadGanttData();

        [gnInfo: gnInfo,
         characterList: characterList,
         resourceList: resourceList,
         placeList: placeList,
         pastsceneList: pastsceneList,
         eventList: eventList,
         relationjson: json,
         gnId: gnIdStr,
         ruleList: Convention.findById(gn.convention_id).conventionHasRules.rule,
//                ruleList: gn.convention.conventionHasRules.rule,
         sexe: params.sexe,
         ganttData: ganttData
        ]
    }


    def saveGanttData() {

        //les données du Gantt se trouve dans params.ganttData
        String ganttData = params.ganttData;

        //def gnInstance = Gn.get(Long.valueOf(params.gnId).longValue())


        redirect(controller: "Time", action: "index", params: [gnId: params.gnId, sexe: params.sexe, ganttData: params.ganttData /*, gnDTD: gnInstance.dtd, screenStep: 2*/])
    }

    def loadGanttData() {

        // ce JSONObject est renvoyé à la page subtitution du gnk et doit contenir les données du gantt (périodes) récupérées en base
        JSONObject obj = new JSONObject();

        redirect(controller: "Time", action: "index", params: [gnId: params.gnId, sexe: params.sexe, ganttData: obj /*, gnDTD: gnInstance.dtd, screenStep: 2*/])

    }


}


