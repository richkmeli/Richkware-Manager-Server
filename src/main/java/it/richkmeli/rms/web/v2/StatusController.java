package it.richkmeli.rms.web.v2;

import it.richkmeli.jframework.crypto.algorithm.RC4;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OkResponse;
import it.richkmeli.rms.web.util.RMSStatusCode;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class StatusController {

    StatusController() {
    }

    @GetMapping(name = "status", path = "/status")
    public String getStatus() {
        return new OkResponse(RMSStatusCode.SUCCESS, "UP").json();
    }
}