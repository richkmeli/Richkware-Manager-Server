package it.richkmeli.rms.web.account;

import it.richkmeli.jframework.web.util.ServletManager;
import it.richkmeli.rms.web.util.RMSServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user")
public class user extends HttpServlet {
    it.richkmeli.jframework.web.account.user user = new it.richkmeli.jframework.web.account.user(); //{
//        @Override
//        protected void doSpecificAction() {
//
//        }
//    };

    public user() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        user.doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        user.doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            user.doDelete(request, response);
        } catch (it.richkmeli.jframework.web.util.ServletException e) {
            request.getSession().setAttribute("error", e);
            request.getRequestDispatcher(RMSServletManager.ERROR_JSP).forward(request, response);
        }
    }
}
