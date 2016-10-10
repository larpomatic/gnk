package org.gnk.resplacetime

import javafx.util.Pair
import org.gnk.tag.Tag

abstract class ReferentialObject {

    static constraints = {
    }

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    String name
    String gender
    String description
    static def genders = ["M", "F", "MP", "FP"]

    // Id referenced into DTD
    static transients = ["DTDId"]
    Integer DTDId;

    abstract ArrayList<Tag> getTags();
    abstract ArrayList<Pair<Tag, Integer>> getTagsAndWeights();
    abstract List<ReferentialObject> getAll();
}
