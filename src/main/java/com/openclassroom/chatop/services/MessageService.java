package com.openclassroom.chatop.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroom.chatop.entities.Message;
import com.openclassroom.chatop.repository.MessagesRepository;

@Service
public class MessageService {
    @Autowired
    private MessagesRepository messagesRepository;

    public void createMessage(Message message) {
		messagesRepository.save(message);	
	}
}
