package com.openclassroom.chatop.services;

import java.sql.Date;
// import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroom.chatop.entity.Role;
import com.openclassroom.chatop.entity.User;
import com.openclassroom.chatop.entity.VerificationToken;
import com.openclassroom.chatop.models.RoleName;
import com.openclassroom.chatop.models.UserDTO;
import com.openclassroom.chatop.models.UserRegistrationDTO;
import com.openclassroom.chatop.repository.RoleRepository;
import com.openclassroom.chatop.repository.UserRepository;
import com.openclassroom.chatop.repository.VerificationTokenRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    // @Autowired
    // private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // public User saveUser(User user) {
    //     user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    //     Optional<Role> userRoleOpt = roleRepository.findByName(RoleName.ROLE_USER);
    //     if (userRoleOpt.isPresent()) {
    //         Role userRole = userRoleOpt.get();
    //         user.setRoles(new HashSet<>(Arrays.asList(userRole)));
    //         return userRepository.save(user);
    //     } else {
    //         throw new RuntimeException("Role not found");
    //     }
    // }

    // public User saveAdmin(User user) {
    //     user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    //     Optional<Role> adminRoleOpt = roleRepository.findByName(RoleName.ROLE_ADMIN);
    //     if (adminRoleOpt.isPresent()) {
    //         Role adminRole = adminRoleOpt.get();
    //         user.setRoles(new HashSet<>(Arrays.asList(adminRole)));
    //         return userRepository.save(user);
    //     } else {
    //         throw new RuntimeException("Role not found");
    //     }
    // }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user, new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        tokenRepository.save(verificationToken);
    }

    public User registerNewUser(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEnabled(false);

        Optional<Role> clientRole = roleRepository.findByName(RoleName.ROLE_USER);
        if (clientRole.isPresent()) {
            Collection<Role> roles = new HashSet<>();
            roles.add(clientRole.get());
            user.setRoles(roles);
        } else {
            throw new RuntimeException("Role CLIENT not found");
        }

        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        createVerificationToken(user, token);
        // sendVerificationEmail(user, token);
        return user;
    }

    public UserDTO findUserByEmail(String email) {
        User user = new User();
        user = userRepository.findByEmail(email).get();
        // UserDTO userDTO 
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        
    }
    
}
