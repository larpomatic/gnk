package org.gnk.naming

import com.gnk.substitution.Tag
import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.tag.TagService
import org.gnk.tag.V2TagService
import org.gnk.utils.Pair
import org.hibernate.FetchMode

class Naming2Service {

    private static boolean VERBOSE = false
    def serviceMethod() {}

    V2TagService v2TagService = new V2TagService()
    Integer selectionNumber = 6
    LinkedList<String> usedFirstName = new LinkedList<String>()
    LinkedList<String> usedName = new LinkedList<String>()

    LinkedList<PersoForNaming> findBestNames(LinkedList<PersoForNaming> persoList, Integer gn_id)
    {
        //region <Initializations>
        LinkedList<PersoForNaming> result = new LinkedList<PersoForNaming>()
        if (VERBOSE== true) println ("Univers = " + persoList.first.universe)
        LinkedList<Firstname> fnlistHomme = getFirstNamebyGender (persoList, "m", persoList.first.universe)
        LinkedList<Firstname> fnlistFemme = getFirstNamebyGender (persoList, "f", persoList.first.universe)
        LinkedList<Name> nlist = getNamebyTag(persoList, persoList.first.universe)
        Collections.shuffle(fnlistHomme)
        Collections.shuffle(fnlistFemme)
        Collections.shuffle (nlist)
        Gn gn = Gn.findById(gn_id)
        //endregion

        //Loop on every character in the list
        long startTime = System.nanoTime();
        for (PersoForNaming character : persoList)
        {
            long startTimeCharacter = System.nanoTime();
            if (character.is_selectedFirstName){
                NameAndWeight maxname
                LinkedList<Firstname> fnlist = character.getgender() == "M" ? fnlistHomme : fnlistFemme
                Integer rankTag = 0;
                List<NameAndWeight> fnweight = new ArrayList<NameAndWeight>()

                for (Tag persotag : character.tag )
                {
                    if (persotag.type.equals("Sexe"))
                    {
                        if (persotag.weight.equals(-101))
                            if (character.getgender() == "F" || character.getgender() == "H")
                                fnlist = character.getgender() == "F" ? fnlistHomme : fnlistFemme
                            else
                                fnlist = persotag.value.equals("Femme") ? fnlistHomme : fnlistFemme
                        else if (character.getgender() == "N")
                            fnlist = persotag.value.equals("Homme") ? fnlistHomme : fnlistFemme
                        else
                            fnlist = character.getgender() == "M" ? fnlistHomme : fnlistFemme
                        break;
                    }
                }

                for (Firstname firstname : fnlist){
                    //calcule la correspondance d'un prenom avec le character
                    rankTag = (new Integer((int) v2TagService.computeComparativeScoreObject(character, firstname, gn)))
                    fnweight.add(new NameAndWeight(firstname.name, rankTag))
                    //println ("NAME AND RANK: " + firstname.name + rankTag)
                }

                if (fnweight.empty)
                    fnweight = getRandomFirstname(fnlist)

                Collections.sort(fnweight)


                if (VERBOSE==true)
                {
                    for (int i = fnweight.size()-1; i > fnweight.size() -1 - selectionNumber; i--)
                        println ("name: " + fnweight.get(i).name + ", rank: " + fnweight.get(i).weight)
                }

                // ranger la liste dans l'ordre et la mettre dans le perso a renvoyer
                while (fnweight.size() > 0)
                {
                    maxname = fnweight.last()
                    if (!usedFirstName.contains(maxname.name) && !character.selectedFirstnames.contains(maxname.name))
                    {
                        character.selectedFirstnames.add(maxname.name)
                        if (character.selectedFirstnames.size() > selectionNumber / 2)
                            usedFirstName.add(character.selectedFirstnames.last())
                    }
                    fnweight.remove(maxname)

                    if (character.selectedFirstnames.size() >= selectionNumber)
                        break
                }
                if (!character.selectedFirstnames.isEmpty())
                    usedFirstName.add(character.selectedFirstnames.first())
            }

            if (!character.relationList.empty){
                List<List<Object>> res = (new ConventionVisitorService()).visit(character, result, gn_id)
                if (!res[1].isEmpty()){
                    result = (LinkedList<PersoForNaming>) res[0]
                    character.selectedNames = (List<String>) res[1]
                }
            }

            // Choix du nom de famille
            if (character.is_selectedName && character.selectedNames.isEmpty())
            {
                NameAndWeight maxname
                List<NameAndWeight> nweight = new ArrayList<NameAndWeight>()
                Integer rankTag = 0;
                for (Name name : nlist){
                    //calcule la correspondance d'un nom avec le character
                    rankTag = (new Integer((int) v2TagService.computeComparativeScoreObject(character, name, gn)))
                    nweight.add(new NameAndWeight(name.name, rankTag))
                }

                if (nweight.size() < persoList.size())
                    nweight += getRandomName(nlist, persoList)

                // verifie que les tags sont bien valides
                if (!nweight.empty)
                {
                    Collections.sort(nweight)
                    // ranger la liste dans l'ordre et la mettre dans le perso a renvoyer
                    while (nweight.size() > 0)
                    {
                        maxname = nweight.last()
                        if (!usedName.contains(maxname.name) && !character.selectedNames.contains(maxname.name))
                        {
                            character.selectedNames.add(maxname.name)
                            if (character.selectedNames.size() > selectionNumber / 2)
                                usedName.add(character.selectedNames.last())
                        }
                        nweight.remove(maxname)

                        if (character.selectedNames.size() >= selectionNumber)
                            break
                    }
                    if (!character.selectedNames.isEmpty())
                        usedName.add(character.selectedNames.first())
                }
            }
            long endTimeCharacter = System.nanoTime();
            println ("Naming the character number " + character.code + ": " + ((endTimeCharacter - startTimeCharacter )/ 1000000000.0))
            result.add(character)
            //print("NAMING 2 SERVICE DONE !")

        }
        long endTime = System.nanoTime();
        println ("Time for naming all the characters: ".toUpperCase()
                + ((endTime - startTime )/ 1000000000.0) + " seconds")
        return result
    }

    // recuperer les prenoms de la base de donnees en fonction de l'univers et du genre (+ les neutre)
    private LinkedList<Firstname> getFirstNamebyGender (LinkedList<PersoForNaming> persoList, String gender, String universe)
    {
        LinkedList<Firstname> fnlist = new LinkedList<Firstname>()
        fnlist = FirstnameHasTag.createCriteria().list {
            createAlias('tag', 'T')
            createAlias('firstname', 'N')
            and{
                eq ('T.name', universe)
                or{
                    eq ('N.gender', gender)
                    eq ('N.gender', 'n')
                }
            }
            maxResults(500)
            order("N.name", "desc")
            projections{
                property("firstname")
            }
            fetchMode("firstname", FetchMode.EAGER)
        }
        print(fnlist.size())
        if(fnlist.isEmpty() || fnlist.size() < (persoList.size() * 1))
        {
            fnlist += Firstname.findAll("from Firstname where gender=\'$gender\' order by rand()", [max: 100])
            return (fnlist)
        }
        return fnlist
    }

    // recuperer les noms de la base de donnees en fonction de l'univers
    private LinkedList<Name> getNamebyTag (LinkedList<PersoForNaming> persoList, String universe)
    {
        LinkedList<Name> nlist = new LinkedList<Name>()
        nlist = NameHasTag.createCriteria().list {
            createAlias('tag', 'T')
            createAlias('name', 'N')
            eq ('T.name', universe)
            maxResults(500)
            order("N.name", "desc")
            projections{
                property("name")
            }
            fetchMode("name", FetchMode.EAGER)
        }
        if(nlist.isEmpty() || nlist.size() < (persoList.size() * 1))
        {
            nlist += Name.findAll("from Name order by rand()", [max: 100])
            return (nlist)
        }
        return nlist
    }

    private LinkedList<NameAndWeight> getRandomFirstname (LinkedList fnlist)
    {
        LinkedList<NameAndWeight> fnweight = new LinkedList<NameAndWeight> ()
        for (Firstname fn : fnlist)
        {
            NameAndWeight firstn = new NameAndWeight(fn.name, 0)

            fnweight.add(firstn)
        }

        return fnweight
    }

    private LinkedList<NameAndWeight> getRandomName (LinkedList nlist, LinkedList persoList)
    {
        LinkedList<NameAndWeight> nweight = new LinkedList<NameAndWeight> ()
        for (Name n : nlist)
        {
            NameAndWeight firstn = new NameAndWeight(n.name, 0)

            nweight.add(firstn)
            if (nweight.size() > persoList.size())
                break;
        }

        return nweight
    }
}
