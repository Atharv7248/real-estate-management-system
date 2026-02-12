package com.reindeer.trail.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;
    private BigDecimal price;

    // 🔥 New field for image file name (e.g., "property1.jpg")
    private String image;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

	public String getOwnerEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOwnerEmail(String value) {
		// TODO Auto-generated method stub
		
	}
}
