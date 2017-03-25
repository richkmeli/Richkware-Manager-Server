package richk.RMS.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.model.ModelException;

@WebServlet("/RemoveDevice")
public class RemoveDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RemoveDevice() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession httpSession = request.getSession();
		Session session = (Session) httpSession.getAttribute("session");
		if (session == null){
			try {
				session = new Session();
				httpSession.setAttribute("session", session);
			} catch (DatabaseException e) {
				httpSession.setAttribute("error", e);
				request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
			}
		}
		
		try {
			session.getDatabaseManager().RemoveDevice(request.getParameter("name"));
			request.getRequestDispatcher("JSP/devices_list_AJAJ.jsp").forward(request, response);

		} catch (ModelException e) {
			httpSession.setAttribute("error", e);
			request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
