package imageshare.model;

import java.awt.image.BufferedImage;
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
	private Date date;
	private String description; // 2048 char max
	private BufferedImage thumbnail;
	private BufferedImage image;
	private int hits;
	private int score;
	
    public Image(String ownerName, int permitted, String subject, String place,
            Date date, String description, BufferedImage thumbnail,
            BufferedImage image) {
        this.photoId = 0;
        this.ownerName = ownerName;
        this.permitted = permitted;
        this.subject = subject;
        this.place = place;
        this.date = date;
        this.description = description;
        this.thumbnail = thumbnail;
        this.image = image;
    }
    
    public Image(String ownerName, int permitted, String subject, String place,
            Date date, String description, BufferedImage thumbnail,
            BufferedImage image, int score) {
        this.photoId = 0;
        this.ownerName = ownerName;
        this.permitted = permitted;
        this.subject = subject;
        this.place = place;
        this.date = date;
        this.description = description;
        this.thumbnail = thumbnail;
        this.image = image;
        this.score = score;
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

	public Date getDate() {
		return date;
	}

	public String getDescription() {
	    return description;
	}
	
    public BufferedImage getThumbnail() {
        return thumbnail;
    }

    public BufferedImage getImage() {
        return image;
    }
    
    public int getHits() {
        return hits;
    }
    
	// setters
	
    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
    
    public void setThumbnail(BufferedImage thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

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

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDescription(String description) {
	    this.description = description;
	}
	
	public void setHits(int hits) {
	    this.hits = hits;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
