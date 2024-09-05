package com.openclassroom.chatop.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.chatop.dto.MessageDTO;
import com.openclassroom.chatop.entities.Message;
import com.openclassroom.chatop.entities.Rental;
import com.openclassroom.chatop.entities.User;
import com.openclassroom.chatop.response.MessageResponseHandler;
import com.openclassroom.chatop.services.MessageService;
import com.openclassroom.chatop.services.RentalsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
public class MessagesController {

    @Autowired
    private MessageService messagesService;

    @Autowired
    private RentalsService rentalsService;

   @PostMapping(path = "/messages")
   @Operation(summary = "POST a message", description = "This operation creates a message and returns a message.")
	@ApiResponses(value = {
		     @ApiResponse(responseCode = "200", description = "Rental created", 
		    		 content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = MessageResponseHandler.class))),
		     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
		     @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content), 
		     @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content),
		     @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content),
	})
	public ResponseEntity<Object> create(@RequestBody @Valid MessageDTO messageDTO) throws Exception {	
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
			

		Rental rental = rentalsService.getRentalById(messageDTO.getRental_id());
			
		Message message = new Message();
		message.setMessage(messageDTO.getMessage());
		message.setRental(rental);
		message.setUser(currentUser);
		
		messagesService.createMessage(message);
			
		return ResponseEntity.ok().body(new MessageResponseHandler("message sent successfully !")); 	
	}
}
