package com.openclassroom.chatop.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.openclassroom.chatop.entities.Rental;
import com.openclassroom.chatop.repository.RentalsRepository;

@Service
public class RentalsService {
    @Autowired
    private RentalsRepository rentalsRepository;
    @Autowired
    private MediaService mediaService;
    
    public void createRental(Rental rental, MultipartFile picture) throws IOException {
		
        String pictureUrl = mediaService.storePicture(picture);
        rental.setPicture(pictureUrl);
        rentalsRepository.save(rental);
    }

    public Rental getRentalById(Long id) {

       return rentalsRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
    }

    public void updateRental(Rental rental) {
		rentalsRepository.save(rental);
	}

  public List<Rental> getAll() {

		List<Rental> rentals = rentalsRepository.findAll();

		return rentals;
	}
}
