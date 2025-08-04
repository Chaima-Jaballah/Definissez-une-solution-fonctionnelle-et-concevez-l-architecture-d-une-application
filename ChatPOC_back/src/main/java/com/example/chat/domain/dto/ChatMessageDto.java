package com.example.chat.domain.dto;

import java.time.Instant;
import java.util.List;

import com.example.chat.domain.model.ChatMessage;

import lombok.Builder;

@Builder
public record ChatMessageDto(Long id, String from, String role, String text, String sessionId, Instant createdAt) {
	public static ChatMessageDto fromEntity(ChatMessage e) {
		if (e == null)
			return null;
		Instant created = null;
		if (e.getCreatedAt() != null) {
			try {
				created = e.getCreatedAt().toInstant();
			} catch (Exception ignore) {
			}
		}
		return ChatMessageDto.builder().id(e.getId()).from(e.getFrom()).role(e.getRole()).text(e.getText())
				.createdAt(created).build();
	}

	public static List<ChatMessageDto> fromEntities(List<ChatMessage> entities) {
		if (entities == null)
			return List.of();
		return entities.stream().map(ChatMessageDto::fromEntity).toList();
	}

	public ChatMessage toEntity() {
		var e = new ChatMessage(this.from, this.role, this.text);
		return e;
	}
}