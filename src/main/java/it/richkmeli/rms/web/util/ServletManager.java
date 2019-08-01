package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.util.Logger;
import it.richkmeli.rms.web.response.KOResponse;
import it.richkmeli.rms.web.response.StatusCode;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ServletManager {
    public static final String ERROR_JSP = "JSP/error.jsp";
    public static final String DEVICES_HTML = "devices.html";
    public static final String LOGIN_HTML = "login.html";
    public static final String DATA_PARAMETER_KEY = "data";

    public enum HTTPVerb {
        GET, DELETE, POST, PUT
    }

    static class Channel {
        private static final String CHANNEL = "channel";
        private static final String WEBAPP = "webapp";
        private static final String RMC = "rmc";
        private static final String RICHKWARE = "richkware";
    }

    public static Map<String, String> doDefaultProcessRequest(HttpServletRequest request, HTTPVerb httpVerb) throws ServletException {
        Map<String, String> attribMap = extractAttributes(request, httpVerb);
        // server session
        Session session = ServletManager.getServerSession(request);

        // check channel: rmc or webapp, if rmc secureconnection first (set something in session)
        if (request.getParameterMap().containsKey(Channel.CHANNEL)) {
            String channel = request.getParameter(Channel.CHANNEL);
            switch (channel) {
                case Channel.RMC:
                    session.setChannel(Channel.RMC);
                    // Extract encrypted data from map
                    String payload = attribMap.get(DATA_PARAMETER_KEY);
                    String decryptedPayload = session.getCryptoServer().decrypt(payload);
                    JSONObject decryptedPayloadJSON = new JSONObject(decryptedPayload);
                    // add each attribute inside encrypted data to map
                    for (String key : decryptedPayloadJSON.keySet()) {
                        String value = decryptedPayloadJSON.getString(key);
                        attribMap.put(key, value);
                    }
                    // remove encrypted data from map
                    attribMap.remove(DATA_PARAMETER_KEY);
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
            Logger.error("ServletManager, channel key not present");
            throw new ServletException(new KOResponse(StatusCode.CHANNEL_UNKNOWN, "channel key not present"));
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
                    output = session.getCryptoServer().encrypt(input);
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

    private static Map<String, String> extractAttributes(HttpServletRequest request, HTTPVerb httpVerb) {
        Map<String, String> attribMap = new HashMap<>();
        HttpSession httpSession = request.getSession();

        if (httpVerb == HTTPVerb.GET) {
            for (String attribute : Collections.list(httpSession.getAttributeNames())) {
                String value = (String) httpSession.getAttribute(attribute);
                attribMap.put(attribute, value);
            }
        } else {
            // todo search info in payload
            Logger.error("search info in payload (POST) not supported yet");
        }
        return attribMap;
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

