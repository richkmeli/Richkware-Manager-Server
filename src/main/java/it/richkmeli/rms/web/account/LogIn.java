package it.richkmeli.rms.web.account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
    it.richkmeli.jframework.web.account.LogIn logIn = new it.richkmeli.jframework.web.account.LogIn() {
        @Override
        protected void doSpecificAction() {

        }
    };

    public LogIn() {
            super();
        }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logIn.doAction(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        logIn.doAction(request,response);
    }
}
