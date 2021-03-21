//package it.richkmeli.rms.web.v1.crypto;
//
//import it.richkmeli.jframework.network.tcp.server.http.crypto.SecureConnectionJob;
//import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
//import it.richkmeli.rms.data.entity.rmc.model.Rmc;
//import it.richkmeli.rms.web.util.RMSServletManager;
//import it.richkmeli.rms.web.util.RMSSession;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet(
//        name = "SecureConnection",
//        description = "",
//        urlPatterns = {"/SecureConnection", "/Richkware-Manager-Server/SecureConnection"}
//)
//public class SecureConnection extends HttpServlet {
//    SecureConnectionJob secureConnection = new SecureConnectionJob() {
//        RMSSession rmsSession;
//
//        @Override
//        protected void doBeforeCryptoAction(HttpServletRequest request, HttpServletResponse response, String clientID) throws Exception {
//            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
//            rmsSession = rmsServletManager.getRMSServerSession();
//
//            rmsSession.setRmcID(clientID);
//        }
//
//        @Override
//        protected void doFinalCryptoAction() throws Exception {
//
//            // _TODO fare controllo se rmcId è già presente. se sì, allora non fare la add
//            if (rmsSession.getRmcID() != null) {
//                if (!rmsSession.getRmcDatabaseManager().checkRmc(rmsSession.getRmcID())) {
//                    rmsSession.getRmcDatabaseManager().addRMC(new Rmc("", rmsSession.getRmcID()));
//                }
//            }
//        }
//    };
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            secureConnection.doAction(request, response);
//        } catch (JServletException e) {
//            request.getSession().setAttribute("error", e);
//            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            secureConnection.doAction(request, response);
//        } catch (JServletException e) {
//            request.getSession().setAttribute("error", e);
//            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
//        }
//    }
//}
