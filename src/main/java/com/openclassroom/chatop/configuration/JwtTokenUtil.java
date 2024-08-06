package com.openclassroom.chatop.configuration;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    public String getUsernameFromToken(String jwtToken) {
        throw new UnsupportedOperationException("Unimplemented method 'getUsernameFromToken'");
    }
    // private String secret = "your_secret_key";

    public String generateToken(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'generateToken'");
    }

    public boolean validateToken(String jwtToken, String username) {
        throw new UnsupportedOperationException("Unimplemented method 'validateToken'");
    }
}
