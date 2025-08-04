package com.example.chat.domain.model;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "chat_message")
@Data
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "session_id")
	private ChatSession session;

	@Column(name = "sender", nullable = false)
	private String from;
	@Column(name = "role", nullable = false)
	private String role; // agent | client
	@Column(name = "text", nullable = false, columnDefinition = "TEXT")
	private String text;

	@Column(name = "created_at", updatable = false)
	@CreationTimestamp
	private java.sql.Timestamp createdAt;

	protected ChatMessage() {
	}

	public ChatMessage(String from, String role, String text) {
		this.from = from;
		this.role = role;
		this.text = text;
	}

	public void setSession(ChatSession s) {
		this.session = s;
	}

}