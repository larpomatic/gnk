package org.gnk.gn

import org.gnk.naming.Firstname
import org.gnk.naming.Name
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
import org.gnk.tag.Univers
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
        "lastnameSet"
	]

	Date t0Date
	String step // selectIntrigue|role2perso|naming|ressplacetime|casting
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
	int nbPlayers
	int nbMen
	int nbWomen
	Univers univers
	HashMap<Tag, Integer> gnTags
	HashMap<Tag, Integer> mainstreamTags
	HashMap<Tag, Integer> evenementialTags

    Set<Firstname> firstnameSet
    Set<Name> lastnameSet

    Set<Resource> resourceSet
    Set<Place> placeSet

	static hasMany = [ gnHasUsers: GnHasUser ]

	static constraints = {
		name maxSize: 45
		date nullable: true
		dtd nullable: true
	}

	static mapping = {
		dtd type: 'text'
		id type:'integer'
		version type: 'integer'
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
}
