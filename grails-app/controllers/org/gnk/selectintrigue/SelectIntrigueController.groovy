package org.gnk.selectintrigue

import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLReaderService
import org.gnk.parser.gn.GnXMLWriterService

import java.text.SimpleDateFormat
import java.util.Map.Entry

import org.gnk.gn.Gn;
import org.gnk.tag.Univers;
import org.gnk.tag.Tag
import org.gnk.tag.TagService

class SelectIntrigueController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[gnInstanceList: Gn.list(params), gnInstanceTotal: Gn.count()]
	}

	def selectIntrigue(Long id) {
		Gn gnInstance
        List<Plot> eligiblePlots = Plot.findAllWhere(isDraft: false);
		Set<Plot> selectedPlotInstanceList = new HashSet<Plot>()
        Set<Plot> selectedEvenementialPlotInstanceList = new HashSet<Plot>()
        Set<Plot> nonTreatedPlots = new HashSet<Plot>(eligiblePlots);
		List<List<String>> statisticResultList = new ArrayList<List<String>>()
		if (id >= 0) {
			gnInstance = Gn.get(id)
            new GNKDataContainerService().ReadDTD(gnInstance)
			if ((params.screenStep as Integer) == 1) {
				String gnDTD = params.gnDTD
				gnInstance.dtd = gnDTD
                new GNKDataContainerService().ReadDTD(gnInstance)
				HashSet<Plot> bannedPlot = new HashSet<Plot>();
				HashSet<Plot> lockedPlot = new HashSet<Plot>();

				params.each {
					if (it.key.startsWith("plot_status_") && it.value != "3") {
						// Locked = 1, Banned= 2, Selected = 3
						Plot plot = Plot.get((it.key - "plot_status_") as Integer);
						if (it.value == "1") {
							lockedPlot.add(plot)
						} else {
							bannedPlot.add(plot)
						}
					} else if (it.key.startsWith("keepBanned_")) {
                        final Plot plotToBan = Plot.get((it.key - "keepBanned_") as Integer)
                        bannedPlot.add(plotToBan);
                        lockedPlot.remove(plotToBan);

					} else if (it.key.startsWith("toLock_")) {
                        final Plot plotToLock = Plot.get((it.key - "toLock_") as Integer)
                        lockedPlot.add(plotToLock);
                        bannedPlot.remove(plotToLock);
                    }
				}

				SelectIntrigueProcessing algo = new SelectIntrigueProcessing(gnInstance, eligiblePlots, bannedPlot, lockedPlot)
				selectedPlotInstanceList = algo.getSelectedPlots();
                selectedEvenementialPlotInstanceList = algo.getSelectedEvenementialPlotList();
				gnInstance.selectedPlotSet = selectedPlotInstanceList;
				gnInstance.bannedPlotSet = bannedPlot;
				gnInstance.lockedPlotSet = lockedPlot;
				gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)
				if (!gnInstance.save(flush: true)) {
					render(view: "selectIntrigue", model: [gnInstance: gnInstance])
					return
				}

				Map<Tag, Integer> res = algo.getTagsResult()
				Integer pipMin = gnInstance.getPipMin()
				Integer pipMax = gnInstance.getPipMax()
				String objectivePip = pipMin.toString() + "-" + pipMax.toString()
				insertNewStatValue("PIP", objectivePip, algo.getPipByPlayer().toString(), statisticResultList)


				for (Entry<Tag, Integer> entry : res.entrySet()) {
					String tagName = entry.getKey().toString()
					String weightObjective = gnInstance.getGnTags().get(entry.getKey()).toString() + "%"
					String weightResult = entry.getValue().toString() + "%"
					insertNewStatValue(tagName, weightObjective, weightResult, statisticResultList)
				}
			}
		}
        nonTreatedPlots.removeAll(selectedPlotInstanceList)
        if (gnInstance && gnInstance.bannedPlotSet)
            nonTreatedPlots.removeAll(gnInstance.bannedPlotSet);
		[gnInstance: gnInstance,
                screenStep: params?.screenStep,
                plotTagList: (new TagService()).getPlotTagQuery(),
                universList: Univers.list(),
                plotInstanceList: selectedPlotInstanceList,
                evenementialPlotInstanceList: selectedEvenementialPlotInstanceList,
                bannedPlotInstanceList: gnInstance?.bannedPlotSet,
                nonTreatedPlots: nonTreatedPlots ,
                statisticResultList: statisticResultList]
	}

	def private insertNewStatValue (String name, String objective, String result, List<List<String>> statisticResultList) {
		List<String> stat = new ArrayList<String>(3)
		stat.add(name)
		stat.add(objective)
		stat.add(result)
		statisticResultList.add(stat)
	}

	def save() {
		Gn gnInstance = new Gn(params)
		formatParams(gnInstance)
		gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)
		if (!gnInstance.save(flush: true)) {
			render(view: "selectIntrigue", model: [gnInstance: gnInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [
			message(code: 'gn.label', default: 'GN'),
			gnInstance.id
		])
		redirect(action: "selectIntrigue",id: gnInstance.id, params: [screenStep: 1, gnDTD: gnInstance.dtd])
	}

	def show(Long id) {
		def gnInstance = Gn.get(id)
        new GNKDataContainerService().ReadDTD(gnInstance)
		if (!gnInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'gn.label', default: 'GN'), id])
			redirect(action: "list")
			return
		}
		[gnInstance: gnInstance]
	}

	def edit(Long id) {
		def gnInstance = Gn.get(id)
        new GNKDataContainerService().ReadDTD(gnInstance)
		if (!gnInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'gn.label', default: 'GN'), id])
			redirect(action: "list")
			return
		}

		[gnInstance: gnInstance, universList: Univers.list()]
	}

	def saveOrUpdate(Long id, Long version) {

		if (id == null || version == null) {
			save()
			return
		}
		def gnInstance = Gn.get(id)
		if (!gnInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'gn.label', default: 'GN'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (gnInstance.version > version) {
				gnInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'gn.label', default: 'GN')] as Object[],
						"Another user has updated this GN while you were editing")
				render(view: "selectIntrigue", model: [gnInstance: gnInstance])
				return
			}
		}
		gnInstance.properties = params
		formatParams(gnInstance)
		gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)
		if (!gnInstance.save(flush: true)) {
			render(view: "selectIntrigue", model: [gnInstance: gnInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [
			message(code: 'gn.label', default: 'GN'),
			gnInstance.id
		])
		redirect(action: "selectIntrigue", id: gnInstance.id, params: [gnInstanceId: gnInstance.id, gnDTD: gnInstance.dtd, screenStep: 1])
	}

	def formatParams (Gn gnInstance) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (params.gnDate) {
			gnInstance.date = sdf.parse(params.gnDate);
		}
        if (params.gnDateHour) {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
            Calendar calHour = Calendar.getInstance();
            calHour.setTime(sdfHour.parse(params.gnDateHour));
            Calendar cal = Calendar.getInstance();
            cal.setTime(gnInstance.date);
            cal.set(Calendar.HOUR_OF_DAY, calHour.get(Calendar.HOUR_OF_DAY))
            cal.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE))
            gnInstance.date = cal.getTime();
        }
		if (params.univers) {
			gnInstance.univers = Univers.get(params.univers as Integer)
		}
		if (params.gnStep) {
			gnInstance.step = params.gnStep
		}
		if (params.isMainstream) {
			gnInstance.isMainstream = Boolean.parseBoolean(params.isMainstream)
		}
		if (params.t0Date) {
			gnInstance.t0Date = sdf.parse(params.t0Date)
		}
        if (params.t0Hour) {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
            Calendar calHour = Calendar.getInstance();
            calHour.setTime(sdfHour.parse(params.t0Hour));
            Calendar cal = Calendar.getInstance();
            cal.setTime(gnInstance.t0Date);
            cal.set(Calendar.HOUR_OF_DAY, calHour.get(Calendar.HOUR_OF_DAY))
            cal.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE))
            gnInstance.t0Date = cal.getTime();
        }
		if (params.gnDuration) {
			gnInstance.duration = params.gnDuration as Integer
		}
		if (params.gnPIPMin) {
			gnInstance.pipMin = params.gnPIPMin as Integer
		}
		if (params.gnPIPMax) {
			gnInstance.pipMax = params.gnPIPMax as Integer
		}
        //TODO Remove PipCore definitively or update if used one day
		if (params.gnPIPCore) {
			gnInstance.pipCore = params.gnPIPCore as Integer
		} else {
            gnInstance.pipCore = gnInstance.pipMin;
        }
		if (params.gnNbPlayers) {
			gnInstance.nbPlayers = params.gnNbPlayers as Integer
		}
		if (params.gnNbMen) {
			gnInstance.nbMen = params.gnNbMen as Integer
		}
		if (params.gnNbWomen) {
			gnInstance.nbWomen = params.gnNbWomen as Integer
		}
		Map<Tag, Integer> gnTags = gnInstance.gnTags
		Map<Tag, Integer> mainstreamTags = gnInstance.mainstreamTags
		Map<Tag, Integer> evenementialTags = gnInstance.evenementialTags
		if (gnTags) {
			gnTags.clear();
		} else {
			gnTags = new HashMap<Tag, Integer>();
			gnInstance.gnTags = gnTags;
		}
		if (mainstreamTags) {
			mainstreamTags.clear();
		} else {
			mainstreamTags = new HashMap<Tag, Integer>();
			gnInstance.mainstreamTags = mainstreamTags;
		}
		if (evenementialTags) {
			evenementialTags.clear();
		} else {
			evenementialTags = new HashMap<Tag, Integer>();
			gnInstance.evenementialTags = evenementialTags;
		}
		params.each {
			if (it.key.startsWith("tags_")) {
				Tag plotTag = Tag.get((it.key - "tags_") as Integer);
				String weight = params.get("weight_tags_" + plotTag.id);
                assert (weight != null && weight.isInteger()): weight
                if (weight != null && weight.isInteger())
				    gnTags.put(plotTag, weight as Integer);
			}
			else if (it.key.startsWith("tagsMainstream_")) {
				Tag plotTag = Tag.get((it.key - "tagsMainstream_") as Integer);
                String weight = params.get("weight_tagsMainstream_" + plotTag.id)
                assert (weight != null && weight.isInteger()): weight
                if (weight != null && weight.isInteger())
				    mainstreamTags.put(plotTag, weight as Integer)
			}
			else if (it.key.startsWith("tagsEvenemential_")) {
				Tag plotTag = Tag.get((it.key - "tagsEvenemential_") as Integer);
                String weight = params.get("weight_tagsEvenemential_" + plotTag.id)
                assert (weight != null && weight.isInteger()) : weight
                if (weight != null && weight.isInteger())
				    evenementialTags.put(plotTag, weight as Integer)
			}
		}
	}

	def displayDTD() {
		String gnDTD2Html = params.gnDTD
		gnDTD2Html = gnDTD2Html.replaceAll("<", "&lt")
		gnDTD2Html = gnDTD2Html.replaceAll(">", "&gt")
		[gnInstanceId: params.gnInstanceId, gnDTD: gnDTD2Html]
	}

	def delete(Long id) {
		def gnInstance = Gn.get(id)
		if (!gnInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'gn.label', default: 'GN'), id])
			redirect(action: "list")
			return
		}
		gnInstance.delete(flush: true)
		flash.message = message(code: 'default.deleted.message', args: [message(code: 'gn.label', default: 'GN'), id])
		redirect(action: "list")
	}
}
