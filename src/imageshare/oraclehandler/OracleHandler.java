package imageshare.oraclehandler;

import imageshare.model.Group;
import imageshare.model.GroupList;
import imageshare.model.Image;
import imageshare.model.Person;
import imageshare.model.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

public class OracleHandler {
	
	private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	//private static final String CONNECTION_STRING = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS"; // use for University
	private static final String CONNECTION_STRING = "jdbc:oracle:thin:@localhost:1525:CRS"; // use for SSH
	private static final String USERNAME = "jyuen";
	private static final String PASSWORD = "pass2014";

	// Singleton
	private static OracleHandler oracleHandler = null;
	private Connection conn;
	
	/**
	 * Prevents instantiation - singleton model
	 */
	protected OracleHandler() {}
	
    /**
     * @return the singleton OracleHandler
     */
	public static OracleHandler getInstance() {
	    if (oracleHandler == null) {
	        oracleHandler = new OracleHandler();
	        oracleHandler.establishConnection();
	    }
	    return oracleHandler;
	}
	
    private void establishConnection() {
        try {
            Class<?> drvClass = Class.forName(ORACLE_DRIVER);
            DriverManager.registerDriver((Driver) drvClass.newInstance());
            oracleHandler.conn = DriverManager.getConnection(CONNECTION_STRING,
                    USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO call on logout?
    public void closeConnection() {
        try {
            OracleHandler.getInstance().conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param statement
     *            Insert statement to insert a record into the Oracle database
     */
    public void insertRecord(String statement) throws Exception {
        PreparedStatement stmt = getInstance().conn.prepareStatement(statement);
        stmt.executeUpdate();
        stmt.execute("commit");
    }

    /**
     * @param statement
     *            Query statement use to query Oracle database
     */
	public Vector<Vector<String>> retrieveResultSet(String statement) throws Exception {
        Vector<Vector<String>> resultVector = null;

        PreparedStatement stmt = getInstance().conn.prepareStatement(statement);
        ResultSet rset = stmt.executeQuery(statement);

        ResultSetMetaData rsmd = rset.getMetaData();
        resultVector = new Vector<Vector<String>>();

        int colCount = rsmd.getColumnCount();

        // algorithm could be improved.
        // if unknown oracle type comes up, google the type number and add it
        // to the else if statements
        while (rset.next()) {
            Vector<String> resultRow = new Vector<String>();

            for (int i = 1; i <= colCount; i++) {
                if (rsmd.getColumnType(i) == Types.INTEGER) {
                    resultRow.add("" + rset.getInt(i));
                } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
                    resultRow.add(rset.getString(i));
                } else if (rsmd.getColumnType(i) == Types.TIMESTAMP) {
                    resultRow.add(rset.getTimestamp(i).toString());
                } else {
                    throw new Exception("UKNOWN ORACLE TYPE: "
                            + rsmd.getColumnType(i));
                }
            }

            resultVector.add(resultRow);
        }

        stmt.close();

        return resultVector;
	}

    /**
     * Executes a generic query.
     * 
     * @param query
     *            sql to execute
     * @return the result set
     * @throws Exception
     *             exception if there was trouble executed the query. Caller
     *             expected to handle.
     */
    public ResultSet executeQuery(String query) throws Exception {
        PreparedStatement stmt = getInstance().conn.prepareStatement(query);
        return stmt.executeQuery(query);
    }
	
    /**
     * Stores the image into the database
     * 
     * @param image
     *            The image to store
     */
    public void storeImage(Image image) throws Exception {
        String query = "INSERT INTO IMAGES VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int imageID = oracleHandler.nextImageID();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(image.getImage(), "jpg", baos);
        InputStream imageInputStream = new ByteArrayInputStream(
                baos.toByteArray());

        ImageIO.write(image.getThumbnail(), "jpg", baos);
        InputStream thumbnailInputStream = new ByteArrayInputStream(
                baos.toByteArray());

        PreparedStatement stmt = getInstance().conn.prepareStatement(query);
        stmt.setInt(1, imageID);
        stmt.setString(2, image.getOwnerName());
        stmt.setInt(3, image.getPermitted());
        stmt.setString(4, image.getSubject());
        stmt.setString(5, image.getPlace());
        stmt.setDate(6, new Date(image.getDate().getTime()));
        stmt.setString(7, image.getDescription());
        stmt.setBinaryStream(8, imageInputStream);
        stmt.setBinaryStream(9, thumbnailInputStream);

        stmt.executeUpdate();
    }
    
    /**
     * @return the next image id
     * @throws Exception
     */
    public int nextImageID() throws Exception {
        String sql = "SELECT image_sequence.nextval from dual";
        ResultSet rs = executeQuery(sql);
        rs.next();
        return rs.getInt(1);
    }
    
    /**
     * Get's a user's groups
     * @param String user
     * @returns List<Group>
     */
    public List<Group> getGroups(String user) throws Exception {
        List<Group> groups = new ArrayList<Group>();
        
        String query = "SELECT * FROM groups WHERE user_name = ?";
        PreparedStatement stmt = getInstance().conn.prepareStatement(query);
        stmt.setString(1, user);
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("group_name");
            int id = rs.getInt("group_id");
            Date date = rs.getDate("date_created");
            groups.add(new Group(id, user, name, date));
        }
        
        return groups;
    }
    
    /**
     * Get all groups where the user either created it or is a part of the
     * group.
     * 
     * @param user
     *            The user to query for involved groups
     */
    public List<Group> getInvolvedGroups(String user) throws Exception {
        List<Group> groups = new ArrayList<Group>();
        
        String query = "SELECT * FROM groups WHERE group_id in "
                + "(SELECT group_id FROM group_lists where "
                + "friend_id = ? ) OR " + "user_name = ?";
        
        PreparedStatement stmt = getInstance().conn.prepareStatement(query);
        stmt.setString(1, user);
        stmt.setString(2, user);
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String name = rs.getString("group_name");
            int id = rs.getInt("group_id");
            Date date = rs.getDate("date_created");
            groups.add(new Group(id, user, name, date));
        }
        
        return groups;     
    }
    
    /**
     * Adds groups to the database
     * @param List<Group> groups
     */
    public void storeGroups(List<Group> groups) throws Exception {
    	String query = "INSERT INTO GROUP VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = null;

        stmt = getInstance().conn.prepareStatement(query);

    	for (Group group: groups) {
    		stmt.setInt(1, group.getGroupId());
    		stmt.setString(2, group.getUsername());
    		stmt.setString(3, group.getGroupname());
    		stmt.setDate(3, new Date(group.getDateCreated().getTime()));
    		stmt.addBatch();
        }
    	stmt.executeBatch();
    }
    
    /**
     * Deletes a friend from a group
     * @param int group_id
     * @param String friend
     * @throws Exception 
     */
    public void delete_friend(int group_id, String friend) throws Exception {
    	String query = "delete from group_lists where group_id = " + group_id
	               + " and friend_id = '" + friend + "'";
    	
		PreparedStatement stmt = getInstance().conn.prepareStatement(query);
	    
		stmt.executeUpdate();
    }

    /**
     * Adds a friend to a group
     * @param int group_id
     * @param String friend
     * @throws Exception 
     */
    public void add_friend(int group_id, String friend) throws Exception {
	String query = "insert into group_lists values(" + group_id
	               + ", '" + friend + "', sysdate, null)";
	
    	PreparedStatement stmt = getInstance().conn.prepareStatement(query);
    
    	stmt.executeUpdate();
    }
    
    /**
     * Deletes a group from the group_lists and groups tables
     * Updates images permitted = 2 where permitted = group_id
     * @param String group_id
     * @throws Exception 
     */
    public void delete_group(String group_id) throws Exception {
		// change permissions in images
		String query_images = "update images set permitted = 2 "
		    + "where permitted = " + group_id;
		PreparedStatement stmt1 = getInstance().conn.prepareStatement(query_images);
	        
		// delete group_lists where group_id == group_id
		String query_group_lists = "delete from group_lists where group_id = "
		    + group_id;
		PreparedStatement stmt2 = getInstance().conn.prepareStatement(query_group_lists);
		
		// delete groups where group_id == group_id
		String query_groups = "delete groups where group_id = " + group_id;
		PreparedStatement stmt3 = getInstance().conn.prepareStatement(query_groups);
		
		stmt1.executeUpdate();
		stmt2.executeUpdate();
		stmt3.executeUpdate();
    }
    
    /**
     * Returns all groupLists in a group
     * @param int group_id
     * @throws Exception 
     * @returns ArrayList<String>
     */
    public List<GroupList> getGroupsLists(int group_id) throws Exception {
    	
        List<GroupList> groupLists = new ArrayList<GroupList>();
        
		String query = "SELECT * "
			    + "FROM group_lists "
			    + "WHERE group_id = "
			    + group_id;
		
        PreparedStatement stmt = getInstance().conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String friendId = rs.getString("friend_id");
            Date dateAdded = rs.getDate("date_added");
            String notice = rs.getString("notice");
            groupLists.add(new GroupList(friendId, dateAdded, notice));
        }
        return groupLists;
    }
    
    /**
     * 
     * @param User model
     * @throws Exception
     */
	public void storeUser(User user) throws Exception {
        String query = "INSERT INTO USERS VALUES (?, ?, ?)";

        PreparedStatement stmt = getInstance().conn.prepareStatement(query);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setDate(3, new java.sql.Date(user.getRegisteredDate().getTime()));
  
        stmt.executeUpdate();
    }

	/**
	 * 
	 * @param User model
	 * @throws Exception
	 */
	public void updateUser(User user) throws Exception {
		String query = "UPDATE USERS SET PASSWORD = ? WHERE USER_NAME = ?";
		
		PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		stmt.setString(1, user.getPassword());
		stmt.setString(2, user.getUsername());
		
		stmt.executeUpdate();
	}
	
	/**
	 * 
	 * @param username
	 * @return User model of username, null if it doesn't exist
	 * @throws Exception
	 */
	public User getUser(String username) throws Exception {
		String query = "SELECT * FROM USERS WHERE USER_NAME = ?";
		User user = null;
		
		PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		stmt.setString(1, username);
		
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			String password = rs.getString(2);
			Date date = rs.getDate(3);
			
			user = new User(username, password, date);
		}
		
		return user;
	}
	
	/**
	 * 
	 * @param email
	 * @return Person model of email, null if it doesn't exist
	 * @throws Exception
	 */
	public Person getPersonByEmail(String email) throws Exception {
		String query = "SELECT * FROM PERSONS WHERE EMAIL = ?";
		Person person = null;
		
		PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		stmt.setString(1, email);
		
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			String username = rs.getString(1);
			String firstname = rs.getString(2);
			String lastname = rs.getString(3);
			String address = rs.getString(4);
			String phone = rs.getString(6);
			
			person = new Person(username, firstname, lastname, address, email, phone);
		}
		
		return person;
	}
	
	/**
	 * 
	 * @param email
	 * @return Person model of username, null if it doesn't exist
	 * @throws Exception
	 */
	public Person getPerson(String username) throws Exception {
		String query = "SELECT * FROM PERSONS WHERE USER_NAME = ?";
		Person person = null;
		
		PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		stmt.setString(1, username);
		
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			String firstname = rs.getString(2);
			String lastname = rs.getString(3);
			String address = rs.getString(4);
			String email = rs.getString(5);
			String phone = rs.getString(6);
			
			person = new Person(username, firstname, lastname, address, email, phone);
		}
		
		return person;
	}
	
	/**
	 * 
	 * @param Person model
	 * @throws Exception
	 */
	public void updatePerson(Person person) throws Exception {
		String query = 
			"UPDATE PERSONS "+
			"SET FIRST_NAME = ?, LAST_NAME = ?, ADDRESS = ?, EMAIL = ?, PHONE = ? "+
			"WHERE USER_NAME = ?";
		
		PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		stmt.setString(1, person.getFirstname());
		stmt.setString(2, person.getLastname());
		stmt.setString(3, person.getAddress());
		stmt.setString(4, person.getEmail());
		stmt.setString(5, person.getPhone());
		stmt.setString(6, person.getUsername());
		
		stmt.executeUpdate();
	}
	
    /**
     * 
     * @param Person model
     * @throws Exception
     */
	public void storePerson(Person person) throws Exception {
        String query = "INSERT INTO PERSONS VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = getInstance().conn.prepareStatement(query);
        stmt.setString(1, person.getUsername());
        stmt.setString(2, person.getFirstname());
        stmt.setString(3, person.getLastname());
        stmt.setString(4, person.getAddress());
        stmt.setString(5, person.getEmail());
        stmt.setString(6, person.getPhone());
  
        stmt.executeUpdate();
    }
	
	// Data Analytics Section
	public String getImagesPerUser() throws Exception {
		String query = 
			"SELECT U.USER_NAME, NVL(IMAGE_COUNT.IMG_COUNT, 0) AS COUNT "+
			"FROM USERS U "+
			"LEFT JOIN "+
			"  ( "+
			"   SELECT OWNER_NAME, COUNT(*) AS IMG_COUNT "+
			"    FROM IMAGES "+
			"    GROUP BY OWNER_NAME "+
			"  ) IMAGE_COUNT "+
			"ON U.USER_NAME = IMAGE_COUNT.OWNER_NAME "+
			"ORDER BY COUNT DESC, U.USER_NAME";
		
		Statement stmt = getInstance().conn.createStatement();
		
		JSONObject jsonResultObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			JSONObject tempJsonObj = new JSONObject();
			
			tempJsonObj.put("username", rs.getString(1));
			tempJsonObj.put("count", rs.getInt(2));
			jsonArray.put(tempJsonObj);
		}
		
		jsonResultObj.put("result", jsonArray);
		
		return jsonResultObj.toString();
	}
}
