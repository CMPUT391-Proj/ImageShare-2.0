package cmput391.Classes;

import java.util.Date;


public class Image {
	
	private int photoId;		// primary key
	private String ownerName;	// 24 char max
	private int permitted;
	private String subject;		// 128 char max
	private String place;		// 128 char max
	private Date timing;
	private byte[] blob;		// binary object  narr = new byte[len];
	
	public Image(String ownerName, int permitted, String subject,
			String place, Date timing, byte[] blob) {
		this.photoId = 0;
		this.ownerName = ownerName;
		this.permitted = permitted;
		this.subject = subject;
		this.place = place;
		this.timing = timing;
		this.blob = blob;
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

	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

}
