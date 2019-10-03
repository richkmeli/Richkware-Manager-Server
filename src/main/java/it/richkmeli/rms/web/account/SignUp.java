package it.richkmeli.rms.web.account;

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
import java.util.Map;

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

        HttpSession httpSession = request.getSession();
        Session session = null;
        PrintWriter out = response.getWriter();

        try {
            session = ServletManager.getServerSession(httpSession);

            if (session.getUser() == null) {
                Map<String, String> attribMap = ServletManager.doDefaultProcessRequest(request);

                String email = attribMap.get("email");
                String pass = attribMap.get("password");
                if (session.getAuthDatabaseManager().isUserPresent(email)) {
                    out.println((new KOResponse(StatusCode.ALREADY_REGISTERED)).json());
                } else {
                    session.getAuthDatabaseManager().addUser(new User(email, pass, false));
                    session.setUser(email);
                    out.println((new OKResponse(StatusCode.SUCCESS)).json());
                }
            } else {
                out.println((new KOResponse(StatusCode.ALREADY_LOGGED)).json());
            }
        } catch (ServletException e) {
            out.println((new KOResponse(StatusCode.ALREADY_LOGGED)).json());
        } catch (DatabaseException e) {
            out.println((new KOResponse(StatusCode.DB_ERROR)).json());
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }

}
