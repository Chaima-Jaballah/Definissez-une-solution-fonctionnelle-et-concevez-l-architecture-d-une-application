package com.example.chat.interfaces.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.chat.application.ChatService;
import com.example.chat.domain.dto.ChatMessageDto;

@Controller
public class ChatWebSocketController {

	private final ChatService chatService;

	public ChatWebSocketController(ChatService chatService) {
		this.chatService = chatService;
	}

	@MessageMapping("/sendMessage")
	@SendTo("/topic/messages")
	public ChatMessageDto send(ChatMessageDto incoming, StompHeaderAccessor accessor) {
		return chatService.handleIncoming(incoming.sessionId(), incoming);
	}
}
