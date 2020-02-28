package it.richkmeli.rms.web.account;

import it.richkmeli.jframework.auth.web.account.UserJob;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.web.util.RMSServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user")
public class user extends HttpServlet {
    UserJob userJob = new UserJob() {
        @Override
        protected void doSpecificAction(HttpServletRequest httpServletRequest) throws JServletException {

        }
    };

    public user() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        userJob.doGet(request, response, rmsServletManager);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        userJob.doGet(request, response, rmsServletManager);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            userJob.doDelete(request, response, rmsServletManager);
       /* } catch (JServletException e) {
            request.getSession().setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }*/
    }
}
