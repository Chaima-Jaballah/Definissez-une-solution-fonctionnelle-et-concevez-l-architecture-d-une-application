package com.example.chat.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.chat.domain.dto.ChatMessageDto;
import com.example.chat.domain.dto.ChatSessionDto;
import com.example.chat.domain.dto.ChatSessionInfo;
import com.example.chat.domain.model.ChatSession;
import com.example.chat.infrastructure.persistance.ChatSessionRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatService {
	private final ChatSessionRepository sessionRepo;
	private final SimpMessagingTemplate messagingTemplate;

	public ChatService(ChatSessionRepository sessionRepo, SimpMessagingTemplate messagingTemplate) {
		this.sessionRepo = sessionRepo;
		this.messagingTemplate = messagingTemplate;
	}

	public ChatSessionDto findById(String sessionId) {
		ChatSession foundSession = this.sessionRepo.findById(sessionId)
				.orElseThrow(() -> new RuntimeException("Session not found !"));
		return ChatSessionDto.fromEntity(foundSession);
	}

	@Transactional
	public ChatMessageDto handleIncoming(String sessionId, ChatMessageDto message) {
		boolean isNewSession = false;
		ChatSessionDto session = ChatSessionDto.fromEntity(this.sessionRepo.findById(sessionId).orElse(null));
		if (session == null) {
			session = ChatSessionDto.builder().sessionId(sessionId).messages(new ArrayList<>())
					.starterName(message.from()).build();
			isNewSession = true;
		}

		session.messages().add(message);
		sessionRepo.save(session.toEntity());

		if (isNewSession) {
			ChatSessionInfo info = new ChatSessionInfo(sessionId, message.from(), 1);
			messagingTemplate.convertAndSend("/topic/sessions", info);
		} else {
			ChatSessionInfo info = new ChatSessionInfo(session.sessionId(), session.starterName(),
					session.messages().size());
			messagingTemplate.convertAndSend("/topic/sessions", info);
		}

		return message;
	}

	public List<ChatSessionInfo> findAll() {
		return sessionRepo.findAll().stream().sorted(Comparator.comparing(ChatSession::getCreatedAt).reversed())
				.map(session -> new ChatSessionInfo(session.getSessionId(), session.getStarterName(),
						session.getMessages().size()))
				.collect(Collectors.toList());
	}
}