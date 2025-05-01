package com.suri.suri_ai_back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suri.suri_ai_back.models.Chat;
import com.suri.suri_ai_back.repositories.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public Chat createChat(Chat chat) {
        
        return chatRepository.save(chat);
    }
}
