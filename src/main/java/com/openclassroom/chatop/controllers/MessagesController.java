package com.openclassroom.chatop.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.chatop.models.Messages;
import com.openclassroom.chatop.services.MessagesService;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {
    @Autowired
    private MessagesService messagesService;

    // Endpoint pour obtenir tous les messages
    @GetMapping
    public ResponseEntity<List<Messages>> getAllMessages() {
        List<Messages> messages = messagesService.findAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Endpoint pour obtenir un message par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Messages> getMessageById(@PathVariable Long id) {
        Optional<Messages> message = messagesService.findMessageById(id);
        return message.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint pour créer un nouveau message
    @PostMapping
    public ResponseEntity<Messages> createMessage(@RequestBody Messages message) {
        Messages savedMessage = messagesService.saveMessage(message);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    // Endpoint pour mettre à jour un message existant
    @PutMapping("/{id}")
    public ResponseEntity<Messages> updateMessage(@PathVariable Long id, @RequestBody Messages message) {
        if (!messagesService.findMessageById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        message.setId(id);
        Messages updatedMessage = messagesService.saveMessage(message);
        return ResponseEntity.ok(updatedMessage);
    }

    // Endpoint pour supprimer un message par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        if (!messagesService.findMessageById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        messagesService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint pour obtenir les messages par rentalId
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<List<Messages>> getMessagesByRentalId(@PathVariable Long rentalId) {
        List<Messages> messages = messagesService.findMessagesByRentalId(rentalId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Endpoint pour obtenir les messages par userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Messages>> getMessagesByUserId(@PathVariable Long userId) {
        List<Messages> messages = messagesService.findMessagesByUserId(userId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
