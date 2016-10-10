package org.gnk.resplacetime

import javafx.util.Pair
import org.gnk.tag.Tag

abstract class GenericObject {

    static constraints = {
    }

     Integer id
     Integer version

     Date lastUpdated
     Date dateCreated
     String code
     String comment
     GnConstant gnConstant
     Integer DTDId



     abstract ArrayList<Tag> getTags()
     abstract Map<Tag, Integer> getTagsAndWeights()
     abstract ArrayList<ReferentialObject> getReferentialObject()
}
