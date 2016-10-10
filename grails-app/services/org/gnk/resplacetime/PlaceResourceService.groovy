package org.gnk.resplacetime

import javafx.util.Pair
import org.gnk.gn.Gn
import org.gnk.selectintrigue.Plot
import org.gnk.tag.TagServiceV2
import org.gnk.utils.Tag

class PlaceResourceService {

    TagServiceV2 tagservice;

    // retourne la liste triée des meilleurs objects qui pourront subtituer au generic object
    ArrayList<Pair<Object, Integer>> findBestObjects (GenericObject GenericObject , Gn gn) {


        // liste contenant les objets et leurs scores par rapport au GenericObject
        ArrayList<Pair<Object, Integer>> sorted_list = new ArrayList<>();

        // on récupère la liste des places/resources et leurs scores
        List<ReferentialObject> all_places = GenericObject instanceof GenericPlace ? Place.all : Resource.all;
        for (ReferentialObject p : all_places) {
            sorted_list.add(new Pair<ReferentialObject, Integer>(p, new Integer((int)tagservice.computeComparativeScoreObject(GenericObject, p, gn))))
        }


        // on trie la sorted_list en fonction du poids de l'object
        Collections.sort(sorted_list , new Comparator<Pair<ReferentialObject, Integer>>() {
            @Override
            public int compare(final Pair<ReferentialObject, Integer> o1, final Pair<ReferentialObject, Integer> o2) {
                if (o1.value > o2.value)
                    return 1;
                else
                    return 0;
            }
        });

        return sorted_list;
    }

    ArrayList<Pair<Tag, ArrayList<Pair<Object, Integer>>>> findBestPlacesForAllUnivers (GenericObject GenericObject, Plot plot) {
        return null;
    }
}
