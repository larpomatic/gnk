package org.gnk.ressplacetime

import com.gnk.substitution.Tag

/**
 * Created with IntelliJ IDEA.
 * User: Mnesyah
 * Date: 10/10/13
 * Time: 22:41
 * To change this template use File | Settings | File Templates.
 */
class GenericResource {
    String code
    String ObjectType
    List<Tag> tagList
    List<ReferentialResource> bannedItemsList
    List<ReferentialResource> resultList

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
