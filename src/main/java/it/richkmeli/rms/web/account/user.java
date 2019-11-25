package it.richkmeli.rms.web.account;

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
        user.doDelete(request, response);
    }
}
