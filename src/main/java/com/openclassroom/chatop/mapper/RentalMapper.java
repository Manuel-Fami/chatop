package com.openclassroom.chatop.mapper;

import com.openclassroom.chatop.dto.RentalDTO;
import com.openclassroom.chatop.entities.Rental;

public class RentalMapper {

    public static RentalDTO toDTO(Rental rental) {
        
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(rental.getId());
        rentalDTO.setName(rental.getName());
        rentalDTO.setSurface(rental.getSurface());
        rentalDTO.setPrice(rental.getPrice());
        rentalDTO.setPicture(rental.getPicture());
        rentalDTO.setDescription(rental.getDescription());
        rentalDTO.setOwner_id(rental.getOwner().getId());
        rentalDTO.setCreated_at(rental.getCreatedAt());
        rentalDTO.setUpdated_at(rental.getUpdatedAt());

        return rentalDTO;
    }
}
