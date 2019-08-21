package it.richkmeli.rms.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.auth.AuthDatabaseManager;
import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.web.response.KOResponse;
import it.richkmeli.rms.web.response.OKResponse;
import it.richkmeli.rms.web.response.StatusCode;
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
        PrintWriter out = response.getWriter();
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        } catch (ServletException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }

        try {

            String user = session.getUser();

            boolean encryption = false;
            if (request.getParameterMap().containsKey("channel")) {
                if ("rmc".equalsIgnoreCase(request.getParameter("channel"))) {
                    encryption = true;
                }
            }

            // Authentication
            if (user != null) {

                if (session.isAdmin()) {
                    if (encryption) {  // RMC
                        String encPayload = session.getCryptoServer().encrypt(GenerateUsersListJSON(session));
                        out.println((new OKResponse(StatusCode.SUCCESS, encPayload)).json());
                    } else {  // WEBAPP
                        // Authentication
                        out.println((new OKResponse(StatusCode.SUCCESS, GenerateUsersListJSON(session)).json()));
                    }

                    out.flush();
                    out.close();
                } else {
                    // non ha privilegi
                    // TODO rimanda da qualche parte perche c'è errore
                    out.println((new KOResponse(StatusCode.GENERIC_ERROR, "You are not admin!").json()));
                }

            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                out.println((new KOResponse(StatusCode.NOT_LOGGED).json()));
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage()).json()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    private String GenerateUsersListJSON(Session session) throws DatabaseException {
        AuthDatabaseManager authDatabaseManager = session.getAuthDatabaseManager();
        List<User> userList = authDatabaseManager.getAllUsers();

        Type type = new TypeToken<List<User>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String usersListJSON = gson.toJson(userList, type);

        return usersListJSON;
    }
}