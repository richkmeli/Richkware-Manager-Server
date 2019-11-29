package it.richkmeli.rms.web.account;

import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.Logger;
import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
   it.richkmeli.jframework.network.tcp.server.http.account.LogIn logIn = new it.richkmeli.jframework.network.tcp.server.http.account.LogIn() {

        @Override
        protected void doSpecificAction(HttpServletRequest httpServletRequest) throws it.richkmeli.jframework.network.tcp.server.http.util.ServletException, DatabaseException {
            RMSServletManager rmsServletManager = new RMSServletManager(httpServletRequest);
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();

            if (rmsSession.getChannel().equalsIgnoreCase(RMSServletManager.Channel.RMC)) {
                RMC rmc = new RMC(rmsSession.getUser(), rmsSession.getRmcID());
                Logger.info("RMC: " + rmc.getAssociatedUser() + " - " + rmc.getRmcId());
                if (!rmsSession.getRmcDatabaseManager().checkRmcUserPair(rmc)) {
                    if (rmsSession.getRmcDatabaseManager().checkRmcUserPair(new RMC("", rmsSession.getRmcID()))) {
                        rmsSession.getRmcDatabaseManager().editRMC(rmc);
                    } else {
                        rmsSession.getRmcDatabaseManager().addRMC(rmc);
                    }
                }
            }
        }
    };

    public LogIn() {
            super();
        }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RMSServletManager rmsServletManager = new RMSServletManager(request);
        logIn.doAction(request,response,rmsServletManager);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        RMSServletManager rmsServletManager = new RMSServletManager(request);
        logIn.doAction(request,response,rmsServletManager);
    }
}
