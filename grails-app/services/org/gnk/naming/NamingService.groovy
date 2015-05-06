
package org.gnk.naming

import com.gnk.substitution.Tag
import org.gnk.tag.TagRelation
import org.hibernate.FetchMode
import org.springframework.util.StopWatch
import org.javatuples.Pair
import org.gnk.tag.TagService
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

        HashMap<Tag, Tag> referenceRelation = new HashMap<>()

        StringBuilder queryFirstName = new StringBuilder("FROM TagRelation where ")
        StringBuilder queryName = new StringBuilder("FROM TagRelation where ")

        LinkedList<Pair<PersoForNaming, LinkedList<Firstname>>> listPersoName = []

        for (PersoForNaming tmp : persoList){

            LinkedList<Firstname> fnlist = null

            if (tmp.is_selectedFirstName) {

                fnlist = tmp.getgender() == "M" ? fnlistHomme : fnlistFemme


                for (Tag persotag : tmp.tag) {

                    if (persotag.type.equals("Sexe")) {
                        if (persotag.weight.equals(-101))
                            if (tmp.getgender() == "F" || tmp.getgender() == "H")
                                fnlist = tmp.getgender() == "F" ? fnlistHomme : fnlistFemme
                            else
                                fnlist = persotag.value.equals("Femme") ? fnlistHomme : fnlistFemme
                        else if (tmp.getgender() == "N")
                            fnlist = persotag.value.equals("Homme") ? fnlistHomme : fnlistFemme
                        else
                            fnlist = tmp.getgender() == "M" ? fnlistHomme : fnlistFemme
                    }

                    for (Firstname fn : fnlist) {

                        Set<FirstnameHasTag> fnHasTags = fn.getExtTags();
                        if (fnHasTags != null) {
                            for (FirstnameHasTag fnHasTag : fnHasTags) {
                                if (!referenceRelation.containsKey(fnHasTag.getTag())
                                        && referenceRelation[fnHasTag.getTag()] != persotag.getValue()) {
                                    String relation1 = " tag1.name = '" + fnHasTag.getTag() + "' and tag2.name = '" + persotag.getValue() + "'"
                                    String relation2 = " tag1.name = '" + persotag.getValue() + "' and tag2.name = '" + fnHasTag.getTag() + "'"
                                    queryFirstName.append(relation1 + " or " + relation2 + " or ")

                                    referenceRelation.put(fnHasTag.getTag(), persotag.getValue())
                                    referenceRelation.put(persotag.getValue(), fnHasTag.getTag())
                                }
                            }
                        }
                    }

                    for (Name n : nlist){
                        Set<NameHasTag> nHasTags = n.getExtTags();
                        if (nHasTags != null) {
                            for (NameHasTag nHasTag : nHasTags) {
                                if (!referenceRelation.containsKey(nHasTag.getTag())
                                        && referenceRelation[nHasTag.getTag()] != persotag.getValue()) {
                                    String relation1 = " tag1.name = '" + nHasTag.getTag() + "' and tag2.name = '" + persotag.getValue() + "'"
                                    String relation2 = " tag1.name = '" + persotag.getValue() + "' and tag2.name = '" + nHasTag.getTag() + "'"
                                    queryName.append(relation1 + " or " + relation2 + " or ")

                                    referenceRelation.put(nHasTag.getTag(), persotag.getValue())
                                    referenceRelation.put(persotag.getValue(), nHasTag.getTag())
                                }
                            }
                        }
                    }
                }
            }
            listPersoName.add(new Pair<PersoForNaming, LinkedList<Firstname>> (tmp, fnlist))
        }

        queryFirstName.setLength(queryFirstName.size() - 3)
        String stringQuery1 = queryFirstName.toString()
        List<TagRelation> AllRelationFirstName = TagRelation.executeQuery(stringQuery1)


        queryName.setLength(queryName.size() - 3)
        String stringQuery2 = queryName.toString()
        List<TagRelation> AllRelationName = TagRelation.executeQuery(stringQuery2)

        // Pour chaque personnage envoye
        for (Pair<PersoForNaming, LinkedList<Firstname>> pairPersoName : listPersoName)
        {
            PersoForNaming tmp = pairPersoName.value0
            LinkedList<Firstname> fnlist = pairPersoName.value1

            // Choix des prenoms
            if (pairPersoName.value1 != null)
            {
                NameAndWeight maxname
                Integer rankTag = 0;
                List<NameAndWeight> fnweight = new ArrayList<NameAndWeight>()

                Map<org.gnk.tag.Tag, Integer> tagMap = new HashMap<org.gnk.tag.Tag, Integer>()
                for (Tag t : tmp.getTag()) {
                    tagMap.put(org.gnk.tag.Tag.findWhere(name: t.value), t.weight)
                }
                //rank
                rankTag = PersoFirstNameRank(AllRelationFirstName, fnlist, tagMap, fnweight)


                    if (fnweight.empty)
                    fnweight = getRandomFirstname(fnlist)

                Collections.sort(fnweight)
                // ranger la liste dans l'ordre et la mettre dans le perso a renvoyer
                while (fnweight.size() > 0)
                {
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
                        rankTag = PersoNameRank(AllRelationName, nlist, tagMap, nweight)

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
            // ajout du personnage a la liste des personnages deja traite pour pouvoir retrouver les noms de famille
            doneperso.add(tmp)

        }

        total.stop()
        print("total = " + total.getTotalTimeSeconds())
        return doneperso
    }

    private int PersoFirstNameRank(List<TagRelation> AllRelation, LinkedList<Firstname> fnlist, tagMap, ArrayList<NameAndWeight> fnweight) {
        int rankTag
        for (Firstname fn : fnlist) {
            Set<FirstnameHasTag> fnHasTags = fn.getExtTags();
            Map<Tag, Integer> challengerTagList = new HashMap<Tag, Integer>();
            if (fnHasTags != null) {
                for (FirstnameHasTag fnHasTag : fnHasTags) {
                    challengerTagList.put(fnHasTag.getTag(), fnHasTag.getWeight());
                }

                //calcule la correspondance d'un prenom avec le caractere
                rankTag = (new TagService()).getTagsMatchingCalculate(tagMap, challengerTagList, AllRelation, Collections.emptyMap());
                fnweight.add(new NameAndWeight(fn.name, rankTag))
            }
        }
        return rankTag
    }

    private int PersoNameRank(List<TagRelation> AllRelation, LinkedList<Name> nlist, tagMap, ArrayList<NameAndWeight> fnweight) {
        int rankTag
        for (Name fn : nlist) {
            Set<FirstnameHasTag> nHasTags = fn.getExtTags();
            Map<Tag, Integer> challengerTagList = new HashMap<Tag, Integer>();
            if (nHasTags != null) {
                for (NameHasTag nHasTag : nHasTags) {
                    challengerTagList.put(nHasTag.getTag(), nHasTag.getWeight());
                }

                //calcule la correspondance d'un prenom avec le caractere
                rankTag = (new TagService()).getTagsMatchingCalculate(tagMap, challengerTagList, AllRelation, Collections.emptyMap());
                fnweight.add(new NameAndWeight(fn.name, rankTag))
            }
        }
        return rankTag
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
