package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.util.Logger;
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

    public static Map<String, String> doDefaultProcess(HttpServletRequest request, HTTPVerb httpVerb) throws ServletException {
        Map<String, String> attribMap = new HashMap<>();
        HttpSession httpSession = request.getSession();
        Session session = ServletManager.getServerSession(httpSession);

        // check channel: rmc or webapp, if rmc secureconnection first (set something in session)
        if (request.getParameterMap().containsKey(Channel.CHANNEL)) {
            String channel = request.getParameter(Channel.CHANNEL);
            switch (channel) {
                case Channel.RICHKWARE:
                    session.setChannel(Channel.RMC);
                    String payload = request.getParameter(DATA_PARAMETER_KEY);
                    String decryptedPayload = session.getCryptoServer().decrypt(payload);
                    JSONObject decryptedPayloadJSON = new JSONObject(decryptedPayload);
                    for (String key : decryptedPayloadJSON.keySet()) {
                        String value = decryptedPayloadJSON.getString(key);
                        attribMap.put(key,value);
                    }
                    break;
                case Channel.WEBAPP:
                    session.setChannel(Channel.WEBAPP);

                    break;
                default:
                    Logger.error("ServletManager, channel " + channel + " unknown");
                    throw new ServletException("ServletManager, channel " + channel + " unknown");
            }

        } else {
            Logger.error("ServletManager, channel key not present");
            throw new ServletException("ServletManager, channel key not present");
        }



        // check login

        // se rmc decritta e rendi trasparente

        // lancia servlet exception cosi il try catch esterno sa cosa dire al client

        return attribMap;
    }

    public static Map<String,String> extractAttributes(HttpServletRequest request, HTTPVerb httpVerb){
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

