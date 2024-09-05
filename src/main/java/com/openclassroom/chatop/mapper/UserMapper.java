package com.openclassroom.chatop.mapper;

import com.openclassroom.chatop.dto.LoginDTO;
import com.openclassroom.chatop.dto.UserDTO;
import com.openclassroom.chatop.dto.UserInfoDTO;
import com.openclassroom.chatop.entities.User;

public class UserMapper {

    public static User registerToUserEntity(UserDTO userdto) {

        User user = new User();
        user.setName(userdto.getName());
        user.setEmail(userdto.getEmail());
        user.setPassword(userdto.getPassword());

        return user;
    }

    public static User loginToUserEntity(LoginDTO loginDTO) {

        User user = new User();
        user.setEmail(loginDTO.getEmail());
        user.setPassword(loginDTO.getPassword());

        return user;
    }

    public static UserInfoDTO userEntityToUserInfoDto(User user) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(user.getId());
        userInfoDTO.setName(user.getName());
        userInfoDTO.setEmail(user.getEmail());
        userInfoDTO.setCreatedAt(user.getCreatedAt());
        userInfoDTO.setUpdatedAt(user.getUpdatedAt());

        return userInfoDTO;
    }


    

}
