package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.model.Users;
import org.example.authservice.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        return userService.register(user);
    }

    @GetMapping("/checkAll")
    public List<Users> checkAllUsers(){
        return userService.checkAllUsers();
    }

    @PostMapping ("/login")
    public String login(@RequestBody Users user){
       return userService.verify(user);
    }
}
