package org.gnk.tag

import org.gnk.gn.Gn
import org.gnk.naming.Firstname
import org.gnk.naming.FirstnameHasTag
import org.gnk.naming.Name
import org.gnk.naming.NameHasTag
import org.gnk.naming.PersoForNaming
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject

public class V2TagService {

    private static int  IDgenericUniverTag = 33089;
    private static int NumberOfGenerationsRelevant = 2;
    private static int NumberofGenerationsParent = 2;
    private static int PonderationParent = 1;
    private static float GNponderation = 0.9;
    private static float Evenementielponderation = 0.6;
    private static float Mainstreamponderation = 0.4;
    private static float plotponderation = 0.2;
    private static float GenericObjectponderation = 1;
    private static float ReferentialObjectponderation = 1;
    private static boolean VERBOSE = false

    V2TagService() {
    }
/**
     * get all the univers
     * @return list of all the univers
     */
    ArrayList<Tag> getUnivers() {
        ArrayList<Tag> UniverListTag = new ArrayList<Tag>();
        Tag genericUnivers = Tag.findById(IDgenericUniverTag);

        def parentList = Tag.findAllByParent(genericUnivers);
        UniverListTag.addAll(parentList);
        Collections.sort(UniverListTag);

        //    for (Tag child in genericUnivers.children)
        //       UniverListTag.add(child);
        // Collections.sort(tagUniversList, new ComparateurTag())

        return UniverListTag;
    }

    /**
     * Calculate the total score of similitude between a GenericObjet and an Objet
     * @param genericObject
     * @param Object
     * @param Gn
     * @return the calculus in Long
     */
    Long computeComparativeScoreObject(GenericObject genericObject, ReferentialObject object, Gn gn) {

        int totalNumberOfTagsUsed = 0;
        int result = 0;
        // initialisation des tags du generic object
        Map<Tag, Integer> map_genericObject = initGenericObjectList(genericObject, gn);

        //récupérer les tags relevants du genericobjet
        map_genericObject.putAll(getRelevantTags(genericObject.getTagsAndWeights(GenericObjectponderation)));
        //for (tags )

        //initialisation des tags de l'object
        Map<Tag, Integer> map_Object = initObjectList(object);


        //récupérer les tags parents
        map_genericObject.putAll(getParentTags(genericObject.getTagsAndWeights(PonderationParent)));


        //récupérer les tags du refenretialobjet
        map_Object.putAll(getRelevantTags(object.getTagsAndWeights(ReferentialObjectponderation)));
        //récupérer les tags parents du referential
        map_Object.putAll(getParentTags(object.getTagsAndWeights(PonderationParent)));

        Long score = 0;

        for (Map.Entry<Tag, Integer> entry_generic : map_genericObject.entrySet()) {
            for (Map.Entry<Tag, Integer> entry : map_Object.entrySet()) {
                if (entry_generic.getKey().getId() == null) {
                    if (entry_generic.getKey().value_substitution.equals(entry.getKey().getName()) /*|| entry_generic.getKey().type_substitution.equals(entry.getKey().getName())*/) {
                        score += computeCumulativeScoreTags(entry_generic.getKey(), entry_generic.getValue(), entry.getValue());
                        //score = tagUniversTreatment(entry_generic.getKey(), score, map_genericObject);
                        totalNumberOfTagsUsed += 1;
                    }
                } else {
                    if (entry_generic.getKey().getId().equals(entry.getKey().getId())) {
                        score += computeCumulativeScoreTags(entry_generic.getKey(), entry_generic.getValue(), entry.getValue());
                        //score = tagUniversTreatment(entry_generic.getKey(), score, map_genericObject);
                        totalNumberOfTagsUsed += 1;
                    }
                }
            }
        }

        result = totalNumberOfTagsUsed == 0 ? score : (score /totalNumberOfTagsUsed);
        return result;
    }

    /**
     * Calculate the total score of similitude between the map of Character 's tags and a Firstname
     * @param map_character
     * @param Firstname
     * @return the calculus in Long
     */
    Long computeComparativeScoreObject(Map<Tag, Integer> map_character, Firstname firstname){
        int totalNumberOfTagsUsed = 0
        int result = 0
        Long score = 0

        Map<Tag, Integer> map_firstname = initObjectList(firstname)
        map_firstname.putAll(getRelevantTags(map_firstname))
        map_firstname.putAll(getParentTags(map_firstname))

        for (Map.Entry<Tag, Integer> entry_character : map_character.entrySet()) {
            for (Map.Entry<Tag, Integer> entry_firstname : map_firstname.entrySet()) {
                if (entry_character.getKey().getId() == null) {
                    if (entry_character.getKey().value_substitution == (entry_firstname.getKey().getName())) {
                        score += computeCumulativeScoreTags(entry_character.getKey(), entry_character.getValue(), entry_firstname.getValue());
                        totalNumberOfTagsUsed += 1;
                    }
                } else {
                    if (entry_character.getKey().getId()== (entry_firstname.getKey().getId())) {
                        score += computeCumulativeScoreTags(entry_character.getKey(), entry_character.getValue(), entry_firstname.getValue());
                        totalNumberOfTagsUsed += 1
                    }
                }
            }
        }
        result = totalNumberOfTagsUsed == 0 ? score : (score /totalNumberOfTagsUsed)
        return result
    }

    /**
     * Calculate the total score of similitude between the map of Character 's tags and a Name
     * @param map_character
     * @param Name
     * @return the calculus in Long
     */
    Long computeComparativeScoreObject(Map<Tag, Integer> map_character, Name name) {
        int totalNumberOfTagsUsed = 0
        int result = 0
        Long score = 0

        Map<Tag, Integer> map_name = initObjectList(name)
        map_name.putAll(getRelevantTags(map_name))
        map_name.putAll(getParentTags(map_name))

        for (Map.Entry<Tag, Integer> entry_character : map_character.entrySet()) {
            for (Map.Entry<Tag, Integer> entry_name : map_name.entrySet()) {
                if (entry_character.getKey().getId() == null) {
                    if (entry_character.getKey().value_substitution == (entry_name.getKey().getName())) {
                        score += computeCumulativeScoreTags(entry_character.getKey(), entry_character.getValue(), entry_name.getValue());
                        totalNumberOfTagsUsed += 1;
                    }
                } else {
                    if (entry_character.getKey().getId()== (entry_name.getKey().getId())) {
                        score += computeCumulativeScoreTags(entry_character.getKey(), entry_character.getValue(), entry_name.getValue());
                        totalNumberOfTagsUsed += 1
                    }
                }
            }
        }
        result = totalNumberOfTagsUsed == 0 ? score : (score /totalNumberOfTagsUsed);
        return result
    }

    /**
     * Initialize the list of tags with the weight of GenericObject Place/Resource and a gn
     * @param GenericObject
     * @param gn
     * @return Map < Tag , Integer >
     */
    Map<Tag, Integer> initGenericObjectList(GenericObject genericObject, Gn gn) {

        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>()

        map_tags.putAll(genericObject.getTagsAndWeights(GenericObjectponderation))
        //for (tags in map_tags)

        //recupérer les tags du gn
        // chaque poids d'un tag du GN est pondéré à 90%
        if (gn.gnTags != null) {
            for (Map.Entry<Tag, Integer> gnTags_list : gn.gnTags.entrySet()) {
                map_tags = addTag(map_tags, gnTags_list.getKey(), new Integer((int) gnTags_list.getValue() * GNponderation));
            }
        }

        if (gn.getUnivers() != null) {
            map_tags = addTag(map_tags, gn.getUnivers(), gn.getUnivers().getWeight());
        }

        if (gn.evenementialTags != null) {
            // chaque poids d'un tag evenementiel du GN est pondéré à 60%
            for (Map.Entry<Tag, Integer> gnevenementialTags_list : gn.evenementialTags.entrySet()) {
                map_tags = addTag(map_tags, gnevenementialTags_list.getKey(), new Integer((int) gnevenementialTags_list.getValue() * Evenementielponderation));
            }
        }


        if (gn.mainstreamTags != null) {
            // chaque poids d'un tag mainstream du GN est pondéré à 40%
            for (Map.Entry<Tag, Integer> gnmainstreamTags_list : gn.mainstreamTags.entrySet()) {
                map_tags = addTag(map_tags, gnmainstreamTags_list.getKey(), new Integer((int) gnmainstreamTags_list.getValue() * Mainstreamponderation));
            }
        }

        // chaque poids d'un tag normal d'une intrigue est pondéré à 20%
        /*if (gn.selectedPlotSet != null) {
            Set<Plot> plotlist = gn.selectedPlotSet;
            for (Plot p : plotlist) {
                for (PlotHasTag tp : p.extTags) {
                    map_tags = addTag(map_tags, tp.tag, new Integer((int) tp.weight * plotponderation));
                }
            }
        }*/

        return map_tags;
    }

    /**
     * Initialize the list of tags with the weight of a character and a gn
     * @param GenericObject
     * @param gn
     * @return Map < Tag , Integer >
     */
    Map<Tag, Integer> initGenericObjectList(PersoForNaming character, Gn gn){
        if (VERBOSE){
        println("------------------------------------------------------")
        println("Char ".toUpperCase() + character.code)
        println("chara univers " + character.universe)
        println("GN name: " + gn.name)
        println("GN id: " + gn.id)
        println("GN tag list: " + gn.tagList)
        println("GN evenmentialTags: " + gn.evenementialTags)
        println("GN mainstreamTags: " + gn.mainstreamTags)
        println("GN univers tags: " + gn.getUnivers())
        println("GN tags: " + gn.getGnTags())
        println("GN firstnameSet: " + gn.firstnameSet)}

        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>()

        if (character.universe){
            map_tags.put(Tag.findWhere(name: character.universe), new Integer((int)100 * GenericObjectponderation))}

        ArrayList<TagRelation> relations = TagRelation.findAllWhere(tag1: Tag.findWhere(name: character.universe))
        if (relations){
            for (def r : relations)
                map_tags.put(r.tag2, (int)(r.weight * GNponderation))
        }

        if (!character.getTag().isEmpty()) {
            for (com.gnk.substitution.Tag t : character.getTag()) {
                map_tags.put(Tag.findWhere(name: t.value), t.weight * GenericObjectponderation)
            }
            if (VERBOSE){
            println("----")
            println("character tags:" )
            for (Map.Entry<Tag, Integer> entry_character : map_tags.entrySet())
            {
                println(" tag: " + entry_character.key.name + ", weight: " + entry_character.value)
            }
            print(System.lineSeparator())}
        }
        //else println(" PAS DE CHARACTER TAGS")
        // chaque poids d'un tag du GN est pondéré à 90%
        if (gn.gnTags != null) {
            for (Map.Entry<Tag, Integer> gnTags_list : gn.gnTags.entrySet()) {
                map_tags = addTag(map_tags, gnTags_list.getKey(), new Integer((int) gnTags_list.getValue() * GNponderation));
            }
            if (VERBOSE){
            println("----")
            println("gntags:" )
            for (Map.Entry<Tag, Integer> entry_character : map_tags.entrySet())
            {
                println(" tag: " + entry_character.key.name + ", weight: " + entry_character.value)
            }
            print(System.lineSeparator())}
        }
        //else println (" PAS DE GNTAGS")
        if (gn.getUnivers() != null) {
            map_tags = addTag(map_tags, gn.getUnivers(), gn.getUnivers().getWeight());
            println("----")
            println("getUnivers tags:" )
            for (Map.Entry<Tag, Integer> entry_character : map_tags.entrySet())
            {
                println(" tag: " + entry_character.key.name + ", weight: " + entry_character.value)
            }
            print(System.lineSeparator())
        }
        //else println (" PAS DE UNIVERS TAGS")
        if (gn.evenementialTags != null) {
            // chaque poids d'un tag evenementiel du GN est pondéré à 60%
            for (Map.Entry<Tag, Integer> gnevenementialTags_list : gn.evenementialTags.entrySet()) {
                map_tags = addTag(map_tags, gnevenementialTags_list.getKey(), new Integer((int) gnevenementialTags_list.getValue() * Evenementielponderation));
            }
            if (VERBOSE){
            println("----")
            println("evenemential tags:" )
            for (Map.Entry<Tag, Integer> entry_character : map_tags.entrySet())
            {
                println(" tag: " + entry_character.key.name + ", weight: " + entry_character.value)
            }
            print(System.lineSeparator())}
        }
        //else println (" PAS DE EVENEMENTIAL TAGS")
        if (gn.mainstreamTags != null) {
            // chaque poids d'un tag mainstream du GN est pondéré à 40%
            for (Map.Entry<Tag, Integer> gnmainstreamTags_list : gn.mainstreamTags.entrySet()) {
                map_tags = addTag(map_tags, gnmainstreamTags_list.getKey(), new Integer((int) gnmainstreamTags_list.getValue() * Mainstreamponderation));
            }
            if (VERBOSE){
            println("----")
            println("mainstream tags:" )
            for (Map.Entry<Tag, Integer> entry_character : map_tags.entrySet())
            {
                println(" tag: " + entry_character.key.name + ", weight: " + entry_character.value)
            }
            print(System.lineSeparator())}
        }
        //else println (" PAS DE MAINSTREAM TAGS")
        return map_tags
    }

    /**
     * Initialize the tags list of an object Place/resource
     * @param object
     * @return Map < Tag , Integer >
     */
    Map<Tag, Integer> initObjectList(ReferentialObject object) {

        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>();

        // récupérer les tags de l'objet
        map_tags.putAll(object.getTagsAndWeights(ReferentialObjectponderation));
        return map_tags;
    }

    /**
     * Initialize the tags list of an object Firstname
     * @param Firstname
     * @return Map < Tag , Integer >
     */
    Map<Tag, Integer> initObjectList(Firstname firstname){
        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>()
        Set<FirstnameHasTag> fnHasTags = firstname.getExtTags()

        if (fnHasTags != null) {
            for (FirstnameHasTag fnHasTag : fnHasTags) {
                map_tags.put(fnHasTag.getTag(), (int)(fnHasTag.getWeight() * ReferentialObjectponderation))
            }
        }
        return map_tags
    }

    /**
     * Initialize the tags list of an object Name
     * @param Name
     * @return Map < Tag , Integer >
     */
    Map<Tag, Integer> initObjectList(Name lastname){
        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>()
        Set<NameHasTag> nHasTags = lastname.getExtTags()

        if (nHasTags != null) {
            for (NameHasTag nHasTag : nHasTags) {
                map_tags.put(nHasTag.getTag(), (int)(nHasTag.getWeight() * ReferentialObjectponderation))
            }
        }
        return map_tags
    }

    /**
     * get tags with weights from an object Place/Resource witch is not generic
     * @param object
     * @return Map < Tag , Integer >
     */
    Map<Tag, Integer> getRelevantTags(Map<Tag, Integer> taglist) {


        Map<Tag, Integer> parents_tags = new HashMap<>();

        ArrayList<Tag> current_gen_parents = new ArrayList<>();
        current_gen_parents.addAll(taglist.keySet());

        ArrayList<Tag> next_gen_parents = new ArrayList<>();


        for (int gen = NumberOfGenerationsRelevant; gen--; gen > 0) {
            for (Tag t in current_gen_parents) {
                ArrayList<Tag> parent = TagRelation.findParents(t);
                for (Tag p in parent) {
                    next_gen_parents.add(p);
                    TagRelation tr = TagRelation.myFindWhere(t, p)
                    if (tr != null) {
                        parents_tags = addTag(parents_tags, p, computeFatherWeight(taglist.get(t), tr.getterWeight()));
                    } else {
                        tr = TagRelation.myFindWhere(p, t)
                        if (tr != null && tr.isBijective)
                            parents_tags = addTag(parents_tags, p, computeFatherWeight(taglist.get(t), tr.getterWeight()));
                    }
                }

            }
            current_gen_parents = next_gen_parents;
            next_gen_parents.clear();
        }

        return parents_tags;

    }

    /**
     *
     * @param tag
     * @param i
     * @param it
     * @return
     */
    Map<Tag, Integer> getRelevantTags(Tag tag, Integer i, Integer it) {
        return null;
    }

    /**
     * Doesn't include tags included in taglist
     * @param object
     * @return
     */
    Map<Tag, Integer> getParentTags(Map<Tag, Integer> taglist) {

        Map<Tag, Integer> parents_tags = new HashMap<>();

        ArrayList<Tag> current_gen_parents = new ArrayList<>();
        current_gen_parents.addAll(taglist.keySet());

        ArrayList<Tag> next_gen_parents = new ArrayList<>();


        for (int gen = NumberofGenerationsParent; gen--; gen > 0) {
            for (Tag t in current_gen_parents) {
                Tag parent = t.getParent();
                if (parent != null) {
                    next_gen_parents.add(parent);
                    parents_tags = addTag(parents_tags, parent, computeFatherWeightParent(taglist.get(t).intValue()));
                }
            }
            current_gen_parents = next_gen_parents;
            next_gen_parents.clear();
        }

        return parents_tags;
    }

    /**
     * Treatment that divide the cumulated weighting tag score when the tag is a universe tag.
     * @param tag
     * @param score
     * @param map_genericObject
     * @return the reduced tag score
     */
    Long tagUniversTreatment(Tag tag, Long score, Map<Tag, Integer> map_genericObject) {
        Long dividedNumber = map_genericObject.size() / 3;

        if ((tag.parentId == (long) IDgenericUniverTag) && (dividedNumber > 1))
            score *= dividedNumber;

        return score;
    }

    /**
     * Compute the cumulative score tag between 2 the Generic_Place list and Place list
     * @param tag
     * @param GPweight
     * @param Pweight
     * @return the weight
     */
    Long computeCumulativeScoreTags(Tag tag, Integer gPweight, Integer pweight) {
        long score = 0;

        if (tag.parent != null && tag.parent.getName() != null && tag.parent.getName() == "Tag Univers") {
            score = 50;
        }
        else {
            score = Math.abs(gPweight) + Math.abs(pweight);
            if (gPweight < 0 || pweight < 0)
                score *= -1;
        }
        return score;
    }

    /**
     * compute the father tag weight in function of ths son weight and the level of relationship with him.
     * @param sonWeight
     * @param relationWeight
     * @return The computed relationship score.
     */
    Integer computeFatherWeight(Integer sonWeight, Integer relationWeight)  {
        Integer result = sonWeight * relationWeight / 100;

        if (result < -100)
            result = -100;

        if (result > 100)
            result = 100;

        return result;
    }


    Integer computeFatherWeightParent(Integer sonWeight) {
        Integer result = sonWeight * PonderationParent;

        if (result < -100)
            result = -100;

        if (result > 100)
            result = 100;

        return result;
    }

    /**
     * Add a Tag/integer pair in a map with managing the case if the pair already exists (keep the bigger absolute value)
     * @param map
     * @param tag
     * @param integer
     * @return The map modified or not.
     */
    private Map<Tag, Integer> addTag(Map<Tag, Integer> map, Tag tag, Integer integer) {
        Integer testValue = map.get(tag);

        if (testValue == null)
            map.put(tag, integer);
        else {
                //map.put(tag, (Integer)((integer.intValue() * testValue.intValue()) /2));
                map.put(tag, (Integer)(integer.intValue() > testValue.intValue() ? integer.intValue() * 1.5 : testValue.intValue() * 1.5));
        }

        return  map;
    }
    
    //Ne pas supprimer, car contient les print pour tester les tags de tous les éléments
    /*
    Long computeComparativeScoreObject(PersoForNaming character, Firstname firstname, Gn gn) {

        int totalNumberOfTagsUsed = 0;
        int result = 0;

        Map<Tag, Integer> map_character = initGenericObjectList(character, gn)
        map_character.putAll(getRelevantTags(map_character))
        map_character.putAll(getParentTags(map_character))


        long startTime = System.nanoTime();
        Map<Tag, Integer> map_firstname = initObjectList(firstname)
        map_firstname.putAll(getRelevantTags(map_firstname))
        map_firstname.putAll(getParentTags(map_firstname))
        long endTime = System.nanoTime();
        println("initObjectList duration: " + ((endTime - startTime) / 1000000000.0) + " seconds")

        Long score = 0;

        for (Map.Entry<Tag, Integer> entry_character : map_character.entrySet()) {
            for (Map.Entry<Tag, Integer> entry_firstname : map_firstname.entrySet()) {
                if (entry_character.getKey().getId() == null) {
                    if (entry_character.getKey().value_substitution == (entry_firstname.getKey().getName())) {
                        score += computeCumulativeScoreTags(entry_character.getKey(), entry_character.getValue(), entry_firstname.getValue());
                        totalNumberOfTagsUsed += 1;
                    }
                } else {
                    if (entry_character.getKey().getId()== (entry_firstname.getKey().getId())) {
                        score += computeCumulativeScoreTags(entry_character.getKey(), entry_character.getValue(), entry_firstname.getValue());
                        totalNumberOfTagsUsed += 1
                    }
                }
            }
        }

        result = totalNumberOfTagsUsed == 0 ? score : (score /totalNumberOfTagsUsed);


        if (VERBOSE){
        print("------------------------------------------------------")
        println("Tags du character number ".toUpperCase() + character.code + ": " )
        for (Map.Entry<Tag, Integer> entry_character : map_character.entrySet())
        {
            println(" tag: " + entry_character.key.name + ", weight: " + entry_character.value)
        }
        print(System.lineSeparator())
        println("Tags du firstname: ".toUpperCase() + firstname.name.toUpperCase() )
        for (Map.Entry<Tag, Integer> entry_firstname : map_firstname.entrySet())
        {
            println(" tag: " + entry_firstname.key.name + ", weight: " + entry_firstname.value)
        }
        println("Ranktag = " + result)
        print(System.lineSeparator())}

        return result
    }*/

}
