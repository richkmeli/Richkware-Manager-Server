package richk.RMS.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.database.DatabaseManager;
import richk.RMS.model.Device;
import richk.RMS.model.ModelException;
import richk.RMS.util.Crypto;
import richk.RMS.util.KeyExchangePayload;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
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
@WebServlet("/DevicesList")
public class DevicesList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DevicesList() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = (Session) httpSession.getAttribute("session");
        if (session == null) {
            try {
                session = new Session();
                httpSession.setAttribute("session", session);
            } catch (DatabaseException e) {
                httpSession.setAttribute("error", e);
                request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
            }
        }


        try {

            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                // DevicesList ? encryption = true/false & phase = 1,2,3,... & kpub = ...
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
                        KeyPair keyPair = Crypto.GetGeneratedKeyPairRSA();

                        // [enc_(KpubC)(AESKey) , sign_(KprivS)(AESKey) , KpubS]
                        List<Object> res = Crypto.KeyExchangeAESRSA(keyPair, kpubC);
                        KeyExchangePayload keyExchangePayload = (KeyExchangePayload) res.get(0);
                        SecretKey AESsecretKey = (SecretKey) res.get(1);
                        // encrypt data (devices List) with AES secret key
                        out = Crypto.EncryptAES(GenerateDevicesListJSON(session), AESsecretKey);
                        // add data to the object
                        keyExchangePayload.setData(out);

                        out = GenerateKeyExchangePayloadJSON(keyExchangePayload);
                    }
                } else {
                    // encryption disabled
                    out = GenerateDevicesListJSON(session);
                }

                // servlet response
                PrintWriter printWriter = response.getWriter();
                printWriter.println(out);
                printWriter.flush();
                printWriter.close();
            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "non loggato");
                request.getRequestDispatcher("login.html").forward(request, response);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private String GenerateDevicesListJSON(Session session) throws ModelException {
        DatabaseManager databaseManager = session.getDatabaseManager();
        List<Device> devicesList = null;

        if(session.isAdmin()){
            // if the user is an Admin, it gets the list of all devices
            devicesList = databaseManager.refreshDevice();

        }else{
            devicesList = databaseManager.refreshDevice(session.getUser());
        }

        Type type = new TypeToken<List<Device>>(){}.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String devicesListJSON = gson.toJson(devicesList, type);

        /*String devicesListJSON = "[ ";
        int index = 0;

        for (Device device : devicesList) {
            String deviceJSON = *//*"'" + index + "' : {"*//* "{"
                    + "'name' : '" + device.getName() + "', "
                    + "'IP' : '" + device.getIP() + "', "
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

    private String GenerateKeyExchangePayloadJSON(KeyExchangePayload keyExchangePayload) throws ModelException {
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






/*try {
            String out = null;

            // DevicesList ? encryption = true/false & phase = 1,2,3,... & kpub = ...
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
            request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
        }*/