package it.richkmeli.rms.web.v1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OkResponse;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.util.RandomStringGenerator;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.entity.device.DeviceDatabaseModel;
import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.web.v1.util.RMSServletManager;
import it.richkmeli.rms.web.v1.util.RMSSession;
import it.richkmeli.rms.web.v1.util.RMSStatusCode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet(
        name = "device",
        description = "",
        urlPatterns = {"/device", "/Richkware-Manager-Server/device"}
)
public class device extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;

    public device() {
        super();
    }

    /**
     * PUT
     * put device to RMS, if for the device is te first call, all the payload is encrypted
     * with the preshared key, otherwise the first key (data0) is encrypted with preshared
     * and others with server-side generated key for specific device (returned at the first
     * call)
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            if (attribMap.containsKey(RMSServletManager.PAYLOAD_KEY.NAME) &&
                    attribMap.containsKey(RMSServletManager.PAYLOAD_KEY.SERVER_PORT) &&
                    attribMap.containsKey(RMSServletManager.PAYLOAD_KEY.ASSOCIATED_USER)) {

                String name = attribMap.get(RMSServletManager.PAYLOAD_KEY.NAME);
                String serverPort = attribMap.get(RMSServletManager.PAYLOAD_KEY.SERVER_PORT);
                String associatedUser = attribMap.get(RMSServletManager.PAYLOAD_KEY.ASSOCIATED_USER);

                String encryptionKey = RandomStringGenerator.generateAlphanumericString(keyLength);

                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                Device newDevice = new Device(
                        name,
                        request.getRemoteAddr(),
                        serverPort,
                        timeStamp,
                        encryptionKey,
                        associatedUser,
                        "",
                        "");

                Logger.info("SERVLET device, doPut: Device: " + name + " " + request.getRemoteAddr() + " " + serverPort + " " + timeStamp + " " + encryptionKey + " " + associatedUser + " ");

                // check in the DB if there is an entry with that name
                DeviceDatabaseModel deviceDatabaseSpringManager = rmsSession.getDeviceDatabaseManager();
                Device oldDevice = deviceDatabaseSpringManager.getDevice(name);

                String message = "";
                if (oldDevice == null) {
                    deviceDatabaseSpringManager.addDevice(newDevice);
                    message = "Device " + newDevice.getName() + " added.";
                } else {
                    // do not change Encryption Key
                    newDevice.setEncryptionKey(oldDevice.getEncryptionKey());
                    deviceDatabaseSpringManager.editDevice(newDevice);
                    message = "Device " + newDevice.getName() + " updated.";
                }

                message = rmsServletManager.doDefaultProcessResponse(message);
                AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, message));
            } else {
                // arguments not present
                AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, "Parameters missing"));
            }
        } catch (JServletException e) {
            AuthServletManager.print(response, e.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
        }


    }

    /**
     * DELETE
     * delete device from device list. Every user can delete only its own device, admin user
     * can delete all devices
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //if the code below is de-commented, this servlet disables DELETE
        //super.doDelete(request, response);
        HttpSession httpSession = request.getSession();
        RMSSession rmsSession = null;
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            rmsSession = rmsServletManager.getRMSServerSession();
        } catch (JServletException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);

        }

        try {
            String user = rmsSession.getUserID();
            // Authentication
            if (user != null) {
                if (request.getParameterMap().containsKey("name")) {
                    String name = request.getParameter("name");

                    Device device = rmsSession.getDeviceDatabaseManager().getDevice(name);

                    if (device.getAssociatedUser().compareTo(rmsSession.getUserID()) == 0 ||
                            rmsSession.isAdmin()) {
                        rmsSession.getDeviceDatabaseManager().removeDevice(name);
                        AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, "Device " + name + " deleted."));
                    } else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        request.getRequestDispatcher(RMSServletManager.LOGIN_HTML).forward(request, response);
                    }

                } else {
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    request.getRequestDispatcher(RMSServletManager.LOGIN_HTML).forward(request, response);
                }
            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "non loggato");
                request.getRequestDispatcher(RMSServletManager.LOGIN_HTML).forward(request, response);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }

    }

    private String GenerateDevicesListJSON(Device device/*, Session session*/) {
        //DatabaseManager databaseManager = session.getDatabaseManager();
        List<Device> devicesList = new ArrayList<>();

        devicesList.add(device);

        Type type = new TypeToken<List<Device>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String devicesListJSON = gson.toJson(devicesList, type);

        return devicesListJSON;
    }

}

