package org.gnk.rights

class RightsService {

    boolean hasRight(int userRight, int lvlRight) {
        if ((userRight & lvlRight) == 0)
            false
        else
            true
    }
}
