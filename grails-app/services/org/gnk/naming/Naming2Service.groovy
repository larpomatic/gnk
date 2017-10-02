package org.gnk.naming

import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.tag.V2TagService
import org.gnk.utils.Pair

class Naming2Service {
    /** Objectif: remplacer l'appel à l'ancien NamingService dans le fichier IntegrationHandler
     * en faisant intervenir le nouveau service V2TagService.
     * return une Linkedlist<PersoForNaming>
     */
    def serviceMethod() {}

    V2TagService v2TagService
    Integer selectionNumber = 10 //10 noms proposés

    //adapter pour des characters
    LinkedList<PersoForNaming> findBestObjects(LinkedList<PersoForNaming> persoList, Integer gn_id)
    {
        // liste contenant les objets et leurs scores par rapport au GenericObject
        ArrayList<Pair<ReferentialObject, Integer>> sorted_list = new ArrayList<>();

        // on récupère la liste des names et leurs scores
        List<ReferentialObject> all_names = //genericObject.getReferentialObject()
        for (ReferentialObject p : all_names) {
            sorted_list.add(new Pair<ReferentialObject, Integer>(p, new Integer((int) v2TagService.computeComparativeScoreObject(genericObject, p, gn))))
        }

        // on trie la sorted_list en fonction du poids de l'object
        Collections.sort(sorted_list, new Comparator<Pair<ReferentialObject, Integer>>() {
            public int compare(final Pair<ReferentialObject, Integer> o1, final Pair<ReferentialObject, Integer> o2) {
                return o1.right.intValue().compareTo(o2.right.intValue());
            }
        });
        Collections.reverse(sorted_list)

    }




    LinkedList<PersoForNaming> findBestNames2(LinkedList<PersoForNaming> persoList, Integer gn_id)
    {
        LinkedList<PersoForNaming> fullName = new LinkedList<PersoForNaming>()
        for (int i = selectionNumber; i > 0; i--)
            fullName.add(new PersoForNaming())

        return fullName
    }

}
