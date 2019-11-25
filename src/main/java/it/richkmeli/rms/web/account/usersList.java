package it.richkmeli.rms.web.account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/usersList")
public class usersList extends HttpServlet {
    it.richkmeli.jframework.web.account.usersList usersList = new it.richkmeli.jframework.web.account.usersList();// {
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
        usersList.doGet(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        usersList.doGet(request,response);
    }
}
