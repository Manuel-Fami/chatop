package com.openclassroom.chatop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {
	
    @NotBlank(message = "name is required")
	private String name;
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
	
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
		this.name = name;
	}

    @NotNull(message = "email cannot be NULL")
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
	    this.email = email;
	}

    @NotNull(message = "password cannot be NULL")
    public String getPassword() {
        return this.password;
    }
    
}