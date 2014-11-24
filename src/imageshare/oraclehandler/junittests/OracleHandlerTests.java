package imageshare.oraclehandler.junittests;

import static org.junit.Assert.*;
import imageshare.model.Person;
import imageshare.model.User;
import imageshare.oraclehandler.OracleHandler;

import java.util.Random;
import java.util.Vector;

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
		
		System.out.println("FINISHED CreateUser");
	}
}
