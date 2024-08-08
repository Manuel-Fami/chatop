package com.openclassroom.chatop.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassroom.chatop.entity.Role;
import com.openclassroom.chatop.entity.User;
import com.openclassroom.chatop.models.RoleName;
import com.openclassroom.chatop.repository.RoleRepository;
import com.openclassroom.chatop.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

     @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // public User saveUser(User user) {
    //     return userRepository.save(user);
    // }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Optional<Role> userRoleOpt = roleRepository.findByName(RoleName.ROLE_USER);
        if (userRoleOpt.isPresent()) {
            Role userRole = userRoleOpt.get();
            user.setRoles(new HashSet<>(Arrays.asList(userRole)));
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Role not found");
        }
    }

    public User saveAdmin(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Optional<Role> adminRoleOpt = roleRepository.findByName(RoleName.ROLE_ADMIN);
        if (adminRoleOpt.isPresent()) {
            Role adminRole = adminRoleOpt.get();
            user.setRoles(new HashSet<>(Arrays.asList(adminRole)));
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Role not found");
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
