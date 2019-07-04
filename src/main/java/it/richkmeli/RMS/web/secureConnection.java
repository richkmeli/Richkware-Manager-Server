package it.richkmeli.RMS.web;

import it.richkmeli.RMS.web.response.KOResponse;
import it.richkmeli.RMS.web.response.OKResponse;
import it.richkmeli.RMS.web.response.StatusCode;
import it.richkmeli.RMS.web.util.ServletException;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.jframework.util.Logger;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

@WebServlet("/secureConnection")
public class secureConnection extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String encryptionKey;

    public secureConnection() {
        super();
        encryptionKey = ResourceBundle.getBundle("configuration").getString("encryptionkey");

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        } catch (ServletException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }

        try {
            PrintWriter out = response.getWriter();

            File secureDataServer = new File("TESTsecureDataServer.txt");
            String serverKey = "testkeyServer";
            // TODO rendere unico il client ID, che sarebbe il codice del RMC, vedi se passarlo come parametro o generato o altro fattore

            if (request.getParameterMap().containsKey("clientID")) {
                String clientID = request.getParameter("clientID");

                String clientResponse = "";
                if (request.getParameterMap().containsKey("data")) {
                    clientResponse = request.getParameter("data");

                    // TODO metti info in sessione per farla prendere dalle altre servlet, solo questa fa init, altrimenti bisogna mettere il device id davanti in tutte le richieste
                    String serverResponse = session.getCryptoServer().init(secureDataServer, serverKey, clientID, clientResponse);

                    int serverState = new JSONObject(serverResponse).getInt("state");
                    String serverPayload = new JSONObject(serverResponse).getString("payload");

                    // TODO cambia con OKResponse
                    out.println(serverPayload);
                    out.flush();
                } else {
                    out.println((new KOResponse(StatusCode.SECURE_CONNECTION, "data parameter not present")).json());
                }
            } else {
                out.println((new KOResponse(StatusCode.SECURE_CONNECTION, "clientID parameter not present")).json());
            }
        } catch (Exception e) {
            Logger.error("SERVLET encryptionKey, doGet", e);
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        super.doDelete(req, resp);
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        try {
            session = ServletManager.getServerSession(httpSession);

           // TODO cancella utente specifico, decidi se farlo solo da autenticato, magari con email o altro fattore di auth

        }catch (ServletException e){
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }

    }
}
