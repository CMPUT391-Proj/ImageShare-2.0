package imageshare.servlets;

import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataAnalysisServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private static final String USERNAME = "username";
    private static final String CURRENT_PAGE = "page";
    private static final String FROM_DATE = "datefrom";
    private static final String TO_DATE = "dateto";
    private static final String SEARCH_TYPE = "searchtype";
	
    private static final String ADMIN = "admin";
    
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
    	String searchType = req.getParameter(SEARCH_TYPE);
    	
    	try {
    		if (!username.equals(ADMIN))
    			throw new Exception(username+" has no privileges to access this page.");
    		
            JSONObject yearJsonResult = OracleHandler.getInstance().getAnalyticsForYear(fromDate, toDate); //final product
            JSONArray yearArray = yearJsonResult.getJSONArray("result");
            
            for (int i=0; i<yearArray.length(); i++) {
            	JSONObject yearObj = yearArray.getJSONObject(i);
            	int year = yearObj.getInt("YEAR");
            	
            	JSONObject monthJsonResult = OracleHandler.getInstance().getAnalyticsForMonthByYear(year, fromDate, toDate);
            	JSONArray monthArray = monthJsonResult.getJSONArray("result");
            	yearObj.put("MONTH_LIST", monthArray);
            	
            	for (int j=0; j<monthArray.length(); j++) {
            		JSONObject monthObj = monthArray.getJSONObject(j);
            		int month = monthObj.getInt("MONTH");
            		
            		JSONObject dayJsonResult = OracleHandler.getInstance().getAnalyticsForDayByYearByMonth(year, month, fromDate, toDate);
            		
            		monthObj.put("WEEK_LIST", convertDaysToWeeksJson(year, month, dayJsonResult).getJSONArray("result"));
            	}
            }
    		
            req.getSession(true).setAttribute("customjson", yearJsonResult.toString());
            
    	} catch (Exception e) {
    		req.getSession(true).setAttribute("error", e.toString());
    		resp.sendRedirect(DATA_ANALYSIS_ROOT_JSP);
    		return;
    	}
    	
    	resp.sendRedirect("datareport");
    }
    
	private JSONObject convertDaysToWeeksJson(int year, int month, JSONObject dayJsonResult) {
		JSONArray dayJsonArray = dayJsonResult.getJSONArray("result");
		JSONArray weekJsonArray = new JSONArray();
		
		Map<Integer,Integer> weekMap = new TreeMap<Integer,Integer>();
		
		for (int i=0; i<dayJsonArray.length(); i++) {
			int day = dayJsonArray.getJSONObject(i).getInt("DAY");
			
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day);
			
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			
			if (weekMap.get(week) != null)
				weekMap.put(week, weekMap.get(week)+1);
			else
				weekMap.put(week, 1);
		}
		
		Iterator<Map.Entry<Integer, Integer>> entries = weekMap.entrySet().iterator();
		while(entries.hasNext()) {
			JSONObject weekObj = new JSONObject();
			
			Map.Entry<Integer, Integer> entry = entries.next();
			weekObj.put("WEEK", entry.getKey());
			weekObj.put("COUNT", entry.getValue());
			
			weekJsonArray.put(weekObj);
		}
		
		JSONObject result = new JSONObject();
		result.put("result", weekJsonArray);
		
		return result;
	}
}
