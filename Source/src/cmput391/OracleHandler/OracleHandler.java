package cmput391.OracleHandler;

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

	public static Vector<Vector<String>> retrieveResultSet(String statement) {
		Vector<Vector<String>> resultVector = null;
		
		try {
			Connection conn = getConnected(ORACLE_DRIVER, CONNECTION_STRING, USERNAME, PASSWORD);
			
			PreparedStatement stmt = conn.prepareStatement(statement);
			ResultSet rset = stmt.executeQuery(statement);
			
			ResultSetMetaData rsmd = rset.getMetaData();		
			resultVector = new Vector<Vector<String>>();

			int colCount = rsmd.getColumnCount();
			
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
	
	private static Connection getConnected(
		String drivername, String dbstring, String username, String password) throws Exception {
		
		@SuppressWarnings("rawtypes")
		Class drvClass = Class.forName(drivername);
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		
		return DriverManager.getConnection(dbstring,username,password);
	}
}
