package it.richkmeli.rms.web.account;

import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.response.KOResponse;
import it.richkmeli.rms.web.response.OKResponse;
import it.richkmeli.rms.web.response.StatusCode;
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

                //TODO change into client=RMC or client=WEB
                boolean encryption = false;
                if (request.getParameterMap().containsKey("encryption")) {
                    if ("true".equalsIgnoreCase(request.getParameter("encryption"))) {
                        encryption = true;
                    }
                }

                String email = "";// = request.getParameter("email");
                String pass = "";// = request.getParameter("password");

                if (encryption) {  // RMC
                    String payload = request.getParameter("data");
                    String decryptedPayload = session.getCryptoServer().decrypt(payload);
                    JSONObject decryptedPayloadJSON = new JSONObject(decryptedPayload);
                    email = decryptedPayloadJSON.getString("email");
                    pass = decryptedPayloadJSON.getString("password");

                } else {
                    // WEBAPP
                    email = request.getParameter("email");
                    pass = request.getParameter("password");
                }

                //String email = request.getParameter("email");
                //String pass = request.getParameter("password");

                System.out.println("email: " + email + " password: " + pass);

                if (session.getAuthDatabaseManager().isUserPresent(email)) {
                    boolean isAdmin = session.getAuthDatabaseManager().isAdmin(email);
                    if (session.getAuthDatabaseManager().checkPassword(email, pass)) {
                        // set userID into the session
                        session.setUser(email);
                        session.setAdmin(isAdmin);

                        //TODO rimuovere
//                        session.setRmcID("RMC-001");
                        if (session.getRmcID() != null && session.getUser() != null) {
                            session.getRmcDatabaseManager().addRMC(new RMC(session.getUser(), session.getRmcID()));
                        }

                        JSONObject adminInfo = new JSONObject();
                        adminInfo.put("admin", isAdmin);

                        String adminInfoS = adminInfo.toString();

                        // encrypt response
                        if(encryption){
                            adminInfoS = session.getCryptoServer().encrypt(adminInfoS);
                        }

                        out.println((new OKResponse(StatusCode.SUCCESS, adminInfoS)).json());
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
