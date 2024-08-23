package com.openclassroom.chatop.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.chatop.entity.Rentals;
import com.openclassroom.chatop.services.RentalsService;

@RestController
@RequestMapping("/rentals")
public class RentalsController {
       @Autowired
    private RentalsService rentalsService;

    @GetMapping
    public List<Rentals> getAllRentals() {
        return rentalsService.findAllRentals();
    }

    @PostMapping
    public ResponseEntity<Rentals> createRental(@RequestBody Rentals rental) {
        Rentals createdRental = rentalsService.saveRental(rental);
        return ResponseEntity.ok(createdRental);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Rentals>> getRentalById(@PathVariable Long id) {
        Optional<Rentals> rental = rentalsService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rentals> updateRental(@PathVariable Long id, @RequestBody Rentals rental) throws Exception {
        Rentals updatedRental = rentalsService.updateUser(id, rental);
        return ResponseEntity.ok(updatedRental);
    }
}
