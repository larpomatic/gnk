package org.gnk.time

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
import org.gnk.substitution.data.Event
import org.gnk.substitution.data.GnInformation
import org.gnk.substitution.data.Pastscene
import org.gnk.substitution.data.Place
import org.gnk.substitution.data.Resource

import java.text.ParseException
import java.text.SimpleDateFormat

class TimeController {

    private org.gnk.naming.Convention convention

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
        gn.step = "substitution";
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)

        gn.save(flush: true);
        //RelationshipGraphService graphservice = new RelationshipGraphService();
        //String json = graphservice.create_graph(gn);

        Graph graph = new Graph(gn)
        String json = graph.buildGlobalGraphJSON();

        session.setAttribute("placeList", inputHandler.placeList)
        //test
        GnInformation gnInfo = inputHandler.gnInfo
        //List<Character> characterList = inputHandler.characterList
        //List<Resource> resourceList = inputHandler.resourceList
        //List<Place> placeList = inputHandler.placeList
        List<Pastscene> pastsceneList = inputHandler.pastsceneList
        List<Event> eventList = inputHandler.eventList
        Map<String, Place> gnPlaceConstantMap = inputHandler.gnPlaceConstantMap

        [gnInfo       : gnInfo,
         //characterList: characterList,
         //resourceList : resourceList,
         //placeList    : placeList,
         pastsceneList: pastsceneList,
         eventList    : eventList,
         relationjson : json,
         gnId         : gnIdStr,
         sexe         : params.sexe
        ]
    }

    def getBack(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()

        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
        /*Integer evenementialId = 0;
        Integer mainstreamId = 0;
        for (Plot plot in gn.selectedPlotSet) {
            if (plot.isEvenemential) {
                evenementialId = Plot.findByName(plot.name).id;
            } else if (plot.isMainstream && gn.isMainstream) {
                mainstreamId = Plot.findByName(plot.name).id; ;
            }
        }*/

        redirect(controller: 'placeSub', action: 'index', params: [gnId: id as String]);
    }

    public Calendar isValidDate(String dateToValidate, String dateFromat){
        if(dateToValidate == null){
            return null;
        }
        if (dateToValidate.indexOf(":") == -1) {
            dateToValidate = dateToValidate + " 00:00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.ERA, GregorianCalendar.BC);
            Date date = sdf.parse(dateToValidate);
            cal.setTime(date)
            return cal;
        } catch (ParseException e) {
            return null;
        }
    }

    def saveOrUpdateDates() {


        def gnInstance = Gn.get(Long.valueOf(params.gnId).longValue())
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gnInstance);

        if (!gnInstance) {

            flash.message = message(code: 'default.not.found.message', args: [message(code: 'gn.label', default: 'GN'), params.gnId])
            redirect(action: "index")
            return
        }

        convention

        if (params.t0DateHour) {
            Calendar calendar = isValidDate(params.t0DateHour as String, "dd/MM/yyyy HH:mm");
            if (params.t0DateHourUnity == "+") {
                calendar.set(calendar.ERA, GregorianCalendar.AD)
//                calendar.ERA = GregorianCalendar.AD;
            } else {
                calendar.set(calendar.ERA, GregorianCalendar.BC)
//                calendar.ERA = GregorianCalendar.BC;
            }
            if (calendar) {
                gnInstance.t0Date = calendar.getTime();
            }
        }

        if (params.gnDuration) {
            gnInstance.duration = params.gnDuration as Integer
        }

        gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)

        if (!gnInstance.save(flush: true) || !gnHasConvention.save(flush: true)) {

        }
        redirect(controller: "Substitution", action: "index", params: [gnId: gnInstance.id, sexe: params.sexe /*, gnDTD: gnInstance.dtd, screenStep: 2*/])
    }

    def getSubDates() {
        JSONObject dateJSONObject = request.JSON
        String gnIdStr = params.gnId
        Integer gnId = null
        if (gnIdStr == null) {
            gnIdStr = session.getAttribute("gnId")
        }
        if (!gnIdStr == null && !(gnIdStr as String).isInteger()) {
            gnId = Integer.parseInt(gnIdStr)
        }

        IntegrationHandler integrationHandler = new IntegrationHandler()
        dateJSONObject = integrationHandler.dateIntegration(dateJSONObject, gnId, params.subDates as boolean)

        params.subDates = true

        render dateJSONObject
    }

    def validateTime() {
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
        // Characters
        //JSONArray charsJSONArray = subJSON.subCharacter
        //outputHandler.updateGnWithNaming(gnkDataContainerService, charsJSONArray)
        // Resources
        //JSONArray resourcesJSONArray = subJSON.subResource
        //outputHandler.updateGnWithResources(gnkDataContainerService, resourcesJSONArray)
        // Places
        //JSONArray placesJSONArray = subJSON.subPlace
        //outputHandler.updateGnWithPlaces(gnkDataContainerService, placesJSONArray)
        //Dates
        JSONObject datesJSON = subJSON.subDate
        outputHandler.updateGnWithDates(gnkDataContainerService, datesJSON)

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
            gnkDataContainerService.SaveDTD(gnkDataContainerService.gn.dtd)
            //line below can be commented to go back to substitution step when leaving the GN creation during publication
            gnkDataContainerService.gn.dtd = gnkDataContainerService.gn.dtd.replace("<STEPS last_step_id=\"substitution\">", "<STEPS last_step_id=\"publication\">");
            if (!gnkDataContainerService.gn.save(flush: true)) {
                redirect(action: "list", controller: "selectIntrigue", params: [gnId: gnDbId])
                return
            }
            // Go to publication
            redirect(controller: "publication", action: "index", params: [gnId: gnDbId])

        }
    }
}
