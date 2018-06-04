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
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp created;
	
	@NotNull
	@Column(nullable = false)
	private boolean viewed;
	
	@NotBlank
	@Type(type="text")
	@Column(nullable = false)
	@Size(min = 2, max = 30, message="Tytuł wiadomości nie może mieć mniej niż 2 i więcej niż 30 znaków!")
	private String topic;
	
	@NotBlank
	@Type(type="text")
	@Column(nullable = false)
	@Size(min = 2, max = 2048, message="Treść wiadomości nie może mieć mniej niż 2 i więcej niż 2048 znaków!")
	private String text;
	
	@ManyToOne
	@JoinColumn
	private User sender;
	
	@ManyToOne
	@JoinColumn
	private User receiver;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	@Override
	public String toString() {
		return String.format("Message(toString) [id=%s, created=%s, viewed=%s, topic=%s, text=%s, sender=%s, receiver=%s]", id,
				created, viewed, topic, text, sender, receiver);
	}
	
	
	

}
