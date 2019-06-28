package it.richkmeli.RMS.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.RMS.web.response.KOResponse;
import it.richkmeli.RMS.web.response.OKResponse;
import it.richkmeli.RMS.web.response.StatusCode;
import it.richkmeli.RMS.web.util.ServletException;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.jframework.auth.model.User;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet("/user")
public class user extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public user() {
        super();
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        }catch (ServletException e){
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
//            httpSession.setAttribute("error", e);
//            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }
        try {

            String user = session.getUser();
            boolean isAdmin = session.getAuthDatabaseManager().isAdmin(user);
            // Authentication
            if (user != null) {

                //if (session.isAdmin()) {
//                out = GenerateUserListJSON(session);
                JSONObject message = new JSONObject();
                message.put("user", user);
                message.put("admin", isAdmin);
                out.println((new OKResponse(StatusCode.SUCCESS, message.toString()).json()));

                // servlet response
                /*} else {
                    // non ha privilegi
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "non ha privilegi");
                    request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
                }*/

            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                out.println((new KOResponse(StatusCode.NOT_LOGGED)).json());
//                httpSession.setAttribute("error", "non loggato");
//                response.sendRedirect(ServletManager.LOGIN_HTML);
                // request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
//            httpSession.setAttribute("error", e);
//            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
        }
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, IOException {
        //if the code below is de-commented, this servlet disables DELETE
        //super.doDelete(req, resp);

        HttpSession httpSession = req.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        }catch (ServletException e){
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher(ServletManager.ERROR_JSP).forward(req, resp);

        }

        try {
            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                if (req.getParameterMap().containsKey("email")) {
                    String email = req.getParameter("email");

                    if (email.compareTo(session.getUser()) == 0 ||
                            session.isAdmin()) {
                        session.getAuthDatabaseManager().removeUser(email);
                        out = "deleted";
                    } else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        req.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(req, resp);
                    }

                } else {
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    req.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(req, resp);
                }
                // servlet response
                PrintWriter printWriter = resp.getWriter();
                printWriter.println(out);
                printWriter.flush();
                printWriter.close();
            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "non loggato");
                req.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(req, resp);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher(ServletManager.ERROR_JSP).forward(req, resp);
        }

    }

    private String GenerateUserListJSON(Session session) {
        //DatabaseManager databaseManager = session.getDatabaseManager();
        List<User> userList = new ArrayList<>();//databaseManager.refreshUser();
        userList.add(new User(session.getUser(), "hidden", session.isAdmin()));

        Type type = new TypeToken<List<User>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String usersListJSON = gson.toJson(userList, type);

        return usersListJSON;
    }
}