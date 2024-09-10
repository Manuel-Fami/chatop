package com.openclassroom.chatop.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassroom.chatop.dto.RentalDTO;
import com.openclassroom.chatop.dto.RentalsDTO;
import com.openclassroom.chatop.entities.Rental;
import com.openclassroom.chatop.entities.User;
import com.openclassroom.chatop.mapper.RentalMapper;
import com.openclassroom.chatop.response.MessageResponseHandler;
import com.openclassroom.chatop.services.RentalsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;

@RestController
@Validated
public class RentalsController {
    @Autowired
    private RentalsService rentalsService;
    
    @SuppressWarnings("null")
    @PostMapping(path = "/rentals", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Create a new rental", description = "This operation creates a new rental and returns a message.")
	@ApiResponses(value = {
		     @ApiResponse(responseCode = "200", description = "Rental created", 
		    		 content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = MessageResponseHandler.class))),
		     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
		     @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content), 
		     @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content)
	})
	public ResponseEntity<Object> create ( 
			@RequestPart("name") @NotNull String name,
	        @RequestPart("surface") @NotNull String surface,
	        @RequestPart("price") @NotNull String price,
	        @RequestPart("picture") @NotNull MultipartFile picture,
	        @RequestPart("description") @NotNull String description
	 ) throws Exception {

    if (picture.isEmpty()) {
        return ResponseEntity.badRequest().body("No image provided");
    }

    if (!picture.getContentType().startsWith("image/")) {
        return ResponseEntity.badRequest().body("File is not an image");
    }
     
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();
    
    Rental rental = new Rental();
    rental.setName(name);
    rental.setSurface(new BigDecimal(surface));
    rental.setPrice(new BigDecimal(price));
    rental.setDescription(description);
    rental.setOwner(currentUser);
				
	rentalsService.createRental(rental, picture);
        
	return ResponseEntity.ok().body("Rental created !");
    
	}

    @PutMapping(path = "/rentals/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Update a rental", description = "This operation updates an existing rental and returns a message.")
	@ApiResponses(value = {
		     @ApiResponse(responseCode = "200", description = "Rental updated", 
		    		 content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = MessageResponseHandler.class))),
		     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
		     @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content), 
		     @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content)
	})    
    public ResponseEntity<Object> update( 
        @PathVariable("id") @NotNull Long id,
        @RequestPart("name") @NotNull String name,
        @RequestPart("surface") @NotNull String surface,
        @RequestPart("price") @NotNull String price,
        @RequestPart("description") @NotNull String description ) throws Exception {
		
        Rental existingRental  = rentalsService.getRentalById(id);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
        if (!existingRental.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body("You are not authorized to modify this rental");
        }
        
        existingRental.setName(name);
        existingRental.setSurface(new BigDecimal(surface));
        existingRental.setPrice(new BigDecimal(price));
        existingRental.setDescription(description);
        
        rentalsService.updateRental(existingRental);
		
		return ResponseEntity.ok().body("Rental updated !");
	}

    @GetMapping(path="/rentals")
    @Operation(summary = "Get all rentals", description = "This operation returns all rentals.")
	@ApiResponses(value = {
		     @ApiResponse(responseCode = "200", description = "Rental created", 
		    		 content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = RentalsDTO.class))),
		     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
		     @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content), 
		     @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content)
	})
	public ResponseEntity< List<RentalDTO>> getAll() {
    List<Rental> rentals = rentalsService.getAll();
    
    List<RentalDTO> rentalDTOList = new ArrayList<>(); // Liste pour stocker les DTOs

    // Boucle for pour convertir chaque Rental en RentalDTO
    for (Rental rental : rentals) {
        RentalDTO rentalDTO = RentalMapper.toDTO(rental); // Conversion de l'entité en DTO
        rentalDTOList.add(rentalDTO); // Ajout du DTO à la liste
    }
    
    return ResponseEntity.ok().body(rentalDTOList); // Retourne la réponse avec la liste des DTOs
    }

    @GetMapping(path="/rentals/{id}")
    @Operation(summary = "GET a rental", description = "This operation returns a specific rental.")
	@ApiResponses(value = {
		     @ApiResponse(responseCode = "200", description = "Rental created", 
		    		 content = @Content(mediaType = "application/json",
	                 schema = @Schema(implementation = RentalDTO.class))),
		     @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
		     @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content), 
		     @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content),
		     @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content),
	})
	public ResponseEntity<RentalDTO> get(@PathVariable("id") @NotNull Long id) {

		Rental rental = rentalsService.getRentalById(id);

        RentalDTO rentalDTO = RentalMapper.toDTO(rental);

		return ResponseEntity.ok().body(rentalDTO);
	}

    
}
