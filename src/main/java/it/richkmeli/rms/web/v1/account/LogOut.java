package it.richkmeli.rms.web.v1.account;

import it.richkmeli.jframework.auth.web.account.LogOutJob;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.rms.web.v1.util.RMSServletManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "LogOut",
        description = "",
        urlPatterns = {"/LogOut", "/Richkware-Manager-Server/LogOut"}
)
public class LogOut extends HttpServlet {
    LogOutJob logOut = new LogOutJob() {
        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) {

        }
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        logOut.doAction(rmsServletManager);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        logOut.doAction(rmsServletManager);
    }
}
