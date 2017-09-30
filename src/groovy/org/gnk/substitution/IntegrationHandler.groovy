package org.gnk.substitution

import com.gnk.substitution.Tag
import grails.util.Environment
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.gn.Gn
import org.gnk.naming.NamingService
import org.gnk.naming.PersoForNaming
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceHasTag
import org.gnk.resplacetime.PlaceResourceService
import org.gnk.resplacetime.Resource
import org.gnk.resplacetime.ResourceService
import org.gnk.resplacetime.PlaceService
import org.gnk.resplacetime.TimeService
import org.gnk.ressplacetime.EventTime
import org.gnk.ressplacetime.GenericPlace
import org.gnk.ressplacetime.GenericResource
import org.gnk.ressplacetime.PastsceneTime
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.ressplacetime.ReferentialPlace
import org.gnk.ressplacetime.ReferentialResource
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.RoleHasRelationWithRole

import org.gnk.selectintrigue.Plot
//import org.gnk.substitution.data.RelationCharacter
import org.gnk.resplacetime.GenericResource
import org.gnk.utils.Pair
import org.gnk.tag.Tag



class IntegrationHandler {

    Map<String, Integer> resultList = new HashMap<>()

    /**
     * Make the naming substitution happen
     * Give a name to the characters of the GN
     *
     * @param charJsonObject the JSON from the NamingController getSubCharacters() request
     *      contain the data from substitution naming _character.gsp table : gnId, Universe and characters
     * @return

     */
    public JSONObject namingIntegration(JSONObject charJsonObject) {
        String universe = charJsonObject.get("universe")
        LinkedList<PersoForNaming> charForNamingList = []
        LinkedList<Map<org.gnk.tag.Tag, Integer>> tagForNamingList = []
        LinkedList<RoleHasRelationWithRole> charsRelBijectives = new LinkedList<RoleHasRelationWithRole>()
        LinkedList<org.apache.commons.lang3.tuple.Pair<String, RoleHasRelationWithRole>> tupleList = new LinkedList<org.apache.commons.lang3.tuple.Pair<String, RoleHasRelationWithRole>>()


        // CharForNamingList construction from json
        for (characterJson in charJsonObject.characters) {
            PersoForNaming charForNaming = new PersoForNaming()

            charForNaming.universe = universe

            charForNaming.code = characterJson.code
            //print ("characterJson.code : " + characterJson.code)
            charForNaming.gender = characterJson.gender

            // Tags
            ArrayList<com.gnk.substitution.Tag> tagList = []
            Map<org.gnk.tag.Tag, Integer> persoTagList = new HashMap<org.gnk.tag.Tag, Integer>();
            for (tagJson in characterJson.tags) {
                //Tag used in calculation
                com.gnk.substitution.Tag tag = new com.gnk.substitution.Tag()
                //Persisted tag from the db, can't contain weight
                org.gnk.tag.Tag ntag = new org.gnk.tag.Tag()

                tag.value = tagJson.value
                tag.type = tagJson.family
                tag.weight = tagJson.weight as Integer
                ntag.name = tagJson.value
                ntag = org.gnk.tag.Tag.createCriteria().list {
                    maxResults(1)
                    eq('name', tagJson.value)
                }[0]

                tagList.add(tag)
                persoTagList.put(ntag, tag.weight)
            }
            charForNaming.tag = tagList

            // Family
            charForNaming.family = []

            charForNaming.relationList = new LinkedList<RoleHasRelationWithRole>()

            for (rel in characterJson.relationList) {
                RoleHasRelationWithRole relationChar = new RoleHasRelationWithRole()
                relationChar.r1 = rel.r1
                relationChar.r2 = rel.r2
                relationChar.type = rel.type
                relationChar.isHidden = rel.isHidden
                relationChar.isBijective = rel.isBijective

                if (rel.isBijective) {
                    org.apache.commons.lang3.tuple.Pair<String, RoleHasRelationWithRole> tuple
                    tuple.key = rel.r2
                    tuple.value = relationChar
                    tuple.value.r1 = rel.r2
                    tuple.value.r2 = rel.r1
                }

                //print(rel.type + " : De [" + rel.role1 + "] Vers [" + rel.role2 + "]")

                charForNaming.relationList.add(relationChar)
            }

            // Firstname
            List<String> proposedFirstnameList = []
            for (firstnameJson in characterJson.proposedFirstnames) {
                proposedFirstnameList.add(firstnameJson)
            }
            charForNaming.selectedFirstnames = proposedFirstnameList
            charForNaming.is_selectedFirstName = proposedFirstnameList.isEmpty()

            List<String> bannedFirstnameList = []
            for (firstnameJson in characterJson.bannedFirstnames) {
            }
            charForNaming.bannedFirstnames = bannedFirstnameList

            // Lastname
            List<String> proposedLastnameList = []
            for (lastnameJson in characterJson.proposedLastnames) {
                proposedLastnameList.add(lastnameJson)
            }
            charForNaming.selectedNames = proposedLastnameList
            charForNaming.is_selectedName = proposedLastnameList.isEmpty()

            List<String> bannedLastnameList = []
            for (lastnameJson in characterJson.bannedLastnames) {
                bannedLastnameList.add(lastnameJson)
            }
            charForNaming.bannedNames = bannedLastnameList

            charForNamingList.add(charForNaming)
            tagForNamingList.add(persoTagList)
        }

        for (c in charForNamingList) {
            for (t in tupleList) {
                if (t.key.equals(c.code)) {
                    c.relationList.add(t.value)
                }
            }
        }

        // NAMING CALL
        NamingService namingService = new NamingService()
        charForNamingList = namingService.namingMethod(charForNamingList, charJsonObject.gnId as Integer)
        // END NAMING CALL

        // joseph: print des noms générés par l'ancien NamingService
        for (PersoForNaming p : charForNamingList)
        {
            println("Prenoms: " + p.selectedFirstnames + "  Noms: " + p.selectedNames)
        }

        // Update json
        for (characterJson in charJsonObject.characters) {
            String code = characterJson.code
            PersoForNaming charForNaming = null

            for (charForNamingIt in charForNamingList) {
                if (charForNamingIt.code == code) {
                    charForNaming = charForNamingIt
                    break
                }
            }

            // Firstname
            characterJson.remove("proposedFirstnames")
            if (charForNaming.selectedFirstnames && !charForNaming.selectedFirstnames.isEmpty()) {
                JSONArray proposedFirstnames = new JSONArray()
                for (name in charForNaming.selectedFirstnames) {
                    proposedFirstnames.put(name)
                }
                characterJson.put("proposedFirstnames", proposedFirstnames)
            }
            // TOREMOVE
            else {
                JSONArray proposedFirstnames = new JSONArray()
                proposedFirstnames.put("<Prénom générique>")
                characterJson.put("proposedFirstnames", proposedFirstnames)
            }

            // Lastname
            characterJson.remove("proposedLastnames")
            if (charForNaming.selectedNames && !charForNaming.selectedNames.isEmpty()) {
                JSONArray proposedLastnames = new JSONArray()
                for (name in charForNaming.selectedNames) {
                    proposedLastnames.put(name)
                }
                characterJson.put("proposedLastnames", proposedLastnames)
            }
            // TOREMOVE
            else {
                JSONArray proposedLastnames = new JSONArray()
                proposedLastnames.put("<Nom générique>")
                characterJson.put("proposedLastnames", proposedLastnames)
            }
        }

        return charJsonObject
    }

    public JSONObject resourceIntegration(JSONObject resourceJsonObject) {
        String universe = resourceJsonObject.get("universe")
        List<GenericResource> genericResourceList = []

        // GenericResourceList construction from json
        for (resourceJson in resourceJsonObject.resources) {

            //GenericResource genericResource = new GenericResource()
            GenericResource gr = new GenericResource()

            gr.code = "res" + resourceJson.gnId + "_plot" + resourceJson.gnPlotId
            gr.plotId = new Integer(Integer.parseInt(resourceJson.gnPlotId))
            gr.gnId = new Integer(resourceJson.IdOfGn)
            gr.totalNumberOfTags = 0;

            List<Tag> tagList = []
            List<Tag> tags = new ArrayList<>();
            for (tagJson in resourceJson.tags) {
                /*GenericResourceHasTag tag = new GenericResourceHasTag()
                tag.tag = new Tag()
                tag.tag = tagJson
                tag.weight = tagJson.weight as Integer*/
                Tag tag = new Tag()
                tag.value_substitution = tagJson.value
                tag.type_substitution = tagJson.family
                tag.weight_substitution = tagJson.weight as Integer
                tags.add(tag)
            }
            gr.taglist = tags

            // Name
            List<Resource> proposedNameList = []
            for (nameJson in resourceJson.proposedNames) {
                Resource referentialResource = new Resource()
                referentialResource.name = nameJson
                proposedNameList.add(referentialResource)
            }
            gr.proposedResources = proposedNameList

            List<Resource> bannedNameList = []
            for (nameJson in resourceJson.bannedNames) {
                Resource referentialResource = new Resource()
                referentialResource.name = nameJson
                bannedNameList.add(referentialResource)
            }
            gr.bannedResources = bannedNameList

            genericResourceList.add(gr)
        }

        // RESOURCE SERVICE CALL
        ResourceService resourceService = new ResourceService()
        PlaceResourceService placeresourceservice = new PlaceResourceService();
        //print "Universe : " + universe
        for (genericResource in genericResourceList) {
            //if (genericResource.proposedResources.isEmpty()) {
                //print "GenericResource IN : " + genericResource
                //genericResource.resultsAllUniverses = placeresourceservice.findBestObjectsForAllUnivers(genericResource,Plot.findById(genericResource.plotId))
                //print "GenericResource OUT : " + genericResource
                genericResource.resultService = placeresourceservice.findBestObjects(genericResource, Gn.findById(genericResource.gnId))
            //}
        }
        // END RESOURCE SERVICE CALL

        // Update json
        for (resourceJson in resourceJsonObject.resources) {
            String code = "res" + resourceJson.gnId + "_plot" + resourceJson.gnPlotId
            GenericResource genericResource = null

            for (genericResourceIt in genericResourceList) {
                if (genericResourceIt.code == code) {
                    genericResource = genericResourceIt
                    break
                }
            }

            // Name A VIRER
            /*resourceJson.remove("proposedNames")
            if (genericResource.resultsAllUniverses != null && !genericResource.resultsAllUniverses.isEmpty()) {
                JSONArray proposedNames = new JSONArray()
                for (Pair<org.gnk.tag.Tag, ArrayList<Pair<ReferentialObject, Integer>>> ref in genericResource.resultsAllUniverses) {
                    for (Pair<ReferentialObject, Integer> ref2 in ref.right)
                        proposedNames.put(ref2.left.name)
                }
                resourceJson.put("proposedNames", proposedNames)
            }*/

            // Name
            resourceJson.remove("proposedNames")
            if (genericResource.resultService != null && !genericResource.resultService.isEmpty()) {
                JSONArray proposedNames = new JSONArray()
                for (Pair<ReferentialObject, Integer> ref in genericResource.resultService) {
                        proposedNames.put(ref.left.name)
                }
                resourceJson.put("proposedNames", proposedNames)
            }

            // TOREMOVE
            else {
                JSONArray proposedNames = new JSONArray()
                proposedNames.put("<Ressource générique>")
                resourceJson.put("proposedNames", proposedNames)
            }
        }

        return resourceJsonObject
    }

    public Map<String, Integer> calcDiffBetweenGenericPlaceTagsAndPlaceTags(Map<String, Integer> gpTags, Map<String, Integer> pTags) {

        for (gpt in gpTags) {
            for (pt in pTags) {
                Integer res = 0
                Integer nbTag = 0
                for (p in pTags) {
                    if (p.key == p.key)
                        nbTag += 100
                    resultList.put(pt.key, res += Math.abs(p.value).intValue())
                }
                pt.value = (pt.value / nbTag) * 100
            }
        }
        return resultList
    }

    public JSONObject placeIntegration(JSONObject placeJsonObject) {
        String universe = placeJsonObject.get("universe")
        List<org.gnk.resplacetime.GenericPlace> genericPlaceList = []

        // GenericPlaceList construction from json
        for (placeJson in placeJsonObject.places) {

            org.gnk.resplacetime.GenericPlace genericPlace = new org.gnk.resplacetime.GenericPlace()

            genericPlace.code = "res" + placeJson.gnId + "_plot" + placeJson.gnPlotId
            genericPlace.plotId = new Integer(Integer.parseInt(placeJson.gnPlotId))
            genericPlace.gnId = new Integer(placeJson.IdOfGn)
            genericPlace.totalNumberOfTags = 0;

            List<Tag> tagList = []
            for (tagJson in placeJson.tags) {
                Tag tag = new Tag()

                tag.value_substitution = tagJson.value
                tag.type_substitution = tagJson.family
                tag.weight_substitution = tagJson.weight as Integer

                tagList.add(tag)
            }
            genericPlace.taglist = tagList

            // Name
            List<Place> proposedNameList = []
            for (nameJson in placeJson.proposedNames) {
                Place referentialPlace = new Place()
                referentialPlace.name = nameJson
                proposedNameList.add(referentialPlace)
            }
            genericPlace.proposedPlaces = proposedNameList

            List<Place> bannedNameList = []
            for (nameJson in placeJson.bannedNames) {
                Place referentialPlace = new Place()
                referentialPlace.name = nameJson
                bannedNameList.add(referentialPlace)
            }
            genericPlace.bannedPlaces = bannedNameList

            genericPlaceList.add(genericPlace)
        }

        // PLACE SERVICE CALL
        //PlaceService placeService = new PlaceService()
        PlaceResourceService placeresourceservice = new PlaceResourceService();
        //print "Universe : " + universe
        for (genericPlace in genericPlaceList) {
            genericPlace.resultService = placeresourceservice.findBestObjects(genericPlace, Gn.findById(genericPlace.gnId))
        }
        // END PLACE SERVICE CALL

        // Update json
        for (placeJson in placeJsonObject.places) {

            // Nom du genericPlaceHasTag, Poids
            Map<String, Integer> genericPlaceTags = new HashMap<>()
            Map<String, Integer> placeTags = new HashMap<>()

            String code = "res" + placeJson.gnId + "_plot" + placeJson.gnPlotId
            org.gnk.resplacetime.GenericPlace genericPlace = null

            for (genericPlaceIt in genericPlaceList) {
                if (genericPlaceIt.code == code) {
                    genericPlace = genericPlaceIt
                    break
                }
            }

            // On fait la liste contenant tous les genericPlace
            for (tag in placeJson.tags) {
                genericPlaceTags.put(tag.value as String, tag.weight as Integer)
            }

            // Name
            placeJson.remove("proposedNames")
            if (genericPlace.resultService != null && !genericPlace.resultService.isEmpty()) {
                JSONArray proposedNames = new JSONArray()
                for (Pair<ReferentialObject, Integer> ref in genericPlace.resultService) {
                    proposedNames.put(ref.left.name)
                }
                placeJson.put("proposedNames", proposedNames)
            }

            // TOREMOVE
            else {
                JSONArray proposedNames = new JSONArray()
                proposedNames.put("<Lieu générique>")
                placeJson.put("proposedNames", proposedNames)
            }
        }

        return placeJsonObject
    }

    public JSONObject dateIntegration(JSONObject dateJsonObject, Integer gnId, boolean subDates) {
        // Date format
        // TODO VINCENT : ADAPATER avec le nouveau format
        String dateFormat = "yyyy.MM.dd HH:mm"

        Integer gnDuration = dateJsonObject.duration as Integer
        Date gnBeginDate = new Date().parse(dateFormat, dateJsonObject.beginDate)

        // TIME SERVICE CALL
        TimeService timeService = new TimeService()
        // Pastscene
        for (pastsceneJson in dateJsonObject.pastscenes) {

            if (subDates){
                pastsceneJson.isYearAbsolute = "true"
                pastsceneJson.isMonthAbsolute = "true"
                pastsceneJson.isDayAbsolute = "true"
                pastsceneJson.isHourAbsolute = "true"
                pastsceneJson.isMinuteAbsolute = "true"
            }

            // Create pastceneTime
            Calendar calPastScene = gnBeginDate.toCalendar()
            // TODO il faut mettre dans le PastsceneTime créé tout en absolute en fonction du pastscene reçu.


            if (pastsceneJson.isYearAbsolute == "false"
                    && pastsceneJson.absoluteYear != null
                    && pastsceneJson.absoluteYear != ""
                    && (pastsceneJson.absoluteYear as Integer) > 1000)
            {
                print("year : " + pastsceneJson.absoluteYear)
                print("htmlId : " + pastsceneJson.htmlId)
            }

            PastsceneTime pastsceneTime = new PastsceneTime()
            pastsceneTime.relativeDateValue = null
            pastsceneTime.relativeDateUnit = null
            /*if (pastsceneJson.timingRelative != "") {
                pastsceneTime.relativeDateValue = pastsceneJson.timingRelative as Integer
            }

            if (pastsceneJson.relativeDateUnit != "") {
                pastsceneTime.relativeDateUnit = pastsceneJson.unitTimingRelative
            }
            */
            pastsceneTime.absoluteYear = null
            if (pastsceneJson.absoluteYear != "") {

                if (pastsceneJson.isYearAbsolute == "true") {
                    //pastsceneTime.absoluteYear = pastsceneJson.absoluteYear as Integer
                    calPastScene.set(Calendar.YEAR, pastsceneJson.absoluteYear as Integer)
                } else {
                    //pastsceneTime.absoluteYear = gnBeginDate.getAt(Calendar.YEAR) - (pastsceneJson.absoluteYear as Integer)
                    calPastScene.add(Calendar.YEAR, -(pastsceneJson.absoluteYear as Integer))
                }

            }

            pastsceneTime.absoluteMonth = null
            if (pastsceneJson.absoluteMonth != "") {
                if (pastsceneJson.isMonthAbsolute  == "true") {
                    calPastScene.set(Calendar.MONTH, pastsceneJson.absoluteMonth as Integer)
                } else {
                    calPastScene.add(Calendar.MONTH, - (pastsceneJson.absoluteMonth as Integer))
                }
            }

            pastsceneTime.absoluteDay = null
            if (pastsceneJson.absoluteDay != "") {
                if (pastsceneJson.isDayAbsolute  == "true") {
                    //pastsceneTime.absoluteDay = pastsceneJson.absoluteDay as Integer
                    calPastScene.set(Calendar.DAY_OF_MONTH, pastsceneJson.absoluteDay as Integer)
                } else {
                    calPastScene.add(Calendar.DAY_OF_MONTH, - (pastsceneJson.absoluteDay as Integer))
                    //pastsceneTime.absoluteDay = gnBeginDate.getAt(Calendar.DAY_OF_MONTH) - (pastsceneJson.absoluteDay as Integer)
                }
            }

            pastsceneTime.absoluteHour = null
            if (pastsceneJson.absoluteHour != "") {
                if (pastsceneJson.isHourAbsolute  == "true") {
                    calPastScene.set(Calendar.HOUR_OF_DAY, pastsceneJson.absoluteHour as Integer)
                } else {
                    calPastScene.add(Calendar.HOUR_OF_DAY, - (pastsceneJson.absoluteHour as Integer))
                }
            }
            pastsceneTime.absoluteMinute = null

            if (pastsceneJson.absoluteMinute != "") {
                if (pastsceneJson.isMinuteAbsolute  == "true") {
                    calPastScene.set(Calendar.MINUTE, pastsceneJson.absoluteMinute as Integer)
                } else {
                    calPastScene.add(Calendar.MINUTE, - (pastsceneJson.absoluteMinute as Integer))
                }
            }

            pastsceneTime.absoluteYear = calPastScene.getAt(Calendar.YEAR)
            // I "know" there are 12 months
            //String[] monthNames = new String[12]; // and populate...
            //String name = monthNames[calendar.get(Calendar.MONTH)];
            pastsceneTime.absoluteMonth = calPastScene.getAt(Calendar.MONTH) + 1
            pastsceneTime.absoluteDay = calPastScene.getAt(Calendar.DAY_OF_MONTH)
            pastsceneTime.absoluteHour = calPastScene.getAt(Calendar.HOUR_OF_DAY)
            pastsceneTime.absoluteMinute = calPastScene.getAt(Calendar.MINUTE)
            pastsceneJson.isYearAbsolute = "true"
            pastsceneJson.isMonthAbsolute = "true"
            pastsceneJson.isDayAbsolute = "true"
            pastsceneJson.isHourAbsolute = "true"
            pastsceneJson.isMinuteAbsolute = "true"

            // Call service
            if (pastsceneJson.isUpdate != null && pastsceneJson.isUpdate == "yes") {
                pastsceneTime = timeService.pastsceneRealDate(pastsceneTime, gnBeginDate, true)
            } else {
                pastsceneTime = timeService.pastsceneRealDate(pastsceneTime, gnBeginDate, false)
            }

            // Update json
            JSONObject date = new JSONObject();
            if (pastsceneTime.absoluteYear != null) {
                date.put("year", pastsceneTime.absoluteYear as String)
            }
            if (pastsceneTime.absoluteMonth != null) {
                date.put("month", pastsceneTime.absoluteMonth as String)
            }
            if (pastsceneTime.absoluteDay != null) {
                date.put("day", pastsceneTime.absoluteDay as String)
            }
            if (pastsceneTime.absoluteHour != null) {
                date.put("hours", pastsceneTime.absoluteHour as String)
            }
            if (pastsceneTime.absoluteMinute != null) {
                date.put("minutes", pastsceneTime.absoluteMinute as String)
            }


            pastsceneJson.put("date", date)
        }
        // Event
        for (eventJson in dateJsonObject.events) {
            // Create eventTime
            EventTime eventTime = new EventTime()
            //GnId in the JSON is the id of the domain class Event
            eventTime.setId(eventJson.gnId as Integer)
            eventTime.setTiming(eventJson.timing as Integer)
            //duration is needed but not present in the JSON
            //eventTime.duration = eventJson.duration as Integer
            if (eventJson.absoluteYear != "") {
                eventTime.absoluteYear = eventJson.absoluteYear as Integer
            }
            if (eventJson.absoluteMonth != "") {
                eventTime.absoluteMonth = eventJson.absoluteMonth as Integer
            }
            if (eventJson.absoluteDay != "") {
                eventTime.absoluteDay = eventJson.absoluteDay as Integer
            }
            if (eventJson.absoluteHour != "") {
                eventTime.absoluteHour = eventJson.absoluteHour as Integer
            }
            if (eventJson.absoluteMinute != "") {
                eventTime.absoluteMinute = eventJson.absoluteMinute as Integer
            }
            // Call service
            eventTime = timeService.eventRealDate(eventTime, gnBeginDate, gnDuration, gnId)
            // Update json
            JSONObject date = new JSONObject();
            if (eventTime.absoluteYear != null) {
                date.put("year", eventTime.absoluteYear as String)
            }
            if (eventTime.absoluteMonth != null) {
                date.put("month", eventTime.absoluteMonth as String)
            }
            if (eventTime.absoluteDay != null) {
                date.put("day", eventTime.absoluteDay as String)
            }
            if (eventTime.absoluteHour != null) {
                date.put("hours", eventTime.absoluteHour as String)
            }
            if (eventTime.absoluteMinute != null) {
                date.put("minutes", eventTime.absoluteMinute as String)
            }

            eventJson.put("date", date)
        }
        // END TIME SERVICE CALL

        return dateJsonObject
    }
}
