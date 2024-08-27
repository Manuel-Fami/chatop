package com.openclassroom.chatop.services;

import com.openclassroom.chatop.entity.UserEntity;

public interface UserService {
    UserEntity register(UserEntity user);
    // UserEntity login(String email, String password);
    UserEntity getCurrentUser(String email);
    UserEntity getCurrentUserById(Long id);
}
