package org.gnk.parser

import org.gnk.gn.Gn
import org.gnk.importation.GNKImportationService
import org.gnk.naming.Firstname
import org.gnk.naming.Name
import org.gnk.parser.gn.GnXMLReaderService
import org.gnk.parser.naming.FirstnameXMLReaderService
import org.gnk.parser.naming.NameXMLReaderService
import org.gnk.parser.naming.SubstitutionNamingXMLReaderService
import org.gnk.parser.place.PlaceXMLReaderService
import org.gnk.parser.place.SubstitutionPlaceXMLReaderService
import org.gnk.parser.plot.PlotXMLReaderService
import org.gnk.parser.resource.ResourceXMLReaderService
import org.gnk.parser.resource.SubstitutionResourceXMLReaderService
import org.gnk.resplacetime.*
import org.gnk.roletoperso.Role
import org.gnk.selectintrigue.Plot
import org.springframework.web.multipart.MultipartFile

class GNKDataContainerService {

    Integer plot_step = -1
    Map<Integer, Plot> plotMap
    Integer place_step = -1
    Map<Integer, Place> placeMap
    Integer resource_step = -1
    Map<Integer, Resource> resourceMap
    Integer firstname_step = -1
    Map<Integer, Firstname> firstnameMap
    Integer name_step = -1
    Map<Integer, Name> nameMap
    Map<Integer, GenericResource> genericResourceMap
    Map<Integer, GenericPlace> genericPlaceMap
//    Map<Integer, GenericTextualClue> genericTextualClueMap
    Gn gn

    /* Exposed Methods */
    def String ImportDTD(MultipartFile file)
    {
        String fileContent = file.getInputStream().getText().replaceFirst("^([\\W]+)<","<")

        //String fileContent = fileStream.getText()
        Gn gnTmp = new Gn()
        gnTmp.dtd = fileContent
        SaveDTD(gnTmp)

        return "importation terminée"
    }

    def void ReadDTD(String dtd) {
        Gn gnTmp = new Gn()
        gnTmp.dtd = dtd
        ReadDTD(gnTmp)
    }

    def void ReadDTD(Gn gnPar) {
        gn = gnPar

        // GNK Data reader
        Node GNK = new XmlParser().parseText(gn.dtd)
        assert (GNK != null)
        if (GNK == null)
            return

        if (GNK.attribute("dtd_version") != "null")
            gn.version = Float.parseFloat(GNK.attribute("dtd_version") as String).toInteger()

        assert (GNK.GN_DATA.size() <= 1)
        if (GNK.GN_DATA.size() <= 0)
            return

        Node GN_DATA = GNK.GN_DATA[0]
        genericResourceMap = new HashMap<>()
        genericPlaceMap = new HashMap<>()
//        genericTextualClueMap = new HashMap<>()

        // PLACES reader
        readPlaces(GN_DATA)
        // RESOURCES reader
        readResource(GN_DATA)
        // FIRSTNAMES reader
        readFirstname(GN_DATA)
        // NAMES reader
        readName(GN_DATA)

        // PLOTS reader
        readPlots(GN_DATA)

        // GN_INFORMATION reader
        new GnXMLReaderService().ReadGnDTD(GNK, gn, this)

        // GN_DEFINITION
        //can be commented if gn.step no longer take the "publication" value
        if (gn.step.equals("publication")) {
            readSubstitution(GNK)
        }
    }

    def void SaveDTD(String dtd) {
        Gn gnTmp = new Gn()
        gnTmp.dtd = dtd
        SaveDTD(gnTmp)
    }

    def void SaveDTD(Gn gnPar) {
        ReadDTD(gnPar)
        GNKImportationService importationService = new GNKImportationService()
        importationService.saveDTD(this)
//        // Gn.findWhere(name: "toto") : What's the point ?
//
////        // TODO: Save PLACES
////        placeMap.each {id, place ->
////            if (!place.save(flush: true))
////                place.errors.each {
////                    println it
////                }
////        }
////        // TODO: Save RESOURCES
////        resourceMap.each {id, resource ->
////            if (!resource.save(flush: true))
////                resource.errors.each {
////                    println it
////                }
////        }
//        // Save FIRSTNAMES
//        firstnameMap.each {id, firstname ->
//            if (!firstname.save(flush: true))
//                firstname.errors.each {
//                    println it
//                }
//        }
//        // Save NAMES
//        nameMap.each {id, name ->
//            if (!name.save(flush: true))
//                name.errors.each {
//                    println it
//                }
//        }
//
//
//        // Save GenericResources
//        genericResourceMap.each { id, genericResource ->
//            // FIXME: comment fusionner BDD et DTD ?
////            GenericResource tmp = GenericResource.findByCode(genericResource.code)
////            if (tmp != null)
////
//            //if (GenericResource.get(code: genericResource.code) == null)
//                if (!genericResource.save(flush: true))
//                    genericResource.errors.each  { println it }
//        }
//
//        // Save GenericPlaces
//        genericPlaceMap.each { id, genericPlace ->
//            if (!genericPlace.save(flush: true))
//                genericPlace.errors.each { println it }
//        }
//
//        // TODO SAVE GenericTextualClues
////        genericTextualClueMap.each { id, genericTextualClue ->
////            if (!genericTextualClue.save())
////                genericTextualClue.errors.each { println it }
////        }
//
//        // Save PLOTS
//        plotMap.each {id, plot ->
//            if (!plot.save(flush: true))
//                plot.errors.each { println it }
//            //SavePlot(plot)
//        }
    }
    /* !Exposed Methods */

    /* Data access */

    def Plot getPlot(Integer DTDId) {
        assert (plotMap != null)
        assert (DTDId != null)
        if (DTDId == null || plotMap == null) {
            return null;
        }
        return plotMap.get(DTDId)
    }

    def Place getPlace(Integer DTDId) {
        assert (placeMap != null)
        return placeMap == null ? null : placeMap.get(DTDId)
    }

    def Resource getResource (Integer DTDId){
        assert (resourceMap != null)
        return resourceMap == null ? null : resourceMap.get(DTDId)
    }

    def Firstname getFirstname (Integer DTDId){
        assert (firstnameMap != null)
        return firstnameMap == null ? null : firstnameMap.get(DTDId)
    }

    def Name getName (Integer DTDId){
        assert (nameMap != null)
        return nameMap == null ? null : nameMap.get(DTDId)
    }

    def Collection<GenericResource> getGenericResources() {
        return  genericResourceMap.values()
    }

    def Collection<GenericPlace> getGenericPlaces() {
        return  genericPlaceMap.values()
    }
    /* !Data access */

    def Role getRole(Integer roleId, Integer plotId) {
        assert (plotMap != null)
        assert (roleId != null && plotId != null)
        if (roleId == null || plotId == null || plotMap == null)
            return null
        Plot plot = plotMap.get(plotId)
        assert (plot != null)
        if (plot == null)
            return null
        for (Role role : plot.roles) {
            if (role.DTDId == roleId)
                return role
        }
        return null
    }

    //copies a place, adds the copy to the placeMap and returns the copy
    public Place copyAndAddPlaceToPlaceMap(Place placeToCopy)
    {
        Place placeCopy = new Place();

        placeCopy.setId(placeToCopy.getId());
        placeCopy.setDescription(placeToCopy.getDescription());
        placeCopy.setGender(placeToCopy.getGender());
        placeCopy.setName(placeToCopy.getName());
        placeCopy.setDateCreated(placeToCopy.getDateCreated());
        placeCopy.setDTDId(placeToCopy.getDTDId());

        placeMap.put(placeMap.size() + 1, placeCopy);

        return placeCopy;

    }


    /* Data preparation */

    private void readPlots(Node GN_DATA) {
        plotMap = new HashMap<Integer, Plot>()

        // PLOTS reader
        assert (GN_DATA.PLOTS.size() <= 1)
        if (GN_DATA.PLOTS.size() <= 0)
            return

        Node PLOTS = GN_DATA.PLOTS[0]
        if (PLOTS.attribute("step_id") != "null" && !PLOTS.attribute("step_id").equals(""))
            plot_step = PLOTS.attribute("step_id") as Integer

        PlotXMLReaderService plotReader = new PlotXMLReaderService()
        NodeList PLOTLIST = PLOTS.PLOT
        for (int i = 0; i < PLOTLIST.size(); i++) {
            // PLOT reader
            Node PLOT = PLOTLIST.get(i)
            Plot plotStored = plotReader.getPlotFromNode(PLOT, this)

            // Insert plot into plotMap
            assert (plotStored != null)
            if (plotStored != null) {
                plotMap.put(plotStored.getDTDId(), plotStored)
            }
        }

    }

    private void readPlaces(Node GN_DATA) {
        placeMap = new HashMap<Integer, Place>()

        // PLACES reader
        if (GN_DATA.PLACES.size() <= 0) {
            return;
        }
        Node PLACES = GN_DATA.PLACES[0]
        if (PLACES.attribute("step_id") != "null")
            place_step = PLACES.attribute("step_id") as Integer

        PlaceXMLReaderService placeReader = new PlaceXMLReaderService()
        NodeList PLACELIST = PLACES.PLACE
        for (int i = 0; i < PLACELIST.size(); ++i) {
            // PLACE reader
            Node PLACE = PLACELIST.get(i)
            Place placeStored = placeReader.getPlaceFromNode(PLACE, this)

            // Insert place into placeMap
            assert (placeStored != null)
            if (placeStored != null)
            {
                if (!placeStored.getDTDId() || placeStored.getDTDId() < 0)
                    placeStored.setDTDId(placeStored.getId())
                placeMap.put(placeStored.getDTDId(), placeStored)
            }

        }
    }

    private void readResource (Node GN_DATA) {
        resourceMap = new HashMap<Integer, Resource>()

        // RESOURCE reader
        if (GN_DATA.RESOURCES.size() <= 0) {
            return;
        }
        Node RESOURCES = GN_DATA.RESOURCES[0]
        if (RESOURCES.attribute("step_id") != "null")
            resource_step = RESOURCES.attribute("step_id") as Integer

        ResourceXMLReaderService resourceReader = new ResourceXMLReaderService()
        NodeList RESOURCELIST = RESOURCES.RESOURCE
        for (int i = 0; i < RESOURCELIST.size(); ++i) {
            // RESOURCE reader
            Node RESOURCE = RESOURCELIST.get(i)
            Resource resourceStored = resourceReader.getResourceFromNode(RESOURCE, this)

            // Insert resource into resourceMap
            assert (resourceStored != null)
            if (resourceStored != null)
                resourceMap.put(resourceStored.getDTDId(), resourceStored)
        }
    }

    private void readSubstitution (Node GNK) {
		assert (GNK.GN_DEFINITION.SUBSTITUTION.size() <= 1)
        if (GNK.GN_DEFINITION.SUBSTITUTION.size() <= 0) {
            return;
        }
		
		// TODO : Faire les appels des diff�rentes parties de substitution

        assert (GNK.GN_DEFINITION.SUBSTITUTION[0].PLACES.size() <= 1)
        if (GNK.GN_DEFINITION.SUBSTITUTION[0].PLACES.size() <= 0)
            return

        Node PLACES = GNK.GN_DEFINITION.SUBSTITUTION[0].PLACES[0]

        //assert (PLACES != null)

        SubstitutionNamingXMLReaderService.readSubstitutionNamingNode(GNK, gn, firstnameMap, nameMap)
		SubstitutionPlaceXMLReaderService.getSubstitutedPlaceFromNode(PLACES, this)
        SubstitutionResourceXMLReaderService.getSubstitutedResourceFromNode(GNK.GN_DEFINITION.SUBSTITUTION[0].RESOURCES[0], this)
		// time


    }

    private void readFirstname (Node GN_DATA) {
        firstnameMap = new HashMap<Integer, Firstname>()

        // FIRSTNAME reader
        assert (GN_DATA.FIRSTNAMES.size() <= 1)
        if (GN_DATA.FIRSTNAMES.size() <= 0) {
            return;
        }
        Node FIRSTNAMES = GN_DATA.FIRSTNAMES[0]
        if (FIRSTNAMES.attribute("step_id") != "null")
            firstname_step = FIRSTNAMES.attribute("step_id") as Integer

        FirstnameXMLReaderService firstnameReader = new FirstnameXMLReaderService()
        NodeList FIRSTNAMELIST = FIRSTNAMES.FIRSTNAME
        for (int i = 0; i < FIRSTNAMELIST.size(); ++i) {
            // FIRSTNAME reader
            Node FIRSTNAME = FIRSTNAMELIST.get(i)
            Firstname firstnameStored = firstnameReader.getFirstnameFromNode(FIRSTNAME, this)

            // Insert firstname into firstnameMap
            assert (firstnameStored != null)
            if (firstnameStored != null)
                firstnameMap.put(firstnameStored.DTDId, firstnameStored)
        }
    }

    private void readName (Node GN_DATA) {
        nameMap = new HashMap<Integer, Name>()

        // NAME reader
        assert (GN_DATA.NAMES.size() <= 1)
        if (GN_DATA.NAMES.size() <= 0) {
            return;
        }
        Node NAMES = GN_DATA.NAMES[0]
        if (NAMES.attribute("step_id") != "null")
            name_step = NAMES.attribute("step_id") as Integer

        NameXMLReaderService nameReader = new NameXMLReaderService()
        NodeList NAMELIST = NAMES.NAME
        for (int i = 0; i < NAMELIST.size(); ++i) {
            // NAME reader
            Node NAME = NAMELIST.get(i)
            Name nameStored = nameReader.getNameFromNode(NAME)

            // Insert name into nameMap
            assert (nameStored != null)
            if (nameStored != null)
                nameMap.put(nameStored.DTDId, nameStored)
        }
    }
    /* !Data preparation */
}
