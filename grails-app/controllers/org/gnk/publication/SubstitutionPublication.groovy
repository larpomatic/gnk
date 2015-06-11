package org.gnk.publication
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Place
import org.gnk.roletoperso.Role
/**
 * Created with IntelliJ IDEA.
 * User: aurel_000
 * Date: 01/11/13
 * Time: 19:33
 * This class is used to replace <I:AL> by Aurélien Legrand or <O:RO> by Rose or <L:CH> by Château
 * Pour l'utiliser : créer une instance de la classe avec la liste des rôles pour un plot donné + le nom des personnages associés, la liste des lieux du plot
 * et la liste des ressources génériques puis appeler la méthode replaceAll
 */
class SubstitutionPublication {

    private HashMap<String, Role> rolesNames
    private placeList
    private List<GenericResource> genericResourceList

    def SubstitutionPublication(HashMap<String, Role> rolesNames, List<Place> placeList, List<GenericResource> genericResourceList)
    {
        this.rolesNames = rolesNames
        this.placeList = placeList
        this.genericResourceList = genericResourceList
        println("===================== Place ======================")
        for (Place place : placeList)
        {
            if (place.genericPlace == null)
                continue
            println(place.genericPlace.code)
        }
        println("===================== Role =====================")
        for (Map.Entry<String, Role> map : rolesNames.entrySet())
        {
            println(map.key)
        }
    }

    def String replaceAll(String input) {
        if (null == input)
            return null

        if (false == input.contains("<") || false == input.contains(">"))
            return input

        while (input.contains("<") && input.contains(">")) {
            int begin = input.indexOf('<')
            int end = input.indexOf('>')

            if (begin - end > 0) //Error
                return null

            String tag = input.substring(begin, end + 1);
            //String tag = input.substring(input.indexOf('<'), input.indexOf('>') + 1)
            String tmp = tag.substring(1, tag.length() - 1).toUpperCase();
            String[] split = tmp.split(":")
            String replacement = ""

            if (split[0].equals("L")) {
                if (split.length == 2) //old tag, for example <i:Hote>
                    replacement = oldReplacePlace(split[1])
                else //enhanced syntax
                    replacement = replacePlace(split[1], split[2])
            } else if (split[0].equals("I")) {
                if (split.length == 2) //old tag, for example <i:Hote>
                    replacement = oldReplaceRole(split[1])
                else //enhanced syntax
                    replacement = replaceRole(split[1], split[2])
            } else if (split[0].equals("O")) {
                if (split.length == 2) //old tag, for example <i:Hote>
                    replacement = oldReplaceResource(split[1])
                else //enhanced syntax
                    replacement = replaceResource(split[1], split[2])
            }

            input = input.replace(tag, replacement);
        }

        return input
    }

    String replaceResource(String syntax, String code) {
        String replacement = "[Ressource générique]"
        String gender = ""
        for (GenericResource genericResource : genericResourceList)
        {
            if (genericResource.code.toUpperCase().equals(code)) {
                replacement = genericResource.selectedResource.name
                gender = genericResource.selectedResource.gender
            }
        }

        //if (gender.isEmpty())
        //return replacement

        if (syntax.contains("M#") && syntax.contains("F#")) {
            String[] switchGender = syntax.split(";")
            if (gender.equals("M"))
                return switchGender[0].substring(2).toLowerCase()
            else //gender = F
                return switchGender[1].substring(2).toLowerCase()
        }

        boolean startsWithVowel = replacement.matches("^[AEIOUY].*")

        switch (syntax) {
            case "NONE" : return replacement
            case "ART" :
                switch (gender) {
                    case "" : return (startsWithVowel) ? "l'" + replacement : "le " + replacement
                    case "M" : return (startsWithVowel) ? "l'" + replacement : "le " + replacement
                    case "F" : return (startsWithVowel) ? "l'" + replacement : "la " + replacement
                    case "MP" : return "les " + replacement
                    case "FP" : return "les " + replacement
                }
                break
            case "POS" :
                switch (gender) {
                    case "" : return "mon " + replacement
                    case "M" : return "mon " + replacement
                    case "F" : return "ma " + replacement
                    case "MP" : return "mes " + replacement
                    case "FP" : return "mes " + replacement
                }
                break
            case "NOM" :
                switch (gender) {
                    case "" : return "un " + replacement
                    case "M" : return "un " + replacement
                    case "F" : return "une " + replacement
                    case "MP" : return "des " + replacement
                    case "FP" : return "des " + replacement
                }
                break
            case "PAR" :
                switch (gender) {
                    case "" : return (startsWithVowel) ? "d'" + replacement : "du " + replacement
                    case "M" : return (startsWithVowel) ? "d'" + replacement : "du " + replacement
                    case "F" : return (startsWithVowel) ? "d'" + replacement : "de la " + replacement
                    case "MP" : return "des " + replacement
                    case "FP" : return "des " + replacement
                }
                break
        }

        return replacement
    }

    String replaceRole(String syntax, String code) {
        String[] character //[fisrtname, lastname, age, gender]
        for (Map.Entry<String, Role> map : rolesNames.entrySet())
        {
            if (map.value.code.toUpperCase().equals(code))
                character = map.key.split(";")
        }

        if (null == character)
            return "[Rôle générique]"

        if (syntax.contains("M#") && syntax.contains("F#")) {
            String[] switchGender = syntax.split(";")
            if (character[3].equals("M"))
                return switchGender[0].substring(2).toLowerCase()
            else //gender = F
                return switchGender[1].substring(2).toLowerCase()
        }

        switch (syntax) {
            case "NONE" : return character[0] + " " + character[1]
            case "PRE" : return character[0]
            case "PAT" : return character[1]
            case "AGE" : return character[2]
            case "INIF" : return character[0].substring(0, 1) + ". " + character[1]
        }

        return "[Rôle générique]"
    }

    String replacePlace(String syntax, String code) {
        String replacement = "[Lieu générique]"
        String gender = ""
        for (Place place : placeList)
        {
            if (null == place.genericPlace)
                continue
            if (place.genericPlace.code.toUpperCase().equals(code)) {
                replacement = place.name
                gender = place.gender
            }
        }

        //if (gender.isEmpty())
        //return replacement

        if (syntax.contains("M#") && syntax.contains("F#")) {
            String[] switchGender = syntax.split(";")
            if (gender.equals("M"))
                return switchGender[0].substring(2).toLowerCase()
            else //gender = F
                return switchGender[1].substring(2).toLowerCase()
        }

        boolean startsWithVowel = replacement.matches("^[AEIOUY].*")

        switch (syntax) {
            case "NONE" : return replacement
            case "ART" :
                switch (gender) {
                    case "" : return (startsWithVowel) ? "l'" + replacement : "le " + replacement
                    case "M" : return (startsWithVowel) ? "l'" + replacement : "le " + replacement
                    case "F" : return (startsWithVowel) ? "l'" + replacement : "la " + replacement
                    case "MP" : return "les " + replacement
                    case "FP" : return "les " + replacement
                }
                break
            case "POS" :
                switch (gender) {
                    case "" : return "mon " + replacement
                    case "M" : return "mon " + replacement
                    case "F" : return "ma " + replacement
                    case "MP" : return "mes " + replacement
                    case "FP" : return "mes " + replacement
                }
                break
            case "NOM" :
                switch (gender) {
                    case "" : return "un " + replacement
                    case "M" : return "un " + replacement
                    case "F" : return "une " + replacement
                    case "MP" : return "des " + replacement
                    case "FP" : return "des " + replacement
                }
                break
            case "PAR" :
                switch (gender) {
                    case "" : return (startsWithVowel) ? "d'" + replacement : "du " + replacement
                    case "M" : return (startsWithVowel) ? "d'" + replacement : "du " + replacement
                    case "F" : return (startsWithVowel) ? "d'" + replacement : "de la " + replacement
                    case "MP" : return "des " + replacement
                    case "FP" : return "des " + replacement
                }
                break
        }

        return replacement
    }

    //Performs substitution between tag and value for old syntax
    def private String oldReplacePlace (String code)
    {
        for (Place place : placeList)
        {
//            print("contenu de placeList: " + place.name)
            if (place.genericPlace == null)
                continue
            if (place.genericPlace.code.toUpperCase().equals(code))
            {
                return place.name
            }
        }
        return "[Lieu générique]"
    }

    //Performs substitution between tag and value for old syntax
    def private String oldReplaceRole (String input)
    {
        for (Map.Entry<String, Role> map : rolesNames.entrySet())
        {
            if (map.value.code.toUpperCase().equals(input))
                if (map.key.split(";").length >= 2)
                    return map.key.split(";")[0] + " " + map.key.split(";")[1]
        }
        return "[Role générique]"
    }

    //Performs substitution between tag and value for old syntax
    def private String oldReplaceResource (String code)
    {
        for (GenericResource genericResource : genericResourceList)
        {
            if (genericResource.code.toUpperCase().equals(code))
                return genericResource.selectedResource.name
        }

        return "[Ressource générique]"
    }
}
