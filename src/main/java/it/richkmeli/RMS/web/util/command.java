package it.richkmeli.RMS.web.util;

import it.richkmeli.RMS.web.response.KOResponse;
import it.richkmeli.RMS.web.response.OKResponse;
import it.richkmeli.RMS.web.response.StatusCode;
import it.richkmeli.jcrypto.KeyExchangePayloadCompat;
import it.richkmeli.jframework.database.DatabaseException;
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

@WebServlet({"/command"})
public class command extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        try {
            session = ServletManager.getServerSession(httpSession);

            if (req.getParameterMap().containsKey("data0") && req.getParameterMap().containsKey("data1")) {

                String deviceName = req.getParameter("data0");
                String requestor = req.getParameter("data1");

                String output = null;

                if (requestor.equalsIgnoreCase("agent")) {
                    output = session.getDeviceDatabaseManager().getCommands(deviceName);
                } else if (requestor.equalsIgnoreCase("client")) {
                    output = session.getDeviceDatabaseManager().getCommandsOutput(deviceName);
                    if (output.isEmpty()) {

                    }
                    session.getDeviceDatabaseManager().setCommandsOutput(deviceName, "");
                }

                if (!output.isEmpty()) {
                    out.println((new OKResponse(StatusCode.SUCCESS, output)).json());
                } else {
                    out.println((new KOResponse(StatusCode.FIELD_EMPTY)).json());
                }

            } else {
                out.println((new KOResponse(StatusCode.GENERIC_ERROR, "Parameters missing")).json());
            }
        } catch (it.richkmeli.RMS.web.util.ServletException | DatabaseException e/* | CryptoException e*/) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: gestire richieste per multipli device

        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        try {
            BufferedReader br = req.getReader();
            String data = br.readLine();
            JSONObject JSONData = new JSONObject(data);
            String deviceName = JSONData.getString("device");
            String commands = JSONData.getString("commands");

            session = ServletManager.getServerSession(httpSession);

            session.getDeviceDatabaseManager().editCommands(deviceName, commands);
            out.println((new OKResponse(StatusCode.SUCCESS)).json());
            br.close();
        } catch (it.richkmeli.RMS.web.util.ServletException | JSONException | DatabaseException e/* | CryptoException e*/) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        try {
            BufferedReader br = req.getReader();
            String response = br.readLine();
            String a;
            while ((a = br.readLine()) != null) {
                System.out.println(a);
            }
            JSONObject JSONData = new JSONObject(response);
            String deviceName = JSONData.getString("device");
            String commandsOutput = JSONData.getString("data");

            session = ServletManager.getServerSession(httpSession);

            session.getDeviceDatabaseManager().setCommandsOutput(deviceName, commandsOutput);
            session.getDeviceDatabaseManager().editCommands(deviceName, "");

            out.println((new OKResponse(StatusCode.SUCCESS)).json());
            br.close();
        } catch (it.richkmeli.RMS.web.util.ServletException | JSONException | DatabaseException e/* | CryptoException e*/) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }
    }

    //TODO: fare metodo per gestire la richiesta degli output da client -> modificare anche webapp

    private String GenerateKeyExchangePayloadJSON(KeyExchangePayloadCompat keyExchangePayload) {
        String keyExchangePayloadJSON;// = "[ ";
        keyExchangePayloadJSON = /*"'" + index + "' : {"*/ "{"
                + "'encryptedAESsecretKey' : '" + keyExchangePayload.getEncryptedAESsecretKey() + "', "
                + "'signatureAESsecretKey' : '" + keyExchangePayload.getSignatureAESsecretKey() + "', "
                + "'kpubServer' : '" + keyExchangePayload.getKpubServer() + "', "
                + "'data' : '" + keyExchangePayload.getData() + "'}";
        //keyExchangePayloadJSON += " ]";
        return keyExchangePayloadJSON;
    }
}
