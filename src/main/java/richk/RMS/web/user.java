package richk.RMS.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;
import richk.RMS.model.ModelException;
import richk.RMS.model.User;

import javax.servlet.ServletException;
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

                //if (session.isAdmin()) {
                    out = GenerateUserListJSON(session);

                    // servlet response
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(out);
                    printWriter.flush();
                    printWriter.close();
                /*} else {
                    // non ha privilegi
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "non ha privilegi");
                    request.getRequestDispatcher("login.html").forward(request, response);
                }*/

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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //if the code below is de-commented, this servlet disables DELETE
        //super.doDelete(req, resp);

        HttpSession httpSession = req.getSession();
        Session session = getServerSession(req, resp);

        try {
            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                if (req.getParameterMap().containsKey("email")) {
                    String email = req.getParameter("email");

                    if(email.compareTo(session.getUser())==0 ||
                            session.isAdmin()){
                        session.getDatabaseManager().removeUser(email);
                        out = "deleted";
                    }else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        req.getRequestDispatcher("login.html").forward(req, resp);
                    }

                }else{
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    req.getRequestDispatcher("login.html").forward(req, resp);
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
                req.getRequestDispatcher("login.html").forward(req, resp);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher("JSP/error.jsp").forward(req, resp);
        }

    }

    private String GenerateUserListJSON(Session session) throws ModelException {
        DatabaseManager databaseManager = session.getDatabaseManager();
        List<User> userList = new ArrayList<>();//databaseManager.refreshUser();
        userList.add(new User(session.getUser(),"hidden",session.isAdmin()));

        Type type = new TypeToken<List<User>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String usersListJSON = gson.toJson(userList, type);

        return usersListJSON;
    }
}