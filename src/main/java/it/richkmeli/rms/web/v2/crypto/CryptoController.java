package it.richkmeli.rms.web.v2.crypto;

import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.network.tcp.server.http.crypto.SecureConnectionJob;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OkResponse;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.rms.data.entity.device.DeviceDatabaseModel;
import it.richkmeli.rms.data.entity.rmc.model.Rmc;
import it.richkmeli.rms.data.entity.user.UserRepository;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;
import it.richkmeli.rms.web.util.RMSStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

@RestController
public class CryptoController {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;
    private final String password;
    SecureConnectionJob secureConnection = new SecureConnectionJob() {
        RMSSession rmsSession;

        @Override
        protected void doBeforeCryptoAction(HttpServletRequest request, HttpServletResponse response, String clientID) throws Exception {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            rmsSession = rmsServletManager.getRMSServerSession();

            rmsSession.setRmcID(clientID);
        }

        @Override
        protected void doFinalCryptoAction() throws Exception {

            //TODO fare controllo se rmcId è già presente. se sì, allora non fare la add
            if (rmsSession.getRmcID() != null) {
                if (!rmsSession.getRmcDatabaseManager().checkRmc(rmsSession.getRmcID())) {
                    rmsSession.getRmcDatabaseManager().addRMC(new Rmc("", rmsSession.getRmcID()));
                }
            }
        }
    };

    CryptoController(UserRepository userRepository) {
        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
    }


    @RequestMapping(name = "SecureConnection", path = "/SecureConnection", method = {RequestMethod.GET, RequestMethod.POST})
    public void deleteRmc() throws ServletException, IOException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {
            secureConnection.doAction(request, response);
        } catch (JServletException | IOException e) {
            request.getSession().setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }
    }

    /**
     * GET
     * get the encryption key respectively of a given specific user
     */
    @GetMapping(name = "encryptionKey", path = "/encryptionKey")
    public void getEncryptionKey() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);

            String id = attribMap.get("id");
            id = Crypto.decryptRC4(id, password);

            DeviceDatabaseModel deviceDatabaseSpringManager = rmsSession.getDeviceDatabaseManager();
            String encryptionKey = deviceDatabaseSpringManager.getEncryptionKey(id);

            // encrypt key server-side generated or error message with pre-shared password
            encryptionKey = Crypto.encryptRC4(encryptionKey, password);
            encryptionKey = "$" + encryptionKey + "#";

            String message = rmsServletManager.doDefaultProcessResponse(encryptionKey);
            AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, message));

        } catch (JServletException e) {
            AuthServletManager.print(response, e.getResponse());
        } catch (AuthDatabaseException e) {
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage()));
        } catch (Exception e) {
            //e.printStackTrace();
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
        }
    }
}
