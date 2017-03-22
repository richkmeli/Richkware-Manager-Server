package richk.RMS.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.derby.tools.sysinfo;

import richk.RMS.Session;
import richk.RMS.database.DatabaseException;
import richk.RMS.model.Device;
import richk.RMS.model.ModelException;
import richk.RMS.util.Crypto;

/**
 * Servlet implementation class AddDeviceServlet
 */
@WebServlet("/AddDeviceServlet")
public class AddDeviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddDeviceServlet() {
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
			
			String data = request.getParameter("data");
			//data = Cripto.EncryptDecrypt(data, 5);
			
			String name = data.substring(1,data.indexOf(","));
			String serverPort = data.substring((data.indexOf(",")+1),(data.length()-1));
			
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			
			Device device = new Device(
					name,
					request.getRemoteAddr(),
					serverPort,
					timeStamp);
			session.getDatabaseManager().AddDevice(device);
			request.getRequestDispatcher("DevicesListServlet").forward(request, response);

		} catch (Exception e) {
			httpSession.setAttribute("error", e);
			request.getRequestDispatcher("JSP/error.jsp").forward(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
