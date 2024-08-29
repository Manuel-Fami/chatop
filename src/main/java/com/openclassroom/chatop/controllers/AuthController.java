package com.openclassroom.chatop.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.chatop.configuration.JwtTokenUtil;
import com.openclassroom.chatop.entity.Rentals;
import com.openclassroom.chatop.entity.User;
import com.openclassroom.chatop.entity.VerificationToken;
import com.openclassroom.chatop.models.AuthenticationRequest;
import com.openclassroom.chatop.models.AuthenticationResponse;
import com.openclassroom.chatop.models.UserDTO;
import com.openclassroom.chatop.models.UserRegistrationDTO;
import com.openclassroom.chatop.repository.UserRepository;
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
    private UserRepository userRepository;

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

    // @Transactional
    // @GetMapping("/auth/verify")
    // public String verifyAccount(@RequestParam("token") String token) {
    //     VerificationToken verificationToken = tokenRepository.findByToken(token);

    //     if (verificationToken == null) {
    //         return "Invalid token";
    //     }

    //     User user = verificationToken.getUser();
    //     Date expiryDate = verificationToken.getExpiryDate();
    //     if (expiryDate == null) {
    //         return "Token expiry date not set";
    //     }

    //     Calendar cal = Calendar.getInstance();
    //     if ((expiryDate.getTime() - cal.getTime().getTime()) <= 0) {
    //         return "Token expired";
    //     }

    //     user.setEnabled(true);
    //     userRepository.save(user);

    //     // Delete the token using its ID
    //     tokenRepository.deleteById(verificationToken.getId());

    //     // Check if the token is successfully deleted
    //     boolean isDeleted = !tokenRepository.existsById(verificationToken.getId());
    //     if (isDeleted) {
    //         return "Account verified and token deleted successfully";
    //     } else {
    //         return "Account verified but token deletion failed";
    //     }
    // }

    @GetMapping("/auth/me")
    public UserDTO getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findUserByEmail(userDetails.getUsername());
    }
}
