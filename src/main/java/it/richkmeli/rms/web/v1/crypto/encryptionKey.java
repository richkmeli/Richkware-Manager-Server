//package it.richkmeli.rms.web.v1.crypto;
//
//import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
//import it.richkmeli.jframework.auth.web.util.AuthServletManager;
//import it.richkmeli.jframework.crypto.Crypto;
//import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
//import it.richkmeli.jframework.network.tcp.server.http.payload.response.OkResponse;
//import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
//import it.richkmeli.rms.data.entity.device.DeviceDatabaseModel;
//import it.richkmeli.rms.web.util.RMSServletManager;
//import it.richkmeli.rms.web.util.RMSSession;
//import it.richkmeli.rms.web.util.RMSStatusCode;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//import java.util.ResourceBundle;
//
//
///**
// * Servlet implementation class encryptionKey
// */
//@WebServlet(
//        name = "encryptionKey",
//        description = "",
//        urlPatterns = {"/encryptionKey", "/Richkware-Manager-Server/encryptionKey"}
//)
//
//public class encryptionKey extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//    private static final int keyLength = 32;
//    private String password;
//
//    public encryptionKey() {
//        super();
//        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
//    }
//
//    /**
//     * GET
//     * get the encryption key respectively of a given specific user
//     *
//     * @param request
//     * @param response
//     * @throws IOException
//     */
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
//            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
//            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);
//
//            String id = attribMap.get("id");
//            id = Crypto.decryptRC4(id, password);
//
//            DeviceDatabaseModel deviceDatabaseSpringManager = rmsSession.getDeviceDatabaseManager();
//            String encryptionKey = deviceDatabaseSpringManager.getEncryptionKey(id);
//
//            // encrypt key server-side generated or error message with pre-shared password
//            encryptionKey = Crypto.encryptRC4(encryptionKey, password);
//            encryptionKey = "$" + encryptionKey + "#";
//
//            String message = rmsServletManager.doDefaultProcessResponse(encryptionKey);
//            AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, message));
//
//        } catch (JServletException e) {
//            AuthServletManager.print(response, e.getResponse());
//        } catch (AuthDatabaseException e) {
//            AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage()));
//        } catch (Exception e) {
//            //e.printStackTrace();
//            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
//        }
//
//    }
//
//}
