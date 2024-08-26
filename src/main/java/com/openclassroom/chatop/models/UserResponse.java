package com.openclassroom.chatop.models;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDate  createdAt;
    private LocalDate  updatedAt;
}
