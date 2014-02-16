package org.gnk.parser.naming

import org.gnk.gn.Gn
import org.gnk.naming.Firstname
import org.gnk.naming.Name

class SubstitutionNamingXMLReaderService {

    def serviceMethod() {

    }

    /* Exposed Methods */

    def static readSubstitutionNamingNode(Node GNK, Gn gn, Map<Integer, Firstname> firstnameMap, Map<Integer, Name> nameMap) {
        Node NAMING = GNK.GN_DEFINITION.SUBSTITUTION[0].NAMING[0]
        if (NAMING != null) {
            NAMING.CHARACTER.each { character ->
                Integer characterId = character.attribute("character_id") as Integer
                Integer characterNameId
                Integer characterFirstNameId

                character.NAME.each { name ->
                    String status = name.attribute("status") as String
                    if (status.equals("selected"))
                        characterNameId = name.attribute("name_id") as Integer
                }

                character.FIRSTNAME.each { firstname ->
                    String status = firstname.attribute("status") as String
                    if (status.equals("selected"))
                        characterFirstNameId = firstname.attribute("firstname_id") as Integer

                }

                gn.getCharacterSet().each { charact ->
                    if (charact.DTDId.equals(characterId)) {
                        Firstname firstname = firstnameMap.get(characterFirstNameId)
                        Name name = nameMap.get(characterNameId)

                        if ((firstname != null) && (name != null)) {
                            charact.firstname = firstname.name
                            charact.lastname = name.name
                        }
                    }
                }

                gn.getterNonPlayerCharSet().each { charact ->
                    if (charact.DTDId.equals(characterId)) {
                        Firstname firstname = firstnameMap.get(characterFirstNameId)
                        Name name = nameMap.get(characterNameId)

                        if ((firstname != null) && (name != null)) {
                            charact.firstname = firstname.name
                            charact.lastname = name.name
                        }
                    }
                }
            }
        }
    }
    /* !Exposed Methods */
}