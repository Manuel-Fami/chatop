package com.openclassroom.chatop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginDTO {

    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;

    @NotNull(message = "email cannot be NULL")
    public String getEmail() {
        return this.email;
    }

    @NotNull(message = "password cannot be NULL")
    public String getPassword() {
        return this.password;
    }
}
