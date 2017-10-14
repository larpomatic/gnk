package org.gnk.naming

import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.tag.V2TagService
import org.gnk.utils.Pair
import org.hibernate.FetchMode

class Naming2Service {

    def serviceMethod() {}

    V2TagService v2TagService
    Integer selectionNumber = 10
    LinkedList<String> usedFirstName = new LinkedList<String>()
    LinkedList<String> usedName = new LinkedList<String>()

    LinkedList<PersoForNaming> findBestObjects(LinkedList<PersoForNaming> persoList, Integer gn_id)
    {
        //region <Lists initializations>
        LinkedList<PersoForNaming> result = new LinkedList<PersoForNaming>()
        LinkedList<Firstname> fnlistHomme = getFirstNamebyGender (persoList, "m", persoList.first.universe)
        LinkedList<Firstname> fnlistFemme = getFirstNamebyGender (persoList, "f", persoList.first.universe)
        LinkedList<Name> nlist = getNamebyTag(persoList, persoList.first.universe)
        Collections.shuffle(fnlistHomme)
        Collections.shuffle(fnlistFemme)
        Collections.shuffle (nlist)
        //endregion

        //Loop on every character in the list
        for (PersoForNaming charater : persoList)
        {

        }

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
