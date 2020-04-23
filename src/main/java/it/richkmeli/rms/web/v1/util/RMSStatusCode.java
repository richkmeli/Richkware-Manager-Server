package it.richkmeli.rms.web.v1.util;

import it.richkmeli.jframework.auth.web.util.AuthStatusCode;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.StatusCode;

/**
 * 1000 --> success
 * 2000 --> generic error
 * 21xx --> account error
 * 22xx --> database error
 * 23xx --> crypto error
 * 24xx --> session error
 */

public class RMSStatusCode extends AuthStatusCode {
  public static final StatusCode CHANNEL_UNKNOWN = new StatusCode(2401, "Channel Unknown");

}

