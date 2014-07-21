package org.gnk.rights

class RightsService {

    boolean hasRight(int userRight, int lvlRight) {

        print userRight
        print lvlRight
        print ((userRight & lvlRight) == 0)
        if ((userRight & lvlRight) == 0)
            false
        else
            true
    }
}
