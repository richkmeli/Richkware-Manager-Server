package richk.RMS.web;

import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;
import richk.RMS.util.Crypto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;


@WebServlet("/GetEncryptionKey")
public class GetEncryptionKey extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;
    private String password;

    public GetEncryptionKey() {
        super();
        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");

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
            String name = request.getParameter("id");
            name = Crypto.DecryptRC4(name, password);

            DatabaseManager db = session.getDatabaseManager();
            String encryptionKey = db.GetEncryptionKey(name);
            if (encryptionKey.isEmpty()) {
                encryptionKey = "Error";
            }

            // encrypt key server-side generated or error message with pre-shared password
            encryptionKey = Crypto.EncryptRC4(encryptionKey, password);

            encryptionKey = "$" + encryptionKey + "#";

            PrintWriter out = response.getWriter();
            out.println(encryptionKey);
            out.flush();

        } catch (Exception e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
