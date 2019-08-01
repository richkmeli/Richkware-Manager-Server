package it.richkmeli.rms.web.util;

import it.richkmeli.rms.web.response.KOResponse;

public class ServletException extends Exception {
    private KOResponse koResponse;

    public ServletException(Exception exception) {
        super(exception);
    }

    public ServletException(KOResponse koResponse) {
        this.koResponse = koResponse;
    }

    public String getKOResponseJSON() {
        return koResponse.json();
    }

    public KOResponse getKOResponse() {
        return koResponse;
    }
}