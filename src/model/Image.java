package model;

// Generated 2015-7-28 23:22:43 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Image generated by hbm2java
 */
@Entity
@Table(name = "image")
public class Image implements java.io.Serializable {

	private Integer id;
	private String name;
	private Integer ispublic;
	private Integer ownerId;
	private String imageId;

	public Image() {
	}

	public Image(String name, Integer ispublic, Integer ownerId, String imageId) {
		this.name = name;
		this.ispublic = ispublic;
		this.ownerId = ownerId;
		this.imageId = imageId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ispublic")
	public Integer getIspublic() {
		return this.ispublic;
	}

	public void setIspublic(Integer ispublic) {
		this.ispublic = ispublic;
	}

	@Column(name = "owner_id")
	public Integer getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	@Column(name = "image_id", length = 200)
	public String getImageId() {
		return this.imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

}