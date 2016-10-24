package org.gnk.resplacetime

import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.utils.Pair
import org.gnk.gn.Gn
import org.gnk.selectintrigue.Plot
import org.gnk.tag.TagServiceV2
import org.gnk.tag.Tag

class PlaceResourceService {

    TagServiceV2 tagservice;
    private static int IDgenericUniverTag = 33089;

    /**
     *
     * @param gn
     * @param genericObject
     * @return
     */
    Gn reset(Gn gn, GenericObject genericObject) {
//        Gn updatedGn = gn;

//        updatedGn.get

        return updatedGn;
    }

    /**
     *
     * @param listObject
     * @param genericObject
     * @param gn
     * @return
     */
    ArrayList<Object> removeSameObjects(ArrayList<Object> listObject, GenericObject genericObject, Gn gn) {

        ArrayList<Object> lastExecList = new ArrayList<>();
        return lastExecList;
    }

    /**
     *
     * @param listObject
     * @param genericObject
     * @return
     */
    ArrayList<Object> raiseLockedObject(ArrayList<Object> listObject, GenericObject genericObject) {
        ArrayList<Object> newListObject = new ArrayList<>();

 /*       for (Object object in listObject) {
            if (object.
        }
*/

            return newListObject;
    }

    // retourne la liste triée des meilleurs objects qui pourront subtituer au generic object
    ArrayList<Pair<Object, Integer>> findBestObjects(GenericObject GenericObject, Gn gn) {

        // liste contenant les objets et leurs scores par rapport au GenericObject
        ArrayList<Pair<Object, Integer>> sorted_list = new ArrayList<>();

        // on récupère la liste des places/resources et leurs scores
        List<ReferentialObject> all_object = genericObject.getReferentialObject();
        for (ReferentialObject p : all_object) {
            sorted_list.add(new Pair<ReferentialObject, Integer>(p, new Integer((int) tagservice.computeComparativeScoreObject(genericObject, p, gn))))
        }

        // on trie la sorted_list en fonction du poids de l'object
        Collections.sort(sorted_list, new Comparator<Pair<ReferentialObject, Integer>>() {
            @Override
            public int compare(final Pair<ReferentialObject, Integer> o1, final Pair<ReferentialObject, Integer> o2) {
                if (o1.value > o2.value)
                    return 1;
                else
                    return 0;
            }
        });

        //removeSameObjects(sorted_list, genericObject,gn )
        //raiseLockedObject(sorted_list, genericObject)

        return sorted_list;
    }

    ArrayList<Pair<Tag, ArrayList<Pair<Object, Integer>>>> findBestObjectsForAllUnivers(GenericObject genericObject, Plot plot) {

        // on réucpère l'ensemble des tagsunivers dans la liste UniverListTag
        ArrayList<Tag> UniverListTag = new ArrayList<Tag>();
        Tag genericUnivers = Tag.findById(IDgenericUniverTag);
        UniverListTag.addAll(Tag.findAllByParent(genericUnivers));

        // liste qui contiendra l'ensemble des résultats cherchés
        ArrayList<Pair<Tag, ArrayList<Pair<Object, Integer>>>> all_objects = new ArrayList<>();

        // On boucle sur les tags Univers en créant des GN avec le plot défini puis on appelle findBestObject, et on insère le résultat dans all_objects
        for (Tag t in UniverListTag) {
            Gn gn = new Gn();
            gn.addPlot(plot);
            gn.setUnivers(t);
            all_objects.addAll(findBestObjects(genericObject, gn));
        }

        return all_objects;
    }

}
