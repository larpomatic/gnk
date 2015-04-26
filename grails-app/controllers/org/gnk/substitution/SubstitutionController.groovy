package org.gnk.substitution

import grails.converters.JSON
import grails.gorm.PagedResultList
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.importation.GNKImportationService
import org.gnk.parser.GNKDataContainerService
import org.gnk.gn.Gn
import org.gnk.resplacetime.GenericPlace
import org.gnk.roletoperso.Character;
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.roletoperso.Graph
import org.gnk.roletoperso.RelationshipGraphService
import org.gnk.selectintrigue.Plot
import org.gnk.substitution.data.Event
import org.gnk.substitution.data.GnInformation
import org.gnk.substitution.data.Pastscene
import org.gnk.substitution.data.Place
import org.gnk.substitution.data.Resource
import org.gnk.substitution.data.Tag
import sun.launcher.resources.launcher_pt_BR

class SubstitutionController {

    String xmlGnTestPath = "DTD_V3.xml"

    def getBack(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
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
        redirect(controller: 'roleToPerso', action: 'roleToPerso', params: [gnId: id as String,
                selectedMainstream: mainstreamId as String,
                selectedEvenemential: evenementialId as String]);
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
        List<Character> characterList = inputHandler.characterList
        List<Resource> resourceList = inputHandler.resourceList
        List<Place> placeList = inputHandler.placeList
        List<Pastscene> pastsceneList = inputHandler.pastsceneList
        List<Event> eventList = inputHandler.eventList
        [gnInfo: gnInfo,
                characterList: characterList,
                resourceList: resourceList,
                placeList: placeList,
                pastsceneList: pastsceneList,
                eventList: eventList,
                relationjson: json,
                gnId: gnIdStr,
                ruleList: gn.gnHasConvention.convention.conventionHasRules.rule]
    }
    /*private void changeCharSex(Gn gn, List<String> sexes)
    {
        //Gn gn = Gn.get(gnId);
        //assert (gn != null)
        //final gnData = new GNKDataContainerService()
        //gnData.ReadDTD(gn)

        for (Character c in gn.getterNonPlayerCharSet())
        {
            String sex = sexes.find {it.toString().startsWith(c.getDTDId() + "-")};
            if ((sex != null) && (sex != "false") && (sex  != "") && (sex != "NO"))
            {
                switch (sex.split("-")[1])
                {
                    case "Homme":
                        c.setGender("M");
                        break;
                    case "Femme":
                        c.setGender("F");
                        break;
                    case "Neutre":
                        c.setGender("N");
                        break;
                }
            }
        }
        //GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        //gn.dtd = gnXMLWriterService.getGNKDTDString(gn)
        //gn.save();
        //return gn;
        return input;
    }*/

    def getSubCharacters() {
        JSONObject charJSONObject = request.JSON

        IntegrationHandler integrationHandler = new IntegrationHandler()
        charJSONObject = integrationHandler.namingIntegration(charJSONObject)

        render charJSONObject
    }

    def getSubResources() {
        JSONObject resourceJSONObject = request.JSON

        IntegrationHandler integrationHandler = new IntegrationHandler()
        resourceJSONObject = integrationHandler.resourceIntegration(resourceJSONObject)

        render resourceJSONObject
    }

    def getSubPlaces() {
        JSONObject placeJSONObject = request.JSON

        IntegrationHandler integrationHandler = new IntegrationHandler()
        placeJSONObject = integrationHandler.placeIntegration(placeJSONObject)

        render placeJSONObject
    }

    def getSubDates() {
        JSONObject dateJSONObject = request.JSON

        IntegrationHandler integrationHandler = new IntegrationHandler()
        dateJSONObject = integrationHandler.dateIntegration(dateJSONObject)

        render dateJSONObject
    }

    def validateSubstitution() {
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
        OutputHandler outputHandler = new OutputHandler()
        // Characters
        JSONArray charsJSONArray = subJSON.subCharacter
        outputHandler.updateGnWithNaming(gnkDataContainerService, charsJSONArray)
        // Resources
        JSONArray resourcesJSONArray = subJSON.subResource
        outputHandler.updateGnWithResources(gnkDataContainerService, resourcesJSONArray)
        // Places
        JSONArray placesJSONArray = subJSON.subPlace
        outputHandler.updateGnWithPlaces(gnkDataContainerService, placesJSONArray)
        // Dates
        JSONObject datesJSON = subJSON.subDate
        outputHandler.updateGnWithDates(gnkDataContainerService, datesJSON)

        // Writer
        GnXMLWriterService gnXMLWriter = new GnXMLWriterService()
        String xmlGN = gnXMLWriter.getGNKDTDString(gnkDataContainerService.gn)

        if (gnDbId == -1) {
            render(text: xmlGN, contentType: "text/xml", encoding: "UTF-8")
        } else {
            //render(text: xmlGN, contentType: "text/xml", encoding: "UTF-8")
            //return
            // Save in DataBase
            gnkDataContainerService.gn.dtd = xmlGN;
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

    def getMergeablePlaces() {
        List<Place> lPlace = session.getAttribute("placeList")
        String placestr = params.place1

        if (!placestr.equals("-1")) {
            List<Place> nlist = new ArrayList<Place>();
            Place p;
            for (Place place : lPlace) {
                if (place.code.equals(placestr)) {
                    p = place;
                    break;
                }
            }
            for (Place place : lPlace) {
                if (place.plotId != p.plotId) {
                    nlist.add(place)
                }
            }
            JSONArray jsonList = new JSONArray();
            for (Place place : nlist) {
                JSONObject jsonsub = new JSONObject();
                jsonsub.put("plotId", place.plotId);
                jsonsub.put("code", place.code);
                jsonList.add(jsonsub);
            }
            render(contentType: "application/json") {
                jsonList
            }
        }
    }

    def merged() {
        String code1 = params.place1
        String code2 = params.place2
        List<Place> placeList = session.getAttribute("placeList")
        Place p1;
        Place p2;
        for(Place place1 : placeList){
            if (place1.code.equals(code1)){
                p1 = place1
            }
            if (place1.code.equals(code2)) {
                p2 = place1
            }
        }

        placeList.remove(p1)
        placeList.remove(p2)
        p1.comment = p1.comment + System.getProperty("line.separator") + p2.comment
        List<Tag> placeTags1 = p1.tagList
        List<Tag> placeTags2 = p2.tagList;
        List<Tag> newplaceTags = new ArrayList<>()
        for (Tag t2 : placeTags2){
            for (Tag t1 : placeTags1){
                if (t2.value.equals(t1.value)){
                    Tag t = new Tag()
                    t.family = t2.family
                    t.value = t2.value
                    t.status = t
                    t.weight = (t2.weight + t1.weight)/2
                    newplaceTags.add(t)
                }
                else
                {
                    newplaceTags.add(t2)
                }
            }
        }
        p1.placeTags = newplaceTags
        JSONArray jsonArray = new JSONArray()
        JSONObject jsonsub = new JSONObject();
        jsonsub.put("plotId", p1.plotId);
        jsonsub.put("code", p1.code);
        jsonsub.put("id", p1.id);
        jsonsub.put("comment", p1.comment);
        jsonsub.put("plotName", p1.plotName);
        for(Tag tag : p1.tagList){
            JSONObject tagsub = new JSONObject()
            tagsub.put("family", tag.family)
            tagsub.put("value", tag.value)
            tagsub.put("weight", tag.weight)
            tagsub.put("status", tag.status)
            jsonArray.add(tagsub)
        }
        jsonsub.put("tags", jsonArray);
        render(contentType: "application/json") {
            jsonsub
        }
        /*
        GNKImportationService gnkImportationService
        final gnId = params.gnId
        session.setAttribute("gnId", gnId)
        Gn gn = Gn.get(gnId as Integer)
        final gnData = new GNKDataContainerService()
        gnData.ReadDTD(gn)

        Map<Integer, GenericPlace> gPlace = gnData.genericPlaceMap
        GenericPlace p1;
        GenericPlace p2;
        ArrayList<Integer> cles = gPlace.keySet().toArray();
        ArrayList<GenericPlace> listplace = new ArrayList<>()
        Iterator it = cles.iterator();
        while (it.hasNext()) {
            Integer cle = it.next();
            GenericPlace place = gPlace.get(cle); // tu peux typer plus finement ici

            if (place.code.equals(params.placeMergeable1)) {
                p1 = place;

            } else {
                if (place.code.equals(params.placeMergeable2)) {
                    p2 = place;
                    p2.id = cle
                }
                else{
                    listplace.add(place)
                }
            }
        }
        p1.comment = p1.comment + "\n" + p2.comment;
        if (p1.extTags && p2.extTags)
            p2.extTags = gnkImportationService.mergedTag(p1.extTags, p2.extTags);
        else{
            if(p2){
                p1.extTags = p2.extTags
            }
        }


        gPlace.remove(p2.id)
        gnData.setGenericPlaceMap(gPlace)
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)
        gn.save()
        session.setAttribute("listPlace", listplace)
        redirect(action: "index1")
        */

    }

    def tagList(){
        String code = params.tagscode
        List<Place> placeList = session.getAttribute("placeList")
        Place p1;

        for(Place place1 : placeList){
            if (place1.code.equals(code)){
                p1 = place1
            }
        }
        List<Tag> placeTags = p1.tagList
        placeTags = placeTags.sort(){Math.abs(it.weight)}
        placeTags = placeTags.reverse()
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray()
        JSONObject jsonsub = new JSONObject();
        for (Tag tag : placeTags){
            jsonsub.put("code", tag.value);
            jsonsub.put("value", tag.weight);
            jsonArray.add(jsonsub)
        }
        json.put("tags", jsonArray)
        render(contentType: "application/json") {
            jsonsub
        }
    }
}
