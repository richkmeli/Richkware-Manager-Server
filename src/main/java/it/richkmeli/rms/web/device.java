package it.richkmeli.rms.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.crypto.util.RandomStringGenerator;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KOResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OKResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.StatusCode;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.util.Logger;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Servlet implementation class DevicesListServlet
 */
@WebServlet("/device")
public class device extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;
    private String password;


    public device() {
        super();
        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
//        PrintWriter out = response.getWriter();
//        HttpSession httpSession = request.getSession();
//        Session session = null;
//        try {
//            session = ServletManager.getServerSession(httpSession);
//
//            String out = null;
//
//            String user = session.getUser();
//            // Authentication
//            if (user != null) {
//                if (request.getParameterMap().containsKey("data0")) {
//                    String name = request.getParameter("data0");
//
//                    Device device = session.getDeviceDatabaseManager().getDevice(name);
//
//                    if (device.getassociatedUser().compareTo(session.getUser()) == 0 ||
//                            session.isAdmin()) {
//                        out = GenerateDevicesListJSON(device);
//                    } else {
//                        // TODO rimanda da qualche parte perche c'è errore
//                        httpSession.setAttribute("error", "non hai i privilegi");
//                        request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
//
//                    }
//
//
//                } else {
//                    // TODO rimanda da qualche parte perche c'è errore
//                    httpSession.setAttribute("error", "dispositivo non specificato");
//                    request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
//                }
//
//                // servlet response
//                PrintWriter printWriter = response.getWriter();
//                printWriter.println(out);
//                printWriter.flush();
//                printWriter.close();
//            } else {
//                // non loggato
//                // TODO rimanda da qualche parte perche c'è errore
//                httpSession.setAttribute("error", "non loggato");
//                request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
//            }
//        }catch (ServletException e){
//            httpSession.setAttribute("error", e);
//            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, IOException {
        PrintWriter out = resp.getWriter();
        //super.doPut(req, resp);
        HttpSession httpSession = req.getSession();
        RMSSession rmsSession = null;
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(req,resp);
            rmsSession = rmsServletManager.getRMSServerSession();

            if (req.getParameterMap().containsKey("data0") &&
                    req.getParameterMap().containsKey("data1") &&
                    req.getParameterMap().containsKey("data2")) {
                //String data
                String name = Crypto.decryptRC4(req.getParameter("data0"), password);
                //String name = data.substring(1, data.indexOf(","));

                // check in the DB if there is an entry with that name
                DeviceDatabaseManager deviceDatabaseManager = rmsSession.getDeviceDatabaseManager();
                Device oldDevice = deviceDatabaseManager.getDevice(name);

                // if this entry exists, then it's used to decrypt the encryption key in the DB
                String serverPort = req.getParameter("data1");
                //String serverPort = data.substring((data.indexOf(",") + 1), (data.length() - 1));
                String associatedUser = req.getParameter("data2");

                if (oldDevice == null) {
                    serverPort = Crypto.decryptRC4(serverPort, password);
                    associatedUser = Crypto.decryptRC4(associatedUser, password);
                } else {
                    serverPort = Crypto.decryptRC4(serverPort, oldDevice.getEncryptionKey());
                    associatedUser = Crypto.decryptRC4(associatedUser, oldDevice.getEncryptionKey());
                }

                String encryptionKey = RandomStringGenerator.generateAlphanumericString(keyLength);

                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                Device newDevice = new Device(
                        name,
                        req.getRemoteAddr(),
                        serverPort,
                        timeStamp,
                        encryptionKey,
                        associatedUser,
                        "",
                        "");

                Logger.info("SERVLET device, doGet: Device: " + name + " " + req.getRemoteAddr() + " " + serverPort + " " + timeStamp + " " + encryptionKey + " " + associatedUser + " ");

                if (oldDevice == null) {
                    deviceDatabaseManager.addDevice(newDevice);
                } else {
                    // do not change Encryption Key
                    newDevice.setEncryptionKey(oldDevice.getEncryptionKey());
                    deviceDatabaseManager.editDevice(newDevice);
                }

                out.println((new OKResponse(StatusCode.SUCCESS)).json());
                //req.getRequestDispatcher("index.html").forward(req, resp);
            } else {
                // argomenti non presenti
                // TODO rimanda da qualche parte perche c'è errore
//                Logger.error("SERVLET device, doGet: argomenti non presenti");
//                httpSession.setAttribute("error", "argomenti non presenti");
//                req.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(req, resp);
                out.println((new KOResponse(StatusCode.GENERIC_ERROR, "Parameters missing")).json());
            }
        } catch (JServletException e) {
            out.println(e.getKOResponseJSON());
        } catch (Exception e){
            //e.printStackTrace();
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }

        out.flush();
        out.close();

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, IOException {
        //if the code below is de-commented, this servlet disables DELETE
        //super.doDelete(req, resp);
        HttpSession httpSession = req.getSession();
        RMSSession rmsSession = null;
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(req,resp);
            rmsSession = rmsServletManager.getRMSServerSession();
        } catch (JServletException e) {
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(req, resp);

        }

        try {
            String out = null;

            String user = rmsSession.getUser();
            // Authentication
            if (user != null) {
                if (req.getParameterMap().containsKey("name")) {
                    String name = req.getParameter("name");

                    Device device = rmsSession.getDeviceDatabaseManager().getDevice(name);

                    if (device.getAssociatedUser().compareTo(rmsSession.getUser()) == 0 ||
                            rmsSession.isAdmin()) {
                        rmsSession.getDeviceDatabaseManager().removeDevice(name);
                        out = "deleted";
                    } else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        req.getRequestDispatcher(RMSServletManager.LOGIN_HTML).forward(req, resp);
                    }

                } else {
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    req.getRequestDispatcher(RMSServletManager.LOGIN_HTML).forward(req, resp);
                }
                // servlet response
                PrintWriter printWriter = resp.getWriter();
                printWriter.println(out);
                printWriter.flush();
                printWriter.close();
            } else {
                // non loggato
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "non loggato");
                req.getRequestDispatcher(RMSServletManager.LOGIN_HTML).forward(req, resp);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(req, resp);
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

