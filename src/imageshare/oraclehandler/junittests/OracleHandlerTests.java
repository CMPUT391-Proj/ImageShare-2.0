package imageshare.oraclehandler.junittests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import imageshare.model.User;
import imageshare.oraclehandler.OracleHandler;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class OracleHandlerTests {
	
	private static final Random rand = new Random();
	private static final int randInt = rand.nextInt(500);
	/*
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
	public void oracleSelectUsers() {
		Vector<Vector<String>> users;
            try {
                users = OracleHandler.getInstance().retrieveResultSet("SELECT * FROM USERS");
    		
    		for (Vector<String> record : users) {
    			String recordString = "";
    			
    			for (String recordCol : record) {
    				if (recordString.isEmpty() == false) {
    					recordString = recordString.concat(", "+recordCol);
    				} else {
    					recordString = recordCol;
    				}
    			}
    			
    			System.out.println(record.size()+" : "+recordString);
    		}
    	
    		assertNotNull(users);
    		assertTrue(users.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
	
		System.out.println("FINISHED oracleSelectUsers");
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
			String result = OracleHandler.getInstance().getImagesPerUser();
			
			assertTrue(result.length() > 0);
			
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("FINISHED ImagesPerUser");
	}
	
	
	@Test
	public void oracleGetAnalyticsByYear() {
		try {
            String result = OracleHandler.getInstance().getAnalyticsForYear("2006-05-21", "2014-11-24").toString();
      
            System.out.println(result);
            
            assertTrue(result.length() > 0);
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		System.out.println("FINISHED oracleGetAnalyticsByYear");
	}
	
	@Test
	public void oracleGetAnalyticsByMonth() {
		try {
            String result = OracleHandler.getInstance().getAnalyticsForMonth("2014-05-21", "2014-11-24").toString();
      
            System.out.println(result);
            
            assertTrue(result.length() > 0);
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		System.out.println("FINISHED oracleGetAnalyticsByMonth");
	}
	
	@Test
	public void oracleGetAnalyticsByDay() {
		try {
            String result = OracleHandler.getInstance().getAnalyticsByDay("2014-05-21", "2014-11-24").toString();
      
            System.out.println(result);
            
            assertTrue(result.length() > 0);
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		System.out.println("FINISHED oracleGetAnalyticsByDay");
	}
	*/
	@Test
	public void generateAnalytics() {
		try {
            JSONObject yearJsonResult = OracleHandler.getInstance().getAnalyticsForYear("2006-05-21", "2014-11-24");
            JSONArray yearArray = yearJsonResult.getJSONArray("result");
            
            for (int i=0; i<yearArray.length(); i++) {
            	JSONObject yearObj = yearArray.getJSONObject(i);
            	int year = yearObj.getInt("YEAR");
            	
            	JSONObject monthJsonResult = OracleHandler.getInstance().getAnalyticsForMonthByYear(year, "2006-05-21", "2014-11-24");
            	JSONArray monthArray = monthJsonResult.getJSONArray("result");
            	yearObj.put("MONTH_LIST", monthArray);
            	
            	for (int j=0; j<monthArray.length(); j++) {
            		JSONObject monthObj = monthArray.getJSONObject(j);
            		int month = monthObj.getInt("MONTH");
            		
            		JSONObject dayJsonResult = OracleHandler.getInstance().getAnalyticsForDayByYearByMonth(year, month, "2006-05-21", "2014-11-24");
            		
            		//convertDaysToWeeksJson()
            		//monthObj.put("DAY", dayJsonResult.getJSONArray("result"));
            	}
            }
            
            System.out.println(yearJsonResult.toString());
            
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		System.out.println("FINISHED generateAnalytics");
	}
	
	private int convertDaysToWeeksJson(JSONObject dayJasonResult) {
		return 1;
	}
}
