package it.richkmeli.RMS.web.account;

import it.richkmeli.RMS.web.util.Session;
import it.richkmeli.RMS.web.util.ServletException;
import it.richkmeli.RMS.web.util.ServletManager;
import it.richkmeli.jframework.auth.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/SignUp"})
public class SignUp extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int keyLength = 32;
    private String password;

    public SignUp() {
        super();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        }catch (ServletException e){
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }

        try {
            // check if is not already logged
            if (session.getUser() == null) {

                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                //    String name = request.getParameter("name");
                //    String lastname = request.getParameter("lastname");


                if (email != null) {
                    // se l'email è già presente nel DB
                    if (session.getAuthDatabaseManager().isUserPresent(email)) {
                        // TODO password gia presente vuoi recuperarla? guarda se html o popup js
                    } else {
                        if (pass != null) {
                            if (pass.length() >= 8) {

                                session.getAuthDatabaseManager().addUser(new User(email, pass, false));
                                // set userID into the session
                                session.setUser(email);

                                //httpSession.setAttribute("emailUser", email);
                                //response.sendRedirect("controlPanel.html");

                                //response.setHeader("Location", "/controlPanel.html");

                                response.sendRedirect("devices.html");
                                //request.getRequestDispatcher("controlPanel.html").forward(request, response);
                            } else {
                                // pass corta
                                // TODO rimanda da qualche parte perche c'è errore
                                httpSession.setAttribute("error", "pass corta");
                                request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
                            }
                        } else {
                            // mancano email o password
                            // TODO rimanda da qualche parte perche c'è errore
                            httpSession.setAttribute("error", "mancano email o password");
                            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
                        }
                    }

                }
            } else {
                // already logged
                // TODO rimanda da qualche parte perche c'è errore
                httpSession.setAttribute("error", "già loggato");
                request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
            }


            //PrintWriter printWriter = response.getWriter();
            //printWriter.println("PTO");

//            String data = request.getParameter("email");
//            printWriter.println(data);
//            printWriter.flush();


        } catch (Exception e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }

}
