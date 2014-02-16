package org.gnk.utils

/**
 * User: Bastien
 * Date: 16/01/14
 * Time: 16:44
 */
public enum Sex {
    NEUTRAL("N"),
    MALE("M"),
    FEMALE("F");

    private String sexStr;

    private Sex(String sexStr) {
        this.sexStr = sexStr;
    }

    public Sex getOpposite() {
        if (this == MALE)
            return FEMALE
        if (this == FEMALE)
            return MALE
        return NEUTRAL
    }

    @Override
    public String toString() {
    }
}