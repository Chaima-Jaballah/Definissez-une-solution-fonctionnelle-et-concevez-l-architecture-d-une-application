package com.example.chat.domain.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.chat.domain.model.ChatMessage;
import com.example.chat.domain.model.ChatSession;

import lombok.Builder;

@Builder
public record ChatSessionDto(String starterName, List<ChatMessageDto> messages, String sessionId) {
	public static ChatSessionDto fromEntity(ChatSession e) {
		if (e == null)
			return null;
		return ChatSessionDto.builder().sessionId(e.getSessionId()).starterName(e.getStarterName())
				.messages(new ArrayList<>(ChatMessageDto.fromEntities(e.getMessages()))).build();
	}

	public static List<ChatSessionDto> fromEntities(List<ChatSession> entities) {
		if (entities == null)
			return List.of();
		return entities.stream().map(ChatSessionDto::fromEntity).collect(Collectors.toList());
	}

	public ChatSession toEntity() {
		ChatSession entity = new ChatSession(this.sessionId, this.starterName);
		if (this.messages != null && !this.messages.isEmpty()) {
			for (ChatMessageDto mDto : this.messages) {
				if (mDto == null)
					continue;
				ChatMessage m = mDto.toEntity();
				if (m != null) {
					entity.addMessage(m); // setSession + ajout
				}
			}
		}
		return entity;
	}

}
