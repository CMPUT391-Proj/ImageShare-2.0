package imageshare.servlets;

import imageshare.model.User;
import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for login.
 * 
 */

public class LoginServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    
    private static final String LOGIN_ERROR = "The credentials that have been given does not match any login record.";
    
    private static final String INDEX_JSP = "index";
    private static final String DASHBOARD_JSP = "imageupload";
    
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String username = req.getParameter(USERNAME);
		String password = req.getParameter(PASSWORD);
		
		try {
			User user = OracleHandler.getInstance().getUser(username);
			
			if (user == null)
				throw new Exception(LOGIN_ERROR);
			
			if (!password.equals(user.getPassword()))
				throw new Exception(LOGIN_ERROR);
			
		} catch (Exception e) {
			req.getSession().setAttribute("error", e.toString());
			resp.sendRedirect(INDEX_JSP);
			return;
		}
		
		resp.sendRedirect(DASHBOARD_JSP);
	}
}
