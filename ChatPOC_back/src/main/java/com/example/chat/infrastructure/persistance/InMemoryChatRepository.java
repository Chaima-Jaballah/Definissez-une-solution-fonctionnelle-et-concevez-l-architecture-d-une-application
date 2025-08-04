package com.example.chat.infrastructure.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.example.chat.domain.dto.ChatSessionDto;
import com.example.chat.domain.repository.ChatRepository;

@Repository
public class InMemoryChatRepository implements ChatRepository {
	private final Map<String, ChatSessionDto> sessions = new ConcurrentHashMap<>();

	public ChatSessionDto findById(String sessionId) {
		return sessions.get(sessionId);
	}

	public void save(ChatSessionDto session) {
		sessions.put(session.sessionId(), session);
	}

	@Override
	public List<ChatSessionDto> findAll() {
		return new ArrayList<>(sessions.values());
	}

}