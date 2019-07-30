package it.richkmeli.rms.web.response;

public class OKResponse extends BasicResponse {

    public OKResponse(StatusCode statusCode, String message) {
        setStatus("OK");
        setStatusCode(statusCode);
        setMessage(message);
    }

    public OKResponse(StatusCode statusCode) {
        this(statusCode, null);
    }

}
