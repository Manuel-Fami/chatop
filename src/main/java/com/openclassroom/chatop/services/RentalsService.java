package com.openclassroom.chatop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassroom.chatop.entity.Rentals;
import com.openclassroom.chatop.repository.RentalsRepository;

@Service
public class RentalsService {
       @Autowired
       private RentalsRepository rentalsRepository;

       public List<Rentals> findAllRentals() {
           return rentalsRepository.findAll();
       }
   
       public Rentals saveRental(Rentals rental) {
           return rentalsRepository.save(rental);
       }
}
