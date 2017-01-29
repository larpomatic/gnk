package org.gnk.gn.redactintrigue

import org.apache.poi.hwpf.usermodel.DateAndTime
import org.docx4j.convert.out.pdf.PdfConversion
import org.docx4j.convert.out.pdf.viaXSLFO.Conversion
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.Tbl
import org.gnk.description.PrintDescriptionService
import org.gnk.publication.WordWriter
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.GnConstant
import org.gnk.resplacetime.GnConstantController
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceService
import org.gnk.ressplacetime.ReferentialPlace
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.roletoperso.RoleHasRelationWithRole
import org.gnk.roletoperso.RoleHasTag
import org.gnk.roletoperso.RoleRelationType
import org.gnk.selectintrigue.Description
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.gnk.tag.Tag
import org.gnk.tag.TagService
import org.gnk.resplacetime.GenericPlaceController

import org.gnk.user.User
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder

import static org.gnk.resplacetime.GenericPlaceController.*

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class RedactIntrigueController {
    PlaceService placeService

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		[plotInstanceList: Plot.list(), plotInstanceTotal: Plot.count()]
	}

	def create() {
		[plotInstance: new Plot(params)]
	}

	def saveAndEdit() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = user.getUsername();
        User currentUser = User.findByUsername(currentUsername);
		def plotInstance = new Plot(params)
		plotInstance.dateCreated = new Date();
		plotInstance.description = "";
		plotInstance.isEvenemential = false;
		plotInstance.isMainstream = false;
		plotInstance.isPublic = false;
		plotInstance.isDraft = true;
		plotInstance.lastUpdated = new Date();
        if (!plotInstance.user)
		plotInstance.user = currentUser;
		if (!plotInstance.save(flush: true)) {
			render(view: "create", model: [plotInstance: plotInstance])
			return
		}
		redirect(action: "edit", id: plotInstance.id)
	}

	def save() {
		Object obj = params
		def plotInstance = new Plot(params)
		if (!plotInstance.save(flush: true)) {
			render(view: "create", model: [plotInstance: plotInstance])
			return
		}
		redirect(action: "show", id: plotInstance.id)
	}

	def edit(Long id) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
			redirect(action: "list")
			return
		}
		int screen = 0
		if (params.screenStep)
		{
			screen = params.screenStep
			screen = (screen - 48)
		}
        TagService tagService = new TagService();
        TreeMap<Long, Pastscene> tmp = orderPastscenes(plotInstance);
        List<Pastscene> pastscenes = new ArrayList<Pastscene>(tmp.values());

        Map<Plot, List<Plot>> variantMap = new HashMap<>();
        List<Plot> plotList = Plot.list();
        for (Plot p : plotList) {
            List<Plot> plotSisterList = new ArrayList<>();
            for (Plot sister : plotList) {
                if ((p.variant == sister.variant || p.variant == sister.id) && p.id != sister.id)
                    plotSisterList.add(sister);
            }
            variantMap.put(p, plotSisterList);
        }

		[plotInstance: plotInstance,
                pastscenes: pastscenes,
                descriptionList : Description.findAllByPlotId(plotInstance.id),
                plotTagList: tagService.getPlotTagQuery(),
                plotUniversList: tagService.getUniversTagQuery(),
                roleTagList: tagService.getRoleTagQuery(),
                resourceTagList: tagService.getResourceTagQuery(),
                placeTagList: tagService.getPlaceTagQuery(),
                relationTypes: RoleRelationType.list(),
                screenStep: screen,
                gnConstantPlaceList: GnConstantController.getGnConstantListFromType(GnConstant.constantTypes.PLACE),
                gnConstantResourceList: GnConstantController.getGnConstantListFromType(GnConstant.constantTypes.RESOURCE),
                variantMap: variantMap,
                availableVariant: Plot.findAllByVariantIsNull()
        ]
 	}

    public TreeMap<Long, Pastscene> orderPastscenes(Plot plot) {
        TreeMap<Long, Pastscene> pastscenes = new TreeMap<Long, Pastscene>();
        Calendar c = null;
        for (Pastscene pastscene : plot.pastescenes) {
            c= Calendar.getInstance();
            if (pastscene.dateYear && pastscene.isAbsoluteYear) {
                c.set(Calendar.YEAR, pastscene.dateYear);
            }
            else if (pastscene.dateYear && !pastscene.isAbsoluteYear) {
                c.add(Calendar.YEAR, pastscene.dateYear * -1)
            }
            if (pastscene.dateMonth && pastscene.isAbsoluteMonth) {
                c.set(Calendar.MONTH, pastscene.dateMonth);
            }
            else if (pastscene.dateMonth && !pastscene.isAbsoluteMonth) {
                c.add(Calendar.MONTH, pastscene.dateMonth * -1)
            }
            if (pastscene.dateDay && pastscene.isAbsoluteDay) {
                c.set(Calendar.DAY_OF_MONTH, pastscene.dateDay);
            }
            else if (pastscene.dateDay && !pastscene.isAbsoluteDay) {
                c.add(Calendar.DAY_OF_MONTH, pastscene.dateDay * -1)
            }
            if (pastscene.dateHour && pastscene.isAbsoluteHour) {
                c.set(Calendar.HOUR_OF_DAY, pastscene.dateHour);
            }
            else if (pastscene.dateHour && !pastscene.isAbsoluteHour) {
                c.add(Calendar.HOUR_OF_DAY, pastscene.dateHour * -1)
            }
            if (pastscene.dateMinute && pastscene.isAbsoluteMinute) {
                c.set(Calendar.MINUTE, pastscene.dateMinute);
            }
            else if (pastscene.dateMinute && !pastscene.isAbsoluteMinute) {
                c.add(Calendar.MINUTE, pastscene.dateMinute * -1)
            }
            Random random = new  Random();
            Long millis = random.nextInt(1000000);
            //If we have several scenes at the same time, they will no longer get replaced in the tree.
            // There is no need to make another loop if there is several scene with the same time since we are making
            // a depth first road.


            while(pastscenes.containsKey(millis))
            {
                millis--;
            }
            pastscenes.put(millis, pastscene);
        }
        return pastscenes;
    }

	def update() {
        def isupdate = true;
		def plotInstance = Plot.get(params.id)
		if (!plotInstance) {
            isupdate = false;
		}
        plotInstance.properties = params

        //Sauvergarde des descriptions en base
        isupdate = updateDescription(plotInstance, isupdate)
        plotInstance.pitchOrga = params.plotPitchOrga == "" ? null : params.plotPitchOrga;
        plotInstance.pitchPj = params.plotPitchPj == "" ? null : params.plotPitchPj;
        plotInstance.pitchPnj = params.plotPitchPnj == "" ? null : params.plotPitchPnj;
        plotInstance.variant = params.plotVariant == "" ? null : Integer.parseInt(params.plotVariant);
        if (plotInstance.extTags) {
            HashSet<PlotHasTag> plotHasTags = plotInstance.extTags;
            PlotHasTag.deleteAll(plotHasTags);
            plotInstance.extTags.clear();
        }
        else {
            plotInstance.extTags = new HashSet<PlotHasTag>();
        }

        plotInstance.save(flush: true);

		params.each {
			if (it.key.startsWith("plotTags_")) {
				PlotHasTag plotHasPlotTag = new PlotHasTag();
				Tag plotTag = Tag.get((it.key - "plotTags_") as Integer);
				plotHasPlotTag.tag = plotTag;
				plotHasPlotTag.weight = params.get("plotTagsWeight_" + plotTag.id) as Integer;
				plotHasPlotTag.plot = plotInstance
				plotInstance.extTags.add(plotHasPlotTag)
			}
		}

		if (!plotInstance.save(flush: true)) {
            isupdate = false;
		}

        render(contentType: "application/json") {
            object(isupdate: isupdate)
        }
	}

	def delete(Long id) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
            render(contentType: "application/json") {
                object(isdelete: true)
            }
		}
        for (GenericResource resource in plotInstance.genericResources) {
            GenericResourceHasTag.executeUpdate("delete GenericResourceHasTag r where r.genericResource = " + resource.id);
        }
        GenericResource.executeUpdate("delete GenericResource g where g.plot = " + plotInstance.id);
        for (Role role in plotInstance.roles) {
            RoleHasRelationWithRole.executeUpdate("delete RoleHasRelationWithRole r where r.role1 = " + role.id);
            RoleHasRelationWithRole.executeUpdate("delete RoleHasRelationWithRole r where r.role2 = " + role.id);
            RoleHasTag.executeUpdate("delete RoleHasTag r where r.role = " + role.id);
            for (RoleHasEvent roleHasEvent in role.roleHasEvents) {
                RoleHasEventHasGenericResource.executeUpdate("delete RoleHasEventHasGenericResource r where r.roleHasEvent = " + roleHasEvent.id);
            }
            RoleHasEvent.executeUpdate("delete RoleHasEvent r where r.role = " + role.id);
            RoleHasPastscene.executeUpdate("delete RoleHasPastscene r where r.role = " + role.id);
        }
        Description.executeUpdate("delete Description d where d.plotId = " + plotInstance.id);
        Role.executeUpdate("delete Role r where r.plot = " + plotInstance.id);
        Pastscene.executeUpdate("update Pastscene p set p.pastscenePredecessor=null where p.plot=" + plotInstance.id);
        Pastscene.executeUpdate("delete Pastscene p where p.plot = " + plotInstance.id);
        Event.executeUpdate("update Event e set e.eventPredecessor=null where e.plot=" + plotInstance.id);
        Event.executeUpdate("delete Event e where e.plot = " + plotInstance.id);
        for (GenericPlace place in plotInstance.genericPlaces) {
            GenericPlaceHasTag.executeUpdate("delete GenericPlaceHasTag r where r.genericPlace = " + place.id);
        }
        GenericPlace.executeUpdate("delete GenericPlace g where g.plot = " + plotInstance.id);
        PlotHasTag.executeUpdate("delete PlotHasTag p where p.plot = " + plotInstance.id)
        Plot.executeUpdate("delete Plot p where p.id = " + plotInstance.id)
        render(contentType: "application/json") {
            object(isdelete: true)
        }
	}

    def updateDescription(Plot plotInstance, Boolean isupdate){


        List<String> pitchOrga = new ArrayList<String>()
        List<String> pitchPj = new ArrayList<String>()
        List<String> pitchPnj = new ArrayList<String>()
        List<String> title = new ArrayList<String>()
        List<Integer> descriptionId = new ArrayList<Integer>()

        int nb_desc = (params.desc_type instanceof String[]) ? params.desc_type.length : 1

        //Récupération des valeurs des champs de chaque description
        for (int i = 0; i < nb_desc; i++)
        {
            pitchOrga.add(params.get("pitchOrga_" + i.toString()).toString());
            pitchPj.add(params.get("pitchPj_" + i.toString()).toString());
            pitchPnj.add(params.get("pitchPnj_" + i.toString()).toString());
            title.add(params.get("titleDescription_" + i.toString()).toString());
            String test_description = (params.description_text instanceof String[]) ? params.description_text[i].toString() : params.description_text
            if (pitchOrga.get(i) == "null" && pitchPj.get(i) == "null" && pitchPnj.get(i) == "null" || test_description == "") {
                //Si un des champs est vide, l'insertion n'est pas validée et un message d'erreur est envoyé
                isupdate = false
                return isupdate
            }
            descriptionId.add(params.get("pitchDescription_" + i.toString()).toString().split('_')[1].toInteger());
            //System.out.println("pitchDescription value : " + params.get("pitchDescription_" + i.toString()).toString().split('_')[1].toInteger())
        }

        //Création et insertion/update des descriptions en base
        for (int i = 0; i < nb_desc; i++)
        {
            def type_description = (params.desc_type instanceof String[]) ? params.desc_type[i].toString() : params.desc_type.toString();
            Description new_description = new Description(plotInstance.id.toInteger(), descriptionId.get(i), type_description, (params.description_text instanceof String[]) ? params.description_text[i].toString() : params.description_text, pitchPnj.get(i), pitchPj.get(i), pitchOrga.get(i), title.get(i));
            if (type_description == "Introduction")
               plotInstance.description = (params.description_text instanceof String[]) ? params.description_text[i].toString() : params.description_text
            plotInstance.add_Description(new_description);
            if (Description.findByIdDescriptionAndPlotId(descriptionId.get(i), plotInstance.id.toInteger()) != null)
            //Description.executeUpdate("update Description d set d.is_pnj = " + new_description.isPnj + ", d.is_pj=" + new_description.isPj + ", d.is_orga=" + new_description.isOrga + ", d.type=" + new_description.type + ", d.pitch=" + new_description.pitch + "where d.plot_id=" + plotInstance.id.toInteger() + "and d.id_description=" + descriptionId.get(i))
                Description.executeUpdate("delete Description d where d.plotId=" + plotInstance.id.toInteger() + "and d.idDescription=" + descriptionId.get(i))
            new_description.save(flush: true)
        }

        //Suppression des descriptions en base
        for (int j = Description.findAllByPlotId(plotInstance.id.toInteger()).size() - 1; j >= descriptionId.size(); j--)
        {
            Description.executeUpdate("delete Description d where d.plotId=" + plotInstance.id.toInteger() + "and d.idDescription=" + j)
        }
        return isupdate
    }

    def print(){
        def plotInstance = Plot.get(params.plotid)
        def folderName = "${request.getSession().getServletContext().getRealPath("/")}word/"
        def folder = new File(folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }

        String fileName = folderName + "${plotInstance.getName().replaceAll(" ", "_").replaceAll("/", "_")}_${System.currentTimeMillis()}"
        File output = new File(fileName + ".pdf")
        String publicationFolder = "${request.getSession().getServletContext().getRealPath("/")}publication/"
        WordWriter wordWriter = new WordWriter("",publicationFolder)
        wordWriter.wordMLPackage = createPrint(wordWriter, fileName, plotInstance)
        wordWriter.wordMLPackage.save(output)
        PdfConversion c = new Conversion(wordWriter.wordMLPackage)
        c.setSaveFO(output)
        c.output(response.outputStream, new PdfSettings())
        response.setHeader("Pragma", "no-cache")
        response.setHeader("Cache-control", "private")
        response.setDateHeader("Expires", 0)
        response.setContentType("application/pdf")
        response.setHeader("Content-Disposition", "filename=\"test.pdf\"")
        response.outputStream << output.newInputStream()
    }

    public WordprocessingMLPackage createPrint(WordWriter wordWriter, String fileName, Plot plot){
        wordWriter.addStyledParagraphOfText("T", plot.getName())
        wordWriter.addStyledParagraphOfText("ST", createSubTile(plot))

        wordWriter.addStyledParagraphOfText("T1", "Description de l'intrigue")
        createDescription(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Résumé de l'intrigue")
        createSummary(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Rôles")
        createRoles(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Places")
        createPlaces(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Ressources")
        createResources(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Relation")
        createRelation(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Scènes Passées")
        createPastScene(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Evénements")
        createEvent(wordWriter, plot)
        return wordWriter.wordMLPackage
    }
    def String createSubTile(Plot plot) {
        String subtitle = "Version " + ((plot.version < 10) ? "0." + plot.version : plot.version.toString().subSequence(0, plot.version.toString().size() - 1) + "." + plot.version.toString().subSequence(plot.version.toString().size() - 1, plot.version.toString().size())) + " update le "
        subtitle += plot.lastUpdated
        subtitle += " par : " + plot.getUser().lastname + " " + plot.getUser().firstname
        return subtitle
    }
    def  createDescription(WordWriter wordWriter,Plot plot) {
        PrintDescriptionService printDescriptionService = new PrintDescriptionService();
        String tag = ""
        def list = []
        wordWriter.addStyledParagraphOfText("T2", "Tags choisis")
        tag = "La liste des Tags associés à l'intrigue sont : \n"
        for (PlotHasTag plotHasTag : plot.extTags) {
            if (!plotHasTag.tag.parent.name.equals("Tag Univers"))
                list.add(plotHasTag)
        }
        list.sort{ -it.weight}
        list.each { plotHasTag ->
            tag +=  plotHasTag.weight + "% - " + plotHasTag.tag.name + " (" + plotHasTag.tag.parent.name + ") " + "\n"
        }
        wordWriter.addStyledParagraphOfText("Normal", tag)

        wordWriter.addStyledParagraphOfText("T2", "Univers")
        tag = "L'intrigue possède les tags Univers suivants : \n"
        list.clear()
        for (PlotHasTag plotHasTag : plot.extTags) {
            if (plotHasTag.tag.parent.name.equals("Tag Univers"))
                list.add(plotHasTag)
        }
        list.sort{ -it.weight}
        list.each { plotHasTag ->
            tag +=  plotHasTag.weight + "% - " + plotHasTag.tag.name + " (" + plotHasTag.tag.parent.name + ") " + "\n"
        }
        wordWriter.addStyledParagraphOfText("Normal", tag)

        wordWriter.addStyledParagraphOfText("T2", "Propriétés de l'intrigue")
        tag = "L'intrigue est : "
        def non_tag = "L'intrigue n'est pas : "
        if (plot.isEvenemential)
            tag += "Evénementielle, "
        else
            non_tag += "Evénementielle"
        if (plot.isMainstream)
            tag += "Mainstream, "
        else
            non_tag += "Mainstream, "
        if (plot.isDraft)
            tag += "Draft, "
        else
            non_tag += "Brouillon, "
        if (plot.isPublic)
            tag +="Publique."
        else
            non_tag += "Publique."
        wordWriter.addStyledParagraphOfText("Normal",tag)
        wordWriter.addStyledParagraphOfText("Normal",non_tag)
        wordWriter.addStyledParagraphOfText("T2", "Pitch Intrigue")
        wordWriter.addStyledParagraphOfText("T3", "Intrigue")
        wordWriter.addStyledParagraphOfText("Normal", plot.description)

        if (plot.list_Description == null)
            plot.list_Description = Description.findAllByPlotId(plot.id)

        printDescriptionService.printDescription(wordWriter, plot.list_Description)
    }

    def createSummary(WordWriter wordWriter, Plot plot){
        wordWriter.addStyledParagraphOfText("T2", "Liste des rôles présents")
        for (Role role : plot.roles)
        {
            wordWriter.addStyledParagraphOfText("Normal", role.code + " - " + getRoleType(role))
        }
        wordWriter.addStyledParagraphOfText("T2", "Liste des places présentes")
        for (GenericPlace genericPlace : plot.genericPlaces)
        {
            wordWriter.addStyledParagraphOfText("Normal", genericPlace.code + " - " + getNameObjectId(genericPlace.objectTypeId))
        }
        wordWriter.addStyledParagraphOfText("T2", "Liste des ressources utilisées")
        for (GenericResource genericResource : plot.genericResources)
        {
            wordWriter.addStyledParagraphOfText("Normal", genericResource.code + " - " + getNameObjectId(genericResource.objectTypeId))
        }
        wordWriter.addStyledParagraphOfText("T2", "Liste des scènes passés passés")
        for (Pastscene pastscene : plot.pastescenes)
        {
            wordWriter.addStyledParagraphOfText("Normal", pastscene.title)
        }
        wordWriter.addStyledParagraphOfText("T2", "Liste des événements présents")
        for (Event event : plot.events)
        {
            String type = ""
            if (event.isPublic)
                type = "Public"
            else
                type = "Privé"
            wordWriter.addStyledParagraphOfText("Normal", event.name + " - " + type)
        }

    }

    def createRoles(WordWriter wordWriter, Plot plot){
        for(Role role : plot.roles)
        {

            wordWriter.addStyledParagraphOfText("T2", role.getCode())

            String txt = "Le rôle est : "  + getRoleType(role)
            if (role.type.equals("PJG"))
                txt += " (" + role.pjgp + "%)"
            wordWriter.addStyledParagraphOfText("Normal", txt)

            wordWriter.addStyledParagraphOfText("Normal", "PIPI : " + role.getPipi())
            wordWriter.addStyledParagraphOfText("Normal", "PIPR : " + role.getPipr())
            txt = "Les tags du role sont : \n"
            for (RoleHasTag roleHasTag : role.roleHasTags)
            {
                txt += roleHasTag.weight + "% - " + roleHasTag.tag.name + "\n"
            }
            wordWriter.addStyledParagraphOfText("Normal", txt)

            wordWriter.addStyledParagraphOfText("T3", "Description")
            wordWriter.addStyledParagraphOfText("Normal", role.description)

            wordWriter.addStyledParagraphOfText("T3", "Scènes passés")
            txt = ""
            for (RoleHasPastscene roleHasPastscene : role.getRoleHasPastscenes()){
                txt = roleHasPastscene.pastscene.getTitle()
                def date = ""
                def numberdate = ""
                (date,numberdate) = createDate(roleHasPastscene.pastscene)
                wordWriter.addStyledParagraphOfText("T5", "Il y  a : " + numberdate + " " + date + " : " )
                wordWriter.addStyledParagraphOfText("T5", txt + " : " + roleHasPastscene.title)
                wordWriter.addStyledParagraphOfText("Normal", roleHasPastscene.description)
            }
            wordWriter.addStyledParagraphOfText("T3", "Evénements")
            txt= ""
            for (RoleHasEvent roleHasEvent : role.getRoleHasEvents())
            {
                txt = roleHasEvent.event.name
                if (roleHasEvent.isAnnounced)
                    txt += " (Annoncé)"
                wordWriter.addStyledParagraphOfText("T5", txt + " : " + roleHasEvent.title)
                wordWriter.addStyledParagraphOfText("Normal", roleHasEvent.description)
            }
            wordWriter.addStyledParagraphOfText("T3", "Synthèses des relations")
            for (RoleHasRelationWithRole role1 : role.roleHasRelationWithRolesForRole1Id){
                def isbijective = ""
                def exclusive = ""
                def hidden = ""
                txt = " <> "
                if (role1.isExclusive)
                    txt = " > "
                wordWriter.addStyledParagraphOfText("T3",role1.weight +" (Poids) "+ role1.roleRelationType.name + txt + role1.getterOtherRole(role).code)
                if (role1.isBijective)
                    isbijective = "bijective "
                if (role1.isExclusive)
                    exclusive = "exclusive "
                if (role1.isHidden)
                    hidden = "cachée "
                wordWriter.addStyledParagraphOfText("Normal", "La relation est : " + isbijective + exclusive + hidden)
//                wordWriter.addStyledParagraphOfText("T4", "Description : ")
//                wordWriter.addStyledParagraphOfText("Normal", role1.description)
            }
            for (RoleHasRelationWithRole role2 : role.roleHasRelationWithRolesForRole2Id){
                def isbijective = ""
                def exclusive = ""
                def hidden = ""
                txt = " <> "
                if (role2.isExclusive)
                    txt = " < "
                wordWriter.addStyledParagraphOfText("T3",role2.weight +" (Poids) "+ role2.roleRelationType.name + txt + role2.getterOtherRole(role).code)
                if (role2.isBijective)
                    isbijective = "bijective "
                if (role2.isExclusive)
                    exclusive = "exclusive "
                if (role2.isHidden)
                    hidden = "cachée "
                wordWriter.addStyledParagraphOfText("Normal", "La relation est : " + isbijective + exclusive + hidden)
//                wordWriter.addStyledParagraphOfText("T4", "Description : ")
//                wordWriter.addStyledParagraphOfText("Normal", role2.description)
            }

        }
    }

    def createPlaces(WordWriter wordWriter, Plot plot){
        String txt = ""
        Tag tagUnivers = new Tag();
        tagUnivers = Tag.findById("33089");
        ArrayList<Tag> universList = Tag.findAllByParent(tagUnivers);

        for (GenericPlace place : plot.genericPlaces) {
            wordWriter.addStyledParagraphOfText("T2", place.code)
            wordWriter.addStyledParagraphOfText("T3", "Type : ")
            wordWriter.addStyledParagraphOfText("Normal", getNameObjectId(place.objectTypeId))
            wordWriter.addStyledParagraphOfText("T3", "Tags")
            txt = "Les tags du lieu sont : \n"
            def list = []
            for (GenericPlaceHasTag genericPlaceHasTag : place.extTags)
            {
                list.add(genericPlaceHasTag)
            }
            list.sort{ -it.weight}
            list.each { placeHasTag ->
                txt +=  placeHasTag.weight + "% - " + placeHasTag.tag.name + "\n"
            }
            wordWriter.addStyledParagraphOfText("Normal", txt)
            wordWriter.addStyledParagraphOfText("T3","Description")
            wordWriter.addStyledParagraphOfText("Normal", place.comment)

/*            org.gnk.ressplacetime.GenericPlace genericplace = new org.gnk.ressplacetime.GenericPlace();

            List<com.gnk.substitution.Tag> tags = new ArrayList<>();
            genericplace.setTagList(place.getTags())
            for (int i = 0; i < universList.size() ; i++) {
                genericplace = placeService.findReferentialPlace(genericplace, universList[i].name)
                // Pour l'univer universList[i] les bestPlaces de la GenericPlace place sont genericplace.resultList
            } */

        }
    }

    def createResources(WordWriter wordWriter, Plot plot){
        String txt = ""
        for (GenericResource resource : plot.genericResources){
            wordWriter.addStyledParagraphOfText("T2", resource.code)
            wordWriter.addStyledParagraphOfText("T3", "Type : ")
            wordWriter.addStyledParagraphOfText("Normal", getNameObjectId(resource.objectTypeId))
            String tag = "La liste des Tags associés à la ressource sont : \n"
            for (GenericResourceHasTag resourceHasTagHasTag : resource.extTags) {
                    tag += resourceHasTagHasTag.weight + "% - " + resourceHasTagHasTag.tag.name + "\n"
            }
            wordWriter.addStyledParagraphOfText("T3", "Tags choisis")
            wordWriter.addStyledParagraphOfText("Normal", tag)
            wordWriter.addStyledParagraphOfText("T3", "Description")
            wordWriter.addStyledParagraphOfText("Normal", resource.comment)
            wordWriter.addStyledParagraphOfText("T5", "La ressource est détenue par : " + resource?.possessedByRole?.code)
            if (resource.isIngameClue())
            {
                wordWriter.addStyledParagraphOfText("T3", "La ressource est présente en jeu")
                wordWriter.addStyledParagraphOfText("T4", resource.title)
                wordWriter.addStyledParagraphOfText("T5", "De : " + resource?.fromRoleText)
                wordWriter.addStyledParagraphOfText("T5", "Pour : " + resource?.toRoleText)
                wordWriter.addStyledParagraphOfText("Normal", resource.description)
            }
        }
    }


    def createRelation(WordWriter wordWriter, Plot plot){
        String text = ""
        for(Role role : plot.roles)
        {
            wordWriter.addStyledParagraphOfText("T2", "Les relations du rôle : " + role.code)
            for (RoleHasRelationWithRole role1 : role.roleHasRelationWithRolesForRole1Id){
                def isbijective = ""
                def exclusive = ""
                def hidden = ""
                def txt = " <-> "
                if (role1.isExclusive)
                    txt = " -> "
                wordWriter.addStyledParagraphOfText("T3",role1.weight +" (Poids) "+ role1.roleRelationType.name + txt + role1.getterOtherRole(role).code)
                if (role1.isBijective)
                    isbijective = "bijective "
                if (role1.isExclusive)
                    exclusive = "exclusive "
                if (role1.isHidden)
                    hidden = "cachée "
                wordWriter.addStyledParagraphOfText("Normal", "La relation est : " + isbijective + exclusive + hidden)
                wordWriter.addStyledParagraphOfText("T4", "Description : ")
                wordWriter.addStyledParagraphOfText("Normal", role1.description)
            }
            for (RoleHasRelationWithRole role2 : role.roleHasRelationWithRolesForRole2Id){
                def isbijective = ""
                def exclusive = ""
                def hidden = ""
                def txt = " <-> "
                if (role2.isExclusive)
                    txt = " <- "
                wordWriter.addStyledParagraphOfText("T3",role2.weight +" (Poids) "+ role2.roleRelationType.name + txt + role2.getterOtherRole(role).code)
                if (role2.isBijective)
                    isbijective = "bijective "
                if (role2.isExclusive)
                    exclusive = "exclusive "
                if (role2.isHidden)
                    hidden = "cachée "
                wordWriter.addStyledParagraphOfText("Normal", "La relation est : " + isbijective + exclusive + hidden)
                wordWriter.addStyledParagraphOfText("T4", "Description : ")
                wordWriter.addStyledParagraphOfText("Normal", role2.description)
            }
            if (role.roleHasRelationWithRolesForRole1Id.isEmpty() && role.roleHasRelationWithRolesForRole2Id.isEmpty())
                wordWriter.addStyledParagraphOfText("Normal", "Ce rôle n'a pas de relation avec d'autres rôles")
        }
    }

    def createPastScene(WordWriter wordWriter, Plot plot){
        String text = ""
        for (Pastscene pastscene : plot.pastescenes)
        {
            wordWriter.addStyledParagraphOfText("T2", pastscene.title)
            if (pastscene.isPublic)
                wordWriter.addStyledParagraphOfText("T4", "La scène passée est publique")
            else
                wordWriter.addStyledParagraphOfText("T4", "La scène passée est privée")
            if (pastscene.genericPlace)
                wordWriter.addStyledParagraphOfText("T4", "La scène est jouée dans la place : " + pastscene.genericPlace.code)
            if (pastscene.pastscenePredecessor)
                wordWriter.addStyledParagraphOfText("T4", "La scène a pour prédécesseur : " + pastscene.pastscenePredecessor.title)
            wordWriter.addStyledParagraphOfText("T3", "Description")
            wordWriter.addStyledParagraphOfText("Normal",pastscene.description)
            def date = ""
            def numberdate = ""
            (date , numberdate) = createDate(pastscene)
            wordWriter.addStyledParagraphOfText("T3", "La scène a eu lieu il y  a : " + numberdate + " " + date)
            //wordWriter.addStyledParagraphOfText("T3", "La scène se déroule dans le lieu : " + pastscene?.genericPlace?.code)
            wordWriter.addStyledParagraphOfText("T3", "Rôles choisis :")
            for (RoleHasPastscene role : pastscene.roleHasPastscenes){
                wordWriter.addStyledParagraphOfText("T4", "Rôle : " + role.role.code)
                wordWriter.addStyledParagraphOfText("T5", "Titre : ")
                wordWriter.addStyledParagraphOfText("Normal", role.title)
                wordWriter.addStyledParagraphOfText("T5", "Description : ")
                wordWriter.addStyledParagraphOfText("Normal", role.description)
            }

        }
    }

    def createEvent(WordWriter wordWriter, Plot plot){
        def eventlist = []
        for (Event event : plot.events) {
            eventlist.add(event)
        }

        eventlist.sort{it.timing}
        for (Event event : eventlist){
            wordWriter.addStyledParagraphOfText("T2", event.name)
            if (event.isPublic)
                wordWriter.addStyledParagraphOfText("T4", "L'événement est public")
            else
                wordWriter.addStyledParagraphOfText("T4", "L'événement est privé")
            if (event.isPlanned)
                wordWriter.addStyledParagraphOfText("T4","L'événement est plannifié")
            else
                wordWriter.addStyledParagraphOfText("T4","L'événement n'est pas plannifié")
            wordWriter.addStyledParagraphOfText("T5", "Durée : " + event.duration + " min")
            wordWriter.addStyledParagraphOfText("T5", " Timing : " + event.timing + " %")
            if (event.genericPlace)
                wordWriter.addStyledParagraphOfText("T5", "Place : " + event.genericPlace.code )
            if (event.eventPredecessor)
                wordWriter.addStyledParagraphOfText("T5", "L'événement précédent est  : " + event.eventPredecessor.name)
            wordWriter.addStyledParagraphOfText("T4", "Description")
            wordWriter.addStyledParagraphOfText("Normal", event.description)
            for (RoleHasEvent role : event.roleHasEvents){
                wordWriter.addStyledParagraphOfText("T4", "Rôle : " + role.role.code)
                if (role.isAnnounced)
                    wordWriter.addStyledParagraphOfText("T5", "Le rôle est annoncé")
                else
                    wordWriter.addStyledParagraphOfText("T5", "Le rôle n'est pas annoncé")
                wordWriter.addStyledParagraphOfText("T5", "Titre : ")
                wordWriter.addStyledParagraphOfText("Normal", role.title)
                wordWriter.addStyledParagraphOfText("T5", "Description : ")
                wordWriter.addStyledParagraphOfText("Normal", role.description)
                if (role.comment){
                    wordWriter.addStyledParagraphOfText("T5", "Commentaire : ")
                    wordWriter.addStyledParagraphOfText("Normal", role.comment)
                }
                wordWriter.addStyledParagraphOfText("T5", "Description de l'événement : ")
                wordWriter.addStyledParagraphOfText("Normal", role.evenementialDescription)
                if (role.roleHasEventHasGenericResources.isEmpty())
                {
                    wordWriter.addStyledParagraphOfText("T4","Le personnage ne possède aucune ressource")
                }
                else{
                    wordWriter.addStyledParagraphOfText("T4", "Le personnage possède les ressources suivantes :")
                    for (RoleHasEventHasGenericResource genericResource : role.roleHasEventHasGenericResources)
                    {
                        wordWriter.addStyledParagraphOfText("Normal", genericResource.genericResource.code + " Quantité : " + genericResource.quantity)
                    }
                }
            }
        }

    }
    def createDate(Pastscene pastscene){
        def date = ""
/*        switch (pastscene.unitTimingRelative) {
            case "Y":
                date = "an(s)"
                break
            case "d":
                date = "jour(s)"
                break
            case "M":
                date = "mois(s)"
                break
            case "h":
                date = "heure(s)"
                break
            case "m":
                date = "minute(s)"
            default:
                date = "something went terribly wrong"
                break
        } */
        def numberdate = ""
        if (pastscene.dateYear)
            numberdate = pastscene.dateYear
        else {
            if (pastscene.dateMonth)
                numberdate = pastscene.dateMonth
            else if (pastscene.dateDay)
                numberdate = pastscene.dateDay
            else
            {
                if (pastscene.dateHour)
                    numberdate = pastscene.dateHour
                else
                if (pastscene.dateMinute)
                    numberdate = pastscene.dateMinute
            }
        }
        return [date,numberdate]
    }

    def getNameObjectId(def objectTypeId){
        def txt = ""
        switch (objectTypeId) {
            case 0:
                txt = "A définir"
                break
            case 1:
                txt = "En jeu"
                break
            case 2:
                txt = "Simulé"
                break
            case 3:
                txt = "Hors jeu"
                break
            default :
                txt = "This should not happen"
                break
            return txt
        }
    }

    def getRoleType(Role role){
        String type = ""
        switch (role.type){
            case "PNJ" :
                type = "Personnage non joueur"
                break
            case "PJ" :
                type = "Personnage joueur"
                break
            case "TPJ" :
                type = "Tout personnage joueur"
                break
            case "PHJ" :
                type = "Personnage non joueur (hors jeu)"
                break
            case "PJG" :
                type = "Personnage joueur générique"
                break
            case "PJB" :
                type = "Personnage PNJsable"
                break
            case "STF" :
                type = "Organisateur"
                break
            default:
                type = "There should be something there"
                break
        }
        return type
    }

    // Unfinished work to duplicate plots
    /*def duplicate(Long id) {
        Plot plotInstance = Plot.get(id)
        Plot duplicatedPlot = new Plot()

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = user.getUsername();
        User currentUser = User.findByUsername(currentUsername);

        duplicatedPlot.name = plotInstance.name + "[duplicate]"
        duplicatedPlot.dateCreated = new Date();
        duplicatedPlot.lastUpdated = new Date();
        duplicatedPlot.user = currentUser;
        duplicatedPlot.isEvenemential = false;
        duplicatedPlot.isMainstream = false;
        duplicatedPlot.isPublic = false;
        duplicatedPlot.isDraft = true;
        duplicatedPlot.description = plotInstance.description;
        duplicatedPlot.pitchOrga = plotInstance.pitchOrga
        duplicatedPlot.pitchPj = plotInstance.pitchPj
        duplicatedPlot.pitchPnj = plotInstance.pitchPnj
        duplicatedPlot.variant = (plotInstance.variant == null ? plotInstance.id : plotInstance.variant)

        if (!duplicatedPlot.save(flush: true)) {
            return
        }

        // Duplicate PlotHasTags
        Set<PlotHasTag> toDuplicatePlotHasTags = plotInstance.getExtTags()
        Set<PlotHasTag> duplicatedPlotHasTags = new HashSet<>();
        for (PlotHasTag p : toDuplicatePlotHasTags) {
            PlotHasTag newPlotHasTag = new PlotHasTag(duplicatedPlot, p.tag, p.weight)
            if (newPlotHasTag.save())
                duplicatedPlotHasTags.add(newPlotHasTag)
        }
        duplicatedPlot.extTags = duplicatedPlotHasTags
        duplicatedPlot.save()

        // Duplicate Roles
        Set<Role> toDuplicateRoles = plotInstance.roles
        Set<Role> duplicatedRoles = new HashSet<>();
        Map<Integer, Role> roleToDuplicateMap = new HashMap<>() // Keep track of the duplicated roles in order to rebuild links
        for (Role r : toDuplicateRoles) {
            Role newDuplicatedRole = new Role()
            newDuplicatedRole.lastUpdated = new Date()
            newDuplicatedRole.dateCreated = new Date()
            newDuplicatedRole.code = r.code
            newDuplicatedRole.pipr = r.pipr
            newDuplicatedRole.pipi = r.pipi
            newDuplicatedRole.pjgp = r.pjgp
            newDuplicatedRole.plot = duplicatedPlot
            newDuplicatedRole.description = r.description
            newDuplicatedRole.type = r.type
            newDuplicatedRole.save()

            roleToDuplicateMap.put(r.id, newDuplicatedRole)
            duplicatedRoles.add(newDuplicatedRole)
        }

        // RoleHasTags
        for (Role r : toDuplicateRoles) {
            Set<RoleHasTag> toDuplicateRoleHasTags = r.roleHasTags
            Set<RoleHasTag> newRoleHasTags = new HashSet<>()
            for (RoleHasTag roleHasTag : toDuplicateRoleHasTags) {
                RoleHasTag newRoleHasTag = new RoleHasTag(roleToDuplicateMap.get(r.id), roleHasTag.tag, roleHasTag.weight)
                if (newRoleHasTag.save())
                    newRoleHasTags.add(newRoleHasTag)
            }
            roleToDuplicateMap.get(r.id).roleHasTags = newRoleHasTags
            roleToDuplicateMap.get(r.id).save()
        }

        // RoleHasRelationWithRole
        for (Role r : toDuplicateRoles) {
            // roleHasRelationWithRolesForRole1Id
            Set<RoleHasRelationWithRole> newRole1Set = new HashSet<>()
            for (RoleHasRelationWithRole roleHasRelationWithRole : r.roleHasRelationWithRolesForRole1Id) {
                RoleHasRelationWithRole newRoleHasRelationWithRole = new RoleHasRelationWithRole(
                        roleToDuplicateMap.get(roleHasRelationWithRole.role1.id),
                        roleToDuplicateMap.get(roleHasRelationWithRole.role2.id),
                        roleHasRelationWithRole.weight,
                        roleHasRelationWithRole.isBijective,
                        roleHasRelationWithRole.roleRelationType,
                        roleHasRelationWithRole.isExclusive,
                        roleHasRelationWithRole.description)
                newRoleHasRelationWithRole.isHidden = roleHasRelationWithRole.isHidden
                if (newRoleHasRelationWithRole.save(flush: true))
                    newRole1Set.add(newRoleHasRelationWithRole)
            }
            roleToDuplicateMap.get(r.id).setRoleHasRelationWithRolesForRole1Id(newRole1Set)
            // roleHasRelationWithRolesForRole2Id
            Set<RoleHasRelationWithRole> newRole2Set = new HashSet<>()
            for (RoleHasRelationWithRole roleHasRelationWithRole : r.roleHasRelationWithRolesForRole2Id) {
                RoleHasRelationWithRole newRoleHasRelationWithRole = new RoleHasRelationWithRole(
                        roleToDuplicateMap.get(roleHasRelationWithRole.role1.id),
                        roleToDuplicateMap.get(roleHasRelationWithRole.role2.id),
                        roleHasRelationWithRole.weight,
                        roleHasRelationWithRole.isBijective,
                        roleHasRelationWithRole.roleRelationType,
                        roleHasRelationWithRole.isExclusive,
                        roleHasRelationWithRole.description)
                if (newRoleHasRelationWithRole.save())
                    newRole2Set.add(newRoleHasRelationWithRole)
            }
            roleToDuplicateMap.get(r.id).setRoleHasRelationWithRolesForRole2Id(newRole2Set)
            roleToDuplicateMap.get(r.id).save()
        }

        duplicatedPlot.roles = duplicatedRoles
        duplicatedPlot.save()

        // Duplicate Events
        Set<Event> toDuplicateEvents = plotInstance.events
        Set<Role> duplicatedEvents = new HashSet<>();
        Map<Integer, Event> eventToDuplicateMap = new HashMap<>() // Keep track of the duplicated events in order to rebuild links
        for (Event e : toDuplicateEvents) {
            Event newDuplicatedEvent = new Event()
            newDuplicatedEvent.lastUpdated = new Date()
            newDuplicatedEvent.dateCreated = new Date()
            newDuplicatedEvent.name = e.name
            newDuplicatedEvent.timing = e.timing
            newDuplicatedEvent.duration = e.duration
            newDuplicatedEvent.isPublic = e.isPublic
            newDuplicatedEvent.isPlanned = e.isPlanned
            newDuplicatedEvent.description = e.description
            newDuplicatedEvent.plot = duplicatedPlot
            // Missing predecessor + genericPlace

            if (newDuplicatedEvent.save()) {
                eventToDuplicateMap.put(e.id, newDuplicatedEvent)
                duplicatedEvents.add(newDuplicatedEvent)
            }
        }

        // Duplicate RoleHasEvent
        for (Event e : toDuplicateEvents) {
            Set<RoleHasEvent> newRoleHasEventSet = new HashSet<>()
            for (RoleHasEvent roleHasEvent : e.roleHasEvents) {
                RoleHasEvent newRoleHasEvent = new RoleHasEvent()
                newRoleHasEvent.lastUpdated = new Date()
                newRoleHasEvent.dateCreated = new Date()
                newRoleHasEvent.title = roleHasEvent.title
                newRoleHasEvent.isAnnounced = roleHasEvent.isAnnounced
                newRoleHasEvent.description = roleHasEvent.description
                newRoleHasEvent.comment = roleHasEvent.comment
                newRoleHasEvent.evenementialDescription = roleHasEvent.evenementialDescription

                newRoleHasEvent.event = eventToDuplicateMap.get(roleHasEvent.event.id)
                Role currentRole = roleToDuplicateMap.get(roleHasEvent.role.id)
                newRoleHasEvent.role = currentRole

                if (newRoleHasEvent.save()) {
                    newRoleHasEventSet.add(newRoleHasEvent)
                    if (null == currentRole.roleHasEvents)
                        currentRole.roleHasEvents = newRoleHasEventSet
                    else
                        currentRole.roleHasEvents.addAll(newRoleHasEventSet)
                    currentRole.save()
                }
                // Missing RoleHasEventHasGenericResources
            }
            Event currentEvent = eventToDuplicateMap.get(e.id)
            if (null == currentEvent.roleHasEvents)
                currentEvent.roleHasEvents = newRoleHasEventSet
            else
                currentEvent.roleHasEvents.addAll(newRoleHasEventSet)
            currentEvent.save()
        }

        // Duplicate Resource

        // Duplicate Place

        // Duplicate PastScenes

        redirect(action: "edit", id: duplicatedPlot.id)
    }*/
}
