package imageshare.model;

import java.util.Date;

/**
 * Represents a image with a name, subject, location, date, and permission
 * settings.
 *
 */
public class Image {
	
	private int photoId;		// primary key
	private String ownerName;	// 24 char max
	private int permitted;
	private String subject;		// 128 char max
	private String place;		// 128 char max
	private Date timing;
	private String description; // 2048 char max
	private byte[] blob;		// binary object  narr = new byte[len];
	
	public Image(String ownerName, int permitted, String subject,
			String place, Date timing, String description, byte[] blob) {
		this.photoId = 0;
		this.ownerName = ownerName;
		this.permitted = permitted;
		this.subject = subject;
		this.place = place;
		this.timing = timing;
		this.description = description;
		this.blob = blob;
	}
	
    public Image(String ownerName, int permitted, String subject, String place,
            Date timing, String description) {
        this(ownerName, permitted, subject, place, timing, description, null);
    }

	// getters
	
	public int getPhotoId() {
		return photoId;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public int getPermitted() {
		return permitted;
	}

	public String getSubject() {
		return subject;
	}

	public String getPlace() {
		return place;
	}

	public Date getTiming() {
		return timing;
	}

	public String getDescription() {
	    return description;
	}
	
	public byte[] getBlob() {
		return blob;
	}

	// setters
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public void setPermitted(int permitted) {
		this.permitted = permitted;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setTiming(Date timing) {
		this.timing = timing;
	}

	public void setDescription(String description) {
	    this.description = description;
	}
	
	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

}
