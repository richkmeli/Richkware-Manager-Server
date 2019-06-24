package it.richkmeli.RMS.web.util;

import it.richkmeli.RMS.web.response.KOResponse;
import it.richkmeli.RMS.web.response.OKResponse;
import it.richkmeli.RMS.web.response.StatusCode;
import it.richkmeli.jcrypto.KeyExchangePayloadCompat;
import it.richkmeli.jframework.database.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet({"/command"})
public class command extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                }

                if (output != null) {
                    out.println((new OKResponse(StatusCode.SUCCESS, output)).json());
                } else {
                    out.println((new KOResponse(StatusCode.GENERIC_ERROR, "Cannot retrieve correct output")).json());
                }


            } else {
                out.println((new KOResponse(StatusCode.GENERIC_ERROR, "Parameters missing")).json());
            }
        } catch (it.richkmeli.RMS.web.util.ServletException | DatabaseException e/* | CryptoException e*/) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO: gestire richieste per multipli device

        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        try {
            session = ServletManager.getServerSession(httpSession);

            if (req.getParameterMap().containsKey("device") && req.getParameterMap().containsKey("commands")) {
                String deviceName = req.getParameter("device");
                String commands = req.getParameter("commands"); //base64(base64(cmd1)##base64(cmd2)...)
                session.getDeviceDatabaseManager().editCommands(deviceName, commands);
                out.println((new OKResponse(StatusCode.SUCCESS)).json());
            } else {
                out.println((new KOResponse(StatusCode.GENERIC_ERROR, "Parameters missing")).json());
            }
        } catch (it.richkmeli.RMS.web.util.ServletException | DatabaseException e/* | CryptoException e*/) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        try {
            session = ServletManager.getServerSession(httpSession);

            if (req.getParameterMap().containsKey("data0") && req.getParameterMap().containsKey("data1")) {
                String deviceName = req.getParameter("data0");
                String commandsOutput = req.getParameter("data1"); //base64(base64(cmd1)##base64(cmd2)...)
                session.getDeviceDatabaseManager().setCommandsOutput(deviceName, commandsOutput);
                session.getDeviceDatabaseManager().editCommands(deviceName, "");
                out.println((new OKResponse(StatusCode.SUCCESS)).json());
            } else {
                out.println((new KOResponse(StatusCode.GENERIC_ERROR, "Parameters missing")).json());
            }
        } catch (it.richkmeli.RMS.web.util.ServletException | DatabaseException e/* | CryptoException e*/) {
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
