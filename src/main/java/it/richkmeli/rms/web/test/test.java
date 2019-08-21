package it.richkmeli.rms.web.test;

import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.crypto.util.RandomStringGenerator;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.Logger;
import it.richkmeli.rms.data.device.model.Device;
import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.util.ServletException;
import it.richkmeli.rms.web.util.ServletManager;
import it.richkmeli.rms.web.util.Session;

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
            //TODO da spostare nella creazione della tabella
            session.getAuthDatabaseManager().addUser(new User("", "00000000", false));
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
                User u = new User(RandomStringGenerator.generateAlphanumericString(8) + "@" + RandomStringGenerator.generateAlphanumericString(8) + "." + RandomStringGenerator.generateAlphanumericString(2),
                        RandomStringGenerator.generateAlphanumericString(10),
                        false);
                session.getAuthDatabaseManager().addUser(u);
                for (int i2 = 0; i2 < 5; i2++) {
                    session.getDeviceDatabaseManager().addDevice(new Device(RandomStringGenerator.generateAlphanumericString(8), "12.34.45.67", "8080", "20-10-2019", RandomStringGenerator.generateAlphanumericString(32), u.getEmail(), "start##start##start##start", ""));
                }
            }
        } catch (DatabaseException e) {
            Logger.error("Session ", e);
        }

        try {

            RMC rmc1 = new RMC("richk@i.it", "test_rmc_ID");
            RMC rmc2 = new RMC("er@fv.it", "test_rmc_ID_2");
            RMC rmc3 = new RMC("er@fv.it", "test_rmc_ID_3");
            RMC rmc4 = new RMC("", "test_rmc_ID_3");

            session.getRmcDatabaseManager().addRMC(rmc1);
            session.getRmcDatabaseManager().addRMC(rmc2);
            session.getRmcDatabaseManager().addRMC(rmc3);
            session.getRmcDatabaseManager().addRMC(rmc4);
        } catch (DatabaseException e) {
            Logger.error("Session ", e);
        }

        out = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<script src=\"js/jquery/jquery.min.js\"></script>\n" +
                "\t\t\t<script>\n" +
                "\t\t\t$(document).ready(function() {\n" +
                "\t\t\t\tdocument.location.replace(\"login.html\")\n" +
                "\t\t\t});\n" +
                "\t\t</script>\n" +
                "\t\t</head>\n" +
                "\t\t<body></body>\n" +
                "</html>";

        PrintWriter printWriter = response.getWriter();
        printWriter.println(out);
        printWriter.flush();
        printWriter.close();
    }

}

