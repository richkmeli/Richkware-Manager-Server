package it.richkmeli.RMS.web.account;

import it.richkmeli.RMS.web.response.KOResponse;
import it.richkmeli.RMS.web.response.OKResponse;
import it.richkmeli.RMS.web.response.StatusCode;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.crypto.Crypto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet({"/SignUp"})
public class SignUp extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;
    private String password;

    public SignUp() {
        super();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);

            // check if is not already logged
            if (session.getUser() == null) {

                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                //    String name = request.getParameter("name");
                //    String lastname = request.getParameter("lastname");

                // se l'email è già presente nel DB
                if (session.getAuthDatabaseManager().isUserPresent(email)) {
                    // TODO password gia presente vuoi recuperarla? guarda se html o popup js
                    out.println((new KOResponse(StatusCode.ALREADY_REGISTERED)).json());
                } else {

                    session.getAuthDatabaseManager().addUser(new User(email, pass, false));
                    // set userID into the session
                    session.setUser(email);

                    out.println((new OKResponse(StatusCode.SUCCESS)).json());

                }
            } else {
                // already logged
                out.println((new KOResponse(StatusCode.ALREADY_LOGGED)).json());
            }

        } catch (Exception e) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }

        out.flush();
        out.close();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }

}
