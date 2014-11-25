package imageshare.servlets;

import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        
        // redirect if not logged in
        if (session.getAttribute("user") == null)
            resp.sendRedirect("index");
        else {
            session.removeAttribute("user");
            session.removeAttribute("error");
            // close connection
            OracleHandler.getInstance().closeConnection();
            resp.sendRedirect("index");
        }
    }
}
