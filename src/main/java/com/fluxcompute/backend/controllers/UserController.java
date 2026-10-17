package com.fluxcompute.backend.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.fluxcompute.backend.models.User;
import com.fluxcompute.backend.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    public UserController(UserService userService,BCryptPasswordEncoder encoder){
        this.userService = userService;
        this.encoder = encoder;
    }
    @GetMapping("/")
    public String home() {
        return "hello";
    }
    
    @PostMapping("/auth/register")
    public String register(@RequestBody User user) {
        System.out.println("----------"+user.getName());
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);
        return "";  
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody User user) {
        return userService.verify(user);
    }
}
