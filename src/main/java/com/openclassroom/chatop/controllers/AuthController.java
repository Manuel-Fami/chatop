package com.openclassroom.chatop.controllers;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.chatop.configuration.JwtTokenUtil;
import com.openclassroom.chatop.entity.User;
import com.openclassroom.chatop.models.AuthenticationRequest;
import com.openclassroom.chatop.models.AuthenticationResponse;
import com.openclassroom.chatop.models.UserDTO;
import com.openclassroom.chatop.models.UserRegistrationDTO;
import com.openclassroom.chatop.repository.VerificationTokenRepository;
import com.openclassroom.chatop.services.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    VerificationTokenRepository tokenRepository;
    
    @PostMapping("/auth/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());

        // Logging the generated JWT
        logger.info("Generated JWT: " + jwt);
        
        AuthenticationResponse authResponse = new AuthenticationResponse(jwt);

        // Add the token to the response header
        response.addHeader("Authorization", "Bearer " + jwt);
        return authResponse;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse>  registerUser(@RequestBody UserRegistrationDTO registrationDTO, HttpServletResponse response) {
        User user = userService.registerNewUser(registrationDTO);
        final String jwt = jwtTokenUtil.generateToken(user.getEmail());

        // Logging the generated JWT
        logger.info("Generated JWT: " + jwt);
        
        AuthenticationResponse authResponse = new AuthenticationResponse(jwt);

        // Add the token to the response header
        response.addHeader("Authorization", "Bearer " + jwt);
        return  ResponseEntity.ok(authResponse);
    }

    @GetMapping("/auth/me")
    public UserDTO getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findUserByEmail(userDetails.getUsername());
    }
}
