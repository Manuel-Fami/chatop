package com.openclassroom.chatop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassroom.chatop.entity.Rentals;
import com.openclassroom.chatop.services.RentalsService;

@RestController
@RequestMapping("/api/rentals")
public class RentalsController {
       @Autowired
    private RentalsService rentalsService;

    @GetMapping
    public List<Rentals> getAllRentals() {
        return rentalsService.findAllRentals();
    }

    @PostMapping
    public Rentals createRental(@RequestBody Rentals rental) {
        return rentalsService.saveRental(rental);
    }
}
