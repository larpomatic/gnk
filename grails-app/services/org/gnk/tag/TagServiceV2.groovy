package org.gnk.tag

import org.gnk.gn.Gn
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.Resource
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag

class TagServiceV2 {

    // retourne le tag univers d'un GN
    Tag getUniver(Gn gn) {
        return gn.getUnivers();
    }


    ArrayList<Tag> getUnivers() {
        ArrayList<Tag> tagUniversList = new ArrayList<Tag>();
        Tag genericUnivers = Tag.findByName("Tag Univers");

        for (Tag child in genericUnivers.children) {
            tagUniversList.add(child);
        }
       // Collections.sort(tagUniversList, new ComparateurTag())

        return tagUniversList;
    }


    // calcule le score total de similarité entre un GenericObjet et un Objet
    Long computeComparativeScoreObject(Object GenericObject, Object Object, Gn gn) {

        Map<Tag, Integer> map_genericObject = initGenericObjectList(GenericObject, gn);
        //map_genericObject.putAll(getRelevantTags(GenericObject));

        Map<Tag, Integer> map_Object = initObjectList(Object);
        //map_Object.putAll(getRelevantTags(Object));


        Long score = 0;


        for (Map.Entry<Tag, Integer> entry_generic : map_genericObject.entrySet()) {
            for (Map.Entry<Tag, Integer> entry : map_genericObject.entrySet())
            {
                if (entry_generic.getKey().getId().equals(entry.getKey().getId())) {
                    score += computeCumulativeScoreTags(entry_generic.getKey(), entry_generic.getValue(), entry.getValue());
                    score = tagUniversTreatment(entry_generic.getKey(), score, map_genericObject);
                }
            }
        }

        return score;
    }

    //initialise la liste de tag avec poids d'un objet générique place/resource et du GN
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

        // chaque poids d'un tan normal du GN est pondéré à 40%

        // récupérer les tags de l'intrigue
        Set<Plot> plotlist = gn.selectedPlotSet;
        for (Plot p : plotlist) {
             for (PlotHasTag tp : p.plotHasTag) {
                 map_tags.put(tp.tag, tp.weight);
             }
        }

        return map_tags;
    }

    // initialise la liste de tag d'un objet place/resource
    Map<Tag, Integer> initObjectList(Object object) {

        Map<Tag, Integer> map_tags = new HashMap<Tag, Integer>();

        // récupérer les tags de l'objet
        map_tags.putAll(getRelevantTags(object));
        return map_tags;
    }

    // récupère les tags avec poids d'un objet place/resource, générique ou non
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


    Map<Tag, Integer> getRelevantTags(Tag tag, Integer i, Integer it) {
        return null;
    }


    Map<Tag, Integer> getParentTags(Object object) {
        def tags = org.gnk.tag.Tag.findAllWhere(parent: object)
        return tags;
    }


    /** fonction qui permet de regler le probleme d'acces a Generic_Place
      * pour le traitement de la ponderation cumulee du tag réalisé dans
      * computeCumulativeScoreTags dans laquelle on doit faire un traitement special si
      * le tag est un tag univers.
      */

    int tagUniversTreatment(Tag tag, Long score, Map<Tag, Integer> map_genericObject){
        int dividingNumber = map_genericObject.size() / 3;

        if (tag.name.toLowerCase().contains("Univers".toLowerCase()))
            if (dividingNumber > 1)
                score *= dividingNumber;

            return score;
    }

    /**
     * Retourne le score de comparaison d’un tag existant dans les 2 listes
     * @param tag : le tag en question
     * @param GPweight : le poids de la liste de Generic_Place
     * @param Pweight : le poids de la liste de Place
     */
    Long computeCumulativeScoreTags(Tag tag, Integer GPweight, Integer Pweight)  {
        long score = 0;

        score = Math.abs(GPweight) + Math.abs(Pweight);
        if (GPweight*Pweight == -1)
            score *= -1;

        return score;
    }

    Integer computeFatherWeight(Integer sonWeight, Integer relationWeight) {
        Integer 
    }


}
