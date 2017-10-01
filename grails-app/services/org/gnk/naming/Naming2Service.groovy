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
     * return une liste de charactères (PersoForNaming dans l'existant)
     */
    def serviceMethod() {}

    V2TagService v2TagService;
    Integer selectionNumber = 10;

    //adapter pour des characters
    ArrayList<Pair<ReferentialObject, Integer>> findBestObjects(GenericObject genericObject, Gn gn)
    {
        if (gn.dtd != null) {
            GNKDataContainerService gnk = new GNKDataContainerService()
            gnk.ReadDTD(gn.dtd)
            gn = gnk.gn
        }

        // liste contenant les objets et leurs scores par rapport au GenericObject
        ArrayList<Pair<ReferentialObject, Integer>> sorted_list = new ArrayList<>();



    }




    LinkedList<PersoForNaming> findBestNames2(LinkedList<PersoForNaming> persoList, Integer gn_id)
    {
        LinkedList<PersoForNaming> fullName = new LinkedList<PersoForNaming>()
        for (int i = selectionNumber; i > 0; i--)
            fullName.add(new PersoForNaming())

        return fullName
    }

}
