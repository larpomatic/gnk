package org.gnk.ressplacetime

import com.gnk.substitution.Tag
import org.gnk.utils.Pair

/**
 * Created with IntelliJ IDEA.
 *
 * User: Mnesyah
 * Date: 10/10/13
 * Time: 22:41
 * To change this template use File | Settings | File Templates.
 */
class GenericResource {
    String code
    List<Tag> tagList
    List<ReferentialResource> bannedItemsList
    ArrayList<Pair<ReferentialObject, Integer>> resultList_bis
    List<ReferentialResource> resultList
    ArrayList<Pair<org.gnk.tag.Tag, ArrayList<Pair<ReferentialObject, Integer>>>> resultListForAllUniverses

    @Override
    public java.lang.String toString() {
        return "GenericResource{" +
                "code='" + code + '\'' +
                ", tagList=" + tagList +
                ", bannedItemsList=" + bannedItemsList +
                ", resultList=" + resultList +
                '}';
    }
}
