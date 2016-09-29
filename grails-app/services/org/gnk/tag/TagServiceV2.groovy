package org.gnk.tag

import org.gnk.gn.Gn
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.Resource
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag

class TagServiceV2 {

    private static int IDgenericUniverTag = 33089;


    /**
     * get the universe from the gn
     * @param gn
     * @return tag of the universe
     */
    Tag getUniver(Gn gn) {
        return gn.getUnivers();
    }

    def findChildren(Tag t) {
        def tags = Tag.findAllWhere(parent: t)
        def tagsTmp = new ArrayList()
        tagsTmp.addAll(tags)
        if (tagsTmp == null)
            return tags
        for (Tag tag : tagsTmp) {
            tags.addAll(findChildren(tag))
        }
        return tags
    }

    /**
     * get all the univers
     * @return list of all the univers
     */
    ArrayList<Tag> getUnivers() {
        ArrayList<Tag> UniverListTag = new ArrayList<Tag>();
        Tag genericUnivers = Tag.findById(IDgenericUniverTag);

        UniverListTag = Tag.findAllByParent(parent: genericUnivers);
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
     * @param gn
     * @return the calculus in Long
     */
    Long computeComparativeScoreObject(Object genericObject, Object object, Gn gn) {

        Map<Tag, Integer> map_genericObject = initGenericObjectList(genericObject, gn);

        Map<Tag, Integer> map_Object = initObjectList(object);


        Long score = 0;


        for (Map.Entry<Tag, Integer> entry_generic : map_genericObject.entrySet()) {
            for (Map.Entry<Tag, Integer> entry : map_Object.entrySet())
            {
                if (entry_generic.getKey().getId().equals(entry.getKey().getId())) {
                    score += computeCumulativeScoreTags(entry_generic.getKey(), entry_generic.getValue(), entry.getValue());
                    score = tagUniversTreatment(entry_generic.getKey(), score, map_genericObject);
                }
            }
        }

        return score;
    }

    /**
     * Initialize the list of tags with the weight of GenericObject Place/Resource and a gn
     * @param GenericObject
     * @param gn
     * @return Map<Tag, Integer>
     */
    Map<Tag, Integer> initGenericObjectList(Object GenericObject, Gn gn) {

        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>();

        //récupérer les tags du genericobjet
        map_tags.putAll(getRelevantTags(GenericObject));

        //recupérer les tags du gn
        // chaque poids d'un tag du GN est pondéré à 90%
        for (Map.Entry<Tag, Integer> gnTags_list : gn.gnTags.entrySet()) {
            map_tags.put(gnTags_list.getKey(), new Integer((int) gnTags_list.getValue() * 0.9));
        }

        // chaque poids d'un tag evenementiel du GN est pondéré à 60%
        for (Map.Entry<Tag, Integer> gnevenementialTags_list : gn.evenementialTags.entrySet()) {
            map_tags.put(gnevenementialTags_list.getKey(), new Integer((int) gnevenementialTags_list.getValue() * 0.6));
        }

        // chaque poids d'un tag mainstream du GN est pondéré à 40%
        for (Map.Entry<Tag, Integer> gnmainstreamTags_list : gn.mainstreamTags.entrySet()) {
            map_tags.put(gnmainstreamTags_list.getKey(), new Integer((int) gnmainstreamTags_list.getValue() * 0.4));
        }

        // chaque poids d'un tag normal d'une intrigue est pondéré à 20%
        Set<Plot> plotlist = gn.selectedPlotSet;
        for (Plot p : plotlist) {
             for (PlotHasTag tp : p.plotHasTag) {
                 map_tags.put(tp.tag, new Integer((int)tp.weight * 0.2));
             }
        }

        return map_tags;
    }

    /**
     * Initialize the tags list of an object Place/resource
     * @param object
     * @return Map<Tag, Integer>
     */
    Map<Tag, Integer> initObjectList(Object object) {

        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>();

        // récupérer les tags de l'objet
        map_tags.putAll(getRelevantTags(object));
        return map_tags;
    }

    /**
     * get tags with weights from an object Place/Resource witch is generic or not
     * @param object
     * @return Map<Tag, Integer>
     */
    Map<Tag, Integer> getRelevantTags(Object object) {

        //récupérer les tags de l'objet dans la base et les stocker dans une map
        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>();

        //si generic resource
        if (object instanceof GenericResource) {

        }

        //si generic place
        if (object instanceof GenericPlace) {

        }

        // si resource
        if (object instanceof Resource) {

        }

        // si place
        if (object instanceof Place) {

        }


        return map_tags;
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
     *
     * @param object
     * @return
     */
    Map<Tag, Integer> getParentTags(Object object) {
        def tags = org.gnk.tag.Tag.findAllWhere(parent: object)
        return tags;
    }

    /**
     * Treatment that divide the cumulated weighting tag score when the tag is a universe tag.
     * @param tag
     * @param score
     * @param map_genericObject
     * @return the reduced tag score
     */
    int tagUniversTreatment(Tag tag, Long score, Map<Tag, Integer> map_genericObject){
        int dividedNumber = map_genericObject.size() / 3;

        if (tag.name.toLowerCase().contains("Univers".toLowerCase()))
            if (dividedNumber > 1)
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
    Long computeCumulativeScoreTags(Tag tag, Integer GPweight, Integer Pweight)  {
        long score = 0;

        score = Math.abs(GPweight) + Math.abs(Pweight);
        if (GPweight*Pweight == -1)
            score *= -1;

        return score;
    }

    // Retourne le poids d’un Tag père en fonction du poids de son fils et de la force de sa relation avec lui.

    /**
     * compute the father tag weight in function of ths son weight and the level of relationship with him.
     * @param sonWeight
     * @param relationWeight
     * @return The computed relationship score.
     */
    Integer computeFatherWeight(Integer sonWeight, Integer relationWeight) {
        Integer result = sonWeight * relationWeight / 100;

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
    private Map<Tag, Integer> addTag (Map<Tag, Integer> map, Tag tag, Integer integer) {
        Integer testValue = map.get(tag);

        if (testValue) {
            if (Math.abs(testValue) < Math.abs(integer))
                map.put(tag, integer);
            else
                return;
        } else
            map.put(tag, integer);
    }

}
