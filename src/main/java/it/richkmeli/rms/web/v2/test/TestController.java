package it.richkmeli.rms.web.v2.test;

import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.auth.model.exception.ModelException;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.util.RandomStringGenerator;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.data.entity.rmc.model.Rmc;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
public class TestController {

    TestController() {

    }

    public static void loadRandomTest(RMSSession rmsSession) {
        try {
            rmsSession.getAuthDatabaseManager().addUser(new User("richk@i.it", "00000000", true));
            rmsSession.getAuthDatabaseManager().addUser(new User("er@fv.it", "00000000", false));
            //TODO da spostare nella creazione della tabella
            rmsSession.getAuthDatabaseManager().addUser(new User("", "00000000", false));
            rmsSession.getAuthDatabaseManager().addUser(new User("richk@i.it", "00000000", true));
        } catch (AuthDatabaseException | ModelException e) {
            e.printStackTrace();
            Logger.error("Session TEST USERS", e);
        }

        try {
            rmsSession.getDeviceDatabaseManager().addDevice(new Device("rick2", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "start##start##start", "","iid","loc"));
            rmsSession.getDeviceDatabaseManager().addDevice(new Device("rick3", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it", "", "","iid","loc"));
            rmsSession.getDeviceDatabaseManager().addDevice(new Device("rick1", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "er@fv.it", "", "","iid","loc"));
            //used for reverse commands
            rmsSession.getDeviceDatabaseManager().addDevice(new Device("DESKTOP-1EVF5Q8/win_10_desktop1", "172.24.9.142", "none", "20-10-18", "ckeroivervioeon", "richk@i.it", "YzNSaGNuUT0jI2MzUmhjblE9IyNjM1JoY25RPQ==", "","iid","loc"));
        } catch (AuthDatabaseException e) {
            Logger.error("Session TEST DEVICES", e);
        }

        try {
            rmsSession.getAuthDatabaseManager().addUser(new User("richk@i.it", "00000000", true));
            rmsSession.getAuthDatabaseManager().addUser(new User("er@fv.it", "00000000", false));
            rmsSession.getAuthDatabaseManager().addUser(new User("", "00000000", false));
            rmsSession.getAuthDatabaseManager().addUser(new User("richk@i.it", "00000000", true));
        } catch (AuthDatabaseException | ModelException e) {
            e.printStackTrace();
            Logger.error("Session TEST USERS", e);
        }

        try {

            for (int i = 0; i < 10; i++) {
                User u = new User(RandomStringGenerator.generateAlphanumericString(8) + "@" + RandomStringGenerator.generateAlphanumericString(8) + "." + RandomStringGenerator.generateAlphanumericString(2),
                        RandomStringGenerator.generateAlphanumericString(10),
                        false);
                rmsSession.getAuthDatabaseManager().addUser(u);
                for (int i2 = 0; i2 < 5; i2++) {
                    rmsSession.getDeviceDatabaseManager().addDevice(new Device(RandomStringGenerator.generateAlphanumericString(8), "12.34.45.67", "8080", "20-10-2019", RandomStringGenerator.generateAlphanumericString(32), u.getEmail(), "start##start##start##start", "","iid","loc"));
                }
            }
            rmsSession.getDeviceDatabaseManager().addDevice(new Device(RandomStringGenerator.generateAlphanumericString(8), "12.34.45.67", "8080", "20-10-2019", RandomStringGenerator.generateAlphanumericString(32), "asd@asd.com", "", "WTJsaGJ3PT0=","iid","loc"));
        } catch (AuthDatabaseException | ModelException e) {
            Logger.error("Session ", e);
        }

        try {
            Rmc rmc1 = new Rmc("richk@i.it", "test_rmc_ID");
            Rmc rmc2 = new Rmc("er@fv.it", "test_rmc_ID_2");
            Rmc rmc3 = new Rmc("er@fv.it", "test_rmc_ID_3");
            Rmc rmc4 = new Rmc("", "test_rmc_ID_3");

            rmsSession.getRmcDatabaseManager().addRMC(rmc1);
            rmsSession.getRmcDatabaseManager().addRMC(rmc2);
            rmsSession.getRmcDatabaseManager().addRMC(rmc3);
            rmsSession.getRmcDatabaseManager().addRMC(rmc4);
        } catch (AuthDatabaseException e) {
            Logger.error("Session ", e);
        }

    }

    @GetMapping(name = "test", path = {"/test", "/Richkware-Manager-Server/test"})
    public void getRmc() throws IOException, ServletException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        HttpSession httpSession = request.getSession();
        RMSSession rmsSession = null;
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            rmsSession = rmsServletManager.getRMSServerSession();
        } catch (JServletException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);

        }
        String out = "";

        loadRandomTest(rmsSession);

        out = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<script src=\"/webjars/jquery/3.3.1/jquery.min.js\"></script>\n" +
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
