package richk.RMS.web;

import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;
import richk.RMS.model.Device;
import richk.RMS.model.ModelException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet("/DevicesListAJAJ")
public class DevicesListAJAJ extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DevicesListAJAJ() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        try {
            DatabaseManager databaseManager = session.getDatabaseManager();
            List<Device> devicesList = databaseManager.RefreshDevice();

            String devicesListJSON = "{ ";
            int index = 0;

            for (Device device : devicesList) {
                String deviceJSON = "'" + index + "' : {"
                        + "'name' : '" + device.getName() + "', "
                        + "'IP' : '" + device.getIP() + "', "
                        + "'serverPort' : '" + device.getServerPort() + "', "
                        + "'lastConnection' : '" + device.getLastConnection() + "'}";
                index++;
                devicesListJSON += deviceJSON;
                if (index < devicesList.size())
                    devicesListJSON += ", ";
            }
            devicesListJSON += " }";

            PrintWriter out = response.getWriter();
            out.println(devicesListJSON);
            out.flush();

        } catch (ModelException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
