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
import richk.RMS.util.RandomStringGenerator;

import javax.servlet.ServletException;
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

    private Session getServerSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        return session;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = getServerSession(request, response);

        try {

            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                if (request.getParameterMap().containsKey("name")) {
                    String name = request.getParameter("name");

                    Device device = session.getDatabaseManager().getDevice(name);

                    if(device.getUserAssociated().compareTo(session.getUser())==0 ||
                            session.isAdmin()){
                        out = GenerateDevicesListJSON(device);
                    }else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        request.getRequestDispatcher("login.html").forward(request, response);

                    }


                }else{
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    request.getRequestDispatcher("login.html").forward(request, response);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPut(req, resp);
        HttpSession httpSession = req.getSession();
        Session session = getServerSession(req, resp);

        try {
            //String data
            String name= req.getParameter("data0");
            //String name = data.substring(1, data.indexOf(","));

            // check in the DB if there is an entry with that name
            DatabaseManager db = session.getDatabaseManager();
            Device oldDevice = db.getDevice(name);

            // if this entry exists, then it's used to decrypt the encryption key in the DB
            String serverPort= req.getParameter("data1");
            //String serverPort = data.substring((data.indexOf(",") + 1), (data.length() - 1));
            String userAssociated = req.getParameter("data2");

            if (oldDevice == null) {
                serverPort = Crypto.DecryptRC4(serverPort, password);
                userAssociated = Crypto.DecryptRC4(userAssociated, password);
            } else {
                serverPort = Crypto.DecryptRC4(serverPort, oldDevice.getEncryptionKey());
                userAssociated = Crypto.DecryptRC4(userAssociated, oldDevice.getEncryptionKey());
            }

            String encryptionKey = RandomStringGenerator.GenerateString(keyLength);

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            Device newDevice = new Device(
                    name,
                    req.getRemoteAddr(),
                    serverPort,
                    timeStamp,
                    encryptionKey,
                    userAssociated);


            if (oldDevice == null) {
                db.addDevice(newDevice);
            } else {
                // do not change Encryption Key
                newDevice.setEncryptionKey(oldDevice.getEncryptionKey());
                db.editDevice(newDevice);
            }

            //req.getRequestDispatcher("index.html").forward(req, resp);

        } catch (Exception e) {
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher("JSP/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //if the code below is de-commented, this servlet disables DELETE
        //super.doDelete(req, resp);

        HttpSession httpSession = req.getSession();
        Session session = getServerSession(req, resp);

        try {
            String out = null;

            String user = session.getUser();
            // Authentication
            if (user != null) {
                if (req.getParameterMap().containsKey("name")) {
                    String name = req.getParameter("name");

                    Device device = session.getDatabaseManager().getDevice(name);

                    if(device.getUserAssociated().compareTo(session.getUser())==0 ||
                            session.isAdmin()){
                        session.getDatabaseManager().removeDevice(name);
                        out = "deleted";
                    }else {
                        // TODO rimanda da qualche parte perche c'è errore
                        httpSession.setAttribute("error", "non hai i privilegi");
                        req.getRequestDispatcher("login.html").forward(req, resp);
                    }

                }else{
                    // TODO rimanda da qualche parte perche c'è errore
                    httpSession.setAttribute("error", "dispositivo non specificato");
                    req.getRequestDispatcher("login.html").forward(req, resp);
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
                req.getRequestDispatcher("login.html").forward(req, resp);
            }
        } catch (Exception e) {
            // redirect to the JSP that handles errors
            httpSession.setAttribute("error", e);
            req.getRequestDispatcher("JSP/error.jsp").forward(req, resp);
        }

    }

    private String GenerateDevicesListJSON(Device device/*, Session session*/) throws ModelException {
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

