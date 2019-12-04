package it.richkmeli.rms.web.account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
    it.richkmeli.jframework.network.tcp.server.http.account.SignUp signUp = new it.richkmeli.jframework.network.tcp.server.http.account.SignUp() {
        @Override
        protected void doSpecificAction() {

        }
    };

    public SignUp() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        signUp.doAction(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        signUp.doAction(request, response);
    }
}
