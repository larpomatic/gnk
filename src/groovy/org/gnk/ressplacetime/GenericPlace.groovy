package org.gnk.ressplacetime

import com.gnk.substitution.Tag

/**
 * Created with IntelliJ IDEA.
 * User: Mnesyah
 * Date: 10/10/13
 * Time: 23:03
 * To change this template use File | Settings | File Templates.
 */
class GenericPlace {
    String code
    List<Tag> tagList
    List<ReferentialPlace> bannedItemsList
    List<ReferentialPlace> resultList

    @Override
    public java.lang.String toString() {
        return "GenericPlace{" +
                "code='" + code + '\'' +
                ", tagList=" + tagList +
                ", bannedItemsList=" + bannedItemsList +
                ", resultList=" + resultList +
                '}';
    }
}
