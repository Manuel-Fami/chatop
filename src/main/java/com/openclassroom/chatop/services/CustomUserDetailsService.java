package com.openclassroom.chatop.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassroom.chatop.entity.UserEntity;
import com.openclassroom.chatop.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    //Méthode qui charge les détails de l'utilisateur en fonction de l'email.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return User.withUsername(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(new ArrayList<>()) // Ajoutez des autorités si nécessaire
                .build();
    }
}
