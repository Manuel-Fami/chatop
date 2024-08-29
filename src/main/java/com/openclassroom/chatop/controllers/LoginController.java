package com.openclassroom.chatop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/")
    public String getGitHub() {
        return "Welcom, Github user";
    }
}
