package it.richkmeli.RMS.web.response;

public class KOResponse extends BasicResponse {

    public KOResponse(StatusCode statusCode, String message) {
        setStatus("KO");
        setStatusCode(statusCode);
        setMessage(message);
    }

    public KOResponse(StatusCode statusCode) {
        this(statusCode, null);
    }

}
