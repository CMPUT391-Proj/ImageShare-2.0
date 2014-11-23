package imageshare.model;

import java.util.ArrayList;
import java.util.List;

public class GroupTuple {
    Group group;
    List<GroupList> groupLists;
    
    /**
     * Constructor
     * @param group
     * @param groupUsers
     */
    public GroupTuple(Group group, List<GroupList> groupLists) {
    	this.group = group;
    	this.groupLists = groupLists;
    }
    
    /**
     * Overloaded constructor for when group members are not known
     * @param name
     * @param id
     */
    public GroupTuple(Group group){
    	this.group = group;
    }
    
    /**
     * Add a Group_list to the group
     * @param user
     */
    public void addGroup_list(GroupList groupList) {
    	this.groupLists.add(groupList);
    }
    
    /**
     * Remove a Group_list to the group
     * @param user
     */
    public void removeGroup_list(GroupList groupList) {
    	this.groupLists.remove(groupList);
    }

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<GroupList> getGroupLists() {
		return groupLists;
	}

	public void setGroupLists(List<GroupList> groupUsers) {
		this.groupLists = groupUsers;
	}
}
