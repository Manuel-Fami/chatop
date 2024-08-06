package com.openclassroom.chatop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.chatop.entity.Messages;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long>{

    List<Messages> findByUserId(Long userId);

    List<Messages> findByRentalId(Long rentalId);
    
}
