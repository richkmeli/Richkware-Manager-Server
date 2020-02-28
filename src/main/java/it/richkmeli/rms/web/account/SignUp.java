package it.richkmeli.rms.web.account;

import it.richkmeli.jframework.auth.web.account.SignUpJob;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
    SignUpJob signUp = new SignUpJob() {
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
