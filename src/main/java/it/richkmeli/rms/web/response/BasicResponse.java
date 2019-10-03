package it.richkmeli.rms.web.response;

import org.json.JSONObject;

public abstract class BasicResponse {

    private String status;
    private StatusCode statusCode;
    String message;

    public BasicResponse() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode.getCode();
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        if (this.message == null) {
            return this.statusCode.getDefMessage();
        } else {
            return this.message;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String json() {
        JSONObject output = new JSONObject();
        output.put("status", getStatus());
        output.put("statusCode", getStatusCode());
        output.put("message", getMessage());
        return output.toString();
    }
}
