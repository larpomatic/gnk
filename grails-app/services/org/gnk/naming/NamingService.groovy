
package org.gnk.naming

import com.gnk.substitution.Tag
import org.gnk.tag.TagService
import org.hibernate.FetchMode
import org.springframework.util.StopWatch;

class NamingService
{
    // Nombre de prenom ou de nom a renvoyer
    Integer selectionNumber = 6
    // Pour �viter de renvoyer toujours le meme prenom en choix 1
    LinkedList<String> usedFirstName = new LinkedList<String>()
    // Pour �viter de renvoyer toujours le meme nom en choix 1
    LinkedList<String> usedName = new LinkedList<String>()

    /* Methode principale naming */
    LinkedList<PersoForNaming> namingMethod(LinkedList<PersoForNaming> persoList, Integer gn_id)
    {
        LinkedList<PersoForNaming> doneperso = new LinkedList<PersoForNaming>()
        // pour les tests de naming
        usedFirstName = new LinkedList<String>()
        usedName = new LinkedList<String>()

        StopWatch total = new StopWatch()
        total.start()

        // liste des prenoms possibles
        LinkedList<Firstname> fnlistHomme = getFirstNamebyGender (persoList, "m", persoList.first.universe)
        LinkedList<Firstname> fnlistFemme = getFirstNamebyGender (persoList, "f", persoList.first.universe)
        Collections.shuffle(fnlistHomme)
        Collections.shuffle(fnlistFemme)
        //liste des noms possibles
        LinkedList<Name> nlist = getNamebyTag(persoList, persoList.first.universe)
        Collections.shuffle (nlist)

        // Pour chaque personnage envoye
        for (PersoForNaming tmp : persoList)
        {
            // Choix des prenoms
            if (tmp.is_selectedFirstName)
            {
                NameAndWeight maxname
                LinkedList<Firstname> fnlist = tmp.getgender() == "M" ? fnlistHomme : fnlistFemme
                Integer rankTag = 0;
                //LinkedList<NameAndWeight> fnweight = new LinkedList<NameAndWeight>()
                List<NameAndWeight> fnweight = new ArrayList<NameAndWeight>()

                for (Tag persotag : tmp.tag )
                {
                    if (persotag.type.equals("Sexe"))
                    {
                        if (persotag.weight.equals(-101))
                            if (tmp.getgender() == "F" || tmp.getgender() == "H")
                                fnlist = tmp.getgender() == "F" ? fnlistHomme : fnlistFemme
                            else
                                fnlist = persotag.value.equals("Femme") ? fnlistHomme : fnlistFemme
                        else if (tmp.getgender() == "N")
                            fnlist = persotag.value.equals("Homme") ? fnlistHomme : fnlistFemme
                        else
                            fnlist = tmp.getgender() == "M" ? fnlistHomme : fnlistFemme
                        break;
                    }
                }

                Map<org.gnk.tag.Tag, Integer> tagMap = new HashMap<org.gnk.tag.Tag, Integer>()
                for (Tag t : tmp.getTag()) {
                    tagMap.put(org.gnk.tag.Tag.findWhere(name: t.value), t.weight)
                }

                for (Firstname fn : fnlist){
                    Set<FirstnameHasTag> fnHasTags = fn.getExtTags();
                    Map<Tag, Integer> challengerTagList = new HashMap<Tag, Integer>();
                    if (fnHasTags != null) {
                        for (FirstnameHasTag fnHasTag : fnHasTags) {
                            challengerTagList.put(fnHasTag.getTag(), fnHasTag.getWeight());
                        }

                        //calcule la correspondance d'un prenom avec le caractere
                        rankTag = (new TagService()).getTagsMatching(tagMap, challengerTagList, Collections.emptyMap());
                        fnweight.add(new NameAndWeight(fn.name, rankTag))
                    }
                }

                if (fnweight.empty)
                    fnweight = getRandomFirstname(fnlist)

                Collections.sort(fnweight)
                // ranger la liste dans l'ordre et la mettre dans le perso a renvoyer
                while (fnweight.size() > 0)
                {
                    /*maxname = fnweight.first
                    for (NameAndWeight nw : fnweight)
                    {
                        if (maxname.weight < nw.weight)
                            maxname = nw
                    }*/
                    maxname = fnweight.last()
                    if (!usedFirstName.contains(maxname.name) && !tmp.selectedFirstnames.contains(maxname.name))
                    {
                        tmp.selectedFirstnames.add(maxname.name)
                        if (tmp.selectedFirstnames.size() > 3)
                            usedFirstName.add(tmp.selectedFirstnames.last())
                    }
                    fnweight.remove(maxname)

                    if (tmp.selectedFirstnames.size() >= selectionNumber)
                        break
                }

                if (!tmp.selectedFirstnames.isEmpty())
                    usedFirstName.add(tmp.selectedFirstnames.first())
            }

            if (!tmp.relationList.empty){
                List<List<Object>> res = (new ConventionVisitorService()).visit(tmp, doneperso, gn_id)
                if (!res[1].isEmpty()){
                    doneperso = (LinkedList<PersoForNaming>) res[0]
                    tmp.selectedNames = (List<String>) res[1]
                }
            }

            // Choix du nom de famille
            if (tmp.is_selectedName && tmp.selectedNames.isEmpty())
            {
                NameAndWeight maxname
                //LinkedList<NameAndWeight> nweight = new LinkedList<NameAndWeight>()
                List<NameAndWeight> nweight = new ArrayList<NameAndWeight>()
                Integer rankTag = 0;

                Map<org.gnk.tag.Tag, Integer> tagMap = new HashMap<org.gnk.tag.Tag, Integer>()
                for (Tag t : tmp.getTag()) {
                    tagMap.put(org.gnk.tag.Tag.findWhere(name: t.value), t.weight)
                }

                for (Name n : nlist){
                    Set<NameHasTag> nHasTags = n.getExtTags();
                    Map<Tag, Integer> challengerTagList = new HashMap<Tag, Integer>();
                    if (nHasTags != null) {
                        for (NameHasTag nHasTag : nHasTags) {
                            challengerTagList.put(nHasTag.getTag(), nHasTag.getWeight());
                        }

                        //calcule la correspondance d'un nom avec le caractere
                        rankTag = (new TagService()).getTagsMatching(tagMap, challengerTagList, Collections.emptyMap());
                        nweight.add(new NameAndWeight(n.name, rankTag))
                    }
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
                        /*maxname = nweight.first
                        for (NameAndWeight nw : nweight)
                        {
                            if (maxname.weight < nw.weight)
                                maxname = nw
                        }*/
                        maxname = nweight.last()
                        if (!usedName.contains(maxname.name) && !tmp.selectedNames.contains(maxname.name))
                        {
                            tmp.selectedNames.add(maxname.name)
                            if (tmp.selectedNames.size() > 3)
                                usedName.add(tmp.selectedNames.last())
                        }
                        nweight.remove(maxname)

                        if (tmp.selectedNames.size() >= selectionNumber)
                            break
                    }
                    if (!tmp.selectedNames.isEmpty())
                        usedName.add(tmp.selectedNames.first())
                }
            }
            //print("     LN " + tmp)
            // ajout du personnage a la liste des personnages deja traite pour pouvoir retrouver les noms de famille
            doneperso.add(tmp)
            print("Perso Done !")
        }
        total.stop()
        print("total = " + total.getTotalTimeSeconds())
        return doneperso
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
