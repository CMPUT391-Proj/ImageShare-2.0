package imageshare.servlets;

import imageshare.model.User;

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
    
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String username = req.getParameter(USERNAME);
		String password = req.getParameter(PASSWORD);
		
		try {
			User user = new User(username, password);
			
			
		} catch (Exception e) {
			req.getSession().setAttribute("error", e.toString());
			return;
		}
		
		//resp.sendRedirect(LOGIN_JSP);
	}
}
