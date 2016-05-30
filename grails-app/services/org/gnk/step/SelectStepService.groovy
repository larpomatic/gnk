package org.gnk.step

import org.gnk.genericevent.LifeController
import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.publication.PublicationController
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleToPersoController
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.SelectIntrigueController
import org.gnk.substitution.SubstitutionController
import org.gnk.tag.TagService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

class SelectStepService {

    /**
     * Execute method getBack to return to the selected step
     * @param id : get the gn concerned
     * @param step : the step chosen by the user
     */
    def chooseStep(Gn gn, String step) {
        switch (gn.step)
        {
            case "publication" :
                getBackPublication(gn.id)
                if (step == "life" || step == "selectIntrigue" || step == "role2perso")
                    getBackSubstitution(gn.id)
                if (step == "role2perso" || step == "selectIntrigue")
                    getBackLife(gn.id)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn.id)
                break;
            case "substitution" :
                getBackSubstitution(gn.id)
                if (step == "selectIntrigue" || step == "role2perso")
                    getBackLife(gn.id)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn.id)
                break;
            case "life" :
                getBackLife(gn.id)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn.id)
                break;
            case "role2perso" :
                getBackSelectIntrigue(gn.id)
                break;
            default:
                break;
        }
    }

    def getBackSelectIntrigue(Long id) {
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
        Set<Plot> selectedMainstreamPlotInstanceList = new HashSet<Plot>();
        Set<Plot> selectedEvenementialPlotInstanceList = new HashSet<Plot>();
        Set<Plot> selectedPlotInstanceList = new HashSet<Plot>();
        Set<Plot> nonTreatedPlots = new HashSet<Plot>();
        Set<Plot> bannedPlotSet = new HashSet<Plot>();
        int evenementialId = 0;
        int mainstreamId = 0;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        /*render(view: "/selectIntrigue/selectIntrigue", model:
                [gnInstance                  : gn,
                 screenStep                  : '1',
                 plotTagList                 : tagService.getPlotTagQuery(),
                 universList                 : tagService.getUniversTagQuery(),
                 plotInstanceList            : selectedPlotInstanceList,
                 evenementialPlotInstanceList: selectedEvenementialPlotInstanceList,
                 mainstreamPlotInstanceList  : selectedMainstreamPlotInstanceList,
                 bannedPlotInstanceList      : bannedPlotSet,
                 nonTreatedPlots             : nonTreatedPlots,
                 statisticResultList         : statisticResultList,
                 evenementialId              : evenementialId,
                 mainstreamId                : mainstreamId,
                 conventionList              : Convention.list()]);*/
    }

    def getBackLife(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "role2perso";
        gn.isLife = false
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
        Integer evenementialId = 0;
        Integer mainstreamId = 0;

//        removeLife(gn)

        for (Plot plot in gn.selectedPlotSet) {
            if (plot.isEvenemential) {
                evenementialId = Plot.findByName(plot.name).id;
            } else if (plot.isMainstream && gn.isMainstream) {
                mainstreamId = Plot.findByName(plot.name).id; ;
            }
        }
        /*redirect(controller: 'roleToPerso', action: 'roleToPerso', params: [gnId                : id as String,
                                                                            selectedMainstream  : mainstreamId as String,
                                                                            selectedEvenemential: evenementialId as String]);*/
    }

    def getBackPublication(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        //gn.step = "substitution";

        /* for (Plot p : gn.getSelectedPlotSet())
         {
             for (GenericPlace gnplace : p.getGenericPlaces())
                 gnplace.setResultList(new ArrayList<ReferentialPlace>());
         }
         gn.setPlaceSet(new HashSet<Place>());*/

        //gn.dtd = gn.dtd.replace("<STEPS last_step_id=\"publication\">", "<STEPS last_step_id=\"substitution\">");
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);

        // trouver un moyen de supprimer les places, les ressources et les names
        gn.dtd = gn.dtd.replace("<STEPS last_step_id=\"publication\">", "<STEPS last_step_id=\"substitution\">");
        gn.save(flush: true);
        List<String> sexes = new ArrayList<>();
        for (Character character in gn.characterSet) {
            sexes.add("sexe_" + character.getDTDId() as String);
        }
        for (Character character in gn.nonPlayerCharSet) {
            sexes.add("sexe_" + character.getDTDId() as String);
        }
        //redirect(controller: 'substitution', action: 'index', params: [gnId: id as String, sexe: sexes]);
    }

    def getBackSubstitution(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()

        //if (gn.isLife)
            gn.step = "life";
        //else
        //    gn.step = "role2perso";

        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
        Integer evenementialId = 0;
        Integer mainstreamId = 0;
        for (Plot plot in gn.selectedPlotSet) {
            if (plot.isEvenemential) {
                evenementialId = Plot.findByName(plot.name).id;
            } else if (plot.isMainstream && gn.isMainstream) {
                mainstreamId = Plot.findByName(plot.name).id; ;
            }
        }
    }

    public isEvenementialIsCompatible(Plot plot, gn) {
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
}
