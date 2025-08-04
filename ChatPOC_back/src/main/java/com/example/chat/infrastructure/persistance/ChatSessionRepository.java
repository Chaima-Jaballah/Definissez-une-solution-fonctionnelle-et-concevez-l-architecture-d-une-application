package com.example.chat.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chat.domain.model.ChatSession;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {
}