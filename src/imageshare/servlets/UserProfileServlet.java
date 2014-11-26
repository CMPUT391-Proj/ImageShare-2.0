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
   
    private static final String ADMIN = "admin";
    
    private static final String NULL_USERNAME_ERROR = "Username is null.";
    private static final String EMPTY_PASSWORD_ERROR = "Password cannot be left empty.";
    private static final String EMPTY_FIELD_ERROR = "Fields cannot be left empty.";
    private static final String EMAIL_IN_USE_ERROR = "Email is already in use with another user.";
    private static final String ADMIN_PERSON_ERROR = "Admin personal details cannot be changed.";
    
    private static final String SUCCESS_MESSAGE = "Update profile success";
    
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
				throw new Exception(NULL_USERNAME_ERROR);
			
			if (password == null)
				throw new Exception(EMPTY_PASSWORD_ERROR);
			
			User user = new User(username, password);
			Person person = null;
			
			if (!username.equals(ADMIN)) {
				if (username.length() == 0 || firstname.length() == 0 || lastname.length() == 0 ||
					address.length() == 0 || email.length() == 0 || phone.length() == 0) {
					throw new Exception(EMPTY_FIELD_ERROR);
				}
				
				person = OracleHandler.getInstance().getPersonByEmail(email);
				
				if (person != null && !person.getUsername().equals(username)) {
					throw new Exception(EMAIL_IN_USE_ERROR);
				}
				else {
					person = new Person(username, firstname, lastname, address, email, phone);
				}
				
				OracleHandler.getInstance().updatePerson(person);
			} else {
				if (firstname.length() > 0 || lastname.length() > 0 || address.length() > 0 || 
					email.length() > 0 || phone.length() > 0) {
					throw new Exception(ADMIN_PERSON_ERROR);
				}
			}
			
			OracleHandler.getInstance().updateUser(user);
			req.getSession(true).setAttribute("success", "");
		} catch (Exception e) {
			req.getSession(true).setAttribute("error", e.toString());
			resp.sendRedirect(USER_PROFILE_JSP);
			return;
		}
		
		resp.sendRedirect(USER_PROFILE_JSP);
	}
}
