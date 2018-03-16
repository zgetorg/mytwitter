package com.gech.twitterclone.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long post_id;

//   @NotEmpty
   private String content;
   
   @ManyToOne
   @JoinColumn(name="user_id")
   private User postedBy;
   private String image;

	public void setPost_id(long post_id) {
		this.post_id = post_id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	private Date postedDate;


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public User getPostedBy() {
		return postedBy;
	}


	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}


	public Date getPostedDate() {
		return postedDate;
	}


	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}


	public long getPost_id() {
		return post_id;
	}

}