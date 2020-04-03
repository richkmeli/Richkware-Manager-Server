package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.crypto.exception.CryptoException;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.log.Logger;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RMSServletManager extends AuthServletManager {
    public static final String ERROR_JSP = "JSP/error.jsp";
    public static final String DEVICES_HTML = "devices.html";
    public static final String LOGIN_HTML = "login.html";
    public static final String DATA_PARAMETER_KEY = "data";
    public static final String HTTP_RMS_SESSION_NAME = "rms_session";
    private static RMSSession rmsSession;

    public RMSServletManager(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        try {
            rmsSession = getRMSServerSession();
        } catch (JServletException e) {
            //e.printStackTrace();
            Logger.error(e);
        }
    }

    public RMSServletManager(AuthServletManager authServletManager) {
        super(authServletManager);
        try {
            rmsSession = getRMSServerSession();
        } catch (JServletException e) {
            //e.printStackTrace();
            Logger.error(e);
        }
    }

    @Override
    public void doSpecificProcessRequestAuth() throws JServletException {

        // check channel: rmc or webapp, if rmc secureconnection first (set something in session)
        if (attribMap.containsKey(Channel.CHANNEL)) {
            String channel = attribMap.get(Channel.CHANNEL);
            switch (channel) {
                case Channel.RMC:
                    rmsSession.setChannel(Channel.RMC);
                    // Extract encrypted data from map
                    String payload = attribMap.get(DATA_PARAMETER_KEY);
                    if (!"".equalsIgnoreCase(payload)) {
                        String decryptedPayload = null;
                        try {
                            decryptedPayload = session.getCryptoServer().decrypt(payload);
                        } catch (CryptoException e) {
                            throw new JServletException(e);
                        }
                        if (!"".equalsIgnoreCase(decryptedPayload)) {
                            JSONObject decryptedPayloadJSON = new JSONObject(decryptedPayload);
                            // add each attribute inside encrypted data to map
                            for (String key : decryptedPayloadJSON.keySet()) {
                                String value = decryptedPayloadJSON.getString(key);
                                attribMap.put(key, value);
                            }
                        }
                        // remove encrypted data from map
                        attribMap.remove(DATA_PARAMETER_KEY);
                    }
                    break;
                case Channel.WEBAPP:
                    rmsSession.setChannel(Channel.WEBAPP);
                    // all attributes are already in map
                    break;
                case Channel.RICHKWARE:
                    rmsSession.setChannel(Channel.RICHKWARE);
                    // all attributes are already in map
                    break;
                default:
                    Logger.error("ServletManager, servlet: " + servletPath + ". + channel " + channel + " unknown");
                    throw new JServletException(new KoResponse(RMSStatusCode.CHANNEL_UNKNOWN, "channel " + channel + " unknown"));
            }
        } else {
            Logger.error("ServletManager, servlet: " + servletPath + ". + channel key is not present.");
            throw new JServletException(new KoResponse(RMSStatusCode.CHANNEL_UNKNOWN, "channel key is not present"));
        }
    }

    @Override
    public String doSpecificProcessResponseAuth(String input) throws JServletException {
        // default: input as output
        String output = input;

        String channel = rmsSession.getChannel();
        if (channel != null) {
            switch (channel) {
                case Channel.RMC:
                    rmsSession.setChannel(Channel.RMC);
                    try {
                        output = session.getCryptoServer().encrypt(input);
                    } catch (CryptoException e) {
                        throw new JServletException(e);
                    }
                    break;
                case Channel.WEBAPP:
                    rmsSession.setChannel(Channel.WEBAPP);
                    break;
                case Channel.RICHKWARE:
                    rmsSession.setChannel(Channel.RICHKWARE);
                    break;
                default:
                    Logger.error("ServletManager, servlet: " + servletPath + ". + channel " + channel + " unknown");
                    throw new JServletException(new KoResponse(RMSStatusCode.CHANNEL_UNKNOWN, "channel " + channel + " unknown"));
            }
        } else {
            Logger.error("ServletManager, servlet: " + servletPath + ". + channel(server session) is null");
            throw new JServletException(new KoResponse(RMSStatusCode.CHANNEL_UNKNOWN, "channel(server session) is null"));
        }
        setRMSServerSession(rmsSession, httpServletRequest);
        return output;
    }

   /* @Override
    public <T extends Session> T getNewSessionInstance() throws ServletException, DatabaseException {
        return (T) new RMSSession(getServerSession());
    }*/

    public static void setRMSServerSession(RMSSession rmsSession, HttpServletRequest request) {
        // http session
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(HTTP_RMS_SESSION_NAME, rmsSession);
    }

    public RMSSession getRMSServerSession() throws JServletException {
        // http session
        HttpSession httpSession = httpServletRequest.getSession();
        // server session
        RMSSession rmsSession1 = (RMSSession) httpSession.getAttribute(HTTP_RMS_SESSION_NAME);
        authSession = getAuthServerSession();
        if (rmsSession1 == null) {
            try {
                rmsSession = new RMSSession(authSession);
                setRMSServerSession(rmsSession, httpServletRequest);
            } catch (DatabaseException e) {
                throw new JServletException(e);
            }
        } else {
            if (rmsSession1.getUserID() == null) {
                Logger.info("RMSSession OK, AuthSession initializing (Login)");
                rmsSession = new RMSSession(rmsSession, authSession);
                setRMSServerSession(rmsSession, httpServletRequest);
            } else {
                rmsSession = rmsSession1;
            }
        }
        return rmsSession;
    }

    public static class Channel {
        public static final String CHANNEL = "channel";
        public static final String WEBAPP = "webapp";
        public static final String RMC = "rmc";
        public static final String RICHKWARE = "richkware";
    }

}

