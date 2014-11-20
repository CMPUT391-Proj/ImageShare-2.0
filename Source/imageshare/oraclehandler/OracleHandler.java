package imageshare.oraclehandler;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

public class OracleHandler {
	
	// connection should stay open, rather than reconnecting
	private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	//private static final String CONNECTION_STRING = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS"; // use for University
	private static final String CONNECTION_STRING = "jdbc:oracle:thin:@localhost:1521:CRS"; // use for SSH
	private static final String USERNAME = "jyuen";
	private static final String PASSWORD = "pass2014";
	
	/*
	 * @param	statement	Insert statement to insert a record into the Oracle database
	 * @return				Returns 0 if true or -1 if false
	 */
	public static int insertRecord(String statement) {
		try {
			Connection conn = getConnected(ORACLE_DRIVER, CONNECTION_STRING, USERNAME, PASSWORD);
			
			PreparedStatement stmt = conn.prepareStatement(statement);
			
			stmt.executeUpdate();
			stmt.execute("commit");
			conn.close();
		} catch (SQLException e) {
			System.err.println("SQL ERROR: "+e.getMessage());
			
			return -1;
		} catch (Exception e) {
			System.err.println("ERROR: "+e.getMessage());
			
			return -1;
		}
		
		return 0;
	}

	/*
	 * @param	statement	Query statement use to query Oracle database
	 * @return				Returns a two dimensional vector which consists of the record and its corresponding column records 						
	 */
	public static Vector<Vector<String>> retrieveResultSet(String statement) {
		Vector<Vector<String>> resultVector = null;
		
		try {
			Connection conn = getConnected(ORACLE_DRIVER, CONNECTION_STRING, USERNAME, PASSWORD);
			
			PreparedStatement stmt = conn.prepareStatement(statement);
			ResultSet rset = stmt.executeQuery(statement);
			
			ResultSetMetaData rsmd = rset.getMetaData();		
			resultVector = new Vector<Vector<String>>();

			int colCount = rsmd.getColumnCount();
			
			// algorithm could be improved.
			// if unknown oracle type comes up, google the type number and add it
			// to the else if statements
			while (rset.next()) {
				Vector<String> resultRow = new Vector<String>();
				
				for (int i=1; i<=colCount; i++) {
					if (rsmd.getColumnType(i) == Types.INTEGER) {
						resultRow.add(""+rset.getInt(i));
					} else if(rsmd.getColumnType(i) == Types.VARCHAR) {
						resultRow.add(rset.getString(i));
					} else if(rsmd.getColumnType(i) == Types.TIMESTAMP){
						resultRow.add(rset.getTimestamp(i).toString());
					} else {
						throw new Exception("UKNOWN ORACLE TYPE: "+rsmd.getColumnType(i));
					}
				}
				
				resultVector.add(resultRow);
			}
			
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println("SQL ERROR: "+e.getMessage());
			
			return null;
		} catch (Exception e) {
			System.err.println("ERROR: "+e.getMessage());
			
			return null;
		}
		
		return resultVector;
	}
	
	/*
	 * @param	driverName	Oracle driver name
	 * @param	connString	Oracle connection string
	 * @param	username	Oracle database username
	 * @param	password	Oracle database password
	 * @return				Connection object
	 */
	private static Connection getConnected(
		String driverName, String connString, String username, String password) throws Exception {
		
		@SuppressWarnings("rawtypes")
		Class drvClass = Class.forName(driverName);
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		
		return DriverManager.getConnection(connString,username,password);
	}
}
