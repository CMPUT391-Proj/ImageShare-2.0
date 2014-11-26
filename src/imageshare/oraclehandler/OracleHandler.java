package imageshare.oraclehandler;

import imageshare.model.Group;
import imageshare.model.GroupList;
import imageshare.model.Image;
import imageshare.model.Person;
import imageshare.model.User;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidParameterException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

public class OracleHandler {

	private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String CONNECTION_STRING = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS"; // use for University
	//private static final String CONNECTION_STRING = "jdbc:oracle:thin:@localhost:1525:CRS"; // use for SSH
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

	public void closeConnection() {
		try {
			OracleHandler.getInstance().conn.close();
			oracleHandler = null;
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

	 private List<Image> retrieveImagesFromResultSet(ResultSet rs)
			 throws Exception {
		 List<Image> images = new ArrayList<Image>();

		 while (rs.next()) {
			 int photo_id = rs.getInt("photo_id");
			 String owner = rs.getString("owner_name");
			 int permitted = rs.getInt("permitted");
			 String subject = rs.getString("subject");
			 String place = rs.getString("place");
			 Date timing = rs.getDate("timing");
			 String description = rs.getString("description");
			 BufferedImage thumbnail = ImageIO.read(rs.getBlob("thumbnail")
					 .getBinaryStream());
			 BufferedImage pic = ImageIO.read(rs.getBlob("photo")
					 .getBinaryStream());

			 Image image = new Image(owner, permitted, subject, place, timing,
					 description, thumbnail, pic);
			 image.setPhotoId(photo_id);
			 images.add(image);
		 }

		 return images;
	 }

	 /**
	  * Retrieves all images from the database that the user can see
	  * @return
	  * @throws Exception
	  */
	 public List<Image> getAllImages(String user) throws Exception {
		 String query;

		 if (user.equalsIgnoreCase("admin")) {
			 query = "SELECT * FROM images";
		 } else {  
			 query = "SELECT distinct * FROM images i WHERE i.owner_name = ? "
					 + "OR i.permitted = 1 OR i.permitted IN "
					 + "(SELECT group_id FROM groups WHERE group_id = i.permitted "
					 + "and user_name = ?) "
					 + "OR i.permitted IN (SELECT group_id FROM group_lists WHERE "
					 + "group_id = i.permitted AND friend_id = ?)";
		 }
		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 if (!user.equalsIgnoreCase("admin")) {
			 stmt.setString(1, user);
			 stmt.setString(2, user);
			 stmt.setString(3, user);
		 }

		 ResultSet rs = stmt.executeQuery();
		 return retrieveImagesFromResultSet(rs);
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

		 ByteArrayOutputStream baosImage = new ByteArrayOutputStream();

		 ImageIO.write(image.getImage(), "jpg", baosImage);
		 InputStream imageInputStream = new ByteArrayInputStream(
				 baosImage.toByteArray());

		 ByteArrayOutputStream baosThumbnail = new ByteArrayOutputStream();

		 ImageIO.write(image.getThumbnail(), "jpg", baosThumbnail);
		 InputStream thumbnailInputStream = new ByteArrayInputStream(
				 baosThumbnail.toByteArray());

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 stmt.setInt(1, imageID);
		 stmt.setString(2, image.getOwnerName());
		 stmt.setInt(3, image.getPermitted());
		 stmt.setString(4, image.getSubject());
		 stmt.setString(5, image.getPlace());
		 stmt.setDate(6, new Date(image.getDate().getTime()));
		 stmt.setString(7, image.getDescription());
		 stmt.setBinaryStream(8, thumbnailInputStream);
		 stmt.setBinaryStream(9, imageInputStream);

		 stmt.executeUpdate();

		 increaseImageHits(imageID);
	 }

	 /**
	  * @return the next image id
	  * @throws Exception
	  */
	 public int nextImageID() throws Exception {
		 String sql = "SELECT image_sequence.nextval from dual";
		 PreparedStatement ps = getInstance().conn.prepareStatement(sql);
		 ResultSet rs = ps.executeQuery();
		 rs.next();
		 return rs.getInt(1);
	 }

	 private void addImagePopularityRow(int photoId) throws Exception {
		 String insert = "INSERT INTO imagepopularity VALUES (?, ?)";
		 PreparedStatement insertStmt = getInstance().conn
				 .prepareStatement(insert);
		 insertStmt.setInt(1, photoId);
		 insertStmt.setInt(2, 0);
		 insertStmt.executeUpdate();        
	 }

	 /**
	  * Increases the popularity count of a image or adds a row if 
	  * it is not previously set.
	  * @param photoId
	  * @throws Exception
	  */
	 public void increaseImageHits(int photoId) throws Exception{
		 // check if image is in imagepopularity table
		 String query = "SELECT * FROM imagepopularity WHERE photo_id = ?";
		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 stmt.setInt(1, photoId);
		 ResultSet rs = stmt.executeQuery();

		 if (!rs.isBeforeFirst()) {
			 // is empty - add a row
			 addImagePopularityRow(photoId);
		 } else {
			 // update hits
			 String update = "UPDATE imagepopularity SET hits = "
					 + "(SELECT hits FROM imagepopularity WHERE photo_id = ?) + 1 "
					 + "WHERE photo_id = ?";
			 PreparedStatement updateStmt = getInstance().conn
					 .prepareStatement(update);
			 updateStmt.setInt(1, photoId);
			 updateStmt.setInt(2, photoId);
			 updateStmt.executeUpdate();
		 }
	 }

	 /**
	  * Get the images in sequence order of decreasing popularity.
	  * @return
	  * @throws Exception 
	  */
	 public List<Image> getImagesByPopularity(String user) throws Exception {
		 String query;

		 if (user.equalsIgnoreCase("admin"))
			 query = "SELECT * FROM images i "
					 + "LEFT JOIN imagepopularity p "
					 + "on p.photo_id = i.photo_id "
					 + "ORDER BY p.hits DESC";
		 else {
			 query = "SELECT * FROM images i "
					 + "LEFT JOIN imagepopularity p "
					 + "ON p.photo_id = i.photo_id "
					 + "WHERE i.owner_name = ? "
					 + "OR i.permitted     = 1 "
					 + "OR i.permitted    IN "
					 + "(SELECT group_id "
					 + "FROM groups "
					 + "WHERE group_id = i.permitted "
					 + "AND user_name  = ? "
					 + ") "
					 + "OR i.permitted IN "
					 + "(SELECT group_id "
					 + "FROM group_lists "
					 + "WHERE group_id = i.permitted "
					 + "AND friend_id  = ? "
					 + ") "
					 + "ORDER BY p.hits DESC ";
		 }

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 if (!user.equalsIgnoreCase("admin")) {
			 stmt.setString(1, user);
			 stmt.setString(2, user);
			 stmt.setString(3, user);
		 }

		 ResultSet rs = stmt.executeQuery();

		 List<Image> images = new ArrayList<Image>();

		 while (rs.next()) {
			 int photo_id = rs.getInt("photo_id");
			 String owner = rs.getString("owner_name");
			 int permitted = rs.getInt("permitted");
			 String subject = rs.getString("subject");
			 String place = rs.getString("place");
			 Date timing = rs.getDate("timing");
			 String description = rs.getString("description");
			 BufferedImage thumbnail = ImageIO.read(rs.getBlob("thumbnail")
					 .getBinaryStream());
			 BufferedImage pic = ImageIO.read(rs.getBlob("photo")
					 .getBinaryStream());
			 int hits = rs.getInt("hits");

			 Image image = new Image(owner, permitted, subject, place, timing,
					 description, thumbnail, pic);
			 image.setPhotoId(photo_id);
			 image.setHits(hits);
			 images.add(image);
		 }

		 return images;
	 }

	 /**
	  * @return the number of popular images, usually 5 but more if there are
	  *         ties.
	  * @param user
	  */
	 public int getNumberOfPopularImages(String user) throws Exception {
		 Map<Integer, Integer> hitMap = new HashMap<Integer, Integer>();
		 int count = 0;

		 String query;
		 if (user.equalsIgnoreCase("admin")) {
			 query = "select p.hits from imagepopularity p "
					 + "order by p.hits desc";
		 } else {
			 query = "SELECT p.hits FROM images i "
					 + "LEFT JOIN imagepopularity p "
					 + "ON p.photo_id = i.photo_id "
					 + "WHERE i.owner_name = ? "
					 + "OR i.permitted     = 1 "
					 + "OR i.permitted    IN "
					 + "(SELECT group_id "
					 + "FROM groups "
					 + "WHERE group_id = i.permitted "
					 + "AND user_name  = ? "
					 + ") "
					 + "OR i.permitted IN "
					 + "(SELECT group_id "
					 + "FROM group_lists "
					 + "WHERE group_id = i.permitted "
					 + "AND friend_id  = ? "
					 + ") "
					 + "ORDER BY p.hits DESC ";
		 }

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 if (!user.equalsIgnoreCase("admin")) {
			 stmt.setString(1, user);
			 stmt.setString(2, user);
			 stmt.setString(3, user);
		 }

		 ResultSet rs = stmt.executeQuery();
		 while (rs.next()) {
			 int hits = rs.getInt("hits");
			 if (hitMap.containsKey(hits)) {
				 hitMap.put(hits, hitMap.get(hits) + 1);
			 } else {
				 if (hitMap.size() < 5)
					 hitMap.put(hits, 1);
				 else
					 break;
			 }
		 }

		 for (Integer hits : hitMap.values())
			 count += hits;

		 return count;
	 }

	 /**
	  * Returns the input stream of the thumbnail for the specified photoId.
	  * 
	  * @param photoId
	  * @return
	  */
	 public InputStream getThumbnailInputStream(int photoId) throws Exception {
		 String query = "SELECT thumbnail FROM images where photo_id = ?";

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 stmt.setInt(1, photoId);

		 ResultSet rs = stmt.executeQuery();
		 if (rs.next()) {
			 return rs.getBinaryStream("thumbnail");
		 } else {
			 throw new InvalidParameterException("Photo id does not exist!");
		 }
	 }

	 /**
	  * Returns the input stream of the image for the specified photoId.
	  * 
	  * @param photoId
	  * @return
	  */
	 public InputStream getImageInputStream(int photoId) throws Exception {
		 String query = "SELECT photo FROM images where photo_id = ?";

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 stmt.setInt(1, photoId);

		 ResultSet rs = stmt.executeQuery();
		 if (rs.next()) {
			 return rs.getBinaryStream("photo");
		 } else {
			 throw new InvalidParameterException("Photo id does not exist!");
		 }
	 }

	 /**
	  * Update the image from the provided photo id.
	  * @param photoId
	  * @param subject
	  * @param location
	  * @param description
	  * @param date
	  * @param security
	  * @throws Exception 
	  */
	 public void updateImage(int photoId, String subject, String location,
			 String description, java.util.Date date, int security) throws Exception {
		 String update = "UPDATE images SET subject = ?," + "place = ?, "
				 + "timing = ?," + "description =?," + "permitted =? "
				 + "WHERE photo_id = ?";
		 PreparedStatement updateStmt = getInstance().conn
				 .prepareStatement(update);
		 updateStmt.setString(1, subject);
		 updateStmt.setString(2, location);
		 updateStmt.setDate(3, new Date(date.getTime()));
		 updateStmt.setString(4, description);
		 updateStmt.setInt(5,security);
		 updateStmt.setInt(6, photoId);
		 updateStmt.executeUpdate();
	 }

	 /**
	  * Return the image given by the photo id
	  * @param photoId
	  * @return
	  * @throws Exception
	  */
	 public Image getImageById(int photoId) throws Exception {
		 String query = "SELECT * FROM images where photo_id = ?";
		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 stmt.setInt(1, photoId);
		 ResultSet rs = stmt.executeQuery();
		 return retrieveImagesFromResultSet(rs).get(0);
	 }

	 /**
	  * Returns the name of the group
	  * @param groupId
	  * @return
	  * @throws Exception
	  */
	 public String getGroupName(int groupId) throws Exception {
		 String query = "SELECT group_name FROM groups WHERE group_id = ?";
		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 stmt.setInt(1, groupId);
		 ResultSet rs = stmt.executeQuery();
		 rs.next();
		 return rs.getString("group_name");
	 }

	 /**
	  * Get's a user's owned groups
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

		 String query;
		 if (user.equalsIgnoreCase("admin")) {
			 query = "SELECT * FROM groups where group_id != 1 "
					 + "and group_id != 2";
		 } else {
			 query = "SELECT * FROM groups WHERE group_id in "
					 + "(SELECT group_id FROM group_lists where "
					 + "friend_id = ? ) OR " + "user_name = ?";
		 }

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 if (!user.equalsIgnoreCase("admin")) {
			 stmt.setString(1, user);
			 stmt.setString(2, user);
		 }

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
	  * Adds a user created group to the database
	  * @param String user
	  * @param String group_name
	  * @throws Exception 
	  */
	 public void storeNewGroup(String user, String group_name) throws Exception {
		 String query = "insert into groups " + "values ("
				 + "group_id_sequence.nextval, '" + user + "', '" + 
				 group_name + "', sysdate)";

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 stmt.executeUpdate();
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
	  * Returns all usernames in a group
	  * @param int group_id
	  * @throws Exception 
	  * @returns ArrayList<String>
	  */
	 public List<String> getUsersInGroup(int group_id) throws Exception {
		 ArrayList<String> friend_ids = new ArrayList<String>();
		 String query = "SELECT friend_id "
				 + "FROM group_lists "
				 + "WHERE group_id = "
				 + group_id;
		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 ResultSet rs = stmt.executeQuery();

		 friend_ids =  user_from_resultset_group(rs);
		 query = "SELECT user_name from groups where group_id = " +
				 group_id;
		 stmt = getInstance().conn.prepareStatement(query);
		 rs = stmt.executeQuery();

		 try
		 {
			 if (rs.next())
			 {
				 friend_ids.add(rs.getString("user_name"));
			 }
		 }
		 catch (Exception e) {
			 e.printStackTrace();
		 }
		 return friend_ids;
	 }

	 /**
	  * Returns all usernames in a group
	  * @param int group_id
	  * @throws Exception 
	  * @returns ArrayList<String>
	  */
	 public List<String> getUsersNotInGroup(int group_id) throws Exception {
		 ArrayList<String> friend_ids = new ArrayList<String>();
		 String query = "SELECT user_name from users where user_name NOT IN (SELECT friend_id "
				 + "FROM group_lists "
				 + "WHERE group_id = "
				 + group_id + ")";
		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 ResultSet rs = stmt.executeQuery();

		 try {
			 while (rs.next()) {
				 String friend_id = rs.getString("user_name");
				 friend_ids.add(friend_id);
			 }
		 } catch (SQLException e) {
			 e.printStackTrace();
		 }
		 return friend_ids;
	 }

	 /**
	  * Gets users in a group
	  * @param rs
	  * @return
	  */
	 public ArrayList<String> user_from_resultset_group(ResultSet rs) {
		 ArrayList<String> all_users;
		 all_users = new ArrayList<String>();

		 try {
			 while (rs.next()) {
				 String friend_id = rs.getString("friend_id");
				 all_users.add(friend_id);
			 }
		 } catch (SQLException e) {
			 e.printStackTrace();
		 }
		 return all_users;
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
	 public JSONObject getImagesPerUserOLD() throws Exception {
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

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 return generateJsonFromPreparedStatement(stmt);
	 }

	 public JSONObject getImagesPerSubject() throws Exception {
		 String query = 
				 "SELECT NVL(SUBJECT,'NO_SUBJECT') AS SUBJECT, COUNT(*) AS COUNT "+
						 "FROM IMAGES "+
						 "GROUP BY SUBJECT "+
						 "ORDER BY SUBJECT";

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 return generateJsonFromPreparedStatementNEW(stmt);
	 }

	 public JSONObject getImagesPerUser() throws Exception {
		 String query = 
				 "SELECT OWNER_NAME, COUNT(*) AS COUNT "+ 
						 "FROM IMAGES "+
						 "GROUP BY OWNER_NAME "+
						 "ORDER BY OWNER_NAME";

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 return generateJsonFromPreparedStatementNEW(stmt);
	 }

	 public JSONObject getAnalyticsForYear(String fromDate, String toDate, String subjectList, String usernameList) throws Exception {
		 String fromDateQuery = null;
		 String toDateQuery = null;

		 fromDateQuery = String.format("TIMING >= TO_DATE('%s', 'yyyy-MM-dd')", fromDate);
		 toDateQuery = String.format("TIMING <= TO_DATE('%s', 'yyyy-MM-dd')", toDate);

		 String query = 
				 String.format(
						 "SELECT TO_CHAR(TRUNC(TIMING), 'yyyy') AS YEAR, COUNT(*) AS COUNT "+
								 "FROM IMAGES "+
								 "WHERE %s " +
								 "AND %s "+
								 "%s "+
								 "%s "+
								 "GROUP BY TO_CHAR(TRUNC(TIMING), 'yyyy') "+
								 "ORDER BY TO_CHAR(TRUNC(TIMING), 'yyyy') ", 
								 fromDateQuery, toDateQuery,
								 (subjectList != null ? String.format("AND SUBJECT IN (%s)", subjectList) : ""),
								 (usernameList != null ? String.format("AND OWNER_NAME IN (%s)", usernameList) : ""));

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 return generateJsonFromPreparedStatementNEW(stmt);
	 }

	 public JSONObject getAnalyticsForMonth(String fromDate, String toDate, String subjectList) throws Exception {
		 String fromDateQuery = null;
		 String toDateQuery = null;

		 fromDateQuery = String.format("TIMING >= TO_DATE('%s', 'yyyy-MM-dd')", fromDate);
		 toDateQuery = String.format("TIMING <= TO_DATE('%s', 'yyyy-MM-dd')", toDate);

		 String query = 
				 String.format(
						 "SELECT TO_CHAR(TRUNC(TIMING), 'MM') AS MONTH, COUNT(*) AS COUNT "+
								 "FROM IMAGES "+
								 "WHERE %s " +
								 "AND %s "+
								 "%s "+
								 "GROUP BY TO_CHAR(TRUNC(TIMING), 'MM') "+
								 "ORDER BY TO_CHAR(TRUNC(TIMING), 'MM') ", 
								 fromDateQuery, toDateQuery, 
								 (subjectList != null ? String.format("AND SUBJECT IN (%s)", subjectList) : ""));

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 return generateJsonFromPreparedStatementNEW(stmt);
	 }

	 public JSONObject getAnalyticsForMonthByYear(int year, String fromDate, String toDate, String subjectList, String usernameList) throws Exception {
		 String fromDateQuery = null;
		 String toDateQuery = null;

		 fromDateQuery = String.format("TIMING >= TO_DATE('%s', 'yyyy-MM-dd')", fromDate);
		 toDateQuery = String.format("TIMING <= TO_DATE('%s', 'yyyy-MM-dd')", toDate);

		 String query = 
				 String.format(
						 "SELECT TO_CHAR(TRUNC(TIMING), 'MM') AS MONTH, COUNT(*) AS COUNT "+
								 "FROM IMAGES "+
								 "WHERE TO_CHAR(TRUNC(TIMING), 'yyyy')=%s "+
								 "AND %s "+
								 "AND %s "+
								 "%s "+
								 "%s "+
								 "GROUP BY TO_CHAR(TRUNC(TIMING), 'MM') "+
								 "ORDER BY TO_CHAR(TRUNC(TIMING), 'MM') ", 
								 year, fromDateQuery, toDateQuery, 
								 (subjectList != null ? String.format("AND SUBJECT IN (%s)", subjectList) : ""),
								 (usernameList != null ? String.format("AND OWNER_NAME IN (%s)", usernameList) : ""));


		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 return generateJsonFromPreparedStatementNEW(stmt);
	 }

	 public JSONObject getAnalyticsForDayByYearByMonth(int year, int month, String fromDate, String toDate, String subjectList, String usernameList) throws Exception {
		 String fromDateQuery = null;
		 String toDateQuery = null;

		 fromDateQuery = String.format("TIMING >= TO_DATE('%s', 'yyyy-MM-dd')", fromDate);
		 toDateQuery = String.format("TIMING <= TO_DATE('%s', 'yyyy-MM-dd')", toDate);

		 String query = 
				 String.format(
						 "SELECT TO_CHAR(TRUNC(TIMING), 'dd') AS DAY, COUNT(*) AS COUNT "+
								 "FROM IMAGES "+
								 "WHERE TO_CHAR(TRUNC(TIMING), 'yyyy')=%s "+
								 "AND TO_CHAR(TRUNC(TIMING), 'MM')=%s "+
								 "AND %s "+
								 "AND %s "+
								 "%s "+
								 "GROUP BY TO_CHAR(TRUNC(TIMING), 'dd') " +
								 "ORDER BY TO_CHAR(TRUNC(TIMING), 'dd') ",
								 year, month,
								 fromDateQuery, toDateQuery,
								 (subjectList != null ? String.format("AND SUBJECT IN (%s)", subjectList) : ""),
								 (usernameList != null ? String.format("AND OWNER_NAME IN (%s)", usernameList) : ""));

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 return generateJsonFromPreparedStatementNEW(stmt);
	 }

	 public JSONObject getAnalyticsForDay(String fromDate, String toDate, String subjectList) throws Exception {
		 String fromDateQuery = null;
		 String toDateQuery = null;

		 fromDateQuery = String.format("TIMING >= TO_DATE('%s', 'yyyy-MM-dd')", fromDate);
		 toDateQuery = String.format("TIMING <= TO_DATE('%s', 'yyyy-MM-dd')", toDate);

		 String query = 
				 String.format(
						 "SELECT TO_CHAR(TRUNC(TIMING), 'dd') AS DAY, COUNT(*) AS COUNT "+
								 "FROM IMAGES "+
								 "WHERE %s "+
								 "AND %s "+
								 "%s %s %s %s "+
								 "GROUP BY TO_CHAR(TRUNC(TIMING), 'dd') " +
								 "ORDER BY TO_CHAR(TRUNC(TIMING), 'dd') ", 
								 fromDateQuery, toDateQuery,
								 (subjectList != null ? String.format("AND SUBJECT IN (%s)", subjectList) : ""));

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);

		 return generateJsonFromPreparedStatementNEW(stmt);
	 }
	 //////////////////////
	 /**
	  * Returns the images by keywords
	  * @param String keywords, String order
	  * @throws Exception 
     @ @return List<Image>
	  */
	 public List<Image> getImagesByKeywords(String keywords, String order) throws Exception {

		 String query = "SELECT * FROM " +
				 "((SELECT score(1)*6 + score(2)*3 + score(3) AS score, photo_id " +
				 "FROM images " +
				 "WHERE ((contains(subject, '" + keywords + "', 1) > 0) OR (contains(place, '" + keywords + "', 2) > 0) " +
				 "OR (contains(description, '" + keywords + "', 3) > 0)) " + order +") t1 " +
				 "inner join images t2 on t1.photo_id = t2.photo_id)" +
				 "" + order;

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 ResultSet rs =  stmt.executeQuery();

		 List<Image> images = retrieveRankedImagesFromResultSet(rs);
		 return images;
	 }

	 private List<Image> retrieveRankedImagesFromResultSet(ResultSet rs)
			 throws Exception {
		 List<Image> images = new ArrayList<Image>();

		 while (rs.next()) {
			 int score = rs.getInt("score");
			 int photo_id = rs.getInt("photo_id");
			 String owner = rs.getString("owner_name");
			 int permitted = rs.getInt("permitted");
			 String subject = rs.getString("subject");
			 String place = rs.getString("place");
			 Date timing = rs.getDate("timing");
			 String description = rs.getString("description");
			 BufferedImage thumbnail = ImageIO.read(rs.getBlob("thumbnail")
					 .getBinaryStream());
			 BufferedImage pic = ImageIO.read(rs.getBlob("photo")
					 .getBinaryStream());

			 Image image = new Image(owner, permitted, subject, place, timing,
					 description, thumbnail, pic, score);
			 image.setPhotoId(photo_id);
			 images.add(image);
		 }

		 return images;
	 }

	 /**
	  * Returns images through search by date
	  * @param String fromdate, String todate, String order
	  * @return ResultSet
	  * @throws List<Image> 
	  */
	 public List<Image> getImagesByDate(String fromdate, String todate, String order) throws Exception {
		 String query = "SELECT * FROM images WHERE (timing BETWEEN '"
				 + fromdate + "' AND '" + todate + "') " + order;
		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 ResultSet rs =  stmt.executeQuery();
		 List<Image> images = retrieveImagesFromResultSet(rs);

		 return images;
	 }

	 /**
	  * Returns the image of the search by keywords and date
	  *
	  * Rank(photo_id) = 6*frequency(subject) + 3*frequency(location)
	  * + frequency(description)
	  *
	  * @param String fromdate, String todate, String keywords, String order
	  * @return ResultSet
	  * @throws Exception 
	  */
	 public List<Image> getImagesByDateAndKeywords(String fromdate, String todate, 
			 String keywords, String order) throws Exception {

		 String query = "SELECT * FROM " +
				 "((SELECT score(1)*6 + score(2)*3 + score(3) AS score, photo_id " +
				 "FROM images " +
				 "WHERE ((timing BETWEEN '" + fromdate + "' AND '" + todate +  " ') " +
				 "AND (contains(subject, '" + keywords + "', 1) > 0) OR (contains(place, '" + keywords + "', 2) > 0) " +
				 "OR (contains(description, '" + keywords + "', 3) > 0)) " + order +") t1 " +
				 "inner join images t2 on t1.photo_id = t2.photo_id)" +
				 "" + order;

		 PreparedStatement stmt = getInstance().conn.prepareStatement(query);
		 ResultSet rs =  stmt.executeQuery();
		 List<Image> images = retrieveRankedImagesFromResultSet(rs);

		 return images;
	 }

	 private JSONObject generateJsonFromPreparedStatementNEW(PreparedStatement stmt) throws Exception {
		 JSONObject jsonResultObj = new JSONObject();
		 JSONArray jsonRecordList = new JSONArray();

		 ResultSet rs = stmt.executeQuery();
		 ResultSetMetaData rsmd = rs.getMetaData();

		 int columnCount = rsmd.getColumnCount();

		 while(rs.next()) {
			 JSONObject jsonRecordData = new JSONObject();

			 for (int i=1; i<=columnCount; i++) {
				 jsonRecordData.put(rsmd.getColumnName(i), getResultSetColData(rs, rsmd.getColumnType(i), i));
			 }

			 jsonRecordList.put(jsonRecordData);
		 }

		 jsonResultObj.put("result", jsonRecordList);

		 return jsonResultObj;
	 }

	 private JSONObject generateJsonFromPreparedStatement(PreparedStatement stmt) throws Exception {
		 JSONObject jsonResultObj = new JSONObject();
		 JSONArray jsonRecordList = new JSONArray();

		 ResultSet rs = stmt.executeQuery();
		 ResultSetMetaData rsmd = rs.getMetaData();

		 int columnCount = rsmd.getColumnCount();

		 JSONArray jsonColNameList = new JSONArray();
		 for (int i=1; i<=columnCount; i++) {
			 JSONObject jsonCol = new JSONObject();

			 jsonCol.put("data", rsmd.getColumnName(i));
			 jsonCol.put("heading", 1);

			 jsonColNameList.put(jsonCol);
		 }
		 jsonRecordList.put(jsonColNameList);

		 while(rs.next()) {
			 JSONArray jsonRecord = new JSONArray();

			 for (int i=1; i<=columnCount; i++) {
				 JSONObject jsonRecordData = new JSONObject();

				 jsonRecordData.put("data", getResultSetColData(rs, rsmd.getColumnType(i), i));
				 jsonRecordData.put("heading", 0);

				 jsonRecord.put(jsonRecordData);
			 }

			 jsonRecordList.put(jsonRecord);
		 }

		 jsonResultObj.put("result", jsonRecordList);

		 return jsonResultObj;
	 }

	 private String getResultSetColData(ResultSet rs, int type, int col) throws Exception {
		 String data;

		 switch(type) {
		 case Types.INTEGER:
			 data = ""+rs.getInt(col);
			 break;
		 case Types.VARCHAR:
			 data = rs.getString(col);
			 break;
		 case Types.TIMESTAMP:
			 data = rs.getTimestamp(col).toString();
			 break;
		 case Types.NUMERIC:
			 data = ""+rs.getInt(col);
			 break;
		 default:
			 throw new Exception("Unsupported column type.");
		 }

		 return data;
	 }
}
