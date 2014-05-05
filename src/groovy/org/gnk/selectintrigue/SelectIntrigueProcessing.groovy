package org.gnk.selectintrigue

//import org.springframework.security.core.userdetails.User

import org.gnk.user.User;

import java.util.*;
import java.util.Map.Entry;

import org.gnk.gn.Gn;
import org.gnk.roletoperso.Role;
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

//    def springSecurityService;

    // FIXME Handle Mainstream

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
                _allEvenementialPlotList.add(plot);
            } else {
                if (plotIsCompatible(plot)) {
                    _allPlotList.add(plot);
                    if (_gn.getIsMainstream() && plot.getIsMainstream()) {
                        _allMainstreamPlotList.add(plot);
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
        // FIXME
//        HttpSession session = request.getSession();
//        String userName = (String) session.getAttribute("USER_NAME");

//        User currentUser = springSecurityService.getCurrentUser();
//        if (!currentUser) {
//            return false;
//        }
//        if (!plot.getIsPublic() && !(currentUser.getPlots().contains(plot))) {
//            return false;
//        }
        int nbTPS_PIP = 0;
        if (plot.getIsDraft())
            return false;
        if (!(plot.hasUnivers(_gn.getUnivers())) && !(plot.isUniversGeneric())) {
            return false;
        }
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
        }
        if (!sexFilter(plot))
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
        if (roleList.size() > _gn.getNbPlayers())
            return false;
        Boolean isPipCoreOk = false;
        for (Role role : roleList) {
            if ((nbTPS_PIP + role.getPipi() + role.getPipr()) > _gn.getPipMax()) {
                return false;
            }
            if (role.getPipi() >= _gn.getPipCore()) {
                isPipCoreOk = true;
            }
        }
        if (!isPipCoreOk) {
            return false;
        }
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