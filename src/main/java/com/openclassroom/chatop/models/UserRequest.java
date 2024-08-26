package com.openclassroom.chatop.models;

import java.util.Date;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Date createdAt;
    private Date updatedAt;
}
