package imageshare.model;

import java.util.Date;


public class Group {
	
	private int groupId;		// primary key
	private String username;	// unique (username, groupname)
	private String groupname;
	private Date dateCreated;
	
    public Group(int groupId, String username, String groupname,
            Date dateCreated) {
        this.groupId = groupId;
        this.username = username;
        this.groupname = groupname;
        this.dateCreated = dateCreated;
    }
    
	// getters
	
	public int getGroupId() {
		return groupId;
	}

	public String getUsername() {
		return username;
	}

	public String getGroupname() {
		return groupname;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	// setters
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
