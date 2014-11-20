package cmput391.Models;

import java.util.Date;


public class User {
	
	private String username; // primary key
	private String password;
	private Date registeredDate;
	
	public User(String username, String password, Date registeredDate) {
		this.username = username;
		this.password = password;
		this.registeredDate = registeredDate;
	}

	// getters
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	// setters
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
}
