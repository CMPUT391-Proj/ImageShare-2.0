package imageshare.oraclehandler.junittests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import imageshare.model.User;
import imageshare.oraclehandler.OracleHandler;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class OracleHandlerTests {
	
	private static final Random rand = new Random();
	private static final int randInt = rand.nextInt(500);
	
	@Test
	public void oracleInsertUsers() {
		try {
            OracleHandler.getInstance().insertRecord(
            	"INSERT INTO USERS (USER_NAME,PASSWORD,DATE_REGISTERED) "+
            	"VALUES ('"+randInt+"','testpass',SYSTIMESTAMP)");
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		System.out.println("FINISHED oracleInsertUsers");
	}
	
	@Test
	public void CreateUser() {
		try {
			User user = new User(""+randInt, "pass");
			
			System.out.println("Created new "+user.getUsername());
			
			assertNotNull(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("FINISHED CreateUser");
	}
	
	@Test
	public void UpdateUser() {
		try {
			User user = new User(""+randInt, "newpass");
			
			OracleHandler.getInstance().updateUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("FINISHED CreateUser");
	}
	
	@Test
	public void ImagesPerUser() {
		try {
			String result = OracleHandler.getInstance().getImagesPerUser().toString();
			
			assertTrue(result.length() > 0);
			
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("FINISHED ImagesPerUser");
	}
	
	@Test
	public void generateAnalytics() {
		try {
            JSONObject yearJsonResult = OracleHandler.getInstance().getAnalyticsForYear("2006-05-21", "2014-11-24", "'Santa Clause', 'mario'", null);
            
            JSONArray yearArray = yearJsonResult.getJSONArray("result");
            
            for (int i=0; i<yearArray.length(); i++) {
            	JSONObject yearObj = yearArray.getJSONObject(i);
            	int year = yearObj.getInt("YEAR");
            	
            	JSONObject monthJsonResult = OracleHandler.getInstance().getAnalyticsForMonthByYear(year, "2006-05-21", "2014-11-24", null, null);
            	JSONArray monthArray = monthJsonResult.getJSONArray("result");
            	yearObj.put("MONTH_LIST", monthArray);
            	
            	for (int j=0; j<monthArray.length(); j++) {
            		JSONObject monthObj = monthArray.getJSONObject(j);
            		int month = monthObj.getInt("MONTH");
            		
            		JSONObject dayJsonResult = OracleHandler.getInstance().getAnalyticsForDayByYearByMonth(year, month, "2006-05-21", "2014-11-24", null, null);
            		
            		monthObj.put("DAY_LIST", convertDaysToWeeksJson(year, month, dayJsonResult).getJSONArray("result"));
            	}
            }
            
            System.out.println(yearJsonResult.toString());
            
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		System.out.println("FINISHED generateAnalytics");
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
