package com.openclassroom.chatop.services;

// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassroom.chatop.entity.UserEntity;
import com.openclassroom.chatop.repository.UserRepository;

public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity register(UserEntity user) {
        // Chiffrer le mot de passe avant de sauvegarder l'utilisateur
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // @Override
    // public UserEntity login(String email, String password) {
    //     Optional<UserEntity> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
    //     if (userOpt.isPresent()) {
    //         UserEntity user = userOpt.get();
    //         if (passwordEncoder.matches(password, user.getPassword())) {
    //             return user;
    //         }
    //     }
    //     // Gérer l'échec de l'authentification
    //     return null; // ou lancer une exception personnalisée
    // }

    @Override
    public UserEntity getCurrentUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity getCurrentUserById(Long id) { // Implémentation de la nouvelle méthode
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
