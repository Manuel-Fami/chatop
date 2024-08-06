package com.openclassroom.chatop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroom.chatop.entity.Messages;
import com.openclassroom.chatop.repository.MessagesRepository;

@Service
public class MessagesService {
    @Autowired
    private MessagesRepository messagesRepository;

    // Méthode pour obtenir tous les messages
    public List<Messages> findAllMessages() {
        return messagesRepository.findAll();
    }

    // Méthode pour obtenir un message par son ID
    public Optional<Messages> findMessageById(Long id) {
        return messagesRepository.findById(id);
    }

    // Méthode pour sauvegarder ou mettre à jour un message
    public Messages saveMessage(Messages message) {
        return messagesRepository.save(message);
    }

    // Méthode pour supprimer un message par son ID
    public void deleteMessage(Long id) {
        messagesRepository.deleteById(id);
    }

    // Méthode pour trouver les messages par rentalId
    public List<Messages> findMessagesByRentalId(Long rentalId) {
        return messagesRepository.findByRentalId(rentalId);
    }

    // Méthode pour trouver les messages par userId
    public List<Messages> findMessagesByUserId(Long userId) {
        return messagesRepository.findByUserId(userId);
    }
}
