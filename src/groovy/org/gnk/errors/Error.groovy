package org.gnk.errors

/**
 * Created by chabi_h on 14/07/2017.
 */
class Error extends Exception {
    public enum Type {
        INFO,
        SERVICE,
        ALERT,
        WARNING,
        ERROR;
    }

    private String message;
    private Type type;

    public Error(String message, Type type) {
        this.message = message;
        this.type = type;
    }
}
