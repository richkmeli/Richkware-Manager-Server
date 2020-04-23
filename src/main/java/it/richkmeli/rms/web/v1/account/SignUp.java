package it.richkmeli.rms.web.v1.account;

import it.richkmeli.jframework.auth.web.account.SignUpJob;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.rms.web.v1.util.RMSServletManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "SignUp",
        description = "",
        urlPatterns = {"/SignUp"}
)
public class SignUp extends HttpServlet {
    SignUpJob signUp = new SignUpJob() {
        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) {

        }
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        signUp.doAction(rmsServletManager);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        signUp.doAction(rmsServletManager);
    }
}
