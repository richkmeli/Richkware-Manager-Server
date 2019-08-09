package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.crypto.exception.CryptoException;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.util.Logger;
import it.richkmeli.rms.web.response.KOResponse;
import it.richkmeli.rms.web.response.StatusCode;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class ServletManager {
    public static final String ERROR_JSP = "JSP/error.jsp";
    public static final String DEVICES_HTML = "devices.html";
    public static final String LOGIN_HTML = "login.html";
    public static final String DATA_PARAMETER_KEY = "data";

    public enum HTTPVerb {
        GET, DELETE, POST, PUT
    }

    public static Map<String, String> doDefaultProcessRequest(HttpServletRequest request, HTTPVerb httpVerb) throws ServletException {
        Map<String, String> attribMap = extractParameters(request, httpVerb);
        // server session
        Session session = ServletManager.getServerSession(request);

        // check channel: rmc or webapp, if rmc secureconnection first (set something in session)
        if (attribMap.containsKey(Channel.CHANNEL)) {
            String channel = attribMap.get(Channel.CHANNEL);
            switch (channel) {
                case Channel.RMC:
                    session.setChannel(Channel.RMC);
                    // Extract encrypted data from map
                    String payload = attribMap.get(DATA_PARAMETER_KEY);
                    if (!"".equalsIgnoreCase(payload)) {
                        String decryptedPayload = null;
                        try {
                            decryptedPayload = session.getCryptoServer().decrypt(payload);
                        } catch (CryptoException e) {
                            throw new ServletException(e);
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
                    session.setChannel(Channel.WEBAPP);
                    // all attributes are already in map
                    break;
                case Channel.RICHKWARE:
                    session.setChannel(Channel.RICHKWARE);
                    // all attributes are already in map
                    break;
                default:
                    Logger.error("ServletManager, channel " + channel + " unknown");
                    throw new ServletException(new KOResponse(StatusCode.CHANNEL_UNKNOWN, "channel " + channel + " unknown"));
            }
        } else {
            Logger.error("ServletManager, channel key is not present");
            throw new ServletException(new KOResponse(StatusCode.CHANNEL_UNKNOWN, "channel key is not present"));
        }

        return attribMap;
    }

    private static Map<String, String> extractParameters(HttpServletRequest request, HTTPVerb httpVerb) {
        Map<String, String> attribMap = new HashMap<>();
        HttpSession httpSession = request.getSession();
        List<String> list = Collections.list(request.getParameterNames());
        if (httpVerb == HTTPVerb.GET) {
            for (String parameter : list) {
                String value = request.getParameter(parameter);
                attribMap.put(parameter, value);
            }
        } else {
            // todo search info in payload
            Logger.error("search info in payload (POST) not supported yet");
        }
        return attribMap;
    }


    public static void CheckLogin(HttpServletRequest request) throws ServletException {
        // server session
        Session session = ServletManager.getServerSession(request);

        String user = session.getUser();
        // Authentication
        if (user == null) {
            Logger.error("ServletManager, user not logged");
            throw new ServletException(new KOResponse(StatusCode.NOT_LOGGED, "user not logged"));
        }

    }

    public static String doDefaultProcessResponse(HttpServletRequest request, String input) throws ServletException {
        // default: input as output
        String output = input;
        // server session
        Session session = ServletManager.getServerSession(request);

        String channel = session.getChannel();
        if (channel != null) {
            switch (channel) {
                case Channel.RMC:
                    session.setChannel(Channel.RMC);
                    try {
                        output = session.getCryptoServer().encrypt(input);
                    } catch (CryptoException e) {
                        throw new ServletException(e);
                    }
                    break;
                case Channel.WEBAPP:
                    session.setChannel(Channel.WEBAPP);
                    break;
                case Channel.RICHKWARE:
                    session.setChannel(Channel.RICHKWARE);
                    break;
                default:
                    Logger.error("ServletManager, channel " + channel + " unknown");
                    throw new ServletException(new KOResponse(StatusCode.CHANNEL_UNKNOWN, "channel " + channel + " unknown"));
            }
        } else {
            Logger.error("ServletManager, channel(server session) is null");
            throw new ServletException(new KOResponse(StatusCode.CHANNEL_UNKNOWN, "channel(server session) is null"));
        }
        return output;
    }

    public static class Channel {
        public static final String CHANNEL = "channel";
        public static final String WEBAPP = "webapp";
        public static final String RMC = "rmc";
        public static final String RICHKWARE = "richkware";
    }

    public static Session getServerSession(HttpServletRequest request) throws ServletException {
        // http session
        HttpSession httpSession = request.getSession();
        // server session
        return ServletManager.getServerSession(httpSession);
    }

    public static Session getServerSession(HttpSession httpSession) throws ServletException {
        Session session = (Session) httpSession.getAttribute("session");
        if (session == null) {
            try {
                session = new Session();
                httpSession.setAttribute("session", session);
            } catch (DatabaseException e) {
                throw new ServletException(e);
                //httpSession.setAttribute("error", e);
                //request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
            }
        }
        return session;
    }

    public static String printHTTPsession(HttpSession httpSession) {
        StringBuilder list = new StringBuilder();
        Enumeration<String> attributes = httpSession.getAttributeNames();
        list.append("{");
        while (attributes.hasMoreElements()) {
            String attribute = attributes.nextElement();
            list.append(attribute + " : " + httpSession.getAttribute(attribute));
            list.append(attributes.hasMoreElements() ? " - " : "}");
        }

        return list.toString();
    }
}

