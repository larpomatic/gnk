package org.gnk.gn.redactintrigue

import org.gnk.roletoperso.RoleRelationType
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.gnk.selectintrigue.PlotHasUnivers
import org.gnk.tag.Tag
import org.gnk.tag.TagService;
import org.gnk.tag.Univers
import org.gnk.user.User
import org.springframework.security.access.annotation.Secured

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class RedactIntrigueController {
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[plotInstanceList: Plot.list(params), plotInstanceTotal: Plot.count()]
	}

	def create() {
		[plotInstance: new Plot(params)]
	}

	def saveAndEdit() {
		def plotInstance = new Plot(params)
		plotInstance.creationDate = new Date();
		plotInstance.description = "";
		plotInstance.isEvenemential = false;
		plotInstance.isMainstream = false;
		plotInstance.isPublic = false;
		plotInstance.isDraft = true;
		plotInstance.updatedDate = new Date();
		plotInstance.user = User.getAll().get(0);
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

	def show(Long id) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
			redirect(action: "list")
			return
		}

		[plotInstance: plotInstance]
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
		[plotInstance: plotInstance,
                plotTagList: tagService.getPlotTagQuery(),
                roleTagList: tagService.getRoleTagQuery(),
                resourceTagList: tagService.getResourceTagQuery(),
                placeTagList: tagService.getPlaceTagQuery(),
                relationTypes: RoleRelationType.list(),
                screenStep: screen]
	}

	def update(Long id) {
        def isupdate = true;
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
            isupdate = false;
		}
		plotInstance.properties = params
		plotInstance.description = params.plotDescription
        plotInstance.pitchOrga = params.plotPitchOrga
        plotInstance.pitchPj = params.plotPitchPj
        plotInstance.pitchPnj = params.plotPitchPnj
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
			redirect(action: "list")
			return
		}
		// FIXME Changement de Base
			plotInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'plot.label', default: 'Plot'), id])
			redirect(action: "list")
	}
}
