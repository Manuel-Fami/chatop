package com.openclassroom.chatop.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    public VerificationToken(String token, User user,  Date expiryDate ) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    
} 