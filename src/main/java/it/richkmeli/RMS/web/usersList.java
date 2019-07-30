package it.richkmeli.rms.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.auth.AuthDatabaseManager;
import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.database.DatabaseException;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        }catch (ServletException e){
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }

        try {

            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {

                if (session.isAdmin()) {
                    out = GenerateUsersListJSON(session);

                    // servlet response
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(out);
                    printWriter.flush();
                    printWriter.close();
                } else {
                    // non ha privilegi
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "non ha privilegi");
                    request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
                }

            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "non loggato");
                request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    private String GenerateUsersListJSON(Session session) throws DatabaseException {
        AuthDatabaseManager authDatabaseManager = session.getAuthDatabaseManager();
        List<User> userList = authDatabaseManager.refreshUser();

        Type type = new TypeToken<List<User>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String usersListJSON = gson.toJson(userList, type);

        return usersListJSON;
    }
}