package org.gnk.errors

import org.apache.xerces.util.MessageFormatter

import java.text.MessageFormat

/**
 * Created by chabi_h on 14/07/2017.
 */
class ErrorHandler {
    Locale language = new Locale("fr", "FR");
    ResourceBundle messages;

    public setLanguage(String language, String country) {
        this.language = new Locale(language, country);
        this.messages = ResourceBundle.getBundle("messages", this.language);
    }

    private ErrorHandler() {
        messages = ResourceBundle.getBundle("messages", this.language);
    }

    private static ErrorHandler INSTANCE;

    public static ErrorHandler getInstance() {
        if (INSTANCE == null)
            new ErrorHandler();
        return INSTANCE;
    }

    public getErrorMessage(String identifier, Object[] args, Error.Type type) {
        MessageFormatter formatter = new MessageFormat("");
        String message = formatter.formatMessage(this.language, this.messages.getString(identifier), args);
        return new Error(message, type);
    }
}
