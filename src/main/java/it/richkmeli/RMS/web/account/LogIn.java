package it.richkmeli.RMS.web.account;

import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.RMS.web.util.ServletException;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.jframework.util.Logger;

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



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = null;
        //Logger.i(ServletManager.printHTTPsession(httpSession));
        try {
            session = ServletManager.getServerSession(httpSession);
        }catch (ServletException e){
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }

        try {
            // check if is not already logged
            if (session.getUser() == null) {

                String email = request.getParameter("email");
                String pass = request.getParameter("password");

                if (email != null) {
                    if (session.getAuthDatabaseManager().isUserPresent(email)) {
                        if (pass != null) {
                            Boolean isAdmin = session.getAuthDatabaseManager().isAdmin(email);
                            if (session.getAuthDatabaseManager().checkPassword(email, pass)) {
                                // set userID into the session
                                session.setUser(email);
                                session.setAdmin(isAdmin);

                                //httpSession.setAttribute("emailUser", email);
                                response.sendRedirect(ServletManager.DEVICES_HTML);

                                //request.getRequestDispatcher("controlPanel.html").forward(request, response);
                            } else {
                                // pass sbagliata
                                // TODO rimanda da qualche parte perche c'è errore
                                httpSession.setAttribute("error", "pass sbagliata");
                                request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
                            }

                        } else {
                            // pass corta
                            // TODO rimanda da qualche parte perche c'è errore
                            httpSession.setAttribute("error", "pass null");
                            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
                        }
                    } else {
                        // mancano email o password
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "mancano email o password");
                        request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
                    }
                } else {
                    // mancano email o password
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "email null");
                    request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
                }
            } else {
                // already logged
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "già loggato");
                request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
            }


        } catch (Exception e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }
}
