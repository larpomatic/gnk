package org.gnk.importation

import org.gnk.naming.Firstname
import org.gnk.naming.FirstnameHasTag
import org.gnk.naming.Name
import org.gnk.naming.NameHasTag
import org.gnk.parser.GNKDataContainerService
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceHasTag

import org.gnk.resplacetime.Resource
import org.gnk.resplacetime.ResourceHasTag

import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.roletoperso.RoleHasTag
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag

import org.gnk.tag.Tag
//import org.gnk.tag.TagFamily

/**
 * Created with IntelliJ IDEA.
 * User: Aurib
 * Date: 28/11/13
 * Time: 21:13
 * To change this template use File | Settings | File Templates.
 */
class GNKImportationService {

    def void saveDTD(GNKDataContainerService dataContainer) {
        // save PLACES
        dataContainer.placeMap.each {id, place ->
            savePlace(place);
        }
        // save RESOURCES
        dataContainer.resourceMap.each {id, resource ->
            saveResource(resource)
        }
        // save FIRSTNAMES
        dataContainer.firstnameMap.each {id, firstname ->
            saveFirstname(firstname)
        }
        // save NAMES
        dataContainer.nameMap.each {id, name ->
            saveName(name)
        }

        // save PLOTS
       /* dataContainer.plotMap.each {id, plot ->
            savePlot(plot)
        }*/
    }

    /* Saving Methods */
    private  void savePlace(Place place){
        Place placeBDD = Place.findByName(place.name);
        if (placeBDD){
            // Update fields
            placeBDD.DTDId = place.DTDId
            placeBDD.description = place.description
            placeBDD.gender = place.gender
        }
        else
            placeBDD = place

        // Update placeHasTags
        List<PlaceHasTag> extTags = []
        place.extTags.each { placeHasTag ->
            placeHasTag.tag = getTag(placeHasTag.tag)
            extTags.add(placeHasTag)
        }

        placeBDD.extTags = []
        extTags.each { placeHasTag ->
            placeBDD.addToExtTags(placeHasTag)
        }
    }

    private void saveResource(Resource resource) {
        Resource resourceBDD = Resource.findByName(resource.name);
        if (resourceBDD){
            // Update fields
            resourceBDD.DTDId = resource.DTDId
            resourceBDD.description = resource.description
            resourceBDD.gender = resource.gender
        }
        else
            resourceBDD = resource

        // Update resourceHasTags
        List<ResourceHasTag> extTags = []
        resource.extTags.each { resourceHasTag ->
            resourceHasTag.tag = getTag(resourceHasTag.tag)
            extTags.add(resourceHasTag)
        }

        resourceBDD.extTags = []
        extTags.each { resourceHasTag ->
            resourceBDD.addToExtTags(resourceHasTag)
        }
    }

    private void saveFirstname(Firstname firstname) {
        Firstname firstnameBDD = Firstname.findByName(firstname.name);
        if (firstnameBDD){
            // Update fields
            firstnameBDD.DTDId = firstname.DTDId
            firstnameBDD.gender = firstname.gender
        }
        else
            firstnameBDD = firstname

        // Update firstnameHasTags
        List<FirstnameHasTag> extTags = []
        firstname.extTags.each { firstnameHasTag ->
            firstnameHasTag.tag = getTag(firstnameHasTag.tag)
            extTags.add(firstnameHasTag)
        }

        firstnameBDD.extTags = []
        extTags.each { firstnameHasTag ->
            firstnameBDD.addToExtTags(firstnameHasTag)
        }
    }

    private void saveName(Name name) {
        Name nameBDD = Name.findByName(name.name);
        if (nameBDD){
            // Update fields
            nameBDD.DTDId = name.DTDId
            nameBDD.gender = name.gender
        }
        else
            nameBDD = name

        // Update nameHasTags
        List<NameHasTag> extTags = []
        name.extTags.each { nameHasTag ->
            nameHasTag.tag = getTag(nameHasTag.tag)
            extTags.add(nameHasTag)
        }

        nameBDD.extTags = []
        extTags.each { nameHasTag ->
            nameBDD.addToExtTags(nameHasTag)
        }
    }

    private void savePlot(Plot plot) {
        Plot plotBDD = Plot.findByName(plot.name);
        if (plotBDD){
            // Update fields
            plotBDD.DTDId = plot.DTDId
            plotBDD.description = plot.description
            plotBDD.isDraft = plot.isDraft
            plotBDD.isEvenemential = plot.isEvenemential
            plotBDD.isPublic = plot.isPublic
            plotBDD.dateCreated = plot.dateCreated
            plotBDD.lastUpdated = plot.lastUpdated
        }
        else
            plotBDD = plot

        // Update plotHasTags
        List<PlotHasTag> extTags = []
        plot.extTags.each { plotHasTag ->
            plotHasTag.tag = getTag(plotHasTag.tag)
            extTags.add(plotHasTag)
        }
        plotBDD.extTags = []
        extTags.each { plotHasTag ->
            plotBDD.addToExtTags(plotHasTag)
        }

        // Update PlotHasUniverse
//        List<PlotHasUnivers> plotHasUniversesList = []
//        plot.plotHasUniverses.each {plotHasUnivers ->
//            plotHasUnivers.univers = getUnivers(plotHasUnivers.univers)
//            plotHasUniversesList.add(plotHasUnivers)
//        }
 //       plotBDD.plotHasUniverses = []
 //       plotHasUniversesList.each {plotHasUniverse ->
 //           plotBDD.addToPlotHasUniverses(plotHasUniverse)
  //      }

        // Update Events
        List<Event> events = []

        for (Event event : plot.events)
        {
            if (event)
                events.add(getEvent(event))
        }
        /*plot.events.each {event ->
            events.add(getEvent(event))
        }*/
        plotBDD.events = []
        events.each {event ->
            plotBDD.addToEvents(event)
        }

        // Update PastScenes
        List<Pastscene> pastscenes = []
        plot.pastescenes.each { pastscene ->
            pastscenes.add(getPastscene(pastscene))
        }
        plotBDD.pastescenes = []
        pastscenes.each { pastscene ->
            plotBDD.addToPastescenes(pastscene)
        }

        // Update Roles
        List<Role> roles = []
        plot.roles.each { role ->
            roles.add(getRole(role))
        }
        plotBDD.roles = []
        roles.each { role ->
            plotBDD.addToRoles(role)
        }
    }

    private Tag getTag(Tag tag){
        Tag tagBDD = null

//        TagFamily tagFamily = TagFamily.findByValue(tag.tagFamily.value)
//        if (tagFamily)  {
//            tagFamily.relevantPlot = tag.tagFamily.relevantPlot
//            tagFamily.relevantResource = tag.tagFamily.relevantResource
//            tagFamily.relevantRole = tag.tagFamily.relevantRole
//            tag.tagFamily = tagFamily
//            tagBDD = Tag.findByNameAndTagFamily(tag.name, tagFamily)
//        }
//        else
            tagBDD = Tag.findByName(tag.name)

        if (!tagBDD)
            tagBDD = tag

//        tagBDD.tagFamily = tagFamily

        return tagBDD
    }

    private Tag getUnivers(Tag univers){
        Tag universBDD = Tag.findByName(univers.name)

        if (!universBDD)
            universBDD = univers

        return universBDD
    }

    private Event getEvent(Event event){
        Event eventBDD = Event.findByName(event.name)

        if (eventBDD) {
            eventBDD.setDTDId(event.getDTDId())
            eventBDD.absoluteDay = event.absoluteDay
            eventBDD.absoluteHour = event.absoluteHour
            eventBDD.absoluteMinute = event.absoluteMinute
            eventBDD.absoluteMonth = event.absoluteMonth
            eventBDD.absoluteYear = event.absoluteYear
            eventBDD.description = event.description
            eventBDD.duration = event.duration
            eventBDD.isPlanned = event.isPlanned
            eventBDD.timing = event.timing
        }
        else
            eventBDD = event

        eventBDD.eventPredecessor = getEvent(event.eventPredecessor)
        eventBDD.genericPlace = getGenericPlace(event.genericPlace)
        return eventBDD
    }

    private GenericPlace getGenericPlace(GenericPlace genericPlace){
        GenericPlace genericPlaceBDD = GenericPlace.findByCode(genericPlace.code)

        if (genericPlaceBDD) {
            genericPlaceBDD.setDTDId(GenericPlace.getDTDId())
            genericPlaceBDD.comment = genericPlace.comment
        }
        else
            genericPlaceBDD = genericPlace

        // Update genericPlaceHasTags
        List<GenericPlaceHasTag> extTags = []
        genericPlaceBDD.extTags.each { genericPlaceHasTag ->
            genericPlaceHasTag.tag = getTag(genericPlaceHasTag.tag)
            extTags.add(genericPlaceHasTag)
        }
        genericPlaceBDD.extTags = []
        extTags.each { genericPlaceHasTag ->
            genericPlaceBDD.addToExtTags(genericPlaceHasTag)
        }
        
        //genericPlaceBDD.bannedPlaces
        List<Place> bannedPlaces = []
        genericPlace.bannedPlaces.each { place ->
            bannedPlaces.add(getPlace(place))
        }
        genericPlaceBDD.events = []
        bannedPlaces.each {place ->
            genericPlaceBDD.bannedPlaces.add(place)
        }
        
        //genericPlaceBDD.proposedPlaces
        List<Place> proposedPlaces = []
        genericPlace.proposedPlaces.each { place ->
            proposedPlaces.add(getPlace(place))
        }
        genericPlaceBDD.events = []
        proposedPlaces.each {place ->
            genericPlaceBDD.proposedPlaces.add(place)
        }
        
        //genericPlaceBDD.selectedPlace
        genericPlaceBDD.selectedPlace = getPlace(genericPlace.selectedPlace)

        return genericPlaceBDD
    }

    private Place getPlace(Place place){
        Place placeBDD = Place.findByName(place.name)

        if (placeBDD) {
            placeBDD.setDTDId(place.getDTDId())
            placeBDD.description = place.description
            placeBDD.gender = place.gender
        }
        else
            placeBDD = place

        // Update placeHasTags
        List<PlaceHasTag> extTags = []
        placeBDD.extTags.each { placeHasTag ->
            placeHasTag.tag = getTag(placeHasTag.tag)
            extTags.add(placeHasTag)
        }
        placeBDD.extTags = []
        extTags.each { placeHasTag ->
            placeBDD.addToExtTags(placeHasTag)
        }
        
//        // Update placeHasUniverses
//        List<PlaceHasUnivers> placeHasUniverses = []
//        place.placeHasUniverses.each { placeHasUnivers ->
//            placeHasUnivers.univers = getUnivers(placeHasUnivers.univers)
//            placeHasUniverses.add(placeHasUnivers)
//        }
        placeBDD.placeHasUniverses = []
        placeHasUniverses.each {placeHasUniverse ->
            placeBDD.addToPlaceHasUniverses(placeHasUniverse)
        }

        return placeBDD
    }

    private Pastscene getPastscene(Pastscene pastscene) {
        Pastscene pastsceneBDD = Pastscene.findByTitle(pastscene.title)

        if (pastsceneBDD){
            pastsceneBDD.setDTDId(pastscene.getDTDId())
            pastsceneBDD.absoluteDay = pastscene.description
            pastsceneBDD.absoluteHour = pastscene.absoluteHour
            pastsceneBDD.absoluteMinute = pastscene.absoluteMinute
            pastsceneBDD.absoluteMonth = pastscene.absoluteMonth
            pastsceneBDD.absoluteYear = pastscene.absoluteYear
            pastsceneBDD.description = pastscene.description
            pastsceneBDD.dateDay = pastscene.dateDay
            pastsceneBDD.dateHour = pastscene.dateHour
            pastsceneBDD.dateMinute = pastscene.dateMinute
            pastsceneBDD.dateMonth = pastscene.dateMonth
            pastsceneBDD.dateYear = pastscene.dateYear
            pastsceneBDD.isPublic = pastscene.isPublic
            pastsceneBDD.timingRelative = pastscene.timingRelative
            pastsceneBDD.unitTimingRelative = pastscene.unitTimingRelative
        }
        else
            pastsceneBDD = pastscene

        pastsceneBDD.pastscenePredecessor = getPastscene(pastscene.pastscenePredecessor)
        pastsceneBDD.genericPlace = getGenericPlace(pastscene.genericPlace)
        
        return pastsceneBDD
    }

    private Role getRole(Role role) {
        Role roleBDD = Role.findByCode(role.code)

        if (roleBDD){
            roleBDD.setDTDId(role.getDTDId())
            roleBDD.description = role.description
            roleBDD.pipi = role.pipi
            roleBDD.pipr = role.pipr
            roleBDD.type = role.type
        }
        else
            roleBDD = role

        // Update roleHasTags
        List<RoleHasTag> roleHasTags = []
        role.roleHasTags.each { roleHasTag ->
            roleHasTag.tag = getTag(roleHasTag.tag)
            roleHasTags.add(roleHasTag)
        }
        roleBDD.roleHasTags = []
        roleHasTags.each { roleHasTag ->
            roleBDD.addToRoleHasTags(roleHasTag)
        }

        // Update roleHasEvents
        List<RoleHasEvent> roleHasEvents = []
        role.roleHasEvents.each { roleHasEvent ->
            roleHasEvent.event = getEvent(roleHasEvent.event)

            // Update roleHasEventHasGenericResources
            List<RoleHasEventHasGenericResource> genericResources = []
            roleHasEvent.roleHasEventHasGenericResources.each {rhehgenericResource ->
                rhehgenericResource.genericResource = getGenericResource(rhehgenericResource.genericResource)
                genericResources.add(rhehgenericResource)
            }
            roleHasEvent.roleHasEventHasGenericResources = []
            genericResources.each { rhehgenericResource ->
                roleHasEvent.addToRoleHasEventHasGenericResources(rhehgenericResource)
            }

            roleHasEvents.add(roleHasEvent)
        }
        roleBDD.roleHasEvents = []
        roleHasEvents.each { roleHasEvent ->
            roleBDD.addToRoleHasEvents(roleHasEvent)
        }

        // Update roleHasPastscenes
        List<RoleHasPastscene> pastscenes = []
        role.roleHasPastscenes.each { roleHasPastscene ->
            roleHasPastscene.pastscene = getPastscene(roleHasPastscene.pastscene)
            pastscenes.add(roleHasPastscene)
        }
        roleBDD.roleHasPastscenes = []
        pastscenes.each { roleHasPastscene ->
            roleBDD.addToRoleHasPastscenes(roleHasPastscene)
        }

        // FIXME : Update roleRelations
        // Update roleHasRelationWithRolesForRole1Id
        // Update roleHasRelationWithRolesForRole2Id

        // TODO when BDD fixed: Update genericTextualCluesForFromRoleId
        // TODO when BDD fixed: Update genericTextualCluesForPossededByRoleId
        // TODO when BDD fixed: Update genericTextualCluesForToRoleId
        
        return roleBDD
    }
    
    private GenericResource getGenericResource (GenericResource genericResource) {
        GenericResource genericResourceBDD = GenericResource.findByCode(genericResource.code)
        
        if (genericResourceBDD) {
            genericResourceBDD.setDTDId(genericResource.getDTDId())
            genericResourceBDD.comment = genericResource.comment
        }
        else
            genericResourceBDD = genericResource

        // Update extTags
        List<GenericResourceHasTag> genericResourceHasTags = []
        genericResource.extTags.each { genericResourceHasTag ->
            genericResourceHasTag.tag = getTag(genericResourceHasTag.tag)
            genericResourceHasTags.add(genericResourceHasTag)
        }
        genericResourceBDD.extTags = []
        genericResourceHasTags.each { genericResourceHasTag ->
            genericResourceBDD.addToExtTags(genericResourceHasTag)
        }
        
        // Update bannedResources
        List<Resource> bannedResources = []
        genericResource.bannedResources.each { bannedResource ->
            bannedResources.add(getResource(bannedResource))
        }
        genericResourceBDD.bannedResources = []
        bannedResources.each { bannedResource ->
            genericResourceBDD.bannedResources.add(bannedResource)
        }

        // Update proposedResources
        List<Resource> proposedResources = []
        genericResource.proposedResources.each { proposedResource ->
            proposedResources.add(getResource(proposedResource))
        }
        genericResourceBDD.proposedResources = []
        proposedResources.each { proposedResource ->
            genericResourceBDD.proposedResources.add(proposedResource)
        }

        // Update selectedResource
        genericResourceBDD.selectedResource = getResource(genericResource.selectedResource)
        // Update roleHasEventHasRessources

        return genericResourceBDD
    }
    
    private getResource(Resource resource) {
        Resource resourceBDD = Resource.findByName(resource.name)
        
        if (resourceBDD) {
            resourceBDD.setDTDId(resource.getDTDId())
            resourceBDD.description = resource.description
            resourceBDD.gender = resource.gender
        }
        else
            resourceBDD = resource

        // Updated extTags
        List<ResourceHasTag> resourceHasTags = []
        resource.extTags.each { resourceHasTag ->
            resourceHasTag.tag = getTag(resourceHasTag.tag)
            resourceHasTags.add(resourceHasTag)
        }
        resourceBDD.extTags = []
        resourceHasTags.each { resourceHasTag ->
            resourceBDD.addToExtTags(resourceHasTag)
        }
        
//        // Updated resourceHasUniverses
//        List<ResourceHasUnivers> resourceHasUniverses = []
//        resource.resourceHasUniverses.each { resourceHasUnivers ->
//            resourceHasUnivers.univers = getUnivers(resourceHasUnivers.univers)
//            resourceHasUniverses.add(resourceHasUnivers)
//        }
   //     resourceBDD.resourceHasUniverses = []
    //    resourceHasUniverses.each {resourceHasUniverse ->
      //      resourceBDD.addToResourceHasUniverses(resourceHasUniverse)
    //    }

        return resourceBDD
    }
    /* !Saving Methods */
}
