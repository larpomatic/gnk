package org.gnk.tag

import org.gnk.gn.Gn
import org.gnk.resplacetime.GenericPlace
import org.gnk.selectintrigue.Plot

class TagServiceV2 {

    Tag getUniver(Gn gn) {
        return gn.getUnivers();
    }

    Long computeComparativeScoreObject(Object GenericObject, Object Object, Gn gn) {

        Map<Tag, Integer> map_genericObject = initGenericObjectList(GenericObject, gn);
        map_genericObject.putAll(getRelevantTags(GenericObject));

        Map<Tag, Integer> map_Object = initObjectList(Object);
        map_Object.putAll(getRelevantTags(Object));


        Long score = 0;

        for (Map.Entry<Tag, Integer> entry_generic : map_genericObject.entrySet()) {
            for (Map.Entry<Tag, Integer> entry : map_genericObject.entrySet())
            {
                if (entry_generic.getKey().getId().equals(entry_generic.getKey().getId())) {
                    score += computeCumulativeScoreTags(entry_generic.getKey(), entry_generic.getValue(), entry.getValue());

                }
            }

        }

        return score;
    }

    Map<Tag, Integer> initGenericObjectList(Object GenericObject, Gn gn) {

        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>();

        //récupérer les tags du genericobjet
        map_tags.putAll(getRelevantTags(GenericObject));

        //recupérer les tags du gn
        // chaque poids d'un tag du GN est pondéré à 90%
        for (Map.Entry<Tag, Integer> gnTags_list : gn.gnTags.entrySet()) {
            map_tags.put(gnTags_list.getKey(), new Integer((int)gnTags_list.getValue() * 0.9 ));
        }

        // chaque poids d'un tag evenementiel du GN est pondéré à 60%
        for (Map.Entry<Tag, Integer> gnevenementialTags_list : gn.evenementialTags.entrySet()) {
            map_tags.put(gnevenementialTags_list.getKey(), new Integer((int)gnevenementialTags_list.getValue() * 0.6));
        }

        // chaque poids d'un tag mainstream du GN est pondéré à 40%
        for (Map.Entry<Tag, Integer> gnmainstreamTags_list : gn.mainstreamTags.entrySet()) {
            map_tags.put(gnmainstreamTags_list.getKey(), new Integer((int)gnmainstreamTags_list.getValue() * 0.4));
        }


        // récupérer les tags de l'intrigue

        return map_tags;
    }

    Map<Tag, Integer> initObjectList(Object object) {

        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>();

        // récupérer les tags de l'objet
        map_tags.putAll(getRelevantTags(object));
        return map_tags;
    }

    Map<Tag, Integer> getRelevantTags(Object) {
       //récupérer les tags de l'objet dans la base et les stocker dans une map
        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>();
        for (Map.Entry<Tag, Integer> entry : map_tags.entrySet()) {
            map_tags.putAll(getRelevantTags(entry.getKey(), entry.getValue(), ))
        }
        return map_tags;
    }

    Map<Tag, Integer> getRelevantTags(Tag tag, Integer i , Integer it) {
        return null;
    }

    Long computeCumulativeScoreTags(Tag tag, Integer i , Integer ii) {
        return null;
        
    }

    Integer computeFatherWeight (Integer sonWeight, Integer relationWeight) {

    }


}
