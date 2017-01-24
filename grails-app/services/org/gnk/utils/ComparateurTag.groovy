package org.gnk.utils

/**
 * Created by Xavier on 17/06/2016.
 */

/**
 * class used to sort out a list of tags
 */
class ComparateurTag implements Comparator<org.gnk.tag.Tag> {

    @Override
    int compare(org.gnk.tag.Tag tag1, org.gnk.tag.Tag tag2) {
        return tag1.getterName().compareTo(tag2.getterName())
    }
}
