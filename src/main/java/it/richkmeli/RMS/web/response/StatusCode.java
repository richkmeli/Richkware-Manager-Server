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
    ALREADY_LOGGED(2100, "Already logged in"),
    MISSING_FIELD(2101, "Check input fields"),
    WRONG_PASSWORD(2102, "Wrong password"),
    ACCOUNT_NOT_FOUND(2103, "Account not found");


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
