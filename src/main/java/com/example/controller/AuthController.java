package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.dto.*;
import com.example.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        return service.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        return service.login(request);
    }

    @PutMapping("/forgot")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request){
        return service.forgotPassword(request);
    }

    @PutMapping("/change")
    public String changePassword(@RequestBody ChangePasswordRequest request){
        return service.changePassword(request);
    }

}
