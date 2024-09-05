package com.openclassroom.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.chatop.entities.Message;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long>{
    
}
