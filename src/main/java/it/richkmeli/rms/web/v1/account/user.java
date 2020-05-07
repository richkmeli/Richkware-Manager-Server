package it.richkmeli.rms.web.v1.account;

import it.richkmeli.jframework.auth.web.account.UserJob;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.rms.web.v1.util.RMSServletManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "user",
        description = "",
        urlPatterns = {"/user", "/Richkware-Manager-Server/user"}
)
public class user extends HttpServlet {
    UserJob userJob = new UserJob() {
        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) throws JServletException {

        }
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        userJob.doGet(rmsServletManager);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        userJob.doDelete(rmsServletManager);
    }
}
