package com.openclassroom.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.chatop.entity.Rentals;

@Repository
public interface RentalsRepository extends JpaRepository<Rentals, Long>{
    
}
