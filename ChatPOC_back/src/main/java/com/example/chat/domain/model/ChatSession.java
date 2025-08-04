package com.example.chat.domain.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Entity
@Table(name = "chat_session")
@Data
@Getter
public class ChatSession {

	@Id
	@Column(name = "session_id", length = 100)
	private String sessionId;

	@Column(name = "starter_name", nullable = false)
	private String starterName;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Timestamp createdAt;

	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ChatMessage> messages = new ArrayList<>();

	public ChatSession() {
	}

	public ChatSession(String sessionId, String starterName) {
		this.sessionId = sessionId;
		this.starterName = starterName;
	}

	public ChatSession(String sessionId, String starterName, ChatMessage msg) {
		this.sessionId = sessionId;
		this.starterName = starterName;
		this.messages.add(msg);
	}

	public void addMessage(ChatMessage msg) {
		msg.setSession(this);
		messages.add(msg);
	}
}
