package imageshare.servlets;

import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Generates data analytics given a date, subject, and/or username
 * 
 */
public class DataAnalysisServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private static final String USERNAME = "username";
    private static final String FROM_DATE = "datefrom";
    private static final String TO_DATE = "dateto";
    private static final String SEARCH_TYPE = "searchtype";
    private static final String SUBJECT_LIST = "subjectlist";
    private static final String USERNAME_LIST = "usernamelist";
	
    private static final String ADMIN = "admin";
    
    private static final String IMAGES_PER_SUBJ = "imagespersubject";
    private static final String IMAGES_PER_USER = "imagesperuser";
    private static final String CUSTOM_SEARCH = "customsearch";
    
    private static final String UNKNOWN_REPORT_TYPE_ERROR = "Unknown report type.";
    
    private static final String DATA_ANALYSIS_ROOT_JSP = "dataanalysis";
    private static final String DATA_REPORT_JSP = "datareport";
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	String username = req.getParameter(USERNAME);
    	String fromDate = req.getParameter(FROM_DATE);
    	String toDate = req.getParameter(TO_DATE);
    	String searchType = req.getParameter(SEARCH_TYPE);
    	
    	String subjectListStr = 
    			req.getParameter(SUBJECT_LIST) != null ? 
    			req.getParameter(SUBJECT_LIST) : null;
    	String usernameListStr = 
    			req.getParameter(USERNAME_LIST) != null ?
    			req.getParameter(USERNAME_LIST) : null;
    	
    	String subjectListFormatted = null;
    	String usernameListFormatted = null;
    	
    	try {
    		if (!username.equals(ADMIN))
    			throw new Exception(username+" has no privileges to access this page.");
    		
    		if (fromDate.length() == 0 || toDate.length() == 0) {
    			throw new Exception("From and To Dates cannot be empty.");
    		}

    		if (subjectListStr != null && subjectListStr.length() > 0) {
    			List<String> subjectList = Arrays.asList(subjectListStr.split("\\s*,\\s*"));
    			
    			StringBuilder sb = new StringBuilder();
    			for (String subject : subjectList) {
    				sb.append(String.format("'%s',", subject));
    			}
    			sb.setLength(sb.length()-1);
    			
    			subjectListFormatted = sb.toString();
    		}

    		if (usernameListStr != null && usernameListStr.length() > 0) {
    			List<String> usernameList = Arrays.asList(usernameListStr.split("\\s*,\\s*"));
    			
    			StringBuilder sb = new StringBuilder();
    			for (String usernameToken : usernameList) {
    				sb.append(String.format("'%s',", usernameToken));
    			}
    			sb.setLength(sb.length()-1);
    			
    			usernameListFormatted = sb.toString();
    		}
    		
    		JSONObject result = null;
    		
    		if (searchType.equals(CUSTOM_SEARCH)) {
    			result = customAnalytics(fromDate, toDate, subjectListFormatted, usernameListFormatted);
    			req.getSession(true).setAttribute("testtitle", "Custom Combined Search");
    			req.getSession(true).setAttribute("search", "customsearch");
    		} 
    		else if (searchType.equals(IMAGES_PER_USER)) {
    			result = imagesPerUser(fromDate, toDate);
    			req.getSession(true).setAttribute("testtitle", "Images Per User");
    			req.getSession(true).setAttribute("search", "imagesperuser");
    		}
    		else if (searchType.equals(IMAGES_PER_SUBJ)) {
    			result = imagesPerSubject(fromDate, toDate);
    			req.getSession(true).setAttribute("testtitle", "Images Per Subject");
    			req.getSession(true).setAttribute("search", "imagespersubject");
    		}
    		else {
    			throw new Exception(UNKNOWN_REPORT_TYPE_ERROR);
    		}
    		
            req.getSession(true).setAttribute("customjson", result.toString());
            
    	} catch (Exception e) {
    		req.getSession(true).setAttribute("error", e.toString());
    		resp.sendRedirect(DATA_ANALYSIS_ROOT_JSP);
    		return;
    	}
    	
    	resp.sendRedirect(DATA_REPORT_JSP);
    }
    
    private JSONObject imagesPerUser(String fromDate, String toDate) throws Exception {
    	JSONObject users = OracleHandler.getInstance().getImagesPerUser();
    	JSONArray usersArray = users.getJSONArray("result");
    	
    	for (int i=0; i<usersArray.length(); i++) {
    		JSONObject userObj = usersArray.getJSONObject(i);
    		
    		String user = String.format("'%s'", userObj.getString("OWNER_NAME"));
    		
    		JSONObject subjectAnalytics = customAnalytics(fromDate, toDate, null, user);
    		userObj.put("data", subjectAnalytics.get("result"));
    	}
    	
    	return users;
    }
    
    private JSONObject imagesPerSubject(String fromDate, String toDate) throws Exception {
    	JSONObject subjects = OracleHandler.getInstance().getImagesPerSubject();
    	JSONArray subjectsArray = subjects.getJSONArray("result");
    	
    	for (int i=0; i<subjectsArray.length(); i++) {
    		JSONObject subjectObj = subjectsArray.getJSONObject(i);
    		String subject = String.format("'%s'", subjectObj.getString("SUBJECT"));
    		
    		JSONObject subjectAnalytics = customAnalytics(fromDate, toDate, subject, null);
    		subjectObj.put("data", subjectAnalytics.get("result"));
    	}
    	
    	return subjects;
    }
    
    private JSONObject customAnalytics(String fromDate, String toDate, String subjectList, String usernameList) throws Exception {
    	JSONObject yearJsonResult = OracleHandler.getInstance().getAnalyticsForYear(fromDate, toDate, subjectList, usernameList); //final product
        JSONArray yearArray = yearJsonResult.getJSONArray("result");
        
        for (int i=0; i<yearArray.length(); i++) {
        	JSONObject yearObj = yearArray.getJSONObject(i);
        	int year = yearObj.getInt("YEAR");
        	
        	JSONObject monthJsonResult = OracleHandler.getInstance().getAnalyticsForMonthByYear(year, fromDate, toDate, subjectList, usernameList);
        	JSONArray monthArray = monthJsonResult.getJSONArray("result");
        	yearObj.put("MONTH_LIST", monthArray);
        	
        	for (int j=0; j<monthArray.length(); j++) {
        		JSONObject monthObj = monthArray.getJSONObject(j);
        		int month = monthObj.getInt("MONTH");
        		
        		JSONObject dayJsonResult = OracleHandler.getInstance().getAnalyticsForDayByYearByMonth(year, month, fromDate, toDate, subjectList, usernameList);
        		
        		monthObj.put("WEEK_LIST", convertDaysToWeeksJson(year, month, dayJsonResult).getJSONArray("result"));
        	}
        }
        
        return yearJsonResult;
    }
    
	private JSONObject convertDaysToWeeksJson(int year, int month, JSONObject dayJsonResult) {
		JSONArray dayJsonArray = dayJsonResult.getJSONArray("result");
		JSONArray weekJsonArray = new JSONArray();
		
		Map<Integer,Integer> weekMap = new TreeMap<Integer,Integer>();
		
		for (int i=0; i<dayJsonArray.length(); i++) {
			int day = dayJsonArray.getJSONObject(i).getInt("DAY");
			int dayCount = dayJsonArray.getJSONObject(i).getInt("COUNT");
			
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day);
			
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			
			if (weekMap.get(week) != null)
				weekMap.put(week, weekMap.get(week)+dayCount);
			else
				weekMap.put(week, dayCount);
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
