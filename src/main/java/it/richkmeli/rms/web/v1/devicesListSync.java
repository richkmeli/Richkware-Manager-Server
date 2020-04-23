package it.richkmeli.rms.web.v1;

import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.web.v1.util.RMSServletManager;
import it.richkmeli.rms.web.v1.util.RMSSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Deprecated
@WebServlet("/devicesListSync")
public class devicesListSync extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public devicesListSync() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        RMSSession rmsSession = null;
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request,response);
            rmsSession = rmsServletManager.getRMSServerSession();
        } catch (JServletException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }

        try {

            httpSession.setAttribute("device", rmsSession.getDeviceDatabaseManager().getAllDevices());
            request.getRequestDispatcher("JSP/device_list.jsp").forward(request, response);

        } catch (DatabaseException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
