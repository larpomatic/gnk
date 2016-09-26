package org.gnk.resplacetime

import javafx.util.Pair
import org.gnk.gn.Gn
import org.gnk.selectintrigue.Plot
import org.gnk.tag.TagServiceV2
import org.gnk.utils.Tag

class PlaceResourceService {

    TagServiceV2 tagservice;

    // retourne la liste triée des meilleurs objects qui pourront subtituer au generic object
    ArrayList<Pair<Object, Integer>> findBestObjects (Object GenericObject , Gn gn) {


        // liste contenant les objets et leurs scores par rapport au GenericObject
        ArrayList<Pair<Object, Integer>> sorted_list = new ArrayList<>();

        // on récupère la liste des places et leurs scores si le generic object est une generic place
        if (GenericObject instanceof GenericPlace) {
            List<Place> all_places = Place.all;
            for (Place p : all_places) {
                sorted_list.add(new Pair<Place, Integer>(p, new Integer((int)tagservice.computeComparativeScoreObject(GenericObject, p, gn))))
            }

        }

        // on récupère la liste des resources et leurs scores si le generic object est une generic resource
        if (GenericObject instanceof GenericResource) {
            List<Resource> all_resources = Resource.all;
            for (Resource r : all_resources) {
                sorted_list.add(new Pair<Resource, Integer>(r, new Integer((int)tagservice.computeComparativeScoreObject(GenericObject, r, gn))))
            }
        }

        // on trie la sorted_list en fonction du poids de l'object
        Collections.sort(sorted_list , new Comparator<Pair<Object, Integer>>() {
            @Override
            public int compare(final Pair<Object, Integer> o1, final Pair<Object, Integer> o2) {
                if (o1.value > o2.value)
                    return 1;
                else
                    return 0;
            }
        });

        return sorted_list;
    }

    ArrayList<Pair<Tag, ArrayList<Pair<Object, Integer>>>> findBestPlacesForAllUnivers (Object GenericObject, Plot plot) {
        return null;
    }
}
