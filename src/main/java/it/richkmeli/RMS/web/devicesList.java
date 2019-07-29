package it.richkmeli.RMS.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.RMS.data.device.DeviceDatabaseManager;
import it.richkmeli.RMS.data.device.model.Device;
import it.richkmeli.RMS.web.response.KOResponse;
import it.richkmeli.RMS.web.response.OKResponse;
import it.richkmeli.RMS.web.response.StatusCode;
import it.richkmeli.RMS.web.util.ServletException;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.jframework.crypto.CryptoCompat;
import it.richkmeli.jframework.crypto.KeyExchangePayloadCompat;
import it.richkmeli.jframework.crypto.exception.CryptoException;
import it.richkmeli.jframework.database.DatabaseException;

import javax.crypto.SecretKey;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.security.KeyPair;
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
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);

            String user = session.getUser();
            // Authentication
            if (user != null) {
                // devicesList ? encryption = true/false & phase = 1,2,3,... & kpub = ...
                //                 |                         |                   |            |
                if (request.getParameterMap().containsKey("encryption")) {
                    String encryption = request.getParameter("encryption");
                    if (encryption.compareTo("true") == 0) {
                        // encryption enabled
                        String kpubC = null;
                        if (request.getParameterMap().containsKey("Kpub")) {
                            kpubC = request.getParameter("Kpub");
                        }
                        // generation of public e private key of server
                        KeyPair keyPair = CryptoCompat.getGeneratedKeyPairRSA();

                        // [enc_(KpubC)(AESKey) , sign_(KprivS)(AESKey) , KpubS]
                        List<Object> res = CryptoCompat.keyExchangeAESRSA(keyPair, kpubC);
                        KeyExchangePayloadCompat keyExchangePayload = (KeyExchangePayloadCompat) res.get(0);
                        SecretKey AESsecretKey = (SecretKey) res.get(1);

                        // encrypt data (devices List) with AES secret key
                        String enc = CryptoCompat.encryptAES(GenerateDevicesListJSON(session), String.valueOf(AESsecretKey));
                        ;
                        // add data to the object
                        keyExchangePayload.setData(enc);

                        String encPayload = GenerateKeyExchangePayloadJSON(keyExchangePayload);

                        out.println((new OKResponse(StatusCode.SUCCESS, encPayload)).json());
                    }
                } else {
                    // encryption disabled
                    String message = GenerateDevicesListJSON(session);
                    out.println((new OKResponse(StatusCode.SUCCESS, message)).json());
                }

                out.flush();
                out.close();
            } else {
                // non loggato
                out.println((new KOResponse(StatusCode.NOT_LOGGED)).json());
            }
        }catch (ServletException e){
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        } catch (CryptoException e) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
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
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);

            String user = session.getUser();
            // Authentication
            if (user != null) {
                // TODO togliere tutti i dispositivi di un un utente
                // TODO qui ci vuole la cancellazione di un singolo device o sempre di tutti?
            } else {
                // non loggato
                out.println((new KOResponse(StatusCode.NOT_LOGGED)).json());
            }
        }catch (ServletException e){
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }

    }

    private String GenerateDevicesListJSON(Session session) throws DatabaseException {
        DeviceDatabaseManager databaseManager = session.getDeviceDatabaseManager();
        List<Device> devicesList = null;

        if (session.isAdmin()) {
            // if the user is an Admin, it gets the list of all devices
            devicesList = databaseManager.refreshDevice();

        } else {
            devicesList = databaseManager.refreshDevice(session.getUser());
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

    private String GenerateKeyExchangePayloadJSON(KeyExchangePayloadCompat keyExchangePayload) {
        String keyExchangePayloadJSON;// = "[ ";
        keyExchangePayloadJSON = /*"'" + index + "' : {"*/ "{"
                + "'encryptedAESsecretKey' : '" + keyExchangePayload.getEncryptedAESsecretKey() + "', "
                + "'signatureAESsecretKey' : '" + keyExchangePayload.getSignatureAESsecretKey() + "', "
                + "'kpubServer' : '" + keyExchangePayload.getKpubServer() + "', "
                + "'data' : '" + keyExchangePayload.getData() + "'}";
        //keyExchangePayloadJSON += " ]";
        return keyExchangePayloadJSON;
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