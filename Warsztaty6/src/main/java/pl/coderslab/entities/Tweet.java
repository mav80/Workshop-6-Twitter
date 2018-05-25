package pl.coderslab.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;


@Entity
public class Tweet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank
	@NotNull
	@Type(type="text")
	@Column(nullable = false)
	private String text;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp created;
	
	private String receiver; //addresse of the message, if indicated by the @ at the beginning of the message
	
	
	
	
	@ManyToOne
	@JoinColumn
	private User user;
	
	
	

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
