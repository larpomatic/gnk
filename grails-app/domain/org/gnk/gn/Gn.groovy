package org.gnk.gn

import org.gnk.naming.Firstname
import org.gnk.naming.Name
import org.gnk.resplacetime.Period
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.Resource
import org.gnk.roletoperso.Role

import java.sql.Timestamp
import java.util.Formatter.DateTime;

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import org.gnk.gn.GnHasUser
import org.gnk.selectintrigue.Plot
import org.gnk.roletoperso.Character
import org.gnk.tag.Tag
import org.gnk.user.User
import org.w3c.dom.Document
import org.w3c.dom.Element

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl

class Gn {

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated

    String name
    Date date
    String dtd

    static transients = [
            "t0Date",
            "step",
            "isMainstream",
            "duration",
            "pipMin",
            "pipMax",
            "pipCore",
            "authors",
            "nbPlayers",
            "nbMen",
            "nbWomen",
            "univers",
            "gnTags",
            "mainstreamTags",
            "evenementialTags",
            "selectedPlotSet",
            "lockedPlotSet",
            "bannedPlotSet",
            "characterSet",
            "nonPlayerCharSet",
            "firstnameSet",
            "lastnameSet",
            "isLife"
    ]

    Date t0Date
    String step // selectIntrigue|role2perso|life|substitution|publication
    boolean isLife

    boolean isMainstream // Or parallelized
    int duration // En heure
    int pipMin
    int pipMax
    int pipCore
    Set<User> authors
    Set<Plot> selectedPlotSet
    Set<Plot> lockedPlotSet
    Set<Plot> bannedPlotSet
    Set<Character> characterSet
    Set<Character> nonPlayerCharSet
    Set<Character> staffCharSet
    int nbPlayers
    int nbMen
    int nbWomen
    Tag univers
    HashMap<Tag, Integer> gnTags
    HashMap<Tag, Integer> mainstreamTags
    HashMap<Tag, Integer> evenementialTags

    Set<Firstname> firstnameSet
    Set<Name> lastnameSet

    Set<Resource> resourceSet
    Set<Place> placeSet

    static hasMany = [ gnHasUsers: GnHasUser, periods: Period ]

    static hasOne = [ gnHasConvention : GnHasConvention ]

    static constraints = {
        name maxSize: 45
        date nullable: true
        dtd nullable: true
    }

    static mapping = {
        dtd type: 'text'
        id type:'integer'
        version type: 'integer'
        periods sort:'beginning'
    }

    public boolean addPlot(Plot plot) {
        if ((plot.isEvenemential) && (this.selectedPlotSet != null))
            this.selectedPlotSet.removeAll { it.isEvenemential == true };
//        if (this.selectedPlotSet == null)
//            this.selectedPlotSet = new HashSet<Plot>();
        selectedPlotSet.add(plot);
    }

    public boolean removePlot(Plot plot) {
        Plot plotToRemove = null;
        for (Plot gnPlot : selectedPlotSet) {
            if (gnPlot.name == plot.name) {
                plotToRemove = gnPlot;
            }
        }
        if (plotToRemove) {
            selectedPlotSet.remove(plotToRemove);
        }
    }

    public boolean hasTag(Tag parTag, String tagListName = "BaseTags") {
        if (tagListName == "EvenementialTags") {
            if (!evenementialTags)
                return false
            for (Tag plotTag : evenementialTags.keySet()) {
                if (plotTag == parTag) {
                    return true;
                }
            }
            return false;
        } else if (tagListName == "MainstreamTags") {
            if (!mainstreamTags)
                return false
            for (Tag plotTag : mainstreamTags.keySet()) {
                if (plotTag == parTag) {
                    return true;
                }
            }
            return false;
        } else {
            if (!gnTags)
                return false
            for (Tag tagK : gnTags.keySet()) {
                if (tagK == parTag) {
                    return true;
                }
            }
            return false;
        }
    }

    public int getTagWeight(Tag parTag, String tagListName = "BaseTags") {
        if (tagListName == "EvenementialTags") {
            if (!evenementialTags)
                return 50;
            for (Tag plotTag : evenementialTags.keySet()) {
                if (plotTag == parTag) {
                    return evenementialTags.get(plotTag);
                }
            }
            return 50;
        } else if (tagListName == "MainstreamTags") {
            if (!mainstreamTags)
                return 50;
            for (Tag plotTag : mainstreamTags.keySet()) {
                if (plotTag == parTag) {
                    return mainstreamTags.get(plotTag);
                }
            }
            return 50;
        } else {
            if (!gnTags)
                return 50;
            for (Tag tagK : gnTags.keySet()) {
                if (tagK == parTag) {
                    return gnTags.get(tagK);
                }
            }
            return 50;
        }
    }

    public boolean hasMainstreamTag(Tag parTag) {
        if (!mainstreamTags)
            return false
        for (Tag plotTag : mainstreamTags.keySet()) {
            if (plotTag == parTag) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEvenementialTag(Tag parTag) {
        if (!evenementialTags)
            return false
        for (Tag plotTag : evenementialTags.keySet()) {
            if (plotTag == parTag) {
                return true;
            }
        }
        return false;
    }

    public Set<Character> getterCharacterSet() {
        return characterSet;
    }

    public Set<Character> getterNonPlayerCharSet() {
        if (nonPlayerCharSet == null)
            nonPlayerCharSet = new HashSet<Character>();
        return nonPlayerCharSet;
    }

    public Character getCharacterContainingRole(Role role) {
        for (Character c : getCharacterSet()) {
            if (c.getSelectedRoles().contains(role))
                return c;
        }
        return null;
    }

    public Character getAllCharacterContainingRole(Role role) {
        for (Character c : getCharacterSet()) {
            if (c.getSelectedRoles().contains(role))
                return c;
        }
        for (Character c : getNonPlayerCharSet()) {
            if (c.getSelectedRoles().contains(role))
                return c;
        }
        return null;
    }

    public Set<Character> getStaffCharSet() {
        if (this.staffCharSet == null)
            this.staffCharSet = new HashSet<Character>();
        return staffCharSet
    }


    void setStaffCharSet(Set<Character> staffCharSet) {
        this.staffCharSet = staffCharSet
    }
}
