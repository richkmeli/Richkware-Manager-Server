package it.richkmeli.rms.web.v1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OkResponse;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.data.model.device._DeviceDatabaseManager;
import it.richkmeli.rms.data.model.device.Device;
import it.richkmeli.rms.web.v1.util.RMSServletManager;
import it.richkmeli.rms.web.v1.util.RMSSession;
import it.richkmeli.rms.web.v1.util.RMSStatusCode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet(
        name = "devices",
        description = "",
        urlPatterns = {"/devices"}
)
public class devices extends HttpServlet {

    /**
     * GET
     * get the device list. Every user can view only its own devices, admin users have
     * visibility to view all devices.
     *
     * @param request
     * @param response
     * @throws IOException
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            rmsServletManager.doDefaultProcessRequest();
            rmsServletManager.checkLogin();

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            _DeviceDatabaseManager databaseManager = rmsSession.getDeviceDatabaseManager();
            List<Device> devices = null;
            if (rmsSession.isAdmin()) {
                // if the user is an Admin, it gets the list of all devices
                devices = databaseManager.getAllDevices();
            } else {
                devices = databaseManager.getUserDevices(rmsSession.getUserID());
            }

            String deviceListJSON = generateDevicesListJSON(devices);

            String message = rmsServletManager.doDefaultProcessResponse(deviceListJSON);
            AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, message));

        } catch (JServletException e) {
            AuthServletManager.print(response, e.getResponse());
        } catch (DatabaseException e) {
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage()));
        } catch (Exception e) {
            //e.printStackTrace();
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
        }
    }

    private String generateDevicesListJSON(List<Device> devices) {
        Type type = new TypeToken<List<Device>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String devicesJSON = gson.toJson(devices, type);

        return devicesJSON;
    }

}

