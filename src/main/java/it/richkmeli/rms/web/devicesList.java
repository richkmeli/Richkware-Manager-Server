package it.richkmeli.rms.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KOResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OKResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.StatusCode;
import it.richkmeli.jframework.network.tcp.server.http.util.ServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.data.device.DeviceDatabaseManager;
import it.richkmeli.rms.data.device.model.Device;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet("/devicesList")
public class devicesList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public devicesList() {
        super();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request);
            rmsServletManager.doDefaultProcessRequest();
            rmsServletManager.checkLogin();

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            String message = rmsServletManager.doDefaultProcessResponse(GenerateDevicesListJSON(rmsSession));

            out.println((new OKResponse(StatusCode.SUCCESS, message)).json());

            out.flush();
            out.close();

        } catch (ServletException e) {
            out.println(e.getKOResponseJSON());
        } catch (DatabaseException e) {
            out.println((new KOResponse(StatusCode.DB_ERROR, e.getMessage())).json());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO quando il metodo è attivo, commenta il super
//        super.doDelete(req, resp);
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        RMSSession rmsSession = null;
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(req);
            rmsSession = rmsServletManager.getRMSServerSession();

            String user = rmsSession.getUser();
            // Authentication
            if (user != null) {
                // TODO togliere tutti i dispositivi di un un utente
                // TODO qui ci vuole la cancellazione di un singolo device o sempre di tutti?
            } else {
                // non loggato
                KOResponse r = new KOResponse(StatusCode.NOT_LOGGED);
                r.setMessage("You are not logged in. You will be redirected to the main page.");
                out.println(r.json());
            }
        } catch (ServletException e) {
            out.println(e.getKOResponseJSON());
        }

    }

    private String GenerateDevicesListJSON(RMSSession rmsSession) throws DatabaseException {
        DeviceDatabaseManager databaseManager = rmsSession.getDeviceDatabaseManager();
        List<Device> devicesList = null;

        if (rmsSession.isAdmin()) {
            // if the user is an Admin, it gets the list of all devices
            devicesList = databaseManager.getAllDevices();

        } else {
            devicesList = databaseManager.getUserDevices(rmsSession.getUser());
        }

        Type type = new TypeToken<List<Device>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String devicesListJSON = gson.toJson(devicesList, type);

        /*String devicesListJSON = "[ ";
        int index = 0;

        for (device device : devicesList) {
            String deviceJSON = *//*"'" + index + "' : {"*//* "{"
                    + "'name' : '" + device.getName() + "', "
                    + "'IP' : '" + device.getIp() + "', "
                    + "'serverPort' : '" + device.getServerPort() + "', "
                    + "'lastConnection' : '" + device.getLastConnection() + "', "
                    + "'encryptionKey' : '" + device.getEncryptionKey() + "'}";
            index++;
            devicesListJSON += deviceJSON;
            if (index < devicesList.size())
                devicesListJSON += ", ";
        }
        devicesListJSON += " ]";*/

        return devicesListJSON;
    }

}






/* CON FASI
try {
            String out = null;

            // devicesList ? encryption = true/false & phase = 1,2,3,... & kpub = ...
            //                 |                         |                   |            |
            if (request.getParameterMap().containsKey("encryption")) {
                String encryption = request.getParameter("encryption");
                if (encryption.compareTo("true") == 0) {
                    // encryption enabled
                    if (request.getParameterMap().containsKey("phase")) {
                        Integer phase = Integer.parseInt(request.getParameter("phase"));
                        switch (phase) {
                            case 1:
                                // phase 1: client sends its Public Key
                                String kpubC = null;
                                if (request.getParameterMap().containsKey("Kpub")) {
                                    kpubC = request.getParameter("Kpub");
                                }
                                // generation of public e private key of server
                                KeyPair keyPair = Crypto.GetGeneratedKeyPairRSA();

                                // [enc_(KpubC)(AESKey) , sign_(KprivS)(AESKey) , KpubS]
                                List<Object> res = Crypto.KeyExchangeAESRSA(keyPair, kpubC);
                                KeyExchangePayload keyExchangePayload = (KeyExchangePayload) res.get(0);
                                SecretKey AESsecretKey = (SecretKey) res.get(1);
                                // store keys into the session
                                session.setAESsecretKey(AESsecretKey);

                                out = GenerateKeyExchangePayloadJSON(keyExchangePayload);
                                break;
                            case 2:
                                // phase 2: Server sends encrypted data with AESKey to the client
                                out = GenerateDevicesListJSON(session);

                                out = Crypto.EncryptAES(out,session.getAESsecretKey());
                            default:
                                break;
                        }
                    } else {
                        // the value of encryption parameter is wrong
                        out = GenerateDevicesListJSON(session);
                    }
                }
            } else {
                // encryption disabled
                out = GenerateDevicesListJSON(session);
            }

            // servlet response
            PrintWriter printWriter = response.getWriter();
            printWriter.println(out);
            printWriter.flush();

        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
        }*/