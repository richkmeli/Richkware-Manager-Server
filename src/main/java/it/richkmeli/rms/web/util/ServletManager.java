package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.crypto.exception.CryptoException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.Logger;
import it.richkmeli.rms.web.response.KOResponse;
import it.richkmeli.rms.web.response.StatusCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class ServletManager {
    public static final String ERROR_JSP = "JSP/error.jsp";
    public static final String DEVICES_HTML = "devices.html";
    public static final String LOGIN_HTML = "login.html";
    public static final String DATA_PARAMETER_KEY = "data";


    public static Map<String, String> doDefaultProcessRequest(HttpServletRequest request) throws ServletException {
        Map<String, String> attribMap = extractParameters(request);
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
                    Logger.error("ServletManager, servlet: " + request.getServletPath() + ". + channel " + channel + " unknown");
                    throw new ServletException(new KOResponse(StatusCode.CHANNEL_UNKNOWN, "channel " + channel + " unknown"));
            }
        } else {
            Logger.error("ServletManager, servlet: " + request.getServletPath() + ". + channel key is not present.");
            throw new ServletException(new KOResponse(StatusCode.CHANNEL_UNKNOWN, "channel key is not present"));
        }

        return attribMap;
    }

    // search parameters into URL (GET, ...) and into body (Data format supported are classic encoding key=att&... and JSON).
    private static Map<String, String> extractParameters(HttpServletRequest request) {
        Map<String, String> attribMap = new HashMap<>();
        // search parameter into URI and body (classic encoding)
        List<String> list = Collections.list(request.getParameterNames());
        for (String parameter : list) {
            String value = request.getParameter(parameter);
            attribMap.put(parameter, value);
        }
        // search JSON parameter into body
        try {
            String body = getBody(request);
            if (!"".equalsIgnoreCase(body)) {
                if (isJSONValid(body)) {
                    JSONObject bodyJSON = new JSONObject(body);
                    for (String key : bodyJSON.keySet()) {
                        String value = bodyJSON.getString(key);
                        attribMap.put(key, value);
                    }
                } else {
                    Logger.info("extractParameters: the body is not JSON formatted.");
                }
            } else {
                Logger.info("extractParameters: the body is empty.");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            Logger.error(e);
        }

        return attribMap;
    }


    public static void checkLogin(HttpServletRequest request) throws ServletException {
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
                    Logger.error("ServletManager, servlet: " + request.getServletPath() + ". + channel " + channel + " unknown");
                    throw new ServletException(new KOResponse(StatusCode.CHANNEL_UNKNOWN, "channel " + channel + " unknown"));
            }
        } else {
            Logger.error("ServletManager, servlet: " + request.getServletPath() + ". + channel(server session) is null");
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

    public static String printHttpHeaders(HttpServletRequest request) {
        StringBuilder out = new StringBuilder();
        Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Enumeration values = request.getHeaders(name);  // support multiple values
            if (values != null) {
                while (values.hasMoreElements()) {
                    String value = (String) values.nextElement();
                    out.append(name + ": " + value + "\n");
                }
            }
        }
        return out.toString();
    }

    public static String printHttpAttributes(HttpSession httpSession) {
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

    public static String getBody(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}

