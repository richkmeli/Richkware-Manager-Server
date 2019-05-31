package it.richkmeli.RMS.web.response;

public enum StatusCode {

    /**
     * Legenda codici:
     * 1000 -> successo
     * 2000 -> errore servizio network
     * 3000 -> errore offline
     *
     * seconda cifra = tipo di servizio
     * terza e quarta cifra = numero errore
     * */

    SUCCESS(1000, ""),
    GENERIC_ERROR(2000, ""),
    NOT_LOGGED(2100, "User is not logged"),
    ALREADY_LOGGED(2101, "Already logged in"),
    MISSING_FIELD(2102, "Check input fields"),
    WRONG_PASSWORD(2103, "Wrong password"),
    ACCOUNT_NOT_FOUND(2104, "Account not found"),
    ALREADY_REGISTERED(2105, "Email already registered"),
    DB_ERROR(3000, "Error in DB");


    private int code;
    private String defMessage;

    StatusCode(int code, String mess) {
        this.code = code;
        this.defMessage = mess;
    }

    public String getDefMessage() {
        return this.defMessage;
    }

    public int getCode() {
        return code;
    }
}
