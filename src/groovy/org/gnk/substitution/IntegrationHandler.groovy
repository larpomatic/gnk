package org.gnk.substitution

import com.gnk.substitution.Tag
import grails.util.Environment
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.gn.Gn
import org.gnk.naming.NamingService
import org.gnk.naming.PersoForNaming
import org.gnk.resplacetime.ResourceService
import org.gnk.resplacetime.PlaceService
import org.gnk.resplacetime.TimeService
import org.gnk.ressplacetime.EventTime
import org.gnk.ressplacetime.GenericPlace
import org.gnk.ressplacetime.GenericResource
import org.gnk.ressplacetime.PastsceneTime
import org.gnk.ressplacetime.ReferentialPlace
import org.gnk.ressplacetime.ReferentialResource
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.RoleHasRelationWithRole
import org.gnk.substitution.data.RelationCharacter

class IntegrationHandler {

    public JSONObject namingIntegration(JSONObject charJsonObject) {
        String universe = charJsonObject.get("universe")
        LinkedList<PersoForNaming> charForNamingList = []
        LinkedList<Map<org.gnk.tag.Tag, Integer>> tagForNamingList = []

        // CharForNamingList construction from json
        for(characterJson in charJsonObject.characters) {
            PersoForNaming charForNaming = new PersoForNaming()

            charForNaming.universe = universe

            charForNaming.code = characterJson.gnId
            //print ("characterJson.gnId : " + characterJson.gnId)
            charForNaming.gender = characterJson.gender

            // Tags
            List<Tag> tagList = []
            Map<org.gnk.tag.Tag, Integer> persoTagList = new HashMap<org.gnk.tag.Tag, Integer>();
            for(tagJson in characterJson.tags) {
                Tag tag = new Tag()
                org.gnk.tag.Tag ntag = new org.gnk.tag.Tag()

                tag.value = tagJson.value
                tag.type = tagJson.family
                tag.weight = tagJson.weight as Integer
                ntag.name = tagJson.value
                ntag = org.gnk.tag.Tag.createCriteria().list{
                    maxResults(1)
                    eq ('name', tagJson.value)
                    }[0]

                tagList.add(tag)
                persoTagList.put(ntag, tag.weight)
            }
            charForNaming.tag = tagList

            // Family
            charForNaming.family = []

            charForNaming.relationList = new LinkedList<RelationCharacter>()

            for (rel in characterJson.relationList) {
                RelationCharacter relationChar = new  RelationCharacter()
                relationChar.role1 = rel.role1
                relationChar.role2 = rel.role2
                relationChar.type = rel.type
                relationChar.isHidden = rel.isHidden
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
                bannedFirstnameList.add(firstnameJson)
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

        // NAMING CALL
        NamingService namingService = new NamingService()
        String s = charJsonObject.gnId
        charForNamingList = namingService.namingMethod(charForNamingList, s.toInteger())
        // END NAMING CALL

        // Update json
        for(characterJson in charJsonObject.characters) {
            String gn_Id = characterJson.gnId
            PersoForNaming charForNaming = null

            for (charForNamingIt in charForNamingList){
                if (charForNamingIt.code == gn_Id) {
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
        for(resourceJson in resourceJsonObject.resources) {
            GenericResource genericResource = new GenericResource()

            genericResource.code = "res" + resourceJson.gnId + "_plot" + resourceJson.gnPlotId

            List<Tag> tagList = []
            for(tagJson in resourceJson.tags) {
                Tag tag = new Tag()

                tag.value = tagJson.value
                tag.type = tagJson.family
                tag.weight = tagJson.weight as Integer

                tagList.add(tag)
            }
            genericResource.tagList = tagList

            // Name
            List<ReferentialResource> proposedNameList = []
            for (nameJson in resourceJson.proposedNames) {
                ReferentialResource referentialResource = new ReferentialResource()
                referentialResource.name = nameJson
                proposedNameList.add(referentialResource)
            }
            genericResource.resultList = proposedNameList

            List<ReferentialResource> bannedNameList = []
            for (nameJson in resourceJson.bannedNames) {
                ReferentialResource referentialResource = new ReferentialResource()
                referentialResource.name = nameJson
                bannedNameList.add(referentialResource)
            }
            genericResource.bannedItemsList = bannedNameList

            genericResourceList.add(genericResource)
        }

        // RESOURCE SERVICE CALL
        ResourceService resourceService = new ResourceService()
        //print "Universe : " + universe
        for(genericResource in genericResourceList) {
            if (genericResource.resultList.isEmpty()) {
                //print "GenericResource IN : " + genericResource
                genericResource = resourceService.findReferentialResource(genericResource, universe)
                //print "GenericResource OUT : " + genericResource
            }
        }
        // END RESOURCE SERVICE CALL

        // Update json
        for(resourceJson in resourceJsonObject.resources) {
            String code = "res" + resourceJson.gnId + "_plot" + resourceJson.gnPlotId
            GenericResource genericResource = null

            for (genericResourceIt in genericResourceList){
                if (genericResourceIt.code == code) {
                    genericResource = genericResourceIt
                    break
                }
            }

            // Name
            resourceJson.remove("proposedNames")
            if (genericResource.resultList != null && !genericResource.resultList.isEmpty()) {
                JSONArray proposedNames = new JSONArray()
                for (referentialResource in genericResource.resultList) {
                    proposedNames.put(referentialResource.name)
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

    public JSONObject placeIntegration(JSONObject placeJsonObject) {
        String universe = placeJsonObject.get("universe")
        List<GenericPlace> genericPlaceList = []

        // GenericPlaceList construction from json
        for(placeJson in placeJsonObject.places) {
            GenericPlace genericPlace = new GenericPlace()

            genericPlace.code = "res" + placeJson.gnId + "_plot" + placeJson.gnPlotId

            List<Tag> tagList = []
            for(tagJson in placeJson.tags) {
                Tag tag = new Tag()

                tag.value = tagJson.value
                tag.type = tagJson.family
                tag.weight = tagJson.weight as Integer

                tagList.add(tag)
            }
            genericPlace.tagList = tagList

            // Name
            List<ReferentialPlace> proposedNameList = []
            for (nameJson in placeJson.proposedNames) {
                ReferentialPlace referentialPlace = new ReferentialPlace()
                referentialPlace.name = nameJson
                proposedNameList.add(referentialPlace)
            }
            genericPlace.resultList = proposedNameList

            List<ReferentialPlace> bannedNameList = []
            for (nameJson in placeJson.bannedNames) {
                ReferentialPlace referentialPlace = new ReferentialPlace()
                referentialPlace.name = nameJson
                bannedNameList.add(referentialPlace)
            }
            genericPlace.bannedItemsList = bannedNameList

            genericPlaceList.add(genericPlace)
        }

        // PLACE SERVICE CALL
        PlaceService placeService = new PlaceService()
        //print "Universe : " + universe
        for(genericPlace in genericPlaceList) {
            if (genericPlace.resultList.isEmpty()) {
                //print "GenericPlace IN : " + genericPlace
                genericPlace = placeService.findReferentialPlace(genericPlace, universe)
                //print "GenericPlace OUT : " + genericPlace
            }
        }
        // END PLACE SERVICE CALL

        // Update json
        for(placeJson in placeJsonObject.places) {
            String code = "res" + placeJson.gnId + "_plot" + placeJson.gnPlotId
            GenericPlace genericPlace = null

            for (genericPlaceIt in genericPlaceList){
                if (genericPlaceIt.code == code) {
                    genericPlace = genericPlaceIt
                    break
                }
            }

            // Name
            placeJson.remove("proposedNames")
            if (genericPlace.resultList != null && !genericPlace.resultList.isEmpty()) {
                JSONArray proposedNames = new JSONArray()
                for (referentialPlace in genericPlace.resultList) {
                    proposedNames.put(referentialPlace.name)
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

    public JSONObject dateIntegration(JSONObject dateJsonObject) {
        // Date format
        String dateFormat = "yyyy.MM.dd HH:mm"

        Integer gnDuration = dateJsonObject.duration as Integer
        Date gnBeginDate = new Date().parse(dateFormat, dateJsonObject.beginDate)

        // TIME SERVICE CALL
        TimeService timeService = new TimeService()
        // Pastscene
        for(pastsceneJson in dateJsonObject.pastscenes) {
            // Create pastceneTime
            PastsceneTime pastsceneTime = new PastsceneTime()
            pastsceneTime.relativeDateValue = null
            if (pastsceneJson.relativeTime != "") {pastsceneTime.relativeDateValue = pastsceneJson.relativeTime as Integer}
            pastsceneTime.relativeDateUnit = null
            if (pastsceneJson.relativeDateUnit != "") {pastsceneTime.relativeDateUnit = pastsceneJson.relativeTimeUnit}
            pastsceneTime.absoluteYear = null
            if (pastsceneJson.absoluteYear != "") {pastsceneTime.absoluteYear = pastsceneJson.absoluteYear as Integer}
            pastsceneTime.absoluteMonth = null
            if (pastsceneJson.absoluteMonth != "") {pastsceneTime.absoluteMonth = pastsceneJson.absoluteMonth as Integer}
            pastsceneTime.absoluteDay = null
            if (pastsceneJson.absoluteDay != "") {pastsceneTime.absoluteDay = pastsceneJson.absoluteDay as Integer}
            pastsceneTime.absoluteHour = null
            if (pastsceneJson.absoluteHour != "") {pastsceneTime.absoluteHour = pastsceneJson.absoluteHour as Integer}
            pastsceneTime.absoluteMinute = null
            if (pastsceneJson.absoluteMinute != "") {pastsceneTime.absoluteMinute = pastsceneJson.absoluteMinute as Integer}

            // Call service
            pastsceneTime = timeService.pastsceneRealDate(pastsceneTime, gnBeginDate)

            // Update json
            JSONObject date = new JSONObject();
            if (pastsceneTime.absoluteYear != null) {date.put("year", pastsceneTime.absoluteYear as String)}
            if (pastsceneTime.absoluteMonth != null) {date.put("month", pastsceneTime.absoluteMonth as String)}
            if (pastsceneTime.absoluteDay != null) {date.put("day", pastsceneTime.absoluteDay as String)}
            if (pastsceneTime.absoluteHour != null) {date.put("hours", pastsceneTime.absoluteHour as String)}
            if (pastsceneTime.absoluteMinute != null) {date.put("minutes", pastsceneTime.absoluteMinute as String)}


            pastsceneJson.put("date", date)
        }
        // Event
        for(eventJson in dateJsonObject.events) {
            // Create eventTime
            EventTime eventTime = new EventTime()
            eventTime.timing = eventJson.timing as Integer

            // Call service
            eventTime = timeService.eventRealDate(eventTime, gnBeginDate, gnDuration)

            // Update json
            JSONObject date = new JSONObject();
            if (eventTime.absoluteYear != null) {date.put("year", eventTime.absoluteYear as String)}
            if (eventTime.absoluteMonth != null) {date.put("month", eventTime.absoluteMonth as String)}
            if (eventTime.absoluteDay != null) {date.put("day", eventTime.absoluteDay as String)}
            if (eventTime.absoluteHour != null) {date.put("hours", eventTime.absoluteHour as String)}
            if (eventTime.absoluteMinute != null) {date.put("minutes", eventTime.absoluteMinute as String)}

            eventJson.put("date", date)
        }
        // END TIME SERVICE CALL

        return dateJsonObject
    }
}
