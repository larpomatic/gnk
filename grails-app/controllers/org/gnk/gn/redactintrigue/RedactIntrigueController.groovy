package org.gnk.gn.redactintrigue

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

		flash.message = message(code: 'default.created.message', args: [
			message(code: 'plot.label', default: 'Plot'),
			plotInstance.id
		])
		redirect(action: "edit", id: plotInstance.id)
	}

	def save() {
		Object obj = params
		def plotInstance = new Plot(params)
		if (!plotInstance.save(flush: true)) {
			render(view: "create", model: [plotInstance: plotInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [
			message(code: 'plot.label', default: 'Plot'),
			plotInstance.id
		])
		redirect(action: "show", id: plotInstance.id)
	}

	def show(Long id) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'plot.label', default: 'Plot'), id])
			redirect(action: "list")
			return
		}

		[plotInstance: plotInstance]
	}

	def edit(Long id) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'plot.label', default: 'Plot'), id])
			redirect(action: "list")
			return
		}
		int screen = 0
		if (params.screenStep)
		{
			screen = params.screenStep 
			screen = (screen - 48)	 
		}
		[plotInstance: plotInstance,
                plotTagList: new TagService().getPlotTagQuery(),
                universList: Univers.list(),
                roleTagList: new TagService().getRoleTagQuery(),
                resourceTagList: new TagService().getResourceTagQuery(),
                placeTagList: new TagService().getPlaceTagQuery(),
                screenStep: screen]
	}

	def update(Long id, Long version) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'plot.label', default: 'Plot'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (plotInstance.version > version) {
				plotInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'plot.label', default: 'Plot')] as Object[],
						"Another user has updated this Plot while you were editing")
				render(view: "edit", model: [plotInstance: plotInstance])
				return
			}
		}

		plotInstance.properties = params
		plotInstance.description = params.plotDescription
		Set<PlotHasTag> toRemove = new HashSet<PlotHasTag>(plotInstance.extTags)

		while (!(toRemove.empty))
		{
			PlotHasTag plotHasPlotTag = toRemove.first()
			toRemove.remove(plotHasPlotTag)
			plotInstance.extTags.remove(plotHasPlotTag);
			plotHasPlotTag.delete(flush: true)
			
		}
		
		Set<PlotHasUnivers> toRemoveUnivers = new HashSet<PlotHasUnivers>(plotInstance.plotHasUniverses)
		
        while (!(toRemoveUnivers.empty))
        {
            PlotHasUnivers plotHasUnivers = toRemoveUnivers.findAll().first()
            toRemoveUnivers.remove(plotHasUnivers)
            plotInstance.plotHasUniverses.remove(plotHasUnivers);
            plotHasUnivers.univers.plotHasUniverses.remove(plotHasUnivers)
            plotHasUnivers.delete(flush: true)

        }
	    
		params.each {
			if (it.key.startsWith("tags_")) {
				PlotHasTag plotHasPlotTag = new PlotHasTag()
				Tag plotTag = Tag.get((it.key - "tags_") as Integer);
				plotHasPlotTag.tag = plotTag
				String weight = params.get("weight_tags_" + plotTag.id);
				plotHasPlotTag.weight = weight as Integer
				plotHasPlotTag.plot = plotInstance
				plotInstance.extTags.add(plotHasPlotTag)
			}
		}
		
		params.each {
			if (it.key.startsWith("universes_")) {
				PlotHasUnivers plotHasUnivers = new PlotHasUnivers()
				plotHasUnivers.univers = Univers.get((it.key - "universes_") as Integer);
				plotHasUnivers.weight = TagService.LOCKED
				plotHasUnivers.plot = plotInstance
				plotInstance.plotHasUniverses.add(plotHasUnivers)
			}
		}

		if (!plotInstance.save(flush: true)) {
			render(view: "edit", model: [plotInstance: plotInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [
			message(code: 'plot.label', default: 'Plot'),
			plotInstance.id
		])
		redirect(action: "show", id: plotInstance.id)
	}

	def delete(Long id) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
			flash.message = mes sage(code: 'default.not.found.message', args: [message(code: 'plot.label', default: 'Plot'), id])
			redirect(action: "list")
			return
		}
		// FIXME Changement de base
		//try {
			plotInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'plot.label', default: 'Plot'), id])
			redirect(action: "list")
		//}
		
		/*catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'plot.label', default: 'Plot'), id])
			redirect(action: "show", id: id)
		}*/
	}
}
