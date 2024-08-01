package com.openclassroom.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.chatop.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
}
