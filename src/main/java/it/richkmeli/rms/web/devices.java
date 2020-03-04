package it.richkmeli.rms.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KOResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OKResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.StatusCode;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.data.device.DeviceDatabaseManager;
import it.richkmeli.rms.data.device.model.Device;
import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet("/devices")
public class devices extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public devices() {
        super();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request,response);
            rmsServletManager.doDefaultProcessRequest();
            rmsServletManager.checkLogin();

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            DeviceDatabaseManager databaseManager = rmsSession.getDeviceDatabaseManager();
            List<Device> devices = null;
            if (rmsSession.isAdmin()) {
                // if the user is an Admin, it gets the list of all devices
                devices = databaseManager.getAllDevices();
            } else {
                devices = databaseManager.getUserDevices(rmsSession.getUserID());
            }

            String deviceListJSON = generateDevicesListJSON(devices);

            String message = rmsServletManager.doDefaultProcessResponse(deviceListJSON);
            out.println((new OKResponse(StatusCode.SUCCESS, message)).json());

            out.flush();
            out.close();

        } catch (JServletException e) {
            out.println(e.getKOResponseJSON());
        } catch (DatabaseException e) {
            out.println((new KOResponse(StatusCode.DB_ERROR, e.getMessage())).json());
        } catch (Exception e){
            //e.printStackTrace();
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, IOException {
        super.doDelete(req, resp);
//        PrintWriter out = resp.getWriter();
//        HttpSession httpSession = req.getSession();
//        RMSSession rmsSession = null;
//        try {
//            RMSServletManager rmsServletManager = new RMSServletManager(req);
//            rmsSession = rmsServletManager.getRMSServerSession();
//
//            String user = rmsSession.getUser();
//            // Authentication
//            if (user != null) {
//                // togliere tutti i dispositivi di un un utente, qui ci vuole la cancellazione di un singolo device o sempre di tutti?
//            } else {
//                // non loggato
//                KOResponse r = new KOResponse(StatusCode.NOT_LOGGED);
//                r.setMessage("You are not logged in. You will be redirected to the main page.");
//                out.println(r.json());
//            }
//        } catch (ServletException e) {
//            out.println(e.getKOResponseJSON());
//        }

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

