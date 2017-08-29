package richk.RMS.web;

import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;
import richk.RMS.model.Device;
import richk.RMS.util.Crypto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/LoadData")
public class LoadData extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoadData() {
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
            String data = request.getParameter("data");
            data = Crypto.DecryptRC4(data, "richktest");

            String name = data.substring(1, data.indexOf(","));
            String serverPort = data.substring((data.indexOf(",") + 1), (data.length() - 1));

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            Device device = new Device(
                    name,
                    request.getRemoteAddr(),
                    serverPort,
                    timeStamp);

            DatabaseManager db = session.getDatabaseManager();
            if (db.IsDevicePresent(name))
                db.EditDevice(device);
            else
                db.AddDevice(device);

            request.getRequestDispatcher("JSP/devices_list_AJAJ.jsp").forward(request, response);

        } catch (Exception e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
