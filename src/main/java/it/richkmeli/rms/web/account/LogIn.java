package it.richkmeli.rms.web.account;

import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.response.KOResponse;
import it.richkmeli.rms.web.response.OKResponse;
import it.richkmeli.rms.web.response.StatusCode;
import it.richkmeli.rms.web.util.ServletException;
import it.richkmeli.rms.web.util.ServletManager;
import it.richkmeli.rms.web.util.Session;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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
        PrintWriter out = response.getWriter();

        try {
            session = ServletManager.getServerSession(httpSession);

            // check if is not already logged
            if (session.getUser() == null) {
                Map<String, String> attribMap = ServletManager.doDefaultProcessRequest(request);

                String email = attribMap.get("email");// = request.getParameter("email");
                String pass = attribMap.get("password");
                ;// = request.getParameter("password");

                System.out.println("email: " + email + " password: " + pass);

                if (session.getAuthDatabaseManager().isUserPresent(email)) {
                    boolean isAdmin = session.getAuthDatabaseManager().isAdmin(email);
                    if (session.getAuthDatabaseManager().checkPassword(email, pass)) {
                        // set userID into the session
                        session.setUser(email);
                        session.setAdmin(isAdmin);


                        if (session.getChannel().equalsIgnoreCase(ServletManager.Channel.RMC)) {
                            RMC rmc = new RMC(session.getUser(), session.getRmcID());
                            if (!session.getRmcDatabaseManager().checkRmcUserPair(rmc)) {
                                if (session.getRmcDatabaseManager().checkRmcUserPair(new RMC("", session.getRmcID()))) {
                                    session.getRmcDatabaseManager().editRMC(rmc);
                                } else {
                                    session.getRmcDatabaseManager().addRMC(rmc);
                                }
                            }
                        }

                        JSONObject adminInfo = new JSONObject();
                        adminInfo.put("admin", isAdmin);

                        String output = ServletManager.doDefaultProcessResponse(request, adminInfo.toString());

                        out.println((new OKResponse(StatusCode.SUCCESS, output)).json());
                    } else {
                        // pass sbagliata
                        out.println((new KOResponse(StatusCode.WRONG_PASSWORD)).json());
                    }
                } else {
                    // mail non trovata
                    out.println((new KOResponse(StatusCode.ACCOUNT_NOT_FOUND, "user: " + request.getAttribute("email") + "; password: " + request.getAttribute("password"))).json());
                }
            } else {
                // already logged
                out.println((new KOResponse(StatusCode.ALREADY_LOGGED)).json());
            }
        } catch (ServletException e) {
            if (e.getMessage().contains("java.lang.Exception: decrypt, crypto not initialized, current stare: 0")) {
                out.println((new KOResponse(StatusCode.SECURE_CONNECTION, e.getMessage())).json());
            } else {
                out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
            }
        } catch (Exception e) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }

        out.flush();
        out.close();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }
}
