package it.richkmeli.RMS.web;

import it.richkmeli.RMS.data.device.DeviceDatabaseManager;
import it.richkmeli.jcrypto.Crypto;
import it.richkmeli.RMS.Session;
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
import java.util.ResourceBundle;


@WebServlet("/encryptionKey")
public class encryptionKey extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;
    private String password;

    public encryptionKey() {
        super();
        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");

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
            if (request.getParameterMap().containsKey("id")){

                String name = request.getParameter("id");
                name = Crypto.DecryptRC4(name, password);

                DeviceDatabaseManager deviceDatabaseManager = session.getDeviceDatabaseManager();
                String encryptionKey = deviceDatabaseManager.getEncryptionKey(name);
                if (encryptionKey.isEmpty()) {
                    encryptionKey = "Error";
                }

                // encrypt key server-side generated or error message with pre-shared password
                encryptionKey = Crypto.EncryptRC4(encryptionKey, password);

                encryptionKey = "$" + encryptionKey + "#";

                PrintWriter out = response.getWriter();
                out.println(encryptionKey);
                out.flush();
            }else{
                // argomenti non presenti
                // TODO rimanda da qualche parte perche c'è errore
                Logger.e("SERVLET encryptionKey, doGet: argomenti non presenti");
                httpSession.setAttribute("error", "argomenti non presenti");
                request.getRequestDispatcher("login.html").forward(request, response);
            }
        } catch (Exception e) {
            Logger.e("SERVLET encryptionKey, doGet",e);
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
