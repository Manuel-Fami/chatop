package com.openclassroom.chatop.dto;

import java.util.List;

public class RentalsDTO {
	private List<RentalsDTO> rentals;
	
	public RentalsDTO(List<RentalsDTO> rentals) {
		this.rentals = rentals;
	}
	
	public List<RentalsDTO> getRentals() {
		return this.rentals;
	}
	
	public void setRentals(List<RentalsDTO> rentals) {
		this.rentals = rentals;
	}

}
