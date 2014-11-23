package imageshare.servlets;

import imageshare.model.Person;
import imageshare.model.User;
import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for registration.
 * 
 */

public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_CONFIRM = "passwordconfirm";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String ADDRESS = "address";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    
    private static final String PASSWORD_ERROR = "The passwords don't match.";
    private static final String INSERT_USER_ERROR = "A user with the same username exists.";
	private static final String INSERT_PERSON_ERROR = "A person with the same email already exists.";
    
	private static final String REGISTRATION_JSP = "registration";
	private static final String LOGIN_JSP = "registration"; // should be changed
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String username = req.getParameter(USERNAME);
		String password = req.getParameter(PASSWORD);
		String passwordConfirm = req.getParameter(PASSWORD_CONFIRM);
		String firstname = req.getParameter(FIRST_NAME);
		String lastname = req.getParameter(LAST_NAME);
		String address = req.getParameter(ADDRESS);
		String email = req.getParameter(EMAIL);
		String phone = req.getParameter(PHONE);
		
		try {
			User user = new User(username, password);
			Person person = new Person(username, firstname, lastname, address, email, phone);
			
			if (!password.equals(passwordConfirm)) {
				throw new Exception(password + " "+ passwordConfirm+ " " + PASSWORD_ERROR);
			}
			
			if (!OracleHandler.getInstance().isSatisfiesConstraint(user)) {
				throw new Exception(INSERT_USER_ERROR);
			}
			
			if(!OracleHandler.getInstance().isSatisfiesConstraint(person)) {
				throw new Exception(INSERT_PERSON_ERROR);
			}
			
			OracleHandler.getInstance().storeUser(user);
			OracleHandler.getInstance().storePerson(person);
			
		} catch (Exception e) {
			req.getSession().setAttribute("error", e.toString());
			resp.sendRedirect(REGISTRATION_JSP);
			return;
		}
		
		// should 
		resp.sendRedirect(LOGIN_JSP);
	}

}
