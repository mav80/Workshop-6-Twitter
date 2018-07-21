package pl.coderslab.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank
	@NotNull
	@Column(nullable = false, unique = true)
	private String username;
	
	@NotBlank
	@NotNull
	@Email
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotNull
	@Column(nullable = false)
	private boolean enabled;
	
	@NotNull
	@Column(nullable = false)
	private boolean deleted;
	
	@Column(nullable = false)
	private int tweeCount;
	
	@Column(nullable = false)
	private int commentCount;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp created;
	
	@Column(nullable = false)
	private boolean admin;
	
	@NotBlank
	@NotNull
	@Column(nullable = false)
	private String password;
	
	
	
	@Lob
	@Column(columnDefinition="mediumblob")
	@Size(max = 1048576, message="Obrazek nie może mieć więcej niż 1MB!")
	private byte[] usrImg;

	
	
	
	
	
	@OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
	private List<Tweet> tweets = new ArrayList<>();
	
	
	@OneToMany(mappedBy = "user")
	private List<Comment> comments = new ArrayList<>();
	
	
	
	
	
	
	@OneToMany(mappedBy = "sender")
	private List<Message> sentMessages = new ArrayList<>();
	
	@OneToMany(mappedBy = "receiver")
	private List<Message> receivedMessages = new ArrayList<>();
	
	
	
	
	
	

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	

	public boolean isDeleted() {
		return deleted;
	}



	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}



	public int getTweeCount() {
		return tweeCount;
	}



	public void setTweeCount(int tweeCount) {
		this.tweeCount = tweeCount;
	}



	public int getCommentCount() {
		return commentCount;
	}



	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}



	public Timestamp getCreated() {
		return created;
	}



	public void setCreated(Timestamp created) {
		this.created = created;
	}



	public boolean isAdmin() {
		return admin;
	}



	public void setAdmin(boolean admin) {
		this.admin = admin;
	}



	public List<Tweet> getTweets() {
		return tweets;
	}



	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	

	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}
	
	



	public List<Comment> getComments() {
		return comments;
	}



	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
	
	



	public List<Message> getSentMessages() {
		return sentMessages;
	}



	public void setSentMessages(List<Message> sentMessages) {
		this.sentMessages = sentMessages;
	}



	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}



	public void setReceivedMessages(List<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}
	



	public byte[] getUsrImg() {
		return usrImg;
	}



	public void setUsrImg(byte[] usrImg) {
		this.usrImg = usrImg;
	}






//	@Override
//	public String toString() {
//		return String.format(
//				"Dane usera (toString): [id=%s, username=%s, password=%s, enabled=%s, email=%s, created=%s, admin=%s, tweeCount=%s, commentCount=%s, tweets=%s]",
//				id, username, password, enabled, email, created, admin, tweeCount, commentCount, tweets);
//	}
	
	
	

	@Override
	public String toString() {
		return String.format(
				"User [id=%s, username=%s, email=%s, enabled=%s, deleted=%s, tweeCount=%s, commentCount=%s, created=%s, admin=%s, password=%s]",
				id, username, email, enabled, deleted, tweeCount, commentCount, created, admin, password);
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (admin ? 1231 : 1237);
		result = prime * result + commentCount;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((receivedMessages == null) ? 0 : receivedMessages.hashCode());
		result = prime * result + ((sentMessages == null) ? 0 : sentMessages.hashCode());
		result = prime * result + tweeCount;
		result = prime * result + ((tweets == null) ? 0 : tweets.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + Arrays.hashCode(usrImg);
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (admin != other.admin)
			return false;
		if (commentCount != other.commentCount)
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (deleted != other.deleted)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (id != other.id)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (receivedMessages == null) {
			if (other.receivedMessages != null)
				return false;
		} else if (!receivedMessages.equals(other.receivedMessages))
			return false;
		if (sentMessages == null) {
			if (other.sentMessages != null)
				return false;
		} else if (!sentMessages.equals(other.sentMessages))
			return false;
		if (tweeCount != other.tweeCount)
			return false;
		if (tweets == null) {
			if (other.tweets != null)
				return false;
		} else if (!tweets.equals(other.tweets))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (!Arrays.equals(usrImg, other.usrImg))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	
	
	

}
