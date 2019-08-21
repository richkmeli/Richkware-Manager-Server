package it.richkmeli.rms.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.Logger;
import it.richkmeli.rms.data.device.model.Device;
import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.response.KOResponse;
import it.richkmeli.rms.web.response.OKResponse;
import it.richkmeli.rms.web.response.StatusCode;
import it.richkmeli.rms.web.util.ServletManager;
import it.richkmeli.rms.web.util.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@WebServlet({"/rmc"})
public class rmc extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        Session session = null;
        PrintWriter out = resp.getWriter();

        List<RMC> clients = null;

        try {
            session = ServletManager.getServerSession(httpSession);

            if (session.getUser() != null) {
                Logger.info("rmc: user is logged correctly");
                Map<String, String> attribMap = ServletManager.doDefaultProcessRequest(req);

                if (session.isAdmin()) {
                    //ottiene tutti i client presenti sul db
                    Logger.info("rmc: Admin user");
                    clients = session.getRmcDatabaseManager().getAllRMCs();
                } else {
                    //ottiene tutti i client associati al suo account
                    Logger.info("rmc: Regular user");
                    clients = session.getRmcDatabaseManager().getRMCs(session.getUser());
                }
                String clientsFormatted = generateRmcListJSON(clients);
                Logger.info("rmc: clientsFormatted: " + clientsFormatted);
                String output = ServletManager.doDefaultProcessResponse(req, clientsFormatted);
                out.println(new OKResponse(StatusCode.SUCCESS, output).json());
            } else {
                out.println((new KOResponse(StatusCode.NOT_LOGGED)).json());
            }

            out.flush();
            out.close();
        } catch (it.richkmeli.rms.web.util.ServletException e) {
            out.println(new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage()).json());
        } catch (DatabaseException e) {
            out.println(new KOResponse(StatusCode.DB_ERROR, e.getMessage()).json());
        }
    }


    private String generateRmcListJSON(List<RMC> clients) {
        Type type = new TypeToken<List<Device>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String rmcListJSON = gson.toJson(clients, type);

        return rmcListJSON;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        super.doDelete(req, resp);
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        // param crittati se rmc

//        if (req.getParameterMap().containsKey("rmcid")) {
//            String rmc = req.getParameter("rmcid");
//            File secureDataServer = new File("TESTsecureDataServer.txt");
//            String serverKey = "testkeyServer";
//            Crypto.Server cryptoServer = new Crypto.Server();
//            cryptoServer.init(secureDataServer, serverKey, rmc, "");
//            cryptoServer.deleteClientData();
//        }

        try {
            session = ServletManager.getServerSession(httpSession);

            // todo controlla che stia cancellando un rmc di cui ha i permessi
            if (req.getParameterMap().containsKey("associatedUser") && req.getParameterMap().containsKey("rmcId")) {
                String associatedUser = req.getParameter("associatedUser");
                String id = req.getParameter("rmcId");
                boolean valid = true;
                if (!session.isAdmin()) {
                    if (session.getUser().equals(associatedUser)) {
                        RMC temp = new RMC(associatedUser, id);
                        if (!session.getRmcDatabaseManager().getRMCs(associatedUser).contains(temp)) {
                            valid = false;
                        }
                    } else {
                        out.println((new KOResponse(StatusCode.GENERIC_ERROR, "You are not allowed to delete this state.")).json());
                    }
                }
                if (valid) {
                    File secureDataServer = new File("TESTsecureDataServer.txt");
                    String serverKey = "testkeyServer";
                    Crypto.Server cryptoServer = new Crypto.Server();
                    cryptoServer.init(secureDataServer, serverKey, id, "");
                    cryptoServer.deleteClientData();
                    session.getRmcDatabaseManager().removeRMC(id);
                }

                out.println((new OKResponse(StatusCode.SUCCESS)).json());
//                if (session.getUser().equals(user)) {
//                    if (!session.isAdmin()) {
//                        //controlla che l'rmc sia associato all'utente
//                        RMC temp = new RMC(user, id);
//                        if (!session.getRmcDatabaseManager().getRMCs(user).contains(temp)) {
//                            valid = false;
//                        }
//                    }
//                    //cancella rmc se valid è true
//                    if (valid) {
//                        File secureDataServer = new File("TESTsecureDataServer.txt");
//                        String serverKey = "testkeyServer";
//                        Crypto.Server cryptoServer = new Crypto.Server();
//                        cryptoServer.init(secureDataServer, serverKey, id, "");
//                        cryptoServer.deleteClientData();
//                        session.getRmcDatabaseManager().removeRMC(id);
//                    }
//                }
            } else {
                //TODO errore: user does not match
                out.println((new KOResponse(StatusCode.GENERIC_ERROR, "Error in parameters passed.")).json());
            }

            // TODO cancella utente specifico, decidi se farlo solo da autenticato, magari con email o altro fattore di auth
//            session.getCryptoServer().deleteClientData();


        } catch (it.richkmeli.rms.web.util.ServletException e) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        } catch (DatabaseException e1) {
            out.println((new KOResponse(StatusCode.DB_ERROR, e1.getMessage())).json());
        }

    }
}
