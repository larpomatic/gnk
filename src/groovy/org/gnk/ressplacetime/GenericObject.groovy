package org.gnk.ressplacetime

import org.gnk.resplacetime.GnConstant
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag

abstract class GenericObject {

    static constraints = {
    }

    /*
     Integer id
     Integer version

     Date lastUpdated
     Date dateCreated
     String code
     String comment
     GnConstant gnConstant
     Integer DTDId
*/
//     static belongsTo = [plot: Plot, objectType: ObjectType]

     abstract ArrayList<Tag> getTags()
     abstract Map<Tag, Integer> getTagsAndWeights(Float ponderation)
     abstract ArrayList<ReferentialObject> getProposedObject()
     abstract ArrayList<ReferentialObject> getReferentialObject()
     abstract ReferentialObject getLockedObject();
     abstract Plot getPlotFromGenericObject()
     abstract String getSubType();
     abstract Plot getPlotbyId();
     abstract ArrayList<Tag> getTaglist()


}
