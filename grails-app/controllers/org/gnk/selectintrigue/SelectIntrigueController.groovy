package org.gnk.selectintrigue

import org.gnk.gn.GnHasConvention
import org.gnk.gn.GnHasUser
import org.gnk.naming.Convention
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Map.Entry

import org.gnk.gn.Gn;

import org.gnk.tag.Tag
import org.gnk.tag.TagService
@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class SelectIntrigueController {

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		[gnInstanceList: Gn.list(params), gnInstanceTotal: Gn.count()]
	}

    def dispatchStep(Long id) {
        if (id == 0) {
            redirect(controller: 'selectIntrigue', action:'selectIntrigue', id: id);
        }
        Gn gn = Gn.get(id);
        assert (gn != null);
        if (gn == null) {
            redirect(controller: 'selectIntrigue', action: "list");
        }
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        String step = gn.step;
        if (step == "selectIntrigue") {
            redirect(action: "selectIntrigue", controller: "selectIntrigue", id: id, params: [screenStep: "1"/*, gnDTD: dtd*/]);
        }
        else if (step == "role2perso") {
            Integer evenementialId = 0;
            Integer mainstreamId = 0;
            for (Plot plot in gn.selectedPlotSet) {
                if (plot.isEvenemential) {
                    evenementialId = Plot.findByName(plot.name).id;
                }
                else if (plot.isMainstream && gn.isMainstream) {
                    mainstreamId = Plot.findByName(plot.name).id;;
                }
            }
            redirect(controller: 'roleToPerso', action:'roleToPerso', params: [gnId: id as String,
                    selectedMainstream: mainstreamId as String,
                    selectedEvenemential: evenementialId as String]);
        }
        else if (step == "substitution") {
            List<String> sexes = new ArrayList<>();
            for (org.gnk.roletoperso.Character character in gn.characterSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            for (org.gnk.roletoperso.Character character in gn.nonPlayerCharSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            redirect(controller: 'substitution', action:'index', params: [gnId: id as String, sexe: sexes]);
        }
        else if (step == "publication") {
            redirect(controller: 'publication', action:'index', params: [gnId: id as String]);
        }
        else {
            redirect(controller: 'selectIntrigue', action:'selectIntrigue', id: 0);
        }
    }

	def selectIntrigue(Long id) {
		Gn gnInstance
        TagService tagService = new TagService();
        List<Plot> eligiblePlots = Plot.findAllWhere(isDraft: false);
		Set<Plot> selectedPlotInstanceList = new HashSet<Plot>();
        Set<Plot> selectedEvenementialPlotInstanceList = new HashSet<Plot>();
        Set<Plot> selectedMainstreamPlotInstanceList = new HashSet<Plot> ();
        Set<Plot> nonTreatedPlots = new HashSet<Plot>(eligiblePlots);
		List<List<String>> statisticResultList = new ArrayList<List<String>>();
        Integer evenementialId = 0;
        Integer mainstreamId = 0;
		if (id >= 0) {
			gnInstance = Gn.get(id)
			if ((params.screenStep as Integer) == 1) {
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
                    else if (it.key.startsWith("selected_evenemential")) {
                        evenementialId = it.value as Integer;
                    }
                    else if (it.key.startsWith("selected_mainstream")) {
                        mainstreamId = it.value as Integer;
                    }
				}

				SelectIntrigueProcessing algo = new SelectIntrigueProcessing(gnInstance, eligiblePlots, bannedPlot, lockedPlot)
				selectedPlotInstanceList = algo.getSelectedPlots();
                selectedEvenementialPlotInstanceList = algo.getSelectedEvenementialPlotList();
                if (selectedEvenementialPlotInstanceList.size() == 0) {
                    flash.message = "Aucune intrigue évenementielle trouvée. Augmentez le nombre de joueurs."
                    render(view: "selectIntrigue", model: [gnInstance: gnInstance, universList: tagService.getUniversTagQuery()])
                    return
                }
                selectedMainstreamPlotInstanceList = algo.getSelectedMainstreamPlotList();
				gnInstance.selectedPlotSet = selectedPlotInstanceList;
				gnInstance.bannedPlotSet = bannedPlot;
				gnInstance.lockedPlotSet = lockedPlot;
				gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)
                gnInstance.step = "selectIntrigue";
				if (!gnInstance.save(flush: true)) {
					render(view: "selectIntrigue", model: [gnInstance: gnInstance, universList: tagService.getUniversTagQuery()])
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
			} else {
                new GNKDataContainerService().ReadDTD(gnInstance)
            }
		}

        nonTreatedPlots.removeAll(selectedPlotInstanceList)
        nonTreatedPlots.removeAll(selectedEvenementialPlotInstanceList);
        if (gnInstance && gnInstance.bannedPlotSet)
            nonTreatedPlots.removeAll(gnInstance.bannedPlotSet);

       [gnInstance: gnInstance,
               screenStep: params?.screenStep,
               plotTagList: tagService.getPlotTagQuery(),
               universList: tagService.getUniversTagQuery(),
               plotInstanceList: selectedPlotInstanceList,
               evenementialPlotInstanceList: selectedEvenementialPlotInstanceList,
               mainstreamPlotInstanceList: selectedMainstreamPlotInstanceList,
               bannedPlotInstanceList: gnInstance?.bannedPlotSet,
               nonTreatedPlots: nonTreatedPlots ,
               statisticResultList: statisticResultList,
               evenementialId: evenementialId,
               mainstreamId: mainstreamId,
               conventionList: Convention.list()]
	}

    def goToRoleToPerso(Long id) {
        Gn gnInstance;
        TagService tagService = new TagService();
        Integer evenementialId = 0;
        Integer mainstreamId = 0;
        if (id >= 0) {
            gnInstance = Gn.get(id)
            if ((params.screenStep as Integer) == 1) {
                String gnDTD = params.gnDTD
                gnInstance.dtd = gnDTD
                new GNKDataContainerService().ReadDTD(gnInstance)
                HashSet<Plot> bannedPlot = new HashSet<Plot>();
                HashSet<Plot> lockedPlot = new HashSet<Plot>();
                HashSet<Plot> selectedPlot = new HashSet<Plot>();
                params.each {
                    if (it.key.startsWith("plot_status_") && it.value != "3") {
                        // Locked = 1, Banned= 2, Selected = 3
                        Plot plot = Plot.get((it.key - "plot_status_") as Integer);
                        if (it.value == "1") {
                            lockedPlot.add(plot)
                        } else if (it.value == "2") {
                            bannedPlot.add(plot)
                        } else {
                            selectedPlot.add(plot);
                        }
                    } else if (it.key.startsWith("keepBanned_")) {
                        final Plot plotToBan = Plot.get((it.key - "keepBanned_") as Integer)
                        bannedPlot.add(plotToBan);
                        lockedPlot.remove(plotToBan);
                        selectedPlot.remove(plotToBan);
                    } else if (it.key.startsWith("toLock_")) {
                        final Plot plotToLock = Plot.get((it.key - "toLock_") as Integer)
                        lockedPlot.add(plotToLock);
                        selectedPlot.add(plotToLock);
                        bannedPlot.remove(plotToLock);
                    }
                    else if (it.key.startsWith("selected_evenemential")) {
                        evenementialId = it.value as Integer;
                    }
                    else if (it.key.startsWith("selected_mainstream")) {
                        mainstreamId = it.value as Integer;
                    }
                }
                gnInstance.selectedPlotSet = selectedPlot;
                gnInstance.bannedPlotSet = bannedPlot;
                gnInstance.lockedPlotSet = lockedPlot;
                gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)
                if (!gnInstance.save(flush: true)) {
                    render(view: "selectIntrigue", model: [gnInstance: gnInstance, universList: tagService.getUniversTagQuery()])
                    return
                }

            } else {
                new GNKDataContainerService().ReadDTD(gnInstance)
            }
        }
        redirect(controller: 'roleToPerso', action:'roleToPerso', params: [gnId: id as String,
                selectedMainstream: mainstreamId as String,
                selectedEvenemential: evenementialId as String]);
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
        GnHasUser gnHasUser = new GnHasUser();
        gnHasUser.isCreator = true;
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = user.getUsername();
        gnHasUser.user = org.gnk.user.User.findByUsername(currentUsername);
        GnHasConvention gnHasConvention = new GnHasConvention()
        if (params.convention) {
            gnHasConvention.version = 1
            gnHasConvention.gn = gnInstance
            gnHasConvention.convention = Convention.findWhere(id: params.convention as Integer)

            gnInstance.gnHasConvention = gnHasConvention
        }

		formatParams(gnInstance)
        gnInstance.step = 'selectIntrigue';
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
        TagService tagService = new TagService();
		[gnInstance: gnInstance, universList: tagService.getUniversTagQuery()]
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

        GnHasConvention gnHasConvention = GnHasConvention.findWhere(gn: gnInstance)
        gnHasConvention.version = gnHasConvention.version + 1
        gnHasConvention.convention = Convention.findWhere(id: params.convention as Integer)

        formatParams(gnInstance)
		gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)

		if (!gnInstance.save(flush: true) || !gnHasConvention.save(flush: true)) {
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
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (params.t0DateHour) {
            Calendar calendar = isValidDate(params.t0DateHour as String, "dd/MM/yyyy HH:mm");
            if (params.t0DateHourUnity == "+") {
                calendar.set(calendar.ERA, GregorianCalendar.AD)
//                calendar.ERA = GregorianCalendar.AD;
            }
            else {
                calendar.set(calendar.ERA, GregorianCalendar.BC)
//                calendar.ERA = GregorianCalendar.BC;
            }
            if (calendar) {
                gnInstance.t0Date = calendar.getTime();
            }
        }
        if (params.gnDateHour) {
            Calendar calendar = isValidDate(params.gnDateHour as String, "dd/MM/yyyy HH:mm");
            if (params.gnDateHourUnity == "+") {
                calendar.set(calendar.ERA, GregorianCalendar.AD)
            }
            else {
                calendar.set(calendar.ERA, GregorianCalendar.BC)
            }
            if (calendar) {
                gnInstance.date = calendar.getTime();
            }
        }
//		if (params.gnDate) {
//			gnInstance.date = sdf.parse(params.gnDate);
//		}
//        if (params.gnDateHour) {
//            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
//            Calendar calHour = Calendar.getInstance();
//            calHour.setTime(sdfHour.parse(params.gnDateHour));
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(gnInstance.date);
//            cal.set(Calendar.HOUR_OF_DAY, calHour.get(Calendar.HOUR_OF_DAY))
//            cal.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE))
//            gnInstance.date = cal.getTime();
//        }
		if (params.univers) {
			gnInstance.univers = Tag.get(params.univers as Integer)
		}
		if (params.gnStep) {
			gnInstance.step = params.gnStep
		}
		if (params.gnArchitechture) {
			gnInstance.isMainstream = Boolean.parseBoolean(params.gnArchitechture)
		}
//		if (params.t0Date) {
//			gnInstance.t0Date = sdf.parse(params.t0Date)
//		}
//        if (params.t0Hour) {
//            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
//            Calendar calHour = Calendar.getInstance();
//            calHour.setTime(sdfHour.parse(params.t0Hour));
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(gnInstance.t0Date);
//            cal.set(Calendar.HOUR_OF_DAY, calHour.get(Calendar.HOUR_OF_DAY))
//            cal.set(Calendar.MINUTE, calHour.get(Calendar.MINUTE))
//            gnInstance.t0Date = cal.getTime();
//        }
		if (params.gnDuration) {
			gnInstance.duration = params.gnDuration as Integer
		}
		if (params.gnPIPMin) {
			gnInstance.pipMin = params.gnPIPMin as Integer
		}
		if (params.gnPIPMax) {
			gnInstance.pipMax = params.gnPIPMax as Integer
		}
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
        gnTags.put(gnInstance.univers, 101);
        mainstreamTags.put(gnInstance.univers, 101);
        evenementialTags.put(gnInstance.univers, 101);
	}

    public Calendar isValidDate(String dateToValidate, String dateFromat){
        if(dateToValidate == null){
            return null;
        }
        if (dateToValidate.indexOf(":") == -1) {
            dateToValidate = dateToValidate + " 00:00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.ERA, GregorianCalendar.BC);
            Date date = sdf.parse(dateToValidate);
            cal.setTime(date)
            return cal;
        } catch (ParseException e) {
            return null;
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
