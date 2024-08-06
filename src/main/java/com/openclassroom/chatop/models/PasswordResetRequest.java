package com.openclassroom.chatop.models;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String password;
}
