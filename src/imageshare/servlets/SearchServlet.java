package imageshare.servlets;

import imageshare.model.Image;
import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class displays the results of the indexed search by querying the
 * database which is including keyword and/or date search.
 */

public class SearchServlet extends HttpServlet implements SingleThreadModel {

	private static final String SEARCH_JSP = "/webapp/jsp/search.jsp";

	String user = "";
	private OracleHandler database;
	private String keywords;
	private String fromDate;
	private String toDate; 
	private String fromdatesql;
	private String todatesql;
	private List<Image> results = new ArrayList<Image>();
	private String sortby;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* if no user logged in, redirect to login page */
		if (user == null) {
			response.sendRedirect("login.jsp");
		};

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(SEARCH_JSP);    
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//      response.setContentType("text/html");
		database = OracleHandler.getInstance();

		keywords = request.getParameter("query");
		fromDate = request.getParameter("fromdate");
		toDate = request.getParameter("todate");
		sortby = request.getParameter("sortby");

		/*
		 * Changing format from yyyy-MM-dd to dd-MMM-yy for sql
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy");

		try {
			Date date = sdf.parse(fromDate);
			Date date1 = sdf.parse(toDate);
			fromdatesql = sdf1.format(date);
			todatesql = sdf1.format(date1);
		}
		catch (Exception e) {
			e.getMessage();
		}


		/*
		 * Get how query will be sorted
		 */
		 String order = null;
		 // time descending
		 if (sortby.equals("1"))
		 {
			 order = "order by timing DESC";
		 }
		 // time ascending
		 else if (sortby.equals("2")) {
			 order = "order by timing";
		 }
		 // rank
		 else {
			 order = "order by 1 DESC";
		 }

		 //        /*
		 //         * The user has to input from and to dates otherwise
		 //         * only keyword search to get resultset of query
		 //         */
		 if (!(keywords.equals(""))) {
			 if((fromDate.equals("")) || (toDate.equals(""))) {
				 try {
					 results = database.getImagesByKeywords(keywords, order);
				 } catch (Exception e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
				 //out.println("Your results for: '" + keywords + "'");
			 }
			 else {
				 try {
					 results = database.getImagesByDateAndKeywords(fromdatesql, 
							 todatesql, keywords, order);
				 } catch (Exception e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }

				 //                out.println("Your results for: '" + keywords + "' Between: "
				 //                            + fromDate + " and " + toDate);
			 }
		 }
		 else if (!((fromDate.equals("")) || (toDate.equals("")))) {

			 if (!(order.equals("order by 1 DESC"))) {

				 try {
					 results = database.getImagesByDate(fromdatesql, todatesql, order);
				 } catch (Exception e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
				 //                out.println("Your results for dates between: " + fromDate
				 //                            + " and " + toDate);
			 }
			 else {
				 //                out.println("<b>Cannot sort by rank with just time, please"
				 //                            + " sort differently or add keywords</b>");
			 }
		 } 
		 else {
			 //out.println("<b>Please enter a search query</b>");
		 }
		 
		 // Generate and append all user visible thumbnails to the page.
		 String	thumbs = "<div class='row'><h1>Search Results</h1></div><div id='gal' class='list-group gallery'>" +
		 		"<div id='gal' class='list-group gallery'>";
		 for (int i = 0; i < results.size(); ++i) {
			 String getURL = "thumbnail?" + results.get(i).getPhotoId(); 
			 String editURL = "updateimage?" + results.get(i).getPhotoId(); 
			 String displayURL = "display?" + results.get(i).getPhotoId(); 

			 thumbs = thumbs + "<div class=\'col-sm-3 col-xs-5 col-md-2 col-lg-2\'>";

			 if (results.get(i).getOwnerName().equals(user)) {  
				 thumbs = thumbs + "<a href='" + 
						 editURL +
						 "'><strong class=\'text-muted\'>Edit</strong></a>";
			 } 

			 thumbs = thumbs + "<div id='" +
					 Integer.toString(results.get(i).getPhotoId()) +
					 "'><a class='thumbnail fancybox' href='" +
					 displayURL +
					 "'><img class='img-responsive' alt='' src='" +
					 getURL +
					 "'/></a></div></div></div>";

		 }
		 request.getSession(true).setAttribute("galHTML", thumbs);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(SEARCH_JSP);    
			requestDispatcher.forward(request, response);

		 //            while(rset.next()) {
		 //                pid = (rset.getObject(2)).toString();
		 //                // specify the servlet for the image
		 //                out.println("<a href=\"/c391proj/browsePicture?big"
		 //                            + pid + "\">");
		 //                // specify the servlet for the thumbnail
		 //                out.println("<img src=\"/c391proj/browsePicture?"
		 //                            + pid + "\"></a>");
		 //            }
		 //        } 
		 //        catch (Exception e) {
		 //            e.getStackTrace();
		 //        }
		 //        
		 //        //database.close_db();
		 //        out.print("</body>");
		 //        out.print("</html>");
		 //        out.close();

	}
}