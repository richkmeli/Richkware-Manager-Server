package it.richkmeli.rms.web.account;

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
    it.richkmeli.jframework.network.tcp.server.http.account.user user = new it.richkmeli.jframework.network.tcp.server.http.account.user() {
        @Override
        protected void doSpecificAction(HttpServletRequest httpServletRequest) throws it.richkmeli.jframework.network.tcp.server.http.util.ServletException, DatabaseException {

        }
    };

    public user() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        user.doGet(request, response, rmsServletManager);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        user.doGet(request, response, rmsServletManager);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            user.doDelete(request, response, rmsServletManager);
        } catch (it.richkmeli.jframework.network.tcp.server.http.util.ServletException e) {
            request.getSession().setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }
    }
}
