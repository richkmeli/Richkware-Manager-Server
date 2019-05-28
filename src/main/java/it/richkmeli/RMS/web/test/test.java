package it.richkmeli.RMS.web.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.RMS.Session;
import it.richkmeli.RMS.data.device.DeviceDatabaseManager;
import it.richkmeli.RMS.data.device.model.Device;
import it.richkmeli.jcrypto.Crypto;
import it.richkmeli.jcrypto.util.RandomStringGenerator;
import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.jframework.util.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/test")
public class test extends HttpServlet {

    public test() {
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


        //TODO: load data for testing
        try {
            session.getAuthDatabaseManager().addUser(new User("richk@i.it", "00000000", true));
            session.getAuthDatabaseManager().addUser(new User("er@fv.it", "00000000", false));
            session.getDeviceDatabaseManager().addDevice(new Device("rick2", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it"));
            session.getDeviceDatabaseManager().addDevice(new Device("rick3", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "richk@i.it"));
            session.getDeviceDatabaseManager().addDevice(new Device("rick1", "43.34.43.34", "40", "20-10-18", "ckeroivervioeon", "er@fv.it"));
            session.getAuthDatabaseManager().addUser(new User("richk@i.it", "00000000", true));
        } catch (DatabaseException e) {
            Logger.e("Session ",e);
        }


    }

}

