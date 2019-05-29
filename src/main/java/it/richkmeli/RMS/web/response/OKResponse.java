package it.richkmeli.RMS.web.response;

import org.json.JSONObject;

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
