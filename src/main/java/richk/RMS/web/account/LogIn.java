package richk.RMS.web.account;

import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/LogIn"})
public class LogIn extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogIn() {
        super();
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
            String email = request.getParameter("email");
            String pass = request.getParameter("password");

            if (email != null) {
                if (session.getDatabaseManager().IsUserPresent(email)) {
                    if (pass != null) {

                        if (session.getDatabaseManager().CheckPassword(new User(email, pass))) {
                            // metti persona nella sessione
//                            session.setPerson(session.getDatabaseManager().GetPerson(email));

                            //httpSession.setAttribute("emailUser", email);
                            response.sendRedirect("controlPanel.html");

                            //request.getRequestDispatcher("controlPanel.html").forward(request, response);
                        } else {
                            // pass sbagliata
                            // TODO rimanda da qualche parte perche c'è errore
                            httpSession.setAttribute("error", "pass sbagliata");
                            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
                        }

                    } else {
                        // pass corta
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "pass null");
                        request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
                    }
                } else {
                    // mancano email o password
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "mancano email o password");
                    request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
                }
            } else {
                // mancano email o password
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "email null");
                request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
            }


        } catch (Exception e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
