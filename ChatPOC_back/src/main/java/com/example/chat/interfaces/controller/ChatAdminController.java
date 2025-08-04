package com.example.chat.interfaces.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.application.ChatService;
import com.example.chat.domain.dto.ChatSessionInfo;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatAdminController {

	private final ChatService chatService;

	public ChatAdminController(ChatService chatService) {
		this.chatService = chatService;
	}

	@GetMapping("/sessions")
	public List<ChatSessionInfo> getSessions() {
		return chatService.findAll();
	}

}
