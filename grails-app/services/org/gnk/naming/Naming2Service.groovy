package org.gnk.naming

import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.tag.V2TagService
import org.gnk.utils.Pair

class Naming2Service {

    def serviceMethod() {}

    V2TagService v2TagService
    Integer selectionNumber = 10
    LinkedList<String> usedFirstName = new LinkedList<String>()
    LinkedList<String> usedName = new LinkedList<String>()

    LinkedList<PersoForNaming> findBestObjects(LinkedList<PersoForNaming> persoList, Integer gn_id)
    {


    }




    LinkedList<PersoForNaming> findBestNames2(LinkedList<PersoForNaming> persoList, Integer gn_id)
    {
        LinkedList<PersoForNaming> fullName = new LinkedList<PersoForNaming>()
        for (int i = selectionNumber; i > 0; i--)
            fullName.add(new PersoForNaming())

        return fullName
    }

}
