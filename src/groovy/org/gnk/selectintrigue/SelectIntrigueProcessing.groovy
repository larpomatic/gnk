package org.gnk.selectintrigue

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

//import org.gnk.user.User

import java.util.*;
import java.util.Map.Entry;

import org.gnk.gn.Gn;
import org.gnk.roletoperso.Role;
import org.gnk.roletoperso.Character;
import org.gnk.roletoperso.RoleHasTag;
import org.gnk.tag.Tag;
import org.gnk.tag.TagService;

public class SelectIntrigueProcessing {

    private Gn _gn;
    private Set<Plot> _selectedMainstreamPlotList;
    private Set<Plot> _selectedEvenementialPlotList;
    private Set<Plot> _selectedPlotList;
    private Set<Plot> _lockedPlotList;
    private Set<Plot> _bannedPlotList;
    private Set<Plot> _allPlotList;
    private Set<Plot> _allEvenementialPlotList;
    private Set<Plot> _allMainstreamPlotList;
    private Map<Tag, Integer> _value;
    private Set<Tag> _bannedTagList;
    private Set<Tag> _bannedEvenementialTagList;
    private Set<Tag> _bannedMainstreamTagList;

    private Integer _minPip;
    private Integer _maxPip;
    private Integer _currentPip;

    public SelectIntrigueProcessing(Gn parGn, List<Plot> parAllPlotList, Set<Plot> bannedList, Set<Plot> lockedPlot) {
        _gn = parGn;
        _selectedPlotList = new HashSet<Plot>();
        _selectedEvenementialPlotList = new HashSet<Plot>();
        _selectedMainstreamPlotList = new HashSet<Plot>();
        _lockedPlotList = lockedPlot;
        _bannedPlotList = bannedList;
        _allPlotList = new HashSet<Plot>();
        _allEvenementialPlotList = new HashSet<Plot>();
        _allMainstreamPlotList = new HashSet<Plot>();
        _value = new HashMap<Tag, Integer>(parGn.getGnTags().size());
        for (Tag tag : parGn.getGnTags().keySet()) {
            _value.put(tag, 0);
        }
        setBannedTagList();
        // on ajoute les intrigues compatibles à leur listes respectives
        for (Plot plot : parAllPlotList) {
            if (plot.getIsEvenemential()) {
                //si l'intrigue évenementielle n'a pas plus de joueurs que le gn alors on l'ajoute
                if (plotIsCompatible(plot)) {
                    _allEvenementialPlotList.add(plot);
                }
            } else {
                if (plotIsCompatible(plot)) {
                    _allPlotList.add(plot);
                    if (_gn.getIsMainstream() && plot.getIsMainstream()) {
                        _allMainstreamPlotList.add(plot); //on ajoute l'intrigue à la liste des mainstreams
                        _allPlotList.remove(plot); //on la retire des évenementielles
                    }
                }
            }
        }

        _maxPip = _gn.getNbPlayers() * _gn.getPipMax();
        int realMinPip = _gn.getNbPlayers() * _gn.getPipMin();
        _minPip = ((_maxPip - realMinPip) / 2) + realMinPip;//Heuristique pour que R2P soit plus facile derrière.

        _currentPip = 0;

        _allPlotList.removeAll(_bannedPlotList);
        _allPlotList.removeAll(_lockedPlotList);
        _selectedPlotList.addAll(_lockedPlotList);
        if (_gn.getIsMainstream()) {
            selectEvenementailAndMainstreamPlots(_allMainstreamPlotList, _gn.getMainstreamTags(), _selectedMainstreamPlotList);
        }
        selectEvenementailAndMainstreamPlots(_allEvenementialPlotList, _gn.getEvenementialTags(), _selectedEvenementialPlotList);
        selectIntrigue();
    }

    public isEvenementialIsCompatible(Plot plot) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = user.getUsername();
        org.gnk.user.User currentUser = org.gnk.user.User.findByUsername(currentUsername);
        if (currentUser == null) {
            return false;
        }
        if (!plot.getIsPublic() && !(currentUser.getPlots().contains(plot))) {
            return false;
        }
        if (plot.getIsDraft())
            return false;
        int countWomen = 0;
        int countMen = 0;
        int countOthers = 0;
        //This function is counting role and not PJ; it need modifications
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
        return (countMen + countWomen + countOthers < _gn.getNbPlayers());
    }

    public Set<Plot> getSelectedPlots() {
        return _selectedPlotList;
    }

    public Set<Plot> getSelectedEvenementialPlotList() {
        return _selectedEvenementialPlotList;
    }

    public Set<Plot> getSelectedMainstreamPlotList() {
        return _selectedMainstreamPlotList;
    }

    public int getPipByPlayer() {
        return _gn.getNbPlayers() > 0 ? _currentPip / _gn.getNbPlayers() : 0;
    }

    public Map<Tag, Integer> getTagsResult() {
        evaluateGn(true);
        return _value;
    }

    private void selectIntrigue() {
        while (executeRound()) {
        }
    }

    private int evaluateGn(boolean flush) {
        int result = 0;
        _currentPip = 0;
        int nbRolePipCoreok = 0;
        if (flush) {
            for (Entry<Tag, Integer> entry : _value.entrySet()) {
                entry.setValue(0);
            }
            for (Plot plot : _selectedPlotList) {
                _currentPip += plot.getSumPipRoles(_gn.nbPlayers);
                for (Entry<Tag, Integer> entry : _value.entrySet()) {
                    if (plot.hasPlotTag(entry.getKey())) {
                        entry.setValue(entry.getValue() + plot.getSumPipRoles(_gn.nbPlayers) * plot.getTagWeight(entry.getKey()));
                    }
                }
                nbRolePipCoreok += plot.getNbRoleOverPipcore(_gn.pipCore);
            }
        }
        for (Entry<Tag, Integer> entry : _value.entrySet()) {
            if (_currentPip > 0) {
                entry.setValue((entry.getValue() / _currentPip)); // On remet
                // sous
                // forme de
                // %age
            }
            int inter = Math.abs(_gn.getGnTags().get(entry.getKey()) - entry.getValue());
            result -= inter * inter;
        }

        Tag gnUnivers = _gn.getUnivers();
        for (Plot plot : _selectedPlotList) {

        }

        //Nombre de role ayant au moins Pipcore pipes qu'il reste à trouver
        int bonusMalusPipcore = _gn.nbPlayers - nbRolePipCoreok;
        if (bonusMalusPipcore < 0)
            bonusMalusPipcore = 0;

        // on récupère un pourcentage, plus le pourcentage est faible, plus l'impact sur result est grand
        int remainPipPercent = (((_gn.getPipMin() * _gn.getNbPlayers()) - _currentPip) * 100)/(_gn.getPipMin() * _gn.getNbPlayers());
        if (remainPipPercent < 0) {
            remainPipPercent = 0;
        }
        if (result != 0) {
            result -= (bonusMalusPipcore * result * 10 *  (100 - remainPipPercent)) / result;
        }
        return result;
    }

    private int evaluateGnWithPlot(Plot plot) {
        if (_selectedPlotList.contains(plot)) {
            return evaluateGn(false);
        }
        _selectedPlotList.add(plot);
        int result = evaluateGn(true);
        _selectedPlotList.remove(plot);
        return result;
    }

    private void setBannedTagList() {
        // on ajoute les tags dans la liste des bannis
        _bannedTagList = new HashSet<Tag>();
        _bannedEvenementialTagList = new HashSet<Tag>();
        for (Entry<Tag, Integer> plotTagEntry : _gn.getGnTags().entrySet()) {
            if (plotTagEntry.getValue() <= TagService.BANNED) {
                _bannedTagList.add(plotTagEntry.getKey());
            }
        }
        for (Entry<Tag, Integer> plotTagEntry : _gn.getEvenementialTags().entrySet()) {
            if (plotTagEntry.getValue() < TagService.BANNED) {
                _bannedEvenementialTagList.add(plotTagEntry.getKey());
            }
        }
        for (Entry<Tag, Integer> plotTagEntry : _gn.getMainstreamTags().entrySet()) {
            if (plotTagEntry.getValue() < TagService.BANNED) {
                _bannedMainstreamTagList.add(plotTagEntry.getKey());
            }
        }
    }

    private boolean plotIsCompatible(Plot plot) {
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
        if (plot.getIsEvenemential()) {
            for (Tag bannedEvenementialPlotTag : _bannedEvenementialTagList) {
                if (plot.hasPlotTag(bannedEvenementialPlotTag)) {
                    return false;
                }
            }
        } else if (plot.getIsMainstream()) {
            for (Tag bannedMainstreamPlotTag : _bannedMainstreamTagList) {
                if (plot.hasPlotTag(bannedMainstreamPlotTag)) {
                    return false;
                }
            }
        } else {
            for (Tag bannedPlotTag : _bannedTagList) {
                if (plot.hasPlotTag(bannedPlotTag)) {
                    return false;
                }
            }
            for (Plot selectedPlot : _allPlotList) {
                if (plot.getVariant() == selectedPlot.getId()
                || ((plot.getVariant() != null) && (plot.getVariant() == selectedPlot.getVariant()))
                || plot.getId() == selectedPlot.getVariant())
                    return false;
            }
            for (Plot lockedPlot : _lockedPlotList) {
                if (plot.getVariant() == lockedPlot.getId()
                        || ((plot.getVariant() != null) && (plot.getVariant() == lockedPlot.getVariant()))
                        || plot.getId() == lockedPlot.getVariant())
                    return false;
            }
        }


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
        if (roleList.size() > _gn.getNbPlayers())
            return false;

        //Resolving PNJsable
        for (Role role : roleSet)
        {
            if (role.isPJB())
            {
                if (roleList.size() < _gn.getNbPlayers()) {
                    roleList.add(role)
                }
            }
        }

        for (Role role : roleList) {
            if ((nbTPS_PIP + role.getPipi() + role.getPipr()) > _gn.getPipMax()) {
                return false;
            }
        }
        if (!sexFilter(plot))
            return false;

        return true;
    }

    private boolean sexFilter(Plot plot) {
        return ((plot.getNbMinMen() <= _gn.getNbPlayers() - _gn.getNbWomen()) && (plot.getNbMinWomen() <= _gn.getNbPlayers() - _gn.getNbMen()));
    }

    private void selectPlot(Plot plot) {
        _selectedPlotList.add(plot);
        _allPlotList.remove(plot);
    }

    private boolean selectEvenementailAndMainstreamPlots(Set<Plot> typePlotList,
                                                         HashMap<Tag, Integer> gnTypeTags,
                                                         Set<Plot> resultTypeList) {
        if (_allPlotList.size() == 0) {
            return false;
        }
        ArrayList<Plot> typePlots = new ArrayList<Plot>();
        HashMap<Integer, Integer> rankMap = new HashMap<Integer, Integer>();
        TagService tagService = new TagService();
        Map<Tag, Integer> challengerTagList = new HashMap<Tag, Integer>();
        for (Plot plot : typePlotList) {
            typePlots.add(plot);
            Set<PlotHasTag> plotHasTags = plot.getExtTags();
            if (plotHasTags != null) {
                for (PlotHasTag plotHasTag : plotHasTags) {
                    challengerTagList.put(plotHasTag.getTag(), plotHasTag.getWeight());
                }
            }
            int rankTag = tagService.getTagsDifferenceToObjective(gnTypeTags, challengerTagList);
            rankMap.put(plot.getId(), rankTag);
        }
        if (typePlots.size() > 1 && rankMap.size() > 1) {
            Collections.sort(typePlots, new customPlotComparator(rankMap));
        }
        resultTypeList.addAll(typePlots);
        return true;
    }

    private class customPlotComparator implements Comparator<Plot> {
        private HashMap<Integer, Integer> _rankMap = new HashMap<Integer, Integer>();

        public customPlotComparator(HashMap<Integer, Integer> rankMap) {
            _rankMap = rankMap;
        }

        public int compare(Plot plot1, Plot plot2) {
            Integer rank1 = _rankMap.get(plot1.getId());
            Integer rank2 = _rankMap.get(plot2.getId());
            return Integer.compare(rank1, rank2);
        }
    }

    private boolean executeRound() {
        Plot selectedPlot = null;
        Integer newValue = null;
        if (_allPlotList.isEmpty()) {
            return false;
        }
        evaluateGn(true);
        for (Plot plot : _allPlotList) {
            if ((plot.getSumPipRoles(_gn.nbPlayers) <= (_maxPip - _currentPip))) {
                Integer tmpValue = evaluateGnWithPlot(plot);
                if (newValue == null || tmpValue > newValue) {
                    selectedPlot = plot;
                    newValue = tmpValue;
                }
            }
            evaluateGn(true);
        }
        if (selectedPlot == null) {
            return false;
        }
        selectPlot(selectedPlot);
        return (_currentPip < _minPip);
    }
}