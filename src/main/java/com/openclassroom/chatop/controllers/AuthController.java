package com.openclassroom.chatop.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.chatop.dto.JwtResponseDTO;
import com.openclassroom.chatop.dto.LoginDTO;
import com.openclassroom.chatop.dto.UserDTO;
import com.openclassroom.chatop.dto.UserInfoDTO;
import com.openclassroom.chatop.entities.User;
import com.openclassroom.chatop.mapper.UserMapper;
import com.openclassroom.chatop.services.AuthService;
import com.openclassroom.chatop.services.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
	private final AuthService authService;
	
	public AuthController(JwtService jwtService, AuthService authService) {
		this.jwtService = jwtService;
		this.authService = authService;
	}
   
    @PostMapping("/register")
	@Operation(summary = "Register a new user", description = "This operation registers a new user and returns a jwt token.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User successfully registered", 
				content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = JwtResponseDTO.class))),
		@ApiResponse(responseCode = "409", description = "Email already exists", content = @Content),
		@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
	})
    public ResponseEntity<JwtResponseDTO> register(@Valid @RequestBody UserDTO userDTO) {

		// Tansformation de l'objet UserDTO en entity User
	    User entity = UserMapper.registerToUserEntity(userDTO); 

		// Enregistrement du User en bdd
        User user = authService.register(entity);	  

		// Création d'un token associé
	    String jwtToken = jwtService.generateToken(user); 

	    return ResponseEntity.ok(new JwtResponseDTO(jwtToken));
	}

	@PostMapping("/login")
	@Operation(summary = "Log in a user", description = "This operation logs in a user and returns a jwt token.")
	@ApiResponses(value = { 
		@ApiResponse(responseCode = "200", description = "User logged in successfully", 
				content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = JwtResponseDTO.class))),
		@ApiResponse(responseCode = "400", description = "Invalid login credentials", content = @Content), 
		@ApiResponse(responseCode = "404", description = "User not found", content = @Content), 
 	})
	public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

		// Tansformation de l'objet UserDTO en entity User
	    User entity = UserMapper.loginToUserEntity(loginDTO); 

		// Vérification du User
		User user = authService.login(entity);

		// Création d'un token associé
		String jwtToken = jwtService.generateToken(user); 
		
		return ResponseEntity.ok(new JwtResponseDTO(jwtToken));
	}

	@GetMapping("/me")
	@Operation(summary = "Get current user", description = "This operation returns the details of the currently authenticated user.")
	    @ApiResponses(value = { 
	        @ApiResponse(responseCode = "200", description = "User details retrieved successfully", 
	        		content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = UserInfoDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
	        @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content), 
	        @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content), 
	 })
	public ResponseEntity<UserInfoDTO> getUser() {

		// Récupération du User avec Spring Security
		User user = authService.getUser();

		// Transformation de l'entity en objet DTO
		UserInfoDTO userInfo = UserMapper.userEntityToUserInfoDto(user); 
			 
		return ResponseEntity.ok(userInfo);
	} 
}