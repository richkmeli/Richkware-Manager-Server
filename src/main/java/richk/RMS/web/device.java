package richk.RMS.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;
import richk.RMS.model.Device;
import richk.RMS.model.ModelException;
import richk.RMS.util.KeyExchangePayload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet("/device")
public class device extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public device() {
        super();
    }

    private Session getServerSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = (Session) httpSession.getAttribute("session");
        if (session == null) {
            try {
                session = new Session();
                httpSession.setAttribute("session", session);
            } catch (DatabaseException e) {
                httpSession.setAttribute("error", e);
                request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
            }
        }
        return session;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = getServerSession(request, response);

        try {

            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                if (request.getParameterMap().containsKey("name")) {
                    String name = request.getParameter("name");

                    Device device = session.getDatabaseManager().getDevice(name);

                    if(device.getUserAssociated().compareTo(session.getUser())==0 ||
                            session.isAdmin()){
                        out = GenerateDevicesListJSON(device);
                    }else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        request.getRequestDispatcher("login.html").forward(request, response);

                    }


                }else{
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    request.getRequestDispatcher("login.html").forward(request, response);
                }

                // servlet response
                PrintWriter printWriter = response.getWriter();
                printWriter.println(out);
                printWriter.flush();
                printWriter.close();
            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "non loggato");
                request.getRequestDispatcher("login.html").forward(request, response);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);

        HttpSession httpSession = req.getSession();
        Session session = getServerSession(req, resp);

        try {
            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                if (req.getParameterMap().containsKey("name")) {
                    String name = req.getParameter("name");

                    Device device = session.getDatabaseManager().getDevice(name);

                    if(device.getUserAssociated().compareTo(session.getUser())==0 ||
                            session.isAdmin()){
                        session.getDatabaseManager().removeDevice(name);
                        //TODO in html impostare che con jquery guarda risposta se c'è deleted, altrimenti errore
                        out = "deleted";
                    }else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        req.getRequestDispatcher("login.html").forward(req, resp);
                    }

                }else{
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    req.getRequestDispatcher("login.html").forward(req, resp);
                }
                // servlet response
                PrintWriter printWriter = resp.getWriter();
                printWriter.println(out);
                printWriter.flush();
                printWriter.close();
            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "non loggato");
                req.getRequestDispatcher("login.html").forward(req, resp);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher("JSP/error.jsp").forward(req, resp);
        }

    }

    private String GenerateDevicesListJSON(Device device/*, Session session*/) throws ModelException {
        //DatabaseManager databaseManager = session.getDatabaseManager();
        List<Device> devicesList = new ArrayList<>();

        devicesList.add(device);

        Type type = new TypeToken<List<Device>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String devicesListJSON = gson.toJson(devicesList, type);

        return devicesListJSON;
    }

}

