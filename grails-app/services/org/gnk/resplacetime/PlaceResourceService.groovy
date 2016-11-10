package org.gnk.resplacetime

import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.utils.Pair
import org.gnk.gn.Gn
import org.gnk.selectintrigue.Plot
import org.gnk.tag.TagServiceV2
import org.gnk.tag.Tag

public class PlaceResourceService {

    TagServiceV2 tagServiceV2;
    private static int IDgenericUniverTag = 33089;

    PlaceResourceService() {
    }
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
//        Plot plotFromGenObj = genericObject.getPlot();

        for (Plot p in gn.getSelectedPlotSet()) {
            for (GenericPlace gp in p.getGenericPlaces()) {
                gp.setSelectedPlace(null)
                gp.setProposedPlaces(new ArrayList<Place>())
            }

            for (GenericResource gr in p.getGenericResources()) {
                gr.setSelectedResource(null);
                gr.setProposedResources(new ArrayList<Resource>())
            }
        }

        return gn;
    }

    /**
     *
     * @param listObject
     * @param genericObject
     * @param gn
     * @return
     */
    ArrayList<Pair<ReferentialObject, Integer>> removeSameObjects(ArrayList<Pair<ReferentialObject, Integer>> listObject, Plot plot) {

        ArrayList<ReferentialObject> plLis = new ArrayList();
        if (listObject[0].left.getSubType().equals("Ressource")) {
            for (GenericResource gr in plot.getGenericResources()) {
                if (gr.selectedResource != null) {
                    plLis.add(gr.selectedResource)
                }
            }
        }
        else {
            for (GenericPlace gp in plot.getGenericPlaces()) {
                if (gp.selectedPlace != null) {
                    plLis.add(gp.selectedPlace)
                }
            }
        }

        ArrayList<Pair<ReferentialObject, Integer>> tmp = listObject.clone()

        for (Pair<ReferentialObject, Integer> pa in tmp) {
            if (plLis.contains(pa.left)) {
                listObject.remove(pa);
                listObject.add(listObject.size() - 1, pa);
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
    ArrayList<Pair<ReferentialObject, Integer>> raiseLockedObject(ArrayList<Pair<ReferentialObject, Integer>> listObject, GenericObject genericObject) {

        if (genericObject.getLockedObject() != null) {
            Integer i = new Integer(9999);
            for (Pair<ReferentialObject, Integer> pa in listObject) {
                if (pa.left.equals(genericObject.getLockedObject())) {
                    listObject.remove(pa);
                    i = pa.right;
                    break;
                }
            }
            listObject.add(0, new Pair<ReferentialObject, Integer>(genericObject.getLockedObject(), i))
        }
        return listObject;
    }

    // retourne la liste triée des meilleurs objects qui pourront subtituer au generic object
    ArrayList<Pair<ReferentialObject, Integer>> findBestObjects(GenericObject GenericObject, Gn gn) {

        // liste contenant les objets et leurs scores par rapport au GenericObject
        ArrayList<Pair<ReferentialObject, Integer>> sorted_list = new ArrayList<>();

        // on récupère la liste des places/resources et leurs scores
        List<ReferentialObject> all_object = GenericObject.getReferentialObject();
        for (ReferentialObject p : all_object) {
            sorted_list.add(new Pair<ReferentialObject, Integer>(p, new Integer((int) tagServiceV2.computeComparativeScoreObject(GenericObject, p, gn))))
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

        sorted_list = removeSameObjects(sorted_list, GenericObject, gn)
        sorted_list = raiseLockedObject(sorted_list, GenericObject)

        return sorted_list;
    }

    ArrayList<Pair<Tag, ArrayList<Pair<ReferentialObject, Integer>>>> findBestObjectsForAllUnivers(GenericObject genericObject, Plot plot) {

        // on réucpère l'ensemble des tagsunivers dans la liste UniverListTag
        ArrayList<Tag> univerListTag = new ArrayList<Tag>();
        Tag genericUnivers = Tag.findById(IDgenericUniverTag);
        univerListTag.addAll(Tag.findAllByParent(genericUnivers));

        // liste qui contiendra l'ensemble des résultats cherchés
        ArrayList<Pair<Tag, ArrayList<Pair<ReferentialObject, Integer>>>> all_objects = new ArrayList<>();

        // On boucle sur les tags Univers en créant des GN avec le plot défini puis on appelle findBestObject, et on insère le résultat dans all_objects
        for (Tag t in univerListTag) {
            Gn gn = new Gn();
            gn.addPlot(plot);
            gn.setUnivers(t);
            all_objects.addAll(new Pair(t, findBestObjects(genericObject, gn)));
        }

        return all_objects;
    }
}
