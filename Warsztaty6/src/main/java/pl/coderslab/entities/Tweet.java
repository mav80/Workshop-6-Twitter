package pl.coderslab.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;


@Entity
public class Tweet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Type(type="text")
	@Column(nullable = false)
	@Size(min = 5, max = 280, message="Tweet nie może mieć mniej niż 5 i więcej niż 280 znaków!")
	private String text;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp created;
	
	private String receiver; //addresse of the message, if indicated by the @ at the beginning of the message
	
	
	
	
	@ManyToOne
	@JoinColumn
	private User user;
	
	@OneToMany(mappedBy = "tweet", fetch=FetchType.EAGER)
	private List<Comment> comments = new ArrayList<>();
	
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
	

	
	
//	@Override
//	public String toString() {
//		return String.format("Dane tweeta (toString): [id=%s, text=%s, created=%s, receiver=%s]", id, text, created, receiver,
//				user);
//	}
	

	@Override
	public String toString() {
		return String.format("Dane tweeta bez usera (toString): [id=%s, text=%s, created=%s, receiver=%s]", id, text, created, receiver);
	}
	
	
	

}
