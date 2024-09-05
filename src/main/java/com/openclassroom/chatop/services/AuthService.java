package com.openclassroom.chatop.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.openclassroom.chatop.entities.User;
import com.openclassroom.chatop.repository.UserRepository;

@Service
public class AuthService {
    //"final" car c'est constante
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //Constructeur (meme nom de la classe)
    public AuthService(
		        UserRepository userRepository,
                PasswordEncoder passwordEncoder,
                AuthenticationManager authenticationManager
		    ) 
            {
		        this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.authenticationManager = authenticationManager;

	 		}

    // Transactional permet d'annuler l'insertion en bdd en cas d'erreur après la méthode d'insertion
    @Transactional
    public User register(User user) {

        // Vérification si email existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");        
        }

        // Encode mot de passe
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));      
        
        // Enregistre user bdd
        userRepository.save(newUser);

        // Authentification Spring Security
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		
        // Retourner les informations de l utilisateur que l'on vent d'ajouter
        return userRepository.findByEmail(user.getEmail())
					.orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User login (User user) {

        // Authentification Spring Security
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        return userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User getUser() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        return currentUser;
    }
}
