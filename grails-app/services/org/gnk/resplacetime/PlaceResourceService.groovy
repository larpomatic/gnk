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

    // Il faut trouver le GenericObject dans le GN en passant par la plot.
    // Puis vider les résultats d’une précédent exécution du Service.
    // On verra en fonction du besoin, il sera peut-être nécessaire d’ajouter une fonction resetAll(Gn)

    Gn reset(Gn gn, GenericObject genericObject) {
        Gn updatedGn = gn;
//        Plot plotFromGenObj = genericObject.getPlot();

        updatedGn.selectedPlotSet.clear();
        updatedGn.characterSet.clear();
        updatedGn.nonPlayerCharSet.clear();
        updatedGn.characterSet.clear();


        return updatedGn;
    }

    /**
     *
     * @param listObject
     * @param genericObject
     * @param gn
     * @return
     */
    ArrayList<Object> removeSameObjects(ArrayList<GenericObject> listObject, Plot plot) {

        ArrayList<GenericObject> plLis = new ArrayList();
        if (listObject[0].getSubType().equals("genericRessource"))
            plLis = plot.getGenericResources()
        else
            plLis = plot.getGenericPlaces()

        for (GenericObject go in plLis) {
            if (listObject.contains(go)) {
                listObject.remove(go);
                listObject.add(listObject.size() - 1, go);
            }
        }
        return listObject;
    }

    /**
     *
     * @param listObject
     * @param genericObject
     * @return
     */
    ArrayList<Object> raiseLockedObject(ArrayList<Object> listObject, GenericObject genericObject) {

        if (listObject.contains(genericObject.getLockedObject())) {
            listObject.remove(genericObject.getLockedObject());
        }
        listObject.add(0, genericObject.getLockedObject());

        return listObject;
    }

    // retourne la liste triée des meilleurs objects qui pourront subtituer au generic object
    ArrayList<Pair<Object, Integer>> findBestObjects(GenericObject GenericObject, Gn gn) {

        // liste contenant les objets et leurs scores par rapport au GenericObject
        ArrayList<Pair<Object, Integer>> sorted_list = new ArrayList<>();

        // on récupère la liste des places/resources et leurs scores
        List<ReferentialObject> all_object = GenericObject.getReferentialObject();
        for (ReferentialObject p : all_object) {
            sorted_list.add(new Pair<ReferentialObject, Integer>(p, new Integer((int) tagservice.computeComparativeScoreObject(GenericObject, p, gn))))
        }

        // on trie la sorted_list en fonction du poids de l'object
        Collections.sort(sorted_list, new Comparator<Pair<ReferentialObject, Integer>>() {
            @Override
            public int compare(final Pair<ReferentialObject, Integer> o1, final Pair<ReferentialObject, Integer> o2) {
                if (o1.right.intValue() > o2.right.intValue())
                    return 1;
                else
                    return 0;
            }
        });

        removeSameObjects(sorted_list, GenericObject, gn)
        raiseLockedObject(sorted_list, GenericObject)

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
