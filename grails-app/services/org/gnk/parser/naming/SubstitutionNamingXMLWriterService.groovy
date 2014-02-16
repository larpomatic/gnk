package org.gnk.parser.naming

import org.gnk.naming.FirstnameHasTag
import org.gnk.naming.NameHasTag
import org.gnk.roletoperso.Character
import org.gnk.naming.Name
import org.gnk.naming.Firstname
import org.w3c.dom.Document
import org.w3c.dom.Element

class SubstitutionNamingXMLWriterService {
    /* Exposed Methods */
    public Element getNamingElement(Document doc, Set<Character> characterSet, Set<Character> nonPlayerCharSet) {
        Element namingE = doc.createElement("NAMING")
        namingE.setAttribute("step_id", "3")

        for (Character character : characterSet) {
            namingE.appendChild(getNamingCharacter(doc, character))
        }

        for (Character character : nonPlayerCharSet) {
            namingE.appendChild(getNamingCharacter(doc, character))
        }

        return namingE
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Element getNamingCharacter(Document doc, Character character) {
        Element characterE = doc.createElement("CHARACTER")
        characterE.setAttribute("character_id", character.DTDId.toString())

        if (character.selectedFirstname != null) {
            characterE.appendChild(getFirstname(doc, character.selectedFirstname, "selected"))
        }
        for(Firstname firstname : character.proposedFirstnames) {
            characterE.appendChild(getFirstname(doc, firstname, "proposed"))
        }
        for(Firstname firstname : character.bannedFirstnames) {
            characterE.appendChild(getFirstname(doc, firstname, "banned"))
        }

        if (character.selectedLastname != null) {
            characterE.appendChild(getLastname(doc, character.selectedLastname, "selected"))
        }
        for(Name lastname : character.proposedLastnames) {
            characterE.appendChild(getLastname(doc, lastname, "proposed"))
        }
        for(Name lastname : character.bannedLastnames) {
            characterE.appendChild(getLastname(doc, lastname, "banned"))
        }

        return characterE
    }

    private Element getFirstname(Document doc, Firstname firstname, String status) {
        Element firstnameE = doc.createElement("FIRSTNAME")
        firstnameE.setAttribute("firstname_id", firstname.DTDId as String)
        firstnameE.setAttribute("status", status)
        return firstnameE
    }

    private Element getLastname(Document doc, Name lastname, String status) {
        Element lastnameE = doc.createElement("NAME")
        lastnameE.setAttribute("name_id", lastname.DTDId as String)
        lastnameE.setAttribute("status", status)
        return lastnameE
    }
    /* !Construction Methods */

    /* Exposed Methods */
    public Element getFirstnamesDataElement(Document doc, Set<Firstname> firstnameSet) {
        Element firstnamesE = doc.createElement("FIRSTNAMES")
        if (firstnameSet != null && !firstnameSet.isEmpty()) {
            firstnamesE.setAttribute("step_id", "3")
            for (Firstname firstname : firstnameSet) {
                Element firstnameE = doc.createElement("FIRSTNAME")
                firstnameE.setAttribute("id", firstname.DTDId as String)
                firstnameE.setAttribute("value", firstname.name)
                firstnameE.setAttribute("gender", firstname.gender)

                Element tagsE = doc.createElement("TAGS")
                for(FirstnameHasTag firstnameHasTag : firstname.extTags) {
                    Element tagE = doc.createElement("TAG")
                    tagE.setAttribute("value", firstnameHasTag.tag.name)
                    tagE.setAttribute("type", firstnameHasTag.tag.tagFamily.value)
                    tagE.setAttribute("weight", firstnameHasTag.weight as String)

                    tagsE.appendChild(tagE)
                }
                firstnameE.appendChild(tagsE)

                firstnamesE.appendChild(firstnameE)
            }
        }
        return firstnamesE
    }

    public Element getLastnamesDataElement(Document doc, Set<Name> lastnameSet) {
        Element lastnamesE = doc.createElement("NAMES")
        if (lastnameSet != null && !lastnameSet.isEmpty()) {
            lastnamesE.setAttribute("step_id", "3")
            for (Name lastname : lastnameSet) {
                Element lastnameE = doc.createElement("NAME")
                lastnameE.setAttribute("id", lastname.DTDId as String)
                lastnameE.setAttribute("value", lastname.name)
                lastnameE.setAttribute("gender", lastname.gender)

                Element tagsE = doc.createElement("TAGS")
                for(NameHasTag lastnameHasTag : lastname.extTags) {
                    Element tagE = doc.createElement("TAG")
                    tagE.setAttribute("value", lastnameHasTag.tag.name)
                    tagE.setAttribute("type", lastnameHasTag.tag.tagFamily.value)
                    tagE.setAttribute("weight", lastnameHasTag.weight  as String)

                    tagsE.appendChild(tagE)
                }
                lastnameE.appendChild(tagsE)

                lastnamesE.appendChild(lastnameE)
            }
        }
        return lastnamesE
    }
    /* !Exposed Methods */
}
