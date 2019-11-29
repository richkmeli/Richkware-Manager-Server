package it.richkmeli.rms.web.account;

import it.richkmeli.rms.web.util.RMSServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/usersList")
public class usersList extends HttpServlet {
    it.richkmeli.jframework.network.tcp.server.http.account.usersList usersList = new it.richkmeli.jframework.network.tcp.server.http.account.usersList();// {
//        @Override
//        protected void doSpecificAction() {
//
//        }
//    };

    public usersList() {
            super();
        }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            usersList.doGet(request,response);
        } catch (it.richkmeli.jframework.network.tcp.server.http.util.ServletException e) {
            request.getSession().setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            usersList.doGet(request,response);
        } catch (it.richkmeli.jframework.network.tcp.server.http.util.ServletException e) {
            request.getSession().setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }
    }
}
