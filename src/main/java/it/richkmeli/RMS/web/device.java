package it.richkmeli.RMS.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.RMS.data.device.DeviceDatabaseManager;
import it.richkmeli.RMS.data.device.model.Device;
import it.richkmeli.RMS.web.util.ServletException;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.jcrypto.Crypto;
import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.jcrypto.util.RandomStringGenerator;
import it.richkmeli.jframework.util.Logger;

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
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        }catch (ServletException e){
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }
        try {

            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                if (request.getParameterMap().containsKey("name")) {
                    String name = request.getParameter("name");

                    Device device = session.getDeviceDatabaseManager().getDevice(name);

                    if (device.getUserAssociated().compareTo(session.getUser()) == 0 ||
                            session.isAdmin()) {
                        out = GenerateDevicesListJSON(device);
                    } else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);

                    }


                } else {
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
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
                request.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(request, response);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, IOException {
        //super.doPut(req, resp);
        HttpSession httpSession = req.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        }catch (ServletException e){
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher(ServletManager.ERROR_JSP).forward(req, resp);

        }

        try {
            if (req.getParameterMap().containsKey("data0") &&
                    req.getParameterMap().containsKey("data1") &&
                    req.getParameterMap().containsKey("data2")) {
                //String data
                String name = req.getParameter("data0");
                //String name = data.substring(1, data.indexOf(","));

                // check in the DB if there is an entry with that name
                DeviceDatabaseManager deviceDatabaseManager = session.getDeviceDatabaseManager();
                Device oldDevice = deviceDatabaseManager.getDevice(name);

                // if this entry exists, then it's used to decrypt the encryption key in the DB
                String serverPort = req.getParameter("data1");
                //String serverPort = data.substring((data.indexOf(",") + 1), (data.length() - 1));
                String userAssociated = req.getParameter("data2");

                if (oldDevice == null) {
                    serverPort = Crypto.DecryptRC4(serverPort, password);
                    userAssociated = Crypto.DecryptRC4(userAssociated, password);
                } else {
                    serverPort = Crypto.DecryptRC4(serverPort, oldDevice.getEncryptionKey());
                    userAssociated = Crypto.DecryptRC4(userAssociated, oldDevice.getEncryptionKey());
                }

                String encryptionKey = RandomStringGenerator.GenerateAlphanumericString(keyLength);

                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                Device newDevice = new Device(
                        name,
                        req.getRemoteAddr(),
                        serverPort,
                        timeStamp,
                        encryptionKey,
                        userAssociated);

                Logger.info("SERVLET device, doGet: Device: " + name + " " + req.getRemoteAddr() + " " + serverPort + " " + timeStamp + " " + encryptionKey + " " + userAssociated + " ");

                if (oldDevice == null) {
                    deviceDatabaseManager.addDevice(newDevice);
                } else {
                    // do not change Encryption Key
                    newDevice.setEncryptionKey(oldDevice.getEncryptionKey());
                    deviceDatabaseManager.editDevice(newDevice);
                }

                //req.getRequestDispatcher("index.html").forward(req, resp);
            } else {
                // argomenti non presenti
                // TODO rimanda da qualche parte perche c'è errore
                Logger.error("SERVLET device, doGet: argomenti non presenti");
                httpSession.setAttribute("error", "argomenti non presenti");
                req.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(req, resp);
            }
        } catch (Exception e) {
            Logger.error("SERVLET device, doGet", e);
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher(ServletManager.ERROR_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, IOException {
        //if the code below is de-commented, this servlet disables DELETE
        //super.doDelete(req, resp);
        HttpSession httpSession = req.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        }catch (ServletException e){
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher(ServletManager.ERROR_JSP).forward(req, resp);

        }

        try {
            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                if (req.getParameterMap().containsKey("name")) {
                    String name = req.getParameter("name");

                    Device device = session.getDeviceDatabaseManager().getDevice(name);

                    if (device.getUserAssociated().compareTo(session.getUser()) == 0 ||
                            session.isAdmin()) {
                        session.getDeviceDatabaseManager().removeDevice(name);
                        out = "deleted";
                    } else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        req.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(req, resp);
                    }

                } else {
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    req.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(req, resp);
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
                req.getRequestDispatcher(ServletManager.LOGIN_HTML).forward(req, resp);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher(ServletManager.ERROR_JSP).forward(req, resp);
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

