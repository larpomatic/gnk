package org.gnk.substitution

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.gn.Gn
import org.gnk.naming.Firstname
import org.gnk.naming.Name
import org.gnk.parser.GNKDataContainerService
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.Resource
import org.gnk.resplacetime.GenericResource
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource

class OutputHandler {

    public updateGnWithNaming(GNKDataContainerService gnkDataContainerService, JSONArray charsJSONArray) {
        Gn gnInst = gnkDataContainerService.gn

        Map<String, Firstname> firstnameMap = new HashMap<String, Firstname>()
        Map<String, Name> lastnameMap = new HashMap<String, Name>()

        Integer counterFirstname = 1;
        Integer counterLastname = 1;
        // FirstnameMap and lastnameMap construction with unique firstname and lastname
        for(characterJSON in charsJSONArray) {

            // Selected firstname
            String selectedFirstnameJSON = characterJSON.selectedFirstname
            if(firstnameMap.get(selectedFirstnameJSON) == null) {
                Firstname firstname = new Firstname()
                firstname.name = selectedFirstnameJSON
                firstname.DTDId = counterFirstname
                counterFirstname++
                firstnameMap.put(selectedFirstnameJSON, firstname)
            }

            // Proposed firstnames
            for (firstnameJSON in characterJSON.proposedFirstnames) {
                if(firstnameMap.get(firstnameJSON) == null) {
                    Firstname firstname = new Firstname()
                    firstname.name = firstnameJSON
                    firstname.DTDId = counterFirstname
                    counterFirstname++
                    firstnameMap.put(firstnameJSON, firstname)
                }
            }

            // Banned firstnames
            for (firstnameJSON in characterJSON.bannedFirstnames) {
                if(firstnameMap.get(firstnameJSON) == null) {
                    Firstname firstname = new Firstname()
                    firstname.name = firstnameJSON
                    firstname.DTDId = counterFirstname
                    counterFirstname++
                    firstnameMap.put(firstnameJSON, firstname)
                }
            }

            // Selected lastname
            String selectedLastnameJSON = characterJSON.selectedLastname
            if(lastnameMap.get(selectedLastnameJSON) == null) {
                Name lastname = new Name()
                lastname.name = selectedLastnameJSON
                lastname.DTDId = counterLastname
                counterLastname++
                lastnameMap.put(selectedLastnameJSON, lastname)
            }

            // Proposed lastnames
            for (lastnameJSON in characterJSON.proposedLastnames) {
                if(lastnameMap.get(lastnameJSON) == null) {
                    Name lastname = new Name()
                    lastname.name = lastnameJSON
                    lastname.DTDId = counterLastname
                    counterLastname++
                    lastnameMap.put(lastnameJSON, lastname)
                }
            }

            // Banned lastnames
            for (lastnameJSON in characterJSON.bannedLastnames) {
                if(lastnameMap.get(lastnameJSON) == null) {
                    Name lastname = new Name()
                    lastname.name = lastnameJSON
                    lastname.DTDId = counterLastname
                    counterLastname++
                    lastnameMap.put(lastnameJSON, lastname)
                }
            }
        }

        // Map to set
        Set<Firstname> firstnameSet = new HashSet<Firstname>()
        Set<Name> lastnameSet = new HashSet<Name>()
        for (el in firstnameMap) {
            Firstname firstnameDB = Firstname.findByName(el.value.name)
            if (firstnameDB != null) {
                firstnameDB.DTDId = el.value.DTDId
                firstnameSet.add(firstnameDB)
            }
            else {
                firstnameSet.add(el.value)
            }
        }
        for (el in lastnameMap) {
            Name lastnameDB = Name.findByName(el.value.name)
            if (lastnameDB != null) {
                lastnameDB.DTDId = el.value.DTDId
                lastnameSet.add(lastnameDB)
            }
            else {
                lastnameSet.add(el.value)
            }
        }
        gnInst.firstnameSet = firstnameSet
        gnInst.lastnameSet = lastnameSet

        // Add firstnames and lastnames to each character
        for(characterJSON in charsJSONArray) {
            // Get character in gn instance
            Integer characterGnId = characterJSON.gnId.toInteger()
            Character character = null
            for (characterEl in gnInst.characterSet) {
                if (characterEl.DTDId == characterGnId) {
                    character = characterEl
                    break
                }
            }
            if (character == null) {
                for (characterEl in gnInst.nonPlayerCharSet) {
                    if (characterEl.DTDId == characterGnId) {
                        character = characterEl
                        break
                    }
                }
            }

            if (character != null) {
                // Lists creation
                // Firstnames
                Firstname selectedFirstname
                List<Firstname> proposedFirstnames = []
                List<Firstname> bannedFirstnames = []
                selectedFirstname = firstnameMap.get(characterJSON.selectedFirstname)
                for (firstnameJSON in characterJSON.proposedFirstnames) {
                    proposedFirstnames.add(firstnameMap.get(firstnameJSON))
                }
                for (firstnameJSON in characterJSON.bannedFirstnames) {
                    bannedFirstnames.add(firstnameMap.get(firstnameJSON))
                }
                character.selectedFirstname = selectedFirstname
                character.proposedFirstnames = proposedFirstnames
                character.bannedFirstnames = bannedFirstnames

                // Lastnames
                Name selectedLastname
                List<Name> proposedLastnames = []
                List<Name> bannedLastnames = []
                selectedLastname = lastnameMap.get(characterJSON.selectedLastname)
                for (lastnameJSON in characterJSON.proposedLastnames) {
                    proposedLastnames.add(lastnameMap.get(lastnameJSON))
                }
                for (lastnameJSON in characterJSON.bannedLastnames) {
                    bannedLastnames.add(lastnameMap.get(lastnameJSON))
                }
                character.selectedLastname = selectedLastname
                character.proposedLastnames = proposedLastnames
                character.bannedLastnames = bannedLastnames
            }
        }
    }

    public updateGnWithResources(GNKDataContainerService gnkDataContainerService, JSONArray resourcesJSONArray) {
        Gn gnInst = gnkDataContainerService.gn

        Map<String, Resource> resourceMap = new HashMap<String, Resource>()

        Integer counterResource = 1;
        // ResourceMap construction with unique resource
        for(resourceJSON in resourcesJSONArray) {

            // Selected resource name
            String selectedResourceNameJSON = resourceJSON.selectedName
            if(resourceMap.get(selectedResourceNameJSON) == null) {
                Resource resource = new Resource()
                resource.name = selectedResourceNameJSON
                resource.DTDId = counterResource
                counterResource++
                resourceMap.put(selectedResourceNameJSON, resource)
            }

            // Proposed resource names
            for (resourceNameJSON in resourceJSON.proposedNames) {
                if(resourceMap.get(resourceNameJSON) == null) {
                    Resource resource = new Resource()
                    resource.name = resourceNameJSON
                    resource.DTDId = counterResource
                    counterResource++
                    resourceMap.put(resourceNameJSON, resource)
                }
            }

            // Banned resource names
            for (resourceNameJSON in resourceJSON.bannedNames) {
                if(resourceMap.get(resourceNameJSON) == null) {
                    Resource resource = new Resource()
                    resource.name = resourceNameJSON
                    resource.DTDId = counterResource
                    counterResource++
                    resourceMap.put(resourceNameJSON, resource)
                }
            }
        }

        // ResourceMap to resourceSet
        Set<Resource> resourceSet = new HashSet<Resource>()
        for (el in resourceMap) {
            Resource resourceDB = Resource.findByName(el.value.name)
            if (resourceDB != null) {
                resourceDB.DTDId = el.value.DTDId
                resourceSet.add(resourceDB)
            }
            else {
                resourceSet.add(el.value)
            }
        }
        gnInst.resourceSet = resourceSet

        // Add resources to each generic resource
        for(genericResourceJSON in resourcesJSONArray) {
            // Get generic resource in gn instance
            Integer genericResourceGnPlotId = genericResourceJSON.gnPlotId as Integer
            Integer genericResourceGnId = genericResourceJSON.gnId as Integer
            GenericResource genericResource = findGenericResourceInGN(gnInst, genericResourceGnPlotId, genericResourceGnId)

            // Gn update
            if (genericResource != null) {
                // Resource lists creation
                Resource selectedResource
                List<Resource> proposedResources = []
                List<Resource> bannedResources = []
                selectedResource = resourceMap.get(genericResourceJSON.selectedName)
                for (nameJSON in genericResourceJSON.proposedNames) {
                    proposedResources.add(resourceMap.get(nameJSON))
                }
                for (nameJSON in genericResourceJSON.bannedNames) {
                    bannedResources.add(resourceMap.get(nameJSON))
                }
                // Gn update
                genericResource.selectedResource = selectedResource
                genericResource.proposedResources = proposedResources
                genericResource.bannedResources = bannedResources
            }
        }
    }

    private GenericResource findGenericResourceInGN(Gn gnInst, Integer genericResourceGnPlotId, Integer genericResourceGnId) {
        for(plot in gnInst.selectedPlotSet) {
            if (plot.DTDId == genericResourceGnPlotId) {
                for(Role role : plot.roles) {
                    if (role.roleHasEvents) {
                        for (RoleHasEvent roleHasEvent : role.roleHasEvents) {
                            if (roleHasEvent.roleHasEventHasGenericResources) {
                                for (RoleHasEventHasGenericResource roleHasEventHasGenericResource : roleHasEvent.roleHasEventHasGenericResources) {
                                    if (roleHasEventHasGenericResource.genericResource.DTDId == genericResourceGnId) {
                                        return roleHasEventHasGenericResource.genericResource
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return null
    }

    public updateGnWithPlaces(GNKDataContainerService gnkDataContainerService, JSONArray placesJSONArray) {
        Gn gnInst = gnkDataContainerService.gn

        Map<String, Place> placeMap = new HashMap<String, Place>()

        Integer counterPlace = 1;
        // PlaceMap construction with unique place
        for(placeJSON in placesJSONArray) {

            // Selected place name
            String selectedPlaceNameJSON = placeJSON.selectedName
            if(placeMap.get(selectedPlaceNameJSON) == null) {
                Place place = new Place()
                place.name = selectedPlaceNameJSON
                place.DTDId = counterPlace
                counterPlace++
                placeMap.put(selectedPlaceNameJSON, place)
            }

            // Proposed resource names
            for (placeNameJSON in placeJSON.proposedNames) {
                if(placeMap.get(placeNameJSON) == null) {
                    Place place = new Place()
                    place.name = placeNameJSON
                    place.DTDId = counterPlace
                    counterPlace++
                    placeMap.put(placeNameJSON, place)
                }
            }

            // Banned resource names
            for (placeNameJSON in placeJSON.bannedNames) {
                if(placeMap.get(placeNameJSON) == null) {
                    Place place = new Place()
                    place.name = placeNameJSON
                    place.DTDId = counterPlace
                    counterPlace++
                    placeMap.put(placeNameJSON, place)
                }
            }
        }

        // PlaceMap to placeSet
        Set<Place> placeSet = new HashSet<Place>()
        for (el in placeMap) {
            Place placeDB = Place.findByName(el.value.name)
            if (placeDB != null) {
                placeDB.DTDId = el.value.DTDId
                placeSet.add(placeDB)
            }
            else {
                placeSet.add(el.value)
            }
        }
        gnInst.placeSet = placeSet

        // Add places to each generic place
        for(genericPlaceJSON in placesJSONArray) {
            // Get generic place in gn instance
            Integer genericPlaceGnPlotId = genericPlaceJSON.gnPlotId as Integer
            Integer genericPlaceGnId = genericPlaceJSON.gnId as Integer
            GenericPlace genericPlace = findGenericPlaceInGN(gnInst, genericPlaceGnPlotId, genericPlaceGnId)

            // Gn update
            if (genericPlace != null) {
                // Place lists creation
                Place selectedPlace
                List<Place> proposedPlaces = []
                List<Place> bannedPlaces = []
                selectedPlace = placeMap.get(genericPlaceJSON.selectedName)
                for (nameJSON in genericPlaceJSON.proposedNames) {
                    proposedPlaces.add(placeMap.get(nameJSON))
                }
                for (nameJSON in genericPlaceJSON.bannedNames) {
                    bannedPlaces.add(placeMap.get(nameJSON))
                }
                // Gn update
                genericPlace.selectedPlace = selectedPlace
                genericPlace.proposedPlaces = proposedPlaces
                genericPlace.bannedPlaces = bannedPlaces
            }
        }
    }

    private GenericPlace findGenericPlaceInGN(Gn gnInst, Integer genericPlaceGnPlotId, Integer genericPlaceGnId) {
        for(plot in gnInst.selectedPlotSet) {
            if (plot.DTDId == genericPlaceGnPlotId) {
                for(pastScene in plot.pastescenes) {
                    if (pastScene.genericPlace != null && pastScene.genericPlace.DTDId == genericPlaceGnId) {
                        return pastScene.genericPlace
                    }
                }
                for(event in plot.events) {
                    if (event.genericPlace != null && event.genericPlace.DTDId == genericPlaceGnId) {
                        return event.genericPlace
                    }
                }
            }
        }

        return null
    }

    public updateGnWithDates(GNKDataContainerService gnkDataContainerService, JSONObject datesJSON) {
        Gn gnInst = gnkDataContainerService.gn

        JSONArray pastscenesJSONArray = datesJSON.pastscenes
        JSONArray eventsJSONArray = datesJSON.events

        // Update pastcenes
        for(pastsceneJSON in pastscenesJSONArray) {
            Integer pastsceneGnPlotId = pastsceneJSON.gnPlotId as Integer
            Integer pastsceneGnId = pastsceneJSON.gnId as Integer
            // Find pastscene in gn
            Pastscene pastscene = findGenericPastsceneInGN(gnInst, pastsceneGnPlotId, pastsceneGnId)
            // Update pastscene absolute date
            JSONObject dateJSON = pastsceneJSON.date
            if (dateJSON.year != null ) {pastscene.absoluteYear = dateJSON.year as Integer}
            if (dateJSON.month != null ) {pastscene.absoluteMonth = dateJSON.month as Integer}
            if (dateJSON.day != null ) {pastscene.absoluteDay = dateJSON.day as Integer}
            if (dateJSON.hours != null ) {pastscene.absoluteHour = dateJSON.hours as Integer}
            if (dateJSON.minutes != null ) {pastscene.absoluteMinute = dateJSON.minutes as Integer}
        }

        // Update events
        for(eventJSON in eventsJSONArray) {
            Integer eventGnPlotId = eventJSON.gnPlotId as Integer
            Integer eventGnId = eventJSON.gnId as Integer
            // Find pastscene in gn
            Event event = findGenericEventInGN(gnInst, eventGnPlotId, eventGnId)
            // Update event absolute date
            JSONObject dateJSON = eventJSON.date
            if (dateJSON.year != null ) {event.absoluteYear = dateJSON.year as Integer}
            if (dateJSON.month != null ) {event.absoluteMonth = dateJSON.month as Integer}
            if (dateJSON.day != null ) {event.absoluteDay = dateJSON.day as Integer}
            if (dateJSON.hours != null ) {event.absoluteHour = dateJSON.hours as Integer}
            if (dateJSON.minutes != null ) {event.absoluteMinute = dateJSON.minutes as Integer}
        }
    }

    private Pastscene findGenericPastsceneInGN(Gn gnInst, Integer pastsceneGnPlotId, Integer pastsceneGnId) {
        for(plot in gnInst.selectedPlotSet) {
            if (plot.DTDId == pastsceneGnPlotId) {
                for(pastScene in plot.pastescenes) {
                    if (pastScene.DTDId == pastsceneGnId) {
                        return pastScene
                    }
                }
            }
        }

        return null
    }
    private Event findGenericEventInGN(Gn gnInst, Integer eventGnPlotId, Integer eventGnId) {
        for(plot in gnInst.selectedPlotSet) {
            if (plot.DTDId == eventGnPlotId) {
                for(event in plot.events) {
                    if (event.DTDId == eventGnId) {
                        return event
                    }
                }
            }
        }

        return null
    }
}
