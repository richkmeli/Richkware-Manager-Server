package it.richkmeli.rms.web.util;

import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

public class ServletManager {
    public static final String ERROR_JSP = "JSP/error.jsp";
    public static final String DEVICES_HTML = "devices.html";
    public static final String LOGIN_HTML = "login.html";

    public static void doDefaultProcess(HttpServletRequest request) throws ServletException {
        // check channel: rmc or webapp, if rmc secureconnection first (set something in session)
        if (request.getParameterMap().containsKey("channel")) {
            String channel = request.getParameter("channel");
            switch (channel){
                case "rmc":
                    break;
                case "webapp":
                    break;
                default:
                    Logger.error("ServletManager, channel "+ channel +" unknown");
                    break;
            }
        }
        // check login

        // se rmc decritta e rendi trasparente

        // lancia servlet exception cosi il try catch esterno sa cosa dire al client
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
