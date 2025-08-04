package com.example.chat.interfaces.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.application.ChatService;
import com.example.chat.domain.dto.ChatMessageDto;
import com.example.chat.domain.dto.ChatSessionDto;

@RequestMapping("/api/chat")
@RestController
@CrossOrigin("*")
public class ChatHistoryController {
	private final ChatService chatService;

	public ChatHistoryController(ChatService chatService) {
		this.chatService = chatService;
	}

	@GetMapping("/history/{sessionId}")
	public List<ChatMessageDto> getHistory(@PathVariable String sessionId) {
		ChatSessionDto session = chatService.findById(sessionId);
		return session != null ? session.messages() : Collections.emptyList();
	}
}