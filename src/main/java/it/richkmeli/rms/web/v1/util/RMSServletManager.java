package it.richkmeli.rms.web.v1.util;

import it.richkmeli.jframework.auth.data.AuthDatabaseModel;
import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.crypto.exception.CryptoException;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.entity.device.DeviceDatabaseModel;
import it.richkmeli.rms.data.entity.device.DeviceDatabaseSpringManager;
import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.data.entity.rmc.RmcDatabaseModel;
import it.richkmeli.rms.data.entity.rmc.RmcDatabaseSpringManager;
import it.richkmeli.rms.data.entity.user.AuthDatabaseSpringManager;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

public class RMSServletManager extends AuthServletManager {
    public static final String ERROR_JSP = "JSP/error.jsp";
    public static final String DEVICES_HTML = "devices.html";
    public static final String LOGIN_HTML = "login.html";
    public static final String HTTP_RMS_SESSION_NAME = "rms_session";
    private static RMSSession rmsSession;
    private String password;

    public RMSServletManager(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        try {
            rmsSession = getRMSServerSession();
            password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
        } catch (JServletException e) {
            //e.printStackTrace();
            Logger.error(e);
        }
    }

    public RMSServletManager(AuthServletManager authServletManager) {
        super(authServletManager);
        try {
            rmsSession = getRMSServerSession();
            password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
        } catch (JServletException e) {
            //e.printStackTrace();
            Logger.error(e);
        }
    }

    public static void setRMSServerSession(RMSSession rmsSession, HttpServletRequest request) {
        // http session
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(HTTP_RMS_SESSION_NAME, rmsSession);
    }

    @Override
    public void doSpecificProcessRequestAuth() throws JServletException {
        // check channel: rmc or webapp, if rmc secureconnection first (set something in session)
        if (attribMap.containsKey(Channel.CHANNEL)) {
            String channel = attribMap.get(Channel.CHANNEL);
            switch (channel) {
                case Channel.RMC:
                    rmsSession.setChannel(Channel.RMC);
                    // Extract encrypted data from map
                    String payload = attribMap.get(ENCRYPTED_PAYLOAD_KEY.DATA);
                    if (payload != null && !"".equalsIgnoreCase(payload)) {
                        String decryptedPayload = null;
                        try {
                            decryptedPayload = session.getCryptoServer().decrypt(payload);
                        } catch (CryptoException e) {
                            throw new JServletException(e);
                        }

                        jsonToMap(decryptedPayload);

                        // remove encrypted data from map
                        attribMap.remove(ENCRYPTED_PAYLOAD_KEY.DATA);
                    }
                    break;
                case Channel.WEBAPP:
                    rmsSession.setChannel(Channel.WEBAPP);
                    // all attributes are already in map
                    break;
                case Channel.RICHKWARE:
                    try {
                        rmsSession.setChannel(Channel.RICHKWARE);

                        // key "data" is encrypted with a specific key for the device identified from the field "data0"
                        // data0: contains deviceID (always encrypted with preshared key)
                        // data: contains serverPort, associatedUser, contains additional info for mobile device, ...
                        if (attribMap.containsKey(ENCRYPTED_PAYLOAD_KEY.DEVICE_ID) &&
                                attribMap.containsKey(ENCRYPTED_PAYLOAD_KEY.DATA)) {
                            String nameEnc = attribMap.get(ENCRYPTED_PAYLOAD_KEY.DEVICE_ID);
                            String dataEnc = attribMap.get(ENCRYPTED_PAYLOAD_KEY.DATA);
                            Logger.info("data0: " + nameEnc + " data: " + dataEnc);

                            String name = Crypto.decryptRC4(nameEnc, password);
                            attribMap.put(PAYLOAD_KEY.NAME, name);

                            // check in the DB if there is an entry with that name
                            DeviceDatabaseModel deviceDatabaseSpringManager = rmsSession.getDeviceDatabaseManager();
                            Device oldDevice = deviceDatabaseSpringManager.getDevice(name);

                            // if this entry exists, then it's used to decrypt the encryption key in the DB
                            String data = null;
                            // at the first call is encrypted with preshared key, at the following with server-side generated key
                            if (oldDevice == null) {
                                attribMap.put(PAYLOAD_KEY.STATE_IN_RMS, "new");
                                data = Crypto.decryptRC4(dataEnc, password);
                            } else {
                                attribMap.put(PAYLOAD_KEY.STATE_IN_RMS, "present");
                                try {
                                    data = Crypto.decryptRC4(dataEnc, oldDevice.getEncryptionKey());
                                } catch (CryptoException ce) {
                                    if (ce.getMessage().contains("Key is not correct.")) {
                                        Logger.info("Probable deletion of the key saved locally. Decrypting using default key.");
                                        data = Crypto.decryptRC4(dataEnc, password);
                                    }
                                }
                            }
                            jsonToMap(data);
                            // remove encrypted data from map
                            attribMap.remove(ENCRYPTED_PAYLOAD_KEY.DEVICE_ID);
                            attribMap.remove(ENCRYPTED_PAYLOAD_KEY.DATA);
                        }

                    } catch (CryptoException e) {
                        Logger.error("ServletManager, servlet: " + servletPath + ". ", e);
                        throw new JServletException(new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));

                    } catch (AuthDatabaseException e) {
                        Logger.error("ServletManager, servlet: " + servletPath + ". ", e);
                        throw new JServletException(new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage()));

                    }
                    break;
                default:
                    Logger.error("ServletManager, servlet: " + servletPath + ". channel " + channel + " unknown");
                    throw new JServletException(new KoResponse(RMSStatusCode.CHANNEL_UNKNOWN, "channel " + channel + " unknown"));
            }
        } else {
            Logger.error("ServletManager, servlet: " + servletPath + ". channel key is not present.");
            throw new JServletException(new KoResponse(RMSStatusCode.CHANNEL_UNKNOWN, "channel key is not present"));
        }
    }

    private void jsonToMap(String data) {
        if (data != null && !"".equalsIgnoreCase(data)) {
            JSONObject decryptedPayloadJSON = new JSONObject(data);
            // add each attribute inside encrypted data to map
            for (String key : decryptedPayloadJSON.keySet()) {
                String value = decryptedPayloadJSON.getString(key);
                attribMap.put(key, value);
            }
        }
    }

    @Override
    public String doSpecificProcessResponseAuth(String input) throws JServletException {
        // default: input as output
        String output = input;

        String channel = rmsSession.getChannel();
        if (channel != null) {
            switch (channel) {
                case Channel.RMC:
                    rmsSession.setChannel(Channel.RMC);
                    try {
                        output = session.getCryptoServer().encrypt(input);
                    } catch (CryptoException e) {
                        e.printStackTrace();
                        throw new JServletException(e);
                    }
                    break;
                case Channel.WEBAPP:
                    rmsSession.setChannel(Channel.WEBAPP);
                    break;
                case Channel.RICHKWARE:
                    rmsSession.setChannel(Channel.RICHKWARE);
                    break;
                default:
                    Logger.error("ServletManager, servlet: " + servletPath + ". + channel " + channel + " unknown");
                    throw new JServletException(new KoResponse(RMSStatusCode.CHANNEL_UNKNOWN, "channel " + channel + " unknown"));
            }
        } else {
            Logger.error("ServletManager, servlet: " + servletPath + ". + channel(server session) is null");
            throw new JServletException(new KoResponse(RMSStatusCode.CHANNEL_UNKNOWN, "channel(server session) is null"));
        }
        setRMSServerSession(rmsSession, httpServletRequest);
        return output;
    }

    @Override
    protected AuthDatabaseModel getAuthDatabaseManagerInstance() throws AuthDatabaseException {
        return AuthDatabaseSpringManager.getInstance();
    }

    public RMSSession getRMSServerSession() throws JServletException {
        //try {
        return getRMSServerSession(
                DeviceDatabaseSpringManager.getInstance(),
                RmcDatabaseSpringManager.getInstance()
                //new DeviceDatabaseJframeworkManager()
        );
        /*} catch (AuthDatabaseException e) {
            throw new JServletException(e);
        }*/
    }

    public RMSSession getRMSServerSession(DeviceDatabaseModel deviceDatabaseModel, RmcDatabaseModel rmcDatabaseModel) throws JServletException {
        // http session
        HttpSession httpSession = httpServletRequest.getSession();
        // server session
        RMSSession rmsSession1 = (RMSSession) httpSession.getAttribute(HTTP_RMS_SESSION_NAME);
        authSession = getAuthServerSession();
        if (rmsSession1 == null) {
            try {
                rmsSession = new RMSSession(authSession, deviceDatabaseModel, rmcDatabaseModel);
                setRMSServerSession(rmsSession, httpServletRequest);
            } catch (AuthDatabaseException e) {
                throw new JServletException(e);
            }
        } else {
            if (rmsSession1.getUserID() == null) {
                Logger.info("RMSSession OK, AuthSession initializing (Login)");
                rmsSession = new RMSSession(rmsSession, authSession);
                setRMSServerSession(rmsSession, httpServletRequest);
            } else {
                rmsSession = rmsSession1;
            }
        }
        return rmsSession;
    }

    public static class Channel {
        public static final String CHANNEL = "channel";
        public static final String WEBAPP = "webapp";
        public static final String RMC = "rmc";
        public static final String RICHKWARE = "richkware";
    }

    public static class ENCRYPTED_PAYLOAD_KEY {
        public static final String DEVICE_ID = "data0";
        public static final String DATA = "data";
    }

    public static class PAYLOAD_KEY {
        public static final String NAME = "name";
        public static final String STATE_IN_RMS = "stateInRMS";
        public static final String ASSOCIATED_USER = "associatedUser";
        public static final String SERVER_PORT = "serverPort";
    }

}

