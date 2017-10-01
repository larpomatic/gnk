package org.gnk.selectintrigue

import org.gnk.gn.GnHasUser
import org.gnk.naming.Convention
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasTag
import org.gnk.step.SelectStepService
import org.gnk.user.User
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import sun.net.www.content.audio.x_aiff

import java.awt.List
import java.util.Collections;
import java.util.List;
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Map.Entry

import org.gnk.gn.Gn;

import org.gnk.tag.Tag
import org.gnk.tag.TagService
@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class SelectIntrigueController {

    SelectStepService selectStepService;
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max, Gn gn) {
        //params.max = Math.min(max ?: 10, 100)

        //User user = User.findById(GnHasUser.findByGnAndIsCreator(gn.id , true).userId);
        //List<GnHasUser> gnHasUser = GnHasUser.findAll()

        [gnInstanceList: Gn.list(params), gnInstanceTotal: Gn.count()]
    }

    def dispatchStep(Long id) {
        if (id == 0) {
            redirect(controller: 'selectIntrigue', action:'selectIntrigue', id: id);
        }
        Gn gn = Gn.get(id);
        String stepID = "step-" +id
        String step = params[stepID]
        assert (gn != null);
        if (gn == null) {
            redirect(controller: 'selectIntrigue', action: "list");
        }
        final gnData = new GNKDataContainerService();
        if (step != gn.step)
            selectStepService.chooseStep(gn, step);
        gnData.ReadDTD(gn);
        step = gn.step;
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
        else if (step == "life"){
            redirect(controller: 'life', action:'life', params: [gnId: id as String]);
        }
        else if (step == "naming") {
            List<String> sexes = new ArrayList<>();
            for (org.gnk.roletoperso.Character character in gn.characterSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            for (org.gnk.roletoperso.Character character in gn.nonPlayerCharSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            redirect(controller: 'naming', action:'index', params: [gnId: id as String, sexe: sexes]);
        }
        else if (step == "ressource") {
            List<String> sexes = new ArrayList<>();
            for (org.gnk.roletoperso.Character character in gn.characterSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            for (org.gnk.roletoperso.Character character in gn.nonPlayerCharSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            redirect(controller: 'ressource', action:'index', params: [gnId: id as String, sexe: sexes]);
        }
        else if (step == "place") {
            List<String> sexes = new ArrayList<>();
            for (org.gnk.roletoperso.Character character in gn.characterSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            for (org.gnk.roletoperso.Character character in gn.nonPlayerCharSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            redirect(controller: 'placeSub', action:'index', params: [gnId: id as String, sexe: sexes]);
        }
        else if (step == "time") {
            List<String> sexes = new ArrayList<>();
            for (org.gnk.roletoperso.Character character in gn.characterSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            for (org.gnk.roletoperso.Character character in gn.nonPlayerCharSet) {
                sexes.add("sexe_" + character.getDTDId() as String);
            }
            redirect(controller: 'time', action:'index', params: [gnId: id as String, sexe: sexes]);
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
        ArrayList<Plot> selectedEvenementialPlotInstanceList = new ArrayList<Plot>();
        ArrayList<Plot> selectedMainstreamPlotInstanceList = new ArrayList<Plot>();
        Set<Plot> nonTreatedPlots = null;
        List<List<String>> statisticResultList = new ArrayList<List<String>>();
        Integer evenementialId = 0;
        Integer mainstreamId = 0;
        if (id >= 0) {
            gnInstance = Gn.get(id)
            if ((params.screenStep as Integer) == 1) {
                final gnData = new GNKDataContainerService().ReadDTD(gnInstance);
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

                        Plot plot = Plot.get((it.key - "selected_mainstream") as Integer);
                        if (it.value == "1" && it.value == "3") {
                            lockedPlot.add(plot)
                            mainstreamId = plot.id
                        } else {
                            bannedPlot.add(plot)
                        }
                    }
                }

                SelectIntrigueProcessing algo = new SelectIntrigueProcessing(gnInstance, eligiblePlots, bannedPlot, lockedPlot)
                nonTreatedPlots = new HashSet<Plot>(algo._allPlotList);
                selectedPlotInstanceList = algo.getSelectedPlots();
                selectedEvenementialPlotInstanceList = algo.getSelectedEvenementialPlotList();
                if ((selectedEvenementialPlotInstanceList != null) && (selectedEvenementialPlotInstanceList.size() >0))
                    evenementialId = selectedEvenementialPlotInstanceList.first().getId();
                if (selectedEvenementialPlotInstanceList.size() == 0) {
                    flash.message = "Aucune intrigue évenementielle trouvée. Augmentez le nombre de joueurs."
                    render(view: "selectIntrigue", model: [gnInstance: gnInstance, universList: tagService.getUniversTagQuery(), conventionList: Convention.list()])
                    return
                }

                if (gnInstance.isMainstream) {
                    selectedMainstreamPlotInstanceList = algo.getSelectedMainstreamPlotList();
                    if ((selectedMainstreamPlotInstanceList != null) && (selectedMainstreamPlotInstanceList.size() > 0))
                        mainstreamId = selectedMainstreamPlotInstanceList.first().getId();
                }
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


        List universList = tagService.getUniversTagQuery();
        //Collections.sort(universList)

        [gnInstance: gnInstance,
         screenStep: params?.screenStep,
         plotTagList: tagService.getPlotTagQuery(),
         universList: universList,
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
                        Plot plot = Plot.get((it.key - "plot_status_") as Integer);
                        mainstreamId = it.value as Integer;
                        if (it.value == "1") {
                            lockedPlot.add(plot)
                        } else if (it.value == "2") {
                            bannedPlot.add(plot)
                        } else if (it.value == "3") {
                            selectedPlot.add(plot)
                        }
                    }
                }
                for (Plot plot : lockedPlot) {
                    gnInstance.addPlot(plot);
                }
                for (Plot plot : bannedPlot) {
                    gnInstance.removePlot(plot);
                }
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

    def getBack(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "selectIntrigue";
        gn.characterSet = null;
        gn.nonPlayerCharSet = null;
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
        TagService tagService = new TagService();
        Set<Plot> selectedMainstreamPlotInstanceList = new HashSet<Plot> ();
        Set<Plot> selectedEvenementialPlotInstanceList = new HashSet<Plot>();
        Set<Plot> selectedPlotInstanceList = new HashSet<Plot>();
        Set<Plot> nonTreatedPlots = new HashSet<Plot>();
        Set<Plot> bannedPlotSet = new HashSet<Plot>();
        int evenementialId = 0;
        int mainstreamId = 0;
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = user.getUsername();
        org.gnk.user.User currentUser = org.gnk.user.User.findByUsername(currentUsername);
        for (Plot plot : Plot.list()) {
            if (currentUser == null) {
                continue;
            }
            if (!plot.getIsPublic() && !(currentUser.getPlots().contains(plot))) {
                continue;
            }
            if (plot.getIsDraft())
                continue;
            if (plot.isEvenemential && isEvenementialIsCompatible(plot, gn)) {
                selectedEvenementialPlotInstanceList.add(plot);
                for (Plot gnPlot : gn.selectedPlotSet) {
                    if (plot.name == gnPlot.name) {
                        evenementialId = plot.id;
                        break;
                    }
                }
                continue;
            }
            if (plot.isMainstream) {
                for (Plot gnPlot : gn.selectedPlotSet) {
                    if (plot.name == gnPlot.name) {
                        mainstreamId = plot.id;
                        break;
                    }
                }
            }
            Boolean isselected = false;
            Boolean isbanned = false;
            for (Plot gnPlot : gn.selectedPlotSet) {
                if (plot.name == gnPlot.name) {
                    isselected = true;
                    break;
                }
            }
            for (Plot gnPlot : gn.bannedPlotSet) {
                if (plot.name == gnPlot.name) {
                    isbanned = true;
                    break;
                }
            }
            if (!isselected && !isbanned) {
                if (plot.isMainstream) {
                    selectedMainstreamPlotInstanceList.add(plot);
                }
                nonTreatedPlots.add(plot);
            }

        }
        for (Plot gnPlot : gn.selectedPlotSet) {
            if (gnPlot.name != "Life" && !gnPlot.isEvenemential) {
                Plot plot = Plot.findByNameAndDateCreated(gnPlot.name, gnPlot.dateCreated);
                selectedPlotInstanceList.add(plot);
                nonTreatedPlots.remove(plot);
            }
        }
        for (Plot gnPlot : gn.bannedPlotSet) {
            Plot plot = Plot.findByNameAndDateCreated(gnPlot.name, gnPlot.dateCreated);
            bannedPlotSet.add(plot);
        }
        List<List<String>> statisticResultList = new ArrayList<List<String>>();
        render(view: "/selectIntrigue/selectIntrigue", model:
                [gnInstance: gn,
                 screenStep: '1',
                 plotTagList: tagService.getPlotTagQuery(),
                 universList: tagService.getUniversTagQuery(),
                 plotInstanceList: selectedPlotInstanceList,
                 evenementialPlotInstanceList: selectedEvenementialPlotInstanceList,
                 mainstreamPlotInstanceList: selectedMainstreamPlotInstanceList,
                 bannedPlotInstanceList: bannedPlotSet,
                 nonTreatedPlots: nonTreatedPlots,
                 statisticResultList: statisticResultList,
                 evenementialId: evenementialId,
                 mainstreamId: mainstreamId,
                 conventionList: Convention.list()]);
    }

    public isEvenementialIsCompGatible(Plot plot, Gn gn) {
        /* int countWomen = 0;
         int countMen = 0;
         int countOthers = 0;
         for (Role role in plot.roles) {
             RoleHasTag Man = RoleHasTag.createCriteria().get {
                 like("tag", Tag.createCriteria().get {
                     like("name", "Homme")
                     like("version", 2)
                 }.first())
             }?.first();
             RoleHasTag Woman = RoleHasTag.createCriteria().get {
                 like("tag", Tag.createCriteria().get {
                     like("name", "Femme")
                     like("version", 2)
                 }.first())
             }?.first();
             if (Man && (Man.weight == 101)) {
                 countMen++;
             } else if (Man && Man.weight == -101) {
                 countWomen++;
             } else if (Woman && Woman.weight == 101) {
                 countWomen++;
             } else if (Woman && Woman.weight == -101) {
                 countMen++;
             } else {
                 countOthers++;
             }
         }
         return (countMen + countWomen + countOthers < gn.getNbPlayers());*/
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = user.getUsername();
        org.gnk.user.User currentUser = org.gnk.user.User.findByUsername(currentUsername);
        if (currentUser == null) {
            return false;
        }
        if (!plot.getIsPublic() && !(currentUser.getPlots().contains(plot))) {
            return false;
        }
        int nbTPS_PIP = 0;
        if (plot.getIsDraft())
            return false;

        final Set<Role> roleList = new HashSet<Role>();

        Set<Role> roleSet = plot.getterRoles();
        assert (roleSet != null);
        if (roleSet == null)
            return false;
        for (Role role : roleSet) {
            if (role.isPJ() || role.isPJG())
                roleList.add(role);
            if (role.isTPJ())
                nbTPS_PIP = role.getPipi() + role.getPipr();
        }
        if (roleList.size() > gn.getNbPlayers())
            return false;

        //Resolving PNJsable
        for (Role role : roleSet)
        {
            if (role.isPJB())
            {
                if (roleList.size() < gn.getNbPlayers()) {
                    //        role.setType("PJ")
                    roleList.add(role)
                }
                //   else
                //      role.setType("PNJ")
            }
        }

        for (Role role : roleList) {
            if ((nbTPS_PIP + role.getPipi() + role.getPipr()) > gn.getPipMax()) {
                return false;
            }
        }
        return true;
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
        gnInstance.convention_id = params.convention as Integer

//        if (params.convention) {
//            gnInstance.convention = Convention.findById(params.convention as Integer)
//        }

        formatParams(gnInstance)
        gnInstance.step = 'selectIntrigue';
        gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)
        if (!gnInstance.save(flush: true)) {
            render(view: "selectIntrigue", model: [gnInstance: gnInstance])
            return
        }
        gnHasUser.gn = gnInstance;
        gnHasUser.save(flush: true);
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

        //gnInstance.convention = Convention.findById(params.convention as Integer)
        gnInstance.convention_id = params.convention as Integer

        TagService tagService = new TagService();
        formatParams(gnInstance)
        gnInstance.dtd = new GnXMLWriterService().getGNKDTDString(gnInstance)

        if (!gnInstance.save(flush: true)) {
            render(view: "selectIntrigue", model: [gnInstance: gnInstance, universList: tagService.getUniversTagQuery()])
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

        // We need a default date
        Calendar calendarStartDate = isValidDate("10/10/2010 10:10", "dd/MM/yyyy HH:mm");
        gnInstance.t0Date = calendarStartDate.getTime();

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
        for (GnHasUser gnHasUser : gnInstance.gnHasUsers) {
            GnHasUser.executeUpdate("delete GnHasUser g where g.gn = " + id);
        }
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'gn.label', default: 'GN'), id])
        redirect(action: "list")
    }
}
