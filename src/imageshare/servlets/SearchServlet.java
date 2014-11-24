package imageshare.servlets;

import imageshare.oraclehandler.OracleHandler;

import java.io.*;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;
import java.text.*;
import java.net.*;

/**
 * This class displays the results of the indexed search by querying the
 * database which is including keyword and/or date search.
 */

public class SearchServlet extends HttpServlet implements SingleThreadModel {
	
	private OracleHandler database;
    private String keywords;
    private String fromDate;
    private String toDate; 
    private String fromdatesql;
    private String todatesql;
    private ResultSet rset;
    private String pid;
    private String sortby;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
//        response.setContentType("text/html");
//        database = OracleHandler.getInstance();
//        
//        keywords = request.getParameter("query");
//        fromDate = request.getParameter("fromdate");
//        toDate = request.getParameter("todate");
//        sortby = request.getParameter("sortby");
//        pid = "";
//        
//        /*
//         * Changing format from yyyy-MM-dd to dd-MMM-yy for sql
//         */
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy");
//        
//        try {
//            Date date = sdf.parse(fromDate);
//            Date date1 = sdf.parse(toDate);
//            fromdatesql = sdf1.format(date);
//            todatesql = sdf1.format(date1);
//        }
//    		catch (Exception e) {
//            e.getMessage();
//        }
//        
//        PrintWriter out = response.getWriter();
//        out.println("<!DOCTYPE html>");
//        out.println("<html>");
//        out.println("<head>");
//        out.println("<title>Search Results</title>");
//
//        /*
//         * Get how query will be sorted
//         */
//        String order = null;
//        // time decending
//        if (sortby.equals("1"))
//        {
//            order = "order by timing DESC";
//        }
//        // time ascending
//        else if (sortby.equals("2")) {
//            order = "order by timing";
//        }
//        // rank
//        else {
//            order = "order by 1 DESC";
//        }
//        
//        /* 
//         * Display header 
//         */    
//        try {
//            request.getRequestDispatcher("includes/header.jsp").include( 
//                                        request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        out.println("</head>");
//        out.println("<body>");
//        out.println("<br>");
//        System.out.println(sortby);	
//        /*
//         * The user has to input from and to dates otherwise
//         * only keyword search to get resultset of query
//         */
//        if (!(keywords.equals(""))) {
//        	
//            if((fromDate.equals("")) || (toDate.equals(""))) {
//                rset = database.getResultByKeywords(keywords, order);
//                out.println("Your results for: '" + keywords + "'");
//            }
//            else {
//                rset = database.getResultsByDateAndKeywords(fromdatesql, 
//                                                todatesql, keywords, order);
//                
//                out.println("Your results for: '" + keywords + "' Between: "
//                            + fromDate + " and " + toDate);
//            }
//        }
//        else if (!((fromDate.equals("")) || (toDate.equals("")))) {
//        	
//            if (!(order.equals("order by 1 DESC"))) {
//            	
//                rset = database.getResultsByDate(fromdatesql, todatesql, order);
//                out.println("Your results for dates between: " + fromDate
//                            + " and " + toDate);
//            }
//            else {
//                out.println("<b>Cannot sort by rank with just time, please"
//                            + " sort differently or add keywords</b>");
//            }
//        } 
//        else {
//            out.println("<b>Please enter a search query</b>");
//        }
//        out.println("<br>");
//
//        /*
//         * Displays the results
//         */
//        try
//        {
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