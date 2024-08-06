package com.openclassroom.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassroom.chatop.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
