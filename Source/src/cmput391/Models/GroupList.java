package cmput391.Models;

import java.util.Date;


public class GroupList {
	
	private int groupId;		// primary key (groupId, friendId)
	private String friendId;	// 24 char max
	private Date dateAdded;
	private String notice;		// 1024 char max
	
	public GroupList(String friendId, Date dateAdded, String notice) {
		this.groupId = 0;
		this.friendId = friendId;
		this.dateAdded = dateAdded;
		this.notice = notice;
	}

	// getters
	
	public int getGroupId() {
		return groupId;
	}

	public String getFriendId() {
		return friendId;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public String getNotice() {
		return notice;
	}

	// setters
	
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
}
