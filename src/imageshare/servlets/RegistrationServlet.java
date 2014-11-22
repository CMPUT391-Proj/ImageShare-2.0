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

// TODO: MALFORMED OBJECT ERROR
public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String ADDRESS = "address";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
	
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
			User user = new User(username, password);
			Person person = new Person(username, firstname, lastname, address, email, phone);
			
			boolean userConstraints = OracleHandler.getInstance().isSatisfiesConstraint(user);
			boolean personConstraints = OracleHandler.getInstance().isSatisfiesConstraint(person);
			
			if (userConstraints && personConstraints) {
				OracleHandler.getInstance().storeUser(user);
				OracleHandler.getInstance().storePerson(person);
			}
		} catch (Exception e) {
			req.getSession().setAttribute("error", e.toString());
		}
		
		resp.sendRedirect("registration");
	}

}
