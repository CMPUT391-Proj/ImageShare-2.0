package imageshare.servlets;

import imageshare.model.Person;
import imageshare.model.User;
import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Updates user profile.
 *
 */
public class UserProfileServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String ADDRESS = "address";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
   
	private static final String USER_PROFILE_JSP = "userprofile";
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String username = req.getParameter(USERNAME);
		String password = req.getParameter(PASSWORD);
		String firstname = req.getParameter(FIRST_NAME);
		String lastname = req.getParameter(LAST_NAME);
		String address = req.getParameter(ADDRESS);
		String email = req.getParameter(EMAIL);
		String phone = req.getParameter(PHONE);
		
		try {
			if (username == null)
				throw new Exception("Username is null.");
			
			User user = new User(username, password);
			Person person = new Person(username, firstname, lastname, address, email, phone);
			
			OracleHandler.getInstance().updateUser(user);
			OracleHandler.getInstance().updatePerson(person);
		} catch (Exception e) {
			req.getSession(true).setAttribute("error", e.toString());
			resp.sendRedirect(USER_PROFILE_JSP);
			return;
		}
		
		resp.sendRedirect(USER_PROFILE_JSP);
	}
}
