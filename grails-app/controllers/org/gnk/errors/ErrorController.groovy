package org.gnk.errors

class ErrorController {

    private ErrorHandler errorHandler = ErrorHandler.instance;

    def getError() {
        Error.Type typeError;
        String type = params.type;
        String identifier = params.identifier;
        Objects[] args = params.args;
        switch (type) {
            case type.toUpperCase() == "ALERT":
                typeError = Error.Type.ALERT;
                break;
            case type.toUpperCase() == "ERROR":
                typeError = Error.Type.ERROR;
                break;
            case type.toUpperCase() == "SERVICE":
                typeError = Error.Type.SERVICE;
                break;
            case type.toUpperCase() == "INFO":
                typeError = Error.Type.INFO;
                break;
            case type.toUpperCase() == "WARNING":
                typeError = Error.Type.WARNING;
                break;
            default:
                typeError = Error.Type.ALERT;
        }
        return errorHandler.getErrorMessage(identifier, args, typeError);
    }

    def index() {}
}
