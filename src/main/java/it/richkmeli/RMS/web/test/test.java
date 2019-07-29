package it.richkmeli.RMS.web.test;

import it.richkmeli.RMS.data.device.model.Device;
import it.richkmeli.RMS.web.util.ServletException;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.crypto.util.RandomStringGenerator;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.util.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class test extends HttpServlet {

    public test() {
        super();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        } catch (ServletException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }
        String out = "";


        try {
            session.getAuthDatabaseManager().addUser(new User("richk@i.it", "00000000", true));
            session.getAuthDatabaseManager().addUser(new User("er@fv.it", "00000000", false));
            session.getAuthDatabaseManager().addUser(new User("richk@i.it", "00000000", true));
        } catch (DatabaseException e) {
            e.printStackTrace();
            Logger.error("Session TEST USERS", e);
        }

        try {
            session.getDeviceDatabaseManager().addDevice(new Device("rick2", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "start##start##start", ""));
            session.getDeviceDatabaseManager().addDevice(new Device("rick3", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "", ""));
            session.getDeviceDatabaseManager().addDevice(new Device("rick1", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "er@fv.it", "", ""));
            //used for reverse commands
            session.getDeviceDatabaseManager().addDevice(new Device("DESKTOP-1EVF5Q8/win_10_desktop1", "172.24.9.142", "none", "20-10-18", "ckeroivervioeon", "richk@i.it", "YzNSaGNuUT0jI2MzUmhjblE9IyNjM1JoY25RPQ==", ""));
        } catch (DatabaseException e) {
            Logger.error("Session TEST DEVICES", e);
        }

        try {

            for (int i = 0; i < 10; i++) {
                User u = new User(RandomStringGenerator.GenerateAlphanumericString(8) + "@" + RandomStringGenerator.GenerateAlphanumericString(8) + "." + RandomStringGenerator.GenerateAlphanumericString(2),
                        RandomStringGenerator.GenerateAlphanumericString(10),
                        false);
                session.getAuthDatabaseManager().addUser(u);
                for (int i2 = 0; i2 < 5; i2++) {
                    session.getDeviceDatabaseManager().addDevice(new Device(RandomStringGenerator.GenerateAlphanumericString(8), "12.34.45.67", "8080", "20-10-2019", RandomStringGenerator.GenerateAlphanumericString(32), u.getEmail(), "start##start##start##start", ""));
                }
            }
        } catch (DatabaseException e) {
            Logger.error("Session ", e);
        }

        out = "OK";

        PrintWriter printWriter = response.getWriter();
        printWriter.println(out);
        printWriter.flush();
        printWriter.close();
    }

}

