package com.openclassroom.chatop.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.openclassroom.chatop.entity.Role;
import com.openclassroom.chatop.models.RoleName;
import com.openclassroom.chatop.repository.RoleRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataInitialize {
    @Autowired
    private RoleRepository roleRepository;

  @PostConstruct
    public void init() {
        List<RoleName> roleNames = Arrays.asList(RoleName.ROLE_ADMIN, RoleName.ROLE_USER);
        for (RoleName roleName : roleNames) {
            if (!roleRepository.findByName(roleName).isPresent()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
}
