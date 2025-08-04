package com.example.chat.domain.repository;

import java.util.List;

import com.example.chat.domain.dto.ChatSessionDto;

public interface ChatRepository {
	ChatSessionDto findById(String sessionId);

	List<ChatSessionDto> findAll();

	void save(ChatSessionDto session);
}