package org.gnk.gn.redactintrigue

import org.apache.poi.hwpf.usermodel.DateAndTime
import org.docx4j.convert.out.pdf.PdfConversion
import org.docx4j.convert.out.pdf.viaXSLFO.Conversion
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.Tbl
import org.gnk.publication.WordWriter
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.Place
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.roletoperso.RoleHasRelationWithRole
import org.gnk.roletoperso.RoleHasTag
import org.gnk.roletoperso.RoleRelationType
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.gnk.tag.Tag
import org.gnk.tag.TagService;

import org.gnk.user.User
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class RedactIntrigueController {
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
        List<Pastscene> pastscenes = new ArrayList<Pastscene>(orderPastscenes(plotInstance).values());
		[plotInstance: plotInstance,
                pastscenes: pastscenes,
                plotTagList: tagService.getPlotTagQuery(),
                plotUniversList: tagService.getUniversTagQuery(),
                roleTagList: tagService.getRoleTagQuery(),
                resourceTagList: tagService.getResourceTagQuery(),
                placeTagList: tagService.getPlaceTagQuery(),
                relationTypes: RoleRelationType.list(),
                screenStep: screen]
	}

    public TreeMap<Long, Pastscene> orderPastscenes(Plot plot) {
        TreeMap<Long, Pastscene> pastscenes = new TreeMap<Long, Pastscene>();
        for (Pastscene pastscene : plot.pastescenes) {
            Calendar c = Calendar.getInstance();
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
            Long millis = c.getTimeInMillis();
            //If we have several scenes at the same time, they will no longer get replaced in the tree.
            // There is no need to make another loop if there is several scene with the same time since we are making
            // a depth first road.
            pastscenes.each { scene ->
                    if (scene.key == millis)
                        millis--
            }
            pastscenes.put(millis, pastscene);
        }
        return pastscenes;
    }

	def update(Long id) {
        def isupdate = true;
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
            isupdate = false;
		}
		plotInstance.properties = params
		plotInstance.description = params.plotDescription == "" ? null : params.plotDescription;
        plotInstance.pitchOrga = params.plotPitchOrga == "" ? null : params.plotPitchOrga;
        plotInstance.pitchPj = params.plotPitchPj == "" ? null : params.plotPitchPj;
        plotInstance.pitchPnj = params.plotPitchPnj == "" ? null : params.plotPitchPnj;
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

        wordWriter.addStyledParagraphOfText("T1", "Description Intrigue")
        createDescription(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Roles")
        createRoles(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Places")
        createPlaces(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Resources")
        createResources(wordWriter, plot)

//        wordWriter.addStyledParagraphOfText("T1", "Relation")
//        createRelation(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Scènes Passées")
        createPastScene(wordWriter, plot)

        wordWriter.addStyledParagraphOfText("T1", "Event")
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
        String tag = ""

        wordWriter.addStyledParagraphOfText("T2", "Tags choisis")
        tag = "La liste des Tags associés à l'intrigue sont : \n"
        for (PlotHasTag plotHasTag : plot.extTags) {
            if (!plotHasTag.tag.parent.name.equals("Tag Univers"))
                tag += plotHasTag.weight + "% - " + plotHasTag.tag.name + " ("  + plotHasTag.tag.parent.name + ") " + "\n"
        }
        wordWriter.addStyledParagraphOfText("Normal", tag)

        wordWriter.addStyledParagraphOfText("T2", "Univers")
        tag = "L'intrigue possède les tags Univers suivants : \n"
        for (PlotHasTag plotHasTag : plot.extTags) {
            if (plotHasTag.tag.parent.name.equals("Tag Univers"))
                tag += plotHasTag.weight + "% - " + plotHasTag.tag.name + "\n"
        }
        wordWriter.addStyledParagraphOfText("Normal", tag)

        wordWriter.addStyledParagraphOfText("T2", "Propriétés de l'intrigue")
        tag = "L'intrigue est : "
        if (plot.isEvenemential)
            tag += "Evenemential, "
        if (plot.isMainstream)
            tag += "Mainstream, "
        if (plot.isDraft)
            tag += "Draft, "
        if (plot.isPublic)
            tag +="Public."
        wordWriter.addStyledParagraphOfText("Normal",tag)

        wordWriter.addStyledParagraphOfText("T2", "Pitch Intrigue")
        wordWriter.addStyledParagraphOfText("T3", "Intrigue")
        wordWriter.addStyledParagraphOfText("Normal", plot.description)

        wordWriter.addStyledParagraphOfText("T3", "Pitch Organisateur")
        if (plot.pitchOrga)
            wordWriter.addStyledParagraphOfText("Normal", plot.pitchOrga)

        wordWriter.addStyledParagraphOfText("T3", "Pitch Joueur")
        if (plot.pitchPj)
            wordWriter.addStyledParagraphOfText("Normal", plot.pitchPj)

        wordWriter.addStyledParagraphOfText("T3", "Pitch Personnage non joueur")
        if (plot.pitchPnj)
            wordWriter.addStyledParagraphOfText("Normal", plot.pitchPnj)
    }

    def createRoles(WordWriter wordWriter, Plot plot){
        for(Role role : plot.roles)
        {

            wordWriter.addStyledParagraphOfText("T2", role.getCode())
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
            String txt = "Le rôle est : "  + type
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
                wordWriter.addStyledParagraphOfText("T5", txt + " : " + roleHasPastscene.title)
                wordWriter.addStyledParagraphOfText("Normal", roleHasPastscene.description)
            }
            wordWriter.addStyledParagraphOfText("T3", "Events")
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
            for (RoleHasRelationWithRole roleHasRelationWithRole : role.getAllRelationsWhichConcernRole(false)){

                wordWriter.addStyledParagraphOfText("T4", roleHasRelationWithRole.getterRoleRelationType().getterName() + " : " + roleHasRelationWithRole.getterRole2().code)
            }

        }
    }

    def createPlaces(WordWriter wordWriter, Plot plot){
        String txt = ""
        for (GenericPlace place : plot.genericPlaces) {
            wordWriter.addStyledParagraphOfText("T2", place.code)
            wordWriter.addStyledParagraphOfText("T3", "Tags")
            txt = "Les tags du lieu sont : \n"
            for (GenericPlaceHasTag genericPlaceHasTag : place.getGenericPlaceHasTag())
            {
                txt += genericPlaceHasTag.tag.name + " (" + genericPlaceHasTag.weight + "%) \n"
            }
            wordWriter.addStyledParagraphOfText("Normal", txt)
            wordWriter.addStyledParagraphOfText("T3","Description")
            wordWriter.addStyledParagraphOfText("Normal", place.comment)


        }

    }

    def createResources(WordWriter wordWriter, Plot plot){
        String txt = ""
        for (GenericResource resource : plot.genericResources){
            wordWriter.addStyledParagraphOfText("T2", resource.code)
            String tag = "La liste des Tags associés à la ressource sont : \n"
            for (GenericResourceHasTag resourceHasTagHasTag : resource.extTags) {
                    tag += resourceHasTagHasTag.weight + "% - " + resourceHasTagHasTag.tag.name + " ("  + resourceHasTagHasTag.tag.parent.name + ") " + "\n"
            }
            wordWriter.addStyledParagraphOfText("T3", "Tags choisis")
            wordWriter.addStyledParagraphOfText("Normal", tag)
            switch (resource.objectTypeId) {
                case 0:
                    txt = "To define"
                    break
                case 1:
                    txt = "In game"
                    break
                case 2:
                    txt = "Simulated"
                    break
                case 3:
                    txt = "Off game"
                    break
                default :
                    txt = "This should not happen"
                break
                }
            wordWriter.addStyledParagraphOfText("T3", "Resource Type : " + txt)
            wordWriter.addStyledParagraphOfText("T3", "Description")
            wordWriter.addStyledParagraphOfText("Normal", resource.comment)
            if (resource.isIngameClue())
            {
                wordWriter.addStyledParagraphOfText("T3", "la resource est présente en jeu")
                wordWriter.addStyledParagraphOfText("T4", resource.title + " ; Possédé par : " + resource.possessedByRole.code)
                wordWriter.addStyledParagraphOfText("Normal", resource.description)
            }
        }
    }


    def createRelation(WordWriter wordWriter, Plot plot){
//        String text = ""
//        for(Role role : plot.roles)
//        {
//            wordWriter.addStyledParagraphOfText("T2", role.code)
//            for (RoleHasRelationWithRole role1 : role.roleHasRelationWithRolesForRole1Id){
//                for (RoleHasRelationWithRole role2 : role.roleHasRelationWithRolesForRole2Id){
//                    if (role2.id == role.id && role1.id != role1.id)
//                        if (role2.)
//                }
//            }
//        }
    }

    def createPastScene(WordWriter wordWriter, Plot plot){
        String text = ""
        for (Pastscene pastscene : plot.pastescenes)
        {
            wordWriter.addStyledParagraphOfText("T2", pastscene.title)
            if (pastscene.isPublic)
                wordWriter.addStyledParagraphOfText("T4", "La scène passé est publique")
            if (pastscene.genericPlace)
                wordWriter.addStyledParagraphOfText("T4", "La scène est joué dans la place : " + pastscene.genericPlace.code)
            if (pastscene.pastscenePredecessor)
                wordWriter.addStyledParagraphOfText("T4", "LA scène a pour prédécesseur : " + pastscene.pastscenePredecessor.title)
            wordWriter.addStyledParagraphOfText("T3", "Description")
            wordWriter.addStyledParagraphOfText("Normal",pastscene.description)
            wordWriter.addStyledParagraphOfText("T3", "Rôles choisis :")
            for (RoleHasPastscene role : pastscene.roleHasPastscenes){
                wordWriter.addStyledParagraphOfText("T4", "Rôle : " + role.role.code)
                wordWriter.addStyledParagraphOfText("T5", "Title : ")
                wordWriter.addStyledParagraphOfText("Normal", role.title)
                wordWriter.addStyledParagraphOfText("T5", "Description : ")
                wordWriter.addStyledParagraphOfText("Normal", role.description)
            }
            wordWriter.addStyledParagraphOfText("T3", "La scène a eu lieu il y  a : ")
        }
    }

    def createEvent(WordWriter wordWriter, Plot plot){
        for (Event event : plot.events)
        {
            wordWriter.addStyledParagraphOfText("T2", event.name)
            if (event.isPublic)
                wordWriter.addStyledParagraphOfText("T4", "L'event est public")
            if (event.isPlanned)
                wordWriter.addStyledParagraphOfText("T4","L'event est plannifié")
            for (RoleHasEvent role : event.roleHasEvents){
                wordWriter.addStyledParagraphOfText("T4", "Rôle : " + role.role.code)
                if (role.isAnnounced)
                    wordWriter.addStyledParagraphOfText("T5", "Le rôle est annoncé")
                wordWriter.addStyledParagraphOfText("T5", "Title : ")
                wordWriter.addStyledParagraphOfText("Normal", role.title)
                wordWriter.addStyledParagraphOfText("T5", "Description : ")
                wordWriter.addStyledParagraphOfText("Normal", role.description)
                if (role.comment){
                    wordWriter.addStyledParagraphOfText("T5", "Comment : ")
                    wordWriter.addStyledParagraphOfText("Normal", role.comment)
                }
                wordWriter.addStyledParagraphOfText("T5", "Evenemential description : ")
                wordWriter.addStyledParagraphOfText("Normal", role.evenementialDescription)
                wordWriter.addStyledParagraphOfText("T4", "Le personnage possède les ressources suivantes :")
                for (RoleHasEventHasGenericResource genericResource : role.roleHasEventHasGenericResources)
                {
                    wordWriter.addStyledParagraphOfText("Normal", genericResource.genericResource.code + " Quantité : " + genericResource.quantity)
                }
            }
            wordWriter.addStyledParagraphOfText("T5", "Duration : " + event.duration + " min")
            wordWriter.addStyledParagraphOfText("T5", " Timing : " + event.timing + " %")
            if (event.genericPlace)
                wordWriter.addStyledParagraphOfText("T5", "Place : " + event.genericPlace.code )
            if (event.eventPredecessor)
                wordWriter.addStyledParagraphOfText("T5", "L'event précédent est  : " + event.eventPredecessor.name)
            wordWriter.addStyledParagraphOfText("T4", "Description")
            wordWriter.addStyledParagraphOfText("Normal", event.description)
        }

    }
}
