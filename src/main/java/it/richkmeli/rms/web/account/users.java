package it.richkmeli.rms.web.account;

import it.richkmeli.jframework.auth.web.account.UsersJob;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.rms.web.util.RMSServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class users extends HttpServlet {
    UsersJob usersListJob = new UsersJob() {
        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) throws JServletException {

        }
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        usersListJob.doGet(rmsServletManager);
    }

}
