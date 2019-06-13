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
                String remainingCommands = req.getParameter("data1");

                String commands = session.getDeviceDatabaseManager().getCommands(deviceName);

                //TODO: capire se i comandi sono già criptati o no
                //encrypt commands
//                String kpubC = null;
//                if (req.getParameterMap().containsKey("Kpub")) {
//                    kpubC = req.getParameter("Kpub");
//                }
//                // generation of public e private key of server
//                KeyPair keyPair = Crypto.GetGeneratedKeyPairRSA();
//
//                // [enc_(KpubC)(AESKey) , sign_(KprivS)(AESKey) , KpubS]
//                List<Object> res = Crypto.KeyExchangeAESRSA(keyPair, kpubC);
//                KeyExchangePayloadCompat keyExchangePayload = (KeyExchangePayloadCompat) res.get(0);
//                SecretKey AESsecretKey = (SecretKey) res.get(1);
//
//                // encrypt data (devices List) with AES secret key
//                String enc = Crypto.EncryptAES(commands, AESsecretKey);
//                ;
//                // add data to the object
//                keyExchangePayload.setData(enc);
//
//                String encPayload = GenerateKeyExchangePayloadJSON(keyExchangePayload);

                out.println((new OKResponse(StatusCode.SUCCESS, commands)).json());

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
