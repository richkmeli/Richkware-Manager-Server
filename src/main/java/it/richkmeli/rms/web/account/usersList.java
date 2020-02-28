package it.richkmeli.rms.web.account;

import it.richkmeli.jframework.auth.web.account.UsersListJob;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.rms.web.util.RMSServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/usersList")
public class usersList extends HttpServlet {
    UsersListJob usersListJob = new UsersListJob() {
        @Override
        protected void doSpecificAction(HttpServletRequest httpServletRequest) throws JServletException {

        }
    };

    public usersList() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //try {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        usersListJob.doGet(request, response, rmsServletManager);
       /* } catch (JServletException e) {
            request.getSession().setAttribute("error", e);
            request.getRequestDispatcher(RMSServletJob.ERROR_JSP).forward(request, response);
        }*/
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //try {
            RMSServletManager rmsServletManager = new RMSServletManager(request, response);
            usersListJob.doGet(request, response, rmsServletManager);
        /*} catch (JServletException e) {
            request.getSession().setAttribute("error", e);
            request.getRequestDispatcher(RMSServletJob.ERROR_JSP).forward(request, response);
        }*/
    }
}
