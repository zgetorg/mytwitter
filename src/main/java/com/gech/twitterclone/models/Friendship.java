package com.gech.twitterclone.models;


import javax.persistence.*;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id", "following_id"})})
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long friendship_id;
   
    @ManyToOne
    @JoinColumn(name="follower_id")
    private User follower;
    
    @ManyToOne
    @JoinColumn(name="following_id")
    private User following;
    
    private boolean confirmed;
    
	public User getFollower() {
		return follower;
	}
	public void setFollower(User follower) {
		this.follower = follower;
	}
	public User getFollowing() {
		return following;
	}
	public void setFollowing(User following) {
		this.following = following;
	}
	public long getFriendship_id() {
		return friendship_id;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	
}