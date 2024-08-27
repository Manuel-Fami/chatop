package com.openclassroom.chatop.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// import com.openclassroom.chatop.configuration.JwtTokenUtil;
import com.openclassroom.chatop.entity.UserEntity;
import com.openclassroom.chatop.models.UserRequest;
import com.openclassroom.chatop.models.UserResponse;
import com.openclassroom.chatop.repository.VerificationTokenRepository;
import com.openclassroom.chatop.services.JwtService;
import com.openclassroom.chatop.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class AuthController {
    
        @Autowired
        private UserService userService;
        private JwtService jwtService;
        private AuthenticationManager authenticationManager;

        @Autowired
        public AuthController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager, VerificationTokenRepository tokenRepository) {
                this.userService = userService;
                this.jwtService = jwtService;
                this.authenticationManager = authenticationManager;
        }
    
        @Operation(summary = "Login a user")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "User logged in successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(example = "{\"token\": \"jwt\"}"))),
                @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
        })
        @PostMapping("/auth/login")
        public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
                try{
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(email, password)
                        );

                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                        String token = jwtService.generateToken(userDetails);

                        return ResponseEntity.ok(Collections.singletonMap("token", token));
                } catch (Exception e) {
                return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
                }    
        }

        @Operation(summary = "Register a new user")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "User registered successfully",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(example = "{\"token\": \"jwt\"}"))),
                @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
        })
        @PostMapping("/auth/register")
        public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
                if (userRequest.getName() == null || userRequest.getEmail() == null || userRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body("{\"error\": \"Invalid input\"}");
                }

                try{
                        UserEntity newUser = new UserEntity();
                        newUser.setName(userRequest.getName());
                        newUser.setEmail(userRequest.getEmail());
                        newUser.setPassword(userRequest.getPassword());
                        newUser.setCreatedAt(new Date());
                        newUser.setUpdatedAt(new Date());

                        @SuppressWarnings("unused")
                        UserEntity registeredUser = userService.register(newUser);
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(newUser.getEmail(), userRequest.getPassword())
                        );
                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
                        String token = jwtService.generateToken(userDetails);
        
                        return ResponseEntity.status(201).body(Collections.singletonMap("token", token));
                } catch (Exception e) {
                        return ResponseEntity.status(400).body("{\"error\": \"Registration failed\"}");
                }


        }

        @Operation(summary = "Get current authenticated user")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Current user details",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = UserEntity.class))),
                @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
        })
        @GetMapping("/auth/me")
        public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
                if (userDetails == null) {
                        return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
                }
                try{
                        UserEntity userEntity = userService.getCurrentUser(userDetails.getUsername());
                        UserResponse userResponse = convertToDTO(userEntity);
                        return ResponseEntity.ok(userResponse);
                } catch (Exception e) {
                        return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
                }
        }

        private UserResponse convertToDTO(UserEntity userEntity) {
                UserResponse userResponse = new UserResponse();
                userResponse.setId(userEntity.getId());
                userResponse.setName(userEntity.getName());
                userResponse.setEmail(userEntity.getEmail());
                userResponse.setCreatedAt(convertToLocalDate(userEntity.getCreatedAt()));
                userResponse.setUpdatedAt(convertToLocalDate(userEntity.getUpdatedAt()));
                return userResponse;
        }

        private LocalDate convertToLocalDate(Date date) {
                return date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
        }
}
