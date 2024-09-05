package com.openclassroom.chatop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageDTO {
    @NotBlank(message = "message is required")
	private String message;
    @NotNull(message = "Rental ID is required")
	private Long rental_id;
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Long getRental_id() {
		return this.rental_id;
	}
	
	public void setRentalId(Long rentalId) {
		this.rental_id = rentalId;
	}
	
}
