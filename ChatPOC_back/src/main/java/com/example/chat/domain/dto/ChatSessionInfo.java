package com.example.chat.domain.dto;

import lombok.Builder;

@Builder
public record ChatSessionInfo(String sessionId, String starterName, int messageCount) {
}
