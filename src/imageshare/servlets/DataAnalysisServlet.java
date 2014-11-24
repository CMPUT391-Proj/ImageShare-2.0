package imageshare.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataAnalysisServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private static final String USERNAME = "user";
    private static final String CURRENT_PAGE = "page";
    private static final String FROM_DATE = "date_from";
    private static final String TO_DATE = "date_to";
	
    
    private static final String DATA_ANALYSIS_ROOT_JSP = "dataanalysis";
    
    private static final String IMAGES_PER_USER_JSP = "imagesperuser";
    private static final String IMAGES_PER_SUBJECT_JSP = "imagespersubject";
    private static final String CUSTOM_ANALYSIS_JSP = "customanalysis";
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	String username = req.getParameter(USERNAME);
    	String currentPage = req.getParameter(CURRENT_PAGE);
    	String fromDate = req.getParameter(FROM_DATE);
    	String toDate = req.getParameter(TO_DATE);
    	String redirectJsp;
    	
    	try {
    		redirectJsp = getRedirect(currentPage);
    		
    	} catch (Exception e) {
    		req.getSession(true).setAttribute("error", e.toString());
    		resp.sendRedirect(DATA_ANALYSIS_ROOT_JSP);
    	}
    }
    
    private String getRedirect(String currentPage) throws Exception {
    	String redirectJsp = DATA_ANALYSIS_ROOT_JSP + "/";
    	
    	if (currentPage.equals(IMAGES_PER_USER_JSP)) {
    		redirectJsp += IMAGES_PER_USER_JSP;
    	} 
    	else if (currentPage.equals(IMAGES_PER_SUBJECT_JSP)) {
    		redirectJsp += IMAGES_PER_SUBJECT_JSP;
    	}
    	else if (currentPage.equals(CUSTOM_ANALYSIS_JSP)){
    		redirectJsp += CUSTOM_ANALYSIS_JSP;
    	}
    	else {
    		throw new Exception("Unknown Jsp originating source.");
    	}
    	
    	return redirectJsp;
    }
}
