package it.richkmeli.RMS.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.RMS.database.DatabaseException;
import it.richkmeli.RMS.database.DatabaseManager;
import it.richkmeli.RMS.model.ModelException;
import it.richkmeli.RMS.model.User;
import it.richkmeli.RMS.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet("/usersList")
public class usersList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public usersList() {
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

                if (session.isAdmin()) {
                    out = GenerateDevicesListJSON(session);

                    // servlet response
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(out);
                    printWriter.flush();
                    printWriter.close();
                } else {
                    // non ha privilegi
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "non ha privilegi");
                    request.getRequestDispatcher("login.html").forward(request, response);
                }

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

    private String GenerateDevicesListJSON(Session session) throws ModelException {
        DatabaseManager databaseManager = session.getDatabaseManager();
        List<User> userList = databaseManager.refreshUser();

        Type type = new TypeToken<List<User>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String usersListJSON = gson.toJson(userList, type);

        return usersListJSON;
    }
}