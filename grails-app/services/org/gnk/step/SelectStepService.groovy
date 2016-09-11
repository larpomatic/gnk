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
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        switch (gn.step)
        {
            case "publication" :
                getBackPublication(gn)
                if (step == "ressource" || step == "place" || step == "naming")
                    getBackTime(gn)
                if (step == "ressource" || step == "naming")
                    getBackPlace(gn)
                if (step == "naming")
                    getBackRessource(gn)
                if (step == "life" || step == "selectIntrigue" || step == "role2perso")
                    getBackSubstitution(gn)
                if (step == "role2perso" || step == "selectIntrigue")
                    getBackLife(gn)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn)
                break;
            case "time" :
                getBackTime(gn)
                if (step == "ressource" || step == "naming")
                    getBackPlace(gn)
                if (step == "naming")
                    getBackRessource(gn)
                if (step == "life" || step == "selectIntrigue" || step == "role2perso")
                    getBackSubstitution(gn)
                if (step == "selectIntrigue" || step == "role2perso")
                    getBackLife(gn)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn)
                break;
            case "place" :
                getBackPlace(gn)
                if (step == "naming")
                    getBackRessource(gn)
                if (step == "life" || step == "selectIntrigue" || step == "role2perso")
                    getBackSubstitution(gn)
                if (step == "selectIntrigue" || step == "role2perso")
                    getBackLife(gn)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn)
                break;
            case "ressource" :
                getBackRessource(gn)
                if (step == "selectIntrigue" || step == "role2perso" || step == "life")
                    getBackSubstitution(gn)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn)
                break;
            case "naming" :
                getBackNaming(gn)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn)
                break;
            case "life" :
                getBackLife(gn)
                if (step == "selectIntrigue")
                    getBackSelectIntrigue(gn)
                break;
            case "role2perso" :
                getBackSelectIntrigue(gn)
                break;
            default:
                break;
        }
    }

    /**
     * delete role2perso step elements in the xml to allow user to return to SelectIntrigue step
     * @param id : get the gn concerned
     */
    def getBackSelectIntrigue(Gn gn) {
        //Gn gn = Gn.get(id);
        //final gnData = new GNKDataContainerService();
        //gnData.ReadDTD(gn);
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
    }

    /**
     * delete Life step elements in the xml to allow user to return to SelectIntrigue step
     * @param id : get the gn concerned
     */
    def getBackLife(Gn gn) {
        /*Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);*/
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "role2perso";
        gn.isLife = false
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
    }

    /**
     * delete publication step elements in the xml to allow user to return to SelectIntrigue step
     * @param id : get the gn concerned
     */
    def getBackPublication(Gn gn) {
        /*Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);*/

        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);

        // trouver un moyen de supprimer les places, les ressources et les names
        gn.dtd = gn.dtd.replace("<STEPS last_step_id=\"publication\">", "<STEPS last_step_id=\"substitution\">");
        gn.removeCharArray()
        gn.step = "time"
        gn.save(flush: true);
    }

    /**
     * delete Substitution step elements in the xml to allow user to return to SelectIntrigue step
     * @param id : get the gn concerned
     */

    def getBackNaming(Gn gn) {
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()

        if (gn.isLife)
            gn.step = "life";
        else
            gn.step = "role2perso";

        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
    }

    def getBackRessource(Gn gn) {
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()

        gn.step = "naming"
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.removeCharArray();
        gn.save(flush: true);
    }


    def getBackPlace(Gn gn) {
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()

        gn.step = "ressource"
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.removeCharArray();
        gn.save(flush: true);
    }

    def getBackTime(Gn gn) {
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()

        gn.step = "place"
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.removeCharArray();
        gn.save(flush: true);
    }

    def getBackSubstitution(Gn gn) {
        /*Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);*/
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "life"
        gn.removeAllCharArray()

        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
    }

    /**
     * Check if the gn and the plot are compatible and so the plot can be associated to the gn
     * @param plot : the plot chosen for the test
     * @param gn : the gn chosen for the test
     * @return result of the test : true if it's compatible, if not false
     */
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

        for (Role role : roleSet)
        {
            if (role.isPJB())
            {
                if (roleList.size() < gn.getNbPlayers()) {
                    roleList.add(role)
                }
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
