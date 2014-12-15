package org.gnk.selectintrigue

import org.gnk.roletoperso.Role
import org.gnk.tag.Tag

class PlotController {

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

	def save() {

        print "-------------- " + params
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

	def deletePlot(Long idPlot) {
		def plotInstance = Plot.get(idPlot)
		plotInstance.delete()
		flash.messageInfo = message(code: 'adminRef.plot.info.delete', args: [plotInstance.name])
		redirect(action: "list")
	}

    def deleteFullPlot() {
        print params
        def plotInstance = Plot.get(params.id)

        for (Role r : plotInstance.roles)
            r.delete()

        plotInstance.delete()
        flash.messageInfo = message(code: 'adminRef.plot.info.delete', args: [plotInstance.name])
        redirect(action: "list")
    }

	def addTagToPlot() {
		 if (params.Plot_select.equals("") || params.Tag_select.equals(""))
		 {
             print "Invalid params"
             flash.message = message(code: 'Erreur : Il faut choisir un tag et une intrigue !')
             redirect(action: "list")
             return
		 }

		 Plot plotInstance = Plot.get(params.Plot_select.toInteger())
		 Tag tagInstance = Tag.get(params.Tag_select.toInteger())

        print tagInstance.name

		 if (plotInstance.equals(null) || tagInstance.equals(null))
		 {
             print "Plot or Tag not found"
             flash.message = message(code: 'Erreur : Tag ou intrigue invalide !')
             redirect(action: "list")
             return
		 }

		 for (PlotHasTag plotTag : PlotHasTag.list())
		 {
             if (plotTag.tag.id == tagInstance.id && plotTag.plot.id == plotInstance.id)
             {
                 print "This tag is already linked to this plot"
                 flash.message = message(code: 'Erreur : Cette relation existe déjà !')
                 redirect(action: "list")
                 return
             }
		 }

        PlotHasTag plotTagInstance = new PlotHasTag()


        plotTagInstance.tag = tagInstance
        plotTagInstance.plot = plotInstance
        plotTagInstance.weight = params.weight.toInteger()
		 if (!plotTagInstance.save())
		    print "error : plotHasPlotTagInstance was not saved."

		 plotInstance.extTags.add(plotTagInstance)
		 plotInstance.save()
		 flash.messageInfo = message(code: 'adminRef.tagToPlot.info.create', args: [tagInstance.name, plotInstance.name])
		 redirect(action: "list")
	}

//    def addUniversToPlot() {
//        if (params.Plot_select.equals("") || params.Tag_select.equals(""))
//        {
//            print "Invalid params"
//            flash.message = message(code: 'Erreur : Il faut choisir un tag et une intrigue !')
//            redirect(action: "list")
//            return
//        }
//
//        Plot plotInstance = Plot.get(params.Plot_select.toInteger())
//        Univers universInstance = Univers.get(params.Univers_select.toInteger())
//
//        PlotHasUnivers plotHasUnivers = new PlotHasUnivers()
//        plotHasUnivers.plot = plotInstance
//        plotHasUnivers.univers = universInstance
//        plotHasUnivers.weight = params.weight.toInteger()
//
//        plotHasUnivers.save()
//
//        flash.messageInfo = message(code: 'adminRef.PlotHasUnivers.info.create', args: [plotInstance.name, universInstance.name])
//        redirect(action: "list")
//    }

//    def removeUnivers()
//    {
//        if (params.plotHasUniversesId != "")
//        {
//            PlotHasUnivers plotHasUnivers = PlotHasUnivers.get(params.plotHasUniversesId)
//            if (plotHasUnivers == null)
//            {
//                flash.message = "Erreur lors de la suppression de l'univers"
//                redirect(action: "list")
//            }
//
//            plotHasUnivers.delete()
//
//            flash.messageInfo = message(code: 'adminRef.PlotHasUnivers.info.delete', args: [plotHasUnivers.plot.name, plotHasUnivers.univers.name])
//            redirect(action: "list")
//        }
//
//    }

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

		[plotInstance: plotInstance]
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
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'plot.label', default: 'Plot'), id])
			redirect(action: "list")
			return
		}

		plotInstance.delete(flush: true)
		flash.message = message(code: 'default.deleted.message', args: [message(code: 'plot.label', default: 'Plot'), id])
		redirect(action: "list")
	}

	def deleteTag() {

        print params
		PlotHasTag plotHasTagInstance = new PlotHasTag()
        plotHasTagInstance = PlotHasTag.get(params.plotHaTagId)

		if (plotHasTagInstance.equals(null))
		{
             print "Error : plotHasPlotTag not found."
             flash.message = message(code: 'Erreur : la relation est invalide.')
             redirect(action: "list")
             return
		}

        plotHasTagInstance.delete(flush: true)
		flash.messageInfo = message(code: 'adminRef.tagIntoTagFamily.info.delete')
		redirect(action: "list")
	}
}
