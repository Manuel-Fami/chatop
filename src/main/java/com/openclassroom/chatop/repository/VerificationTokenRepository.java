package com.openclassroom.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.openclassroom.chatop.entity.VerificationToken;

import jakarta.transaction.Transactional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationToken vt WHERE vt.id = :id")
    void deleteById(@Param("id") Long id);
}
