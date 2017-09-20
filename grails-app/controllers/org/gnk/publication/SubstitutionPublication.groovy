package org.gnk.publication
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Place
import org.gnk.roletoperso.Role

import java.text.SimpleDateFormat

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
    private Date gnDate
    private SimpleDateFormat simpleGnDate;
    private SimpleDateFormat simpleGnHour;

    def SubstitutionPublication(HashMap<String, Role> rolesNames, List<Place> placeList, List<GenericResource> genericResourceList, Date gnDate)
    {
        this.rolesNames = rolesNames
        this.placeList = placeList
        this.genericResourceList = genericResourceList
        this.gnDate = gnDate;
        simpleGnDate = new SimpleDateFormat("dd/MM/yyyy");
        simpleGnHour = new SimpleDateFormat("HH:mm");

        /*println("===================== Place ======================")
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
            } else if (split[0].equals("K")) {
                //GN constants were implemented after enhanced syntax, so we only require one function
                replacement = replaceGnConstant(split[1], split[2])
            }

            input = input.replace(tag, replacement);
        }

        return input
    }

    String replaceGnConstant(String syntax, String code) {
        String replacement = "[Constante du GN]"
        String gender = ""
        if (code.equals("GN-LIEU")) {
            replacement = "[GN-Lieu]"
            for (Place place : placeList) {
                replacement = place.name;
                gender = place.gender;
                break;
            }
        } else if (code.equals("GN-DATE")) {
            replacement = simpleGnDate.format(gnDate);
        } else if (code.equals("GN-HEURE")) {
            replacement = simpleGnHour.format(gnDate);
        } else {

        }


        if (syntax.contains("M#") && syntax.contains("F#")) {
            String[] switchGender = syntax.split(";")
            if (gender.equals("M"))
                return switchGender[0].substring(2).toLowerCase()
            else //gender = F
                return switchGender[1].substring(2).toLowerCase()
        }

        boolean startsWithVowel = replacement.startsWith('' +
                "a") || replacement.startsWith("e") || replacement.startsWith("i") || replacement.startsWith("o") || replacement.startsWith("u") || replacement.startsWith("y") || replacement.startsWith("h") || replacement.startsWith("é")|| replacement.startsWith("è") || replacement.startsWith("ê")
        //replacement.matches("^[HAEIOUY].*")

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
            case "POSM" :
                switch (gender) {
                    case "" : return "mon " + replacement
                    case "M" : return "mon " + replacement
                    case "F" : return "ma " + replacement
                    case "MP" : return "mes " + replacement
                    case "FP" : return "mes " + replacement
                }
                break
            case "POST" :
                switch (gender) {
                    case "" : return "ton " + replacement
                    case "M" : return "ton " + replacement
                    case "F" : return "ta " + replacement
                    case "MP" : return "tes " + replacement
                    case "FP" : return "tes " + replacement
                }
                break
            case "POSS" :
                switch (gender) {
                    case "" : return "son " + replacement
                    case "M" : return "son " + replacement
                    case "F" : return "sa " + replacement
                    case "MP" : return "ses " + replacement
                    case "FP" : return "ses " + replacement
                }
                break
            case "POSN" :
                switch (gender) {
                    case "" : return "notre " + replacement
                    case "M" : return "notre " + replacement
                    case "F" : return "notre " + replacement
                    case "MP" : return "nos " + replacement
                    case "FP" : return "nos " + replacement
                }
                break
            case "POSV" :
                switch (gender) {
                    case "" : return "votre " + replacement
                    case "M" : return "votre " + replacement
                    case "F" : return "votre " + replacement
                    case "MP" : return "vos " + replacement
                    case "FP" : return "vos " + replacement
                }
                break
            case "POSL" :
                switch (gender) {
                    case "" : return "leur " + replacement
                    case "M" : return "leur " + replacement
                    case "F" : return "leur " + replacement
                    case "MP" : return "leurs " + replacement
                    case "FP" : return "leurs " + replacement
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

    String replaceResource(String syntax, String code) {
        String replacement = "[Ressource générique]"
        String gender = ""
        String content = "[Contenu indice]";

        for (GenericResource genericResource : genericResourceList)
        {
            if (genericResource.code.toUpperCase().equals(code)) {
                if (genericResource.selectedResource != null) {
                    replacement = genericResource.selectedResource.name
                    gender = genericResource.selectedResource.gender
                }

                if (genericResource.isIngameClue())
                    content = genericResource.description;
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

        boolean startsWithVowel = replacement.startsWith('' +
                "a") || replacement.startsWith("e") || replacement.startsWith("i") || replacement.startsWith("o") || replacement.startsWith("u") || replacement.startsWith("y") || replacement.startsWith("h") || replacement.startsWith("é")|| replacement.startsWith("è") || replacement.startsWith("ê")
        //replacement.matches("^[HAEIOUY].*")

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
            case "POSM" :
                switch (gender) {
                    case "" : return "mon " + replacement
                    case "M" : return "mon " + replacement
                    case "F" : return "ma " + replacement
                    case "MP" : return "mes " + replacement
                    case "FP" : return "mes " + replacement
                }
                break
            case "POST" :
                switch (gender) {
                    case "" : return "ton " + replacement
                    case "M" : return "ton " + replacement
                    case "F" : return "ta " + replacement
                    case "MP" : return "tes " + replacement
                    case "FP" : return "tes " + replacement
                }
                break
            case "POSS" :
                switch (gender) {
                    case "" : return "son " + replacement
                    case "M" : return "son " + replacement
                    case "F" : return "sa " + replacement
                    case "MP" : return "ses " + replacement
                    case "FP" : return "ses " + replacement
                }
                break
            case "POSN" :
                switch (gender) {
                    case "" : return "notre " + replacement
                    case "M" : return "notre " + replacement
                    case "F" : return "notre " + replacement
                    case "MP" : return "nos " + replacement
                    case "FP" : return "nos " + replacement
                }
                break
            case "POSV" :
                switch (gender) {
                    case "" : return "votre " + replacement
                    case "M" : return "votre " + replacement
                    case "F" : return "votre " + replacement
                    case "MP" : return "vos " + replacement
                    case "FP" : return "vos " + replacement
                }
                break
            case "POSL" :
                switch (gender) {
                    case "" : return "leur " + replacement
                    case "M" : return "leur " + replacement
                    case "F" : return "leur " + replacement
                    case "MP" : return "leurs " + replacement
                    case "FP" : return "leurs " + replacement
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
            case "CONT" :
                return content;
                break
        }

        return replacement
    }

    String replaceRole(String syntax, String code) {
        String[] character //[fisrtname, lastname, age, gender]
        // Recherche du 'character' correspondant au 'code'
        for (Map.Entry<String, Role> map : rolesNames.entrySet()) {
            // 'character' sera le dernier personage qui correspond au 'code' s'il n'est pas un PJG ou TPJ
            if (map.value.code.toUpperCase().equals(code)) {
                if (map.value.PJG)
                    return replaceListingRole(syntax, code, "PJG")
                else if (map.value.TPJ)
                    return replaceListingRole(syntax, code, "TPJ")
                else
                    character = map.key.split(";")
            }
        }

        if (character == null)
            return "[Rôle générique]"

        // Retourne le changement de genre
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

    // Devrait remplacer les roles(TPJ/PJG) avec 'code' par l'élément en prenant la 'syntax'
    // 'type' = "TPJ" ou "PJG"
    String replaceListingRole(String syntax, String code, String type) {
        String result = ""
        String[] character
        boolean isFirst = true
        for (Map.Entry<String, Role> map : rolesNames.entrySet())
        {
            if (map.value.code.toUpperCase().equals(code) && map.value.type.equals(type))
            {
                character = map.key.split(";")
                if (null == character)
                    continue
                // pas de check sur genderSwap
                // Si c'est le premier élément ou le seul on ne met pas de '; ' devant
                if (isFirst)
                    isFirst = false
                else
                    result += ", "

                /*
                * Switch pour savoir le contenu que l'on veut afficher
                * "NONE" -> "Prénom Patronyme" ex: Azraël Godsword
                * "PRE" -> "Prénom" ex: Azraël
                * "PAT" -> "Patronyme" nom de famille, ex: Godsword
                * "AGE" -> "Age" ex: 200000
                * "INIF" -> ex: Az. Godsword
                */
                switch (syntax) {
                    case "NONE" : result += character[0] + " " + character[1]
                        break
                    case "PRE" : result += character[0]
                        break
                    case "PAT" : result +=  character[1]
                        break
                    case "AGE" : result += character[2]
                        break
                    case "INIF" : result += character[0].substring(0, 1) + ". " + character[1]
                        break
                }
            }
        }
        if (!result.isEmpty())
            return result
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

        boolean startsWithVowel = replacement.startsWith('' +
                "a") || replacement.startsWith("e") || replacement.startsWith("i") || replacement.startsWith("o") || replacement.startsWith("u") || replacement.startsWith("y") || replacement.startsWith("h") || replacement.startsWith("é")|| replacement.startsWith("è") || replacement.startsWith("ê")
        //replacement.matches("^[HAEIOUY].*")

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
            case "POSM" :
                switch (gender) {
                    case "" : return "mon " + replacement
                    case "M" : return "mon " + replacement
                    case "F" : return (startsWithVowel) ? "mon" + replacement : "ma " + replacement
                    case "MP" : return "mes " + replacement
                    case "FP" : return "mes " + replacement
                }
                break
            case "POST" :
                switch (gender) {
                    case "" : return "ton " + replacement
                    case "M" : return "ton " + replacement
                    case "F" : return (startsWithVowel) ? "ton" + replacement : "ta " + replacement
                    case "MP" : return "tes " + replacement
                    case "FP" : return "tes " + replacement
                }
                break
            case "POSS" :
                switch (gender) {
                    case "" : return "son " + replacement
                    case "M" : return "son " + replacement
                    case "F" : return (startsWithVowel) ? "son" + replacement :  "sa " + replacement
                    case "MP" : return "ses " + replacement
                    case "FP" : return "ses " + replacement
                }
                break
            case "POSN" :
                switch (gender) {
                    case "" : return "notre " + replacement
                    case "M" : return "notre " + replacement
                    case "F" : return "notre " + replacement
                    case "MP" : return "nos " + replacement
                    case "FP" : return "nos " + replacement
                }
                break
            case "POSV" :
                switch (gender) {
                    case "" : return "votre " + replacement
                    case "M" : return "votre " + replacement
                    case "F" : return "votre " + replacement
                    case "MP" : return "vos " + replacement
                    case "FP" : return "vos " + replacement
                }
                break
            case "POSL" :
                switch (gender) {
                    case "" : return "leur " + replacement
                    case "M" : return "leur " + replacement
                    case "F" : return "leur " + replacement
                    case "MP" : return "leurs " + replacement
                    case "FP" : return "leurs " + replacement
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
                if(genericResource.selectedResource != null)
                    if(genericResource.selectedResource.name != null)
                        return genericResource.selectedResource.name
        }

        return "[Ressource générique]"
    }
}
