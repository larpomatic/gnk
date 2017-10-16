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
     * Calculate the total score of similitude between a Character and a Firstname
     * @param PersoForNaming
     * @param Firstname
     * @param Gn
     * @return the calculus in Long
     */
    Long computeComparativeScoreObject(PersoForNaming character, Firstname firstname, Gn gn) {

        Map<Tag, Integer> initial_map_tags = new HashMap<Tag, Integer>()
        for (com.gnk.substitution.Tag t : character.getTag()) {
            initial_map_tags.put(Tag.findWhere(name: t.value), t.weight * GenericObjectponderation)
        }
        int totalNumberOfTagsUsed = 0;
        int result = 0;
        // initialisation des tags du character
        Map<Tag, Integer> map_character = initGenericObjectList(character, gn)

        //récupérer les tags relevants du character
        map_character.putAll(getRelevantTags(initial_map_tags))

        //initialisation des tags du firstname
        Map<Tag, Integer> map_firstname = initObjectList(firstname)

        //récupérer les tags parents
        map_character.putAll(getParentTags(initial_map_tags))

        //récupérer les tags relevants du firstname
        map_firstname.putAll(getRelevantTags(map_firstname))
        //récupérer les tags parents du referential
        map_firstname.putAll(getParentTags(initObjectList(firstname)))

        Long score = 0;

        for (Map.Entry<Tag, Integer> entry_character : map_character.entrySet()) {
            for (Map.Entry<Tag, Integer> entry_firstname : map_firstname.entrySet()) {
                if (entry_character.getKey().getId() == null) {
                    print("ok1")
                    if (entry_character.getKey().value_substitution == (entry_firstname.getKey().getName())) {
                        print("ok2")
                        score += computeCumulativeScoreTags(entry_character.getKey(), entry_character.getValue(), entry_firstname.getValue());
                        //score = tagUniversTreatment(entry_character.getKey(), score, map_character);
                        totalNumberOfTagsUsed += 1;
                        println ("entry_character.getKey().value_substitution == (entry_firstname.getKey().getName())")
                    }
                } else {
                    println ("ok3") // entry_character.getKey().getId() != null
                    println ("entry_character.getKey().getId() = " + entry_character.getKey().getId() +
                    ", entry_firstname.getKey().getId() =" + entry_firstname.getKey().getId())
                    if (entry_character.getKey().getId()== (entry_firstname.getKey().getId())) {
                        println ("ok4")
                        score += computeCumulativeScoreTags(entry_character.getKey(), entry_character.getValue(), entry_firstname.getValue());
                        //score = tagUniversTreatment(entry_character.getKey(), score, map_character);
                        totalNumberOfTagsUsed += 1;
                    }
                }
            }
        }

        //println ("totalNumberOfTagsUsed = " + totalNumberOfTagsUsed + ", score = " + score)
        result = totalNumberOfTagsUsed == 0 ? score : (score /totalNumberOfTagsUsed);
        return result;
    }

    /**
     * Calculate the total score of similitude between a Character and a Name
     * @param PersoForNaming
     * @param Name
     * @param Gn
     * @return the calculus in Long
     */
    Long computeComparativeScoreObject(PersoForNaming character, Name name, Gn gn) {

        Map<Tag, Integer> initial_map_tags = new HashMap<Tag, Integer>()
        for (com.gnk.substitution.Tag t : character.getTag()) {
            initial_map_tags.put(Tag.findWhere(name: t.value), t.weight * GenericObjectponderation)
        }
        int totalNumberOfTagsUsed = 0;
        int result = 0;
        // initialisation des tags du character
        Map<Tag, Integer> map_character = initGenericObjectList(character, gn)

        //récupérer les tags relevants du character
        map_character.putAll(getRelevantTags(initial_map_tags))

        //initialisation des tags du name
        Map<Tag, Integer> map_name = initObjectList(name)

        //récupérer les tags parents
        map_character.putAll(getParentTags(initial_map_tags))

        //récupérer les tags relevants du firstname
        map_name.putAll(getRelevantTags(map_name))
        //récupérer les tags parents du referential
        map_name.putAll(getParentTags(initObjectList(name)))

        Long score = 0;

        for (Map.Entry<Tag, Integer> entry_generic : map_character.entrySet()) {
            for (Map.Entry<Tag, Integer> entry : map_name.entrySet()) {
                if (entry_generic.getKey().getId() == null) {
                    if (entry_generic.getKey().value_substitution.equals(entry.getKey().getName()) /*|| entry_generic.getKey().type_substitution.equals(entry.getKey().getName())*/) {
                        score += computeCumulativeScoreTags(entry_generic.getKey(), entry_generic.getValue(), entry.getValue());
                        //score = tagUniversTreatment(entry_generic.getKey(), score, map_character);
                        totalNumberOfTagsUsed += 1;
                    }
                } else {
                    if (entry_generic.getKey().getId().equals(entry.getKey().getId())) {
                        score += computeCumulativeScoreTags(entry_generic.getKey(), entry_generic.getValue(), entry.getValue());
                        //score = tagUniversTreatment(entry_generic.getKey(), score, map_character);
                        totalNumberOfTagsUsed += 1;
                    }
                }
            }
        }

        result = totalNumberOfTagsUsed == 0 ? score : (score /totalNumberOfTagsUsed);
        return result;
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
        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>()
        for (com.gnk.substitution.Tag t : character.getTag()) {
            map_tags.put(Tag.findWhere(name: t.value), t.weight * GenericObjectponderation)
        }


        // RECUPERER LES TAGS DU GN

        // chaque poids d'un tag du GN est pondéré à 90%
        if (gn.gnTags != null) {
            for (Map.Entry<Tag, Integer> gnTags_list : gn.gnTags.entrySet()) {
                map_tags = addTag(map_tags, gnTags_list.getKey(), new Integer((int) gnTags_list.getValue() * GNponderation));
            }
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
                map_tags.put(fnHasTag.getTag(), fnHasTag.getWeight() * ReferentialObjectponderation)
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
                map_tags.put(nHasTag.getTag(), nHasTag.getWeight() * ReferentialObjectponderation)
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

        if (tag.parent != null && tag.parent.getName() != null && tag.parent.getName().equals("Tag Univers")) {
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
}
