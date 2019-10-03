package it.richkmeli.RMS.web.account;

import it.richkmeli.RMS.web.response.KOResponse;
import it.richkmeli.RMS.web.response.OKResponse;
import it.richkmeli.RMS.web.response.StatusCode;
import it.richkmeli.RMS.web.util.ServletException;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.jframework.database.DatabaseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet({"/LogIn"})
public class LogIn extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogIn() {
        super();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
//        System.out.println("Received request! data contained: " + request.getParameterNames());
        HttpSession httpSession = request.getSession();
        Session session = null;
        PrintWriter out = response.getWriter();
        //Logger.i(ServletManager.printHTTPsession(httpSession));
        try {
            session = ServletManager.getServerSession(httpSession);

            // check if is not already logged
            if (session.getUser() == null) {

                String email = request.getParameter("email");
                String pass = request.getParameter("password");

                System.out.println("email: " + email + " password: " + pass);


                if (session.getAuthDatabaseManager().isUserPresent(email)) {
                    boolean isAdmin = session.getAuthDatabaseManager().isAdmin(email);
                    if (session.getAuthDatabaseManager().checkPassword(email, pass)) {
                        // set userID into the session
                        session.setUser(email);
                        session.setAdmin(isAdmin);

                        out.println((new OKResponse(StatusCode.SUCCESS)).json());
                    } else {
                        // pass sbagliata
                        out.println((new KOResponse(StatusCode.WRONG_PASSWORD)).json());
                    }
                } else {
                    // mail non trovata
                    out.println((new KOResponse(StatusCode.ACCOUNT_NOT_FOUND)).json());
                }
            } else {
                // already logged
                out.println((new KOResponse(StatusCode.ALREADY_LOGGED)).json());
            }

            out.flush();
            out.close();
        } catch (ServletException e) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());

        } catch (DatabaseException e) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }
}
