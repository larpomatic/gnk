package org.gnk.publication

import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.Resource
import org.gnk.roletoperso.Character
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
//        println("===================== Generic Ressource =========")
//        for (GenericResource genericResource : genericResourceList)
//        {
//            if (genericResource.genericRessourceHasIngameClue != null)
//                println(genericResource.selectedResource.name+"==INGAME CLUE : " + genericResource.genericRessourceHasIngameClue.title)
//            else
//                println(genericResource.selectedResource.name)
//        }
//        println("===================== Place ======================")
//        for (Place place : placeList)
//        {
//            if (place.genericPlace == null)
//                continue
//            println(place.genericPlace.code)
//        }
//        println("===================== Role =====================")
//        for (Map.Entry<String, Role> map : rolesNames.entrySet())
//        {
//            println(map.key)
//        }
    }

    def String replaceAll (String input)
    {
        if (input == null)
            return null
        if (!input.contains("<") || !input.contains(">"))
            return input

        boolean open = false
        Integer start = null
        Integer end = null
        StringBuilder res = new StringBuilder("")
        StringBuilder tmp = new StringBuilder("")

        for (int i = 0; i < input.length(); i++)
        {
            char c = input.toCharArray()[i]
            // Errors
            if (c == "<" && open)
                return null
            if (c == ">" && !open)
                return null
            if (c == "<" && !open)
            {
                open = true
                start = i
            }

            if (open)
                tmp.append(c)
            if (!open)
                res.append(c)

            if (c == ">" && open)
            {
                open = false
                end = i

                char switchChar = tmp.toString().charAt(1).charValue().toUpperCase()
                String code = tmp.toString().substring(3, tmp.length() - 1).toUpperCase()
                if (switchChar == 'L')
                    res.append(replacePlace(code))
                else if (switchChar == 'O')
                    res.append(replaceResource(code))
                else if (switchChar == 'I')
                    res.append(replaceRole(code))
                else
                    res.append(tmp.toString())
                tmp = new StringBuilder()
            }
        }

        return res
    }

    def private String replacePlace (String code)
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

    def private String replaceRole (String input)
    {
        for (Map.Entry<String, Role> map : rolesNames.entrySet())
        {
            if (map.value.code.toUpperCase().equals(input))
                return map.key
        }
        return "[Role générique]"
    }

    def private String replaceResource (String code)
    {
        for (GenericResource genericResource : genericResourceList)
        {
            if (genericResource.code.toUpperCase().equals(code))
                return genericResource.selectedResource.name
        }

        return "[Ressource générique]"
    }
}
