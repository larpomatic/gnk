package org.gnk.ressource

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.gn.Gn
import org.gnk.naming.NamingController
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Graph
import org.gnk.selectintrigue.Plot
import org.gnk.substitution.InputHandler
import org.gnk.substitution.IntegrationHandler
import org.gnk.substitution.OutputHandler
import org.gnk.resplacetime.Event
import org.gnk.substitution.data.GnInformation
import org.gnk.substitution.data.Place
import org.gnk.substitution.data.Resource

class RessourceController {

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
        gn.step = "ressource";
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)

        gn.save(flush: true);
        //RelationshipGraphService graphservice = new RelationshipGraphService();
        //String json = graphservice.create_graph(gn);

        Graph graph = new Graph(gn)
        String json = graph.buildGlobalGraphJSON();

        session.setAttribute("placeList", inputHandler.placeList)
        //test
        GnInformation gnInfo = inputHandler.gnInfo
        List<Character> characterList = inputHandler.characterList
        List<Resource> resourceList = inputHandler.resourceList
        List<Event> eventList = inputHandler.eventList
        Map<String, Place> gnPlaceConstantMap = inputHandler.gnPlaceConstantMap

        [gnInfo: gnInfo,
         characterList: characterList,
         resourceList: resourceList,
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


        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.removeCharArray();
        gn.save(flush: true);
        redirect(controller: 'naming', action: 'index', params: [gnId: id as String]);
    }

    def getSubResources() {
        JSONObject resourceJSONObject = request.JSON

        IntegrationHandler integrationHandler = new IntegrationHandler()
        resourceJSONObject = integrationHandler.resourceIntegration(resourceJSONObject)

        render resourceJSONObject
    }

    def validateRessource() {
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
        OutputHandler outputHandler = NamingController.getOutputHandler()
        // Resources
        JSONArray resourcesJSONArray = subJSON.subResource
        Gn gn = Gn.get(gnDbId)
        gn.setCharArray(resourcesJSONArray)
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
            // Go to Place
            redirect(controller: "placeSub", action: "index", params: [gnId: gnDbId])

        }
    }
}
