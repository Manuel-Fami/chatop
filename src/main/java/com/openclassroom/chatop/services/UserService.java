package com.openclassroom.chatop.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroom.chatop.entity.Role;
import com.openclassroom.chatop.entity.User;
import com.openclassroom.chatop.models.RoleName;
import com.openclassroom.chatop.models.UserDTO;
import com.openclassroom.chatop.models.UserRegistrationDTO;
import com.openclassroom.chatop.repository.RoleRepository;
import com.openclassroom.chatop.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User registerNewUser(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEnabled(true);

        Optional<Role> clientRole = roleRepository.findByName(RoleName.ROLE_USER);
        if (clientRole.isPresent()) {
            Collection<Role> roles = new HashSet<>();
            roles.add(clientRole.get());
            user.setRoles(roles);
        } else {
            throw new RuntimeException("Role CLIENT not found");
        }

        userRepository.save(user);        
        return user;
    }

    public UserDTO findUserByEmail(String email) {
        User user = new User();
        user = userRepository.findByEmail(email).get();
        // UserDTO userDTO 
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        
    }
    
}
