package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dto.*;
import com.example.entity.User;
import com.example.repository.UserRepository;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String register(RegisterRequest request) {
        if (request.getUsername() == null || request.getEmail() == null || request.getPassword() == null) {
            return "Error: Missing required fields";
        }
        
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return "Error: Email already registered";
        }
        
        User user = new User(request.getUsername(), request.getEmail(), request.getPassword());
        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public String login(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return "Error: Email and password are required";
        }
        
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return "Error: User not found";
        }
        
        if (user.get().getPassword().equals(request.getPassword())) {
            return "Login successful";
        } else {
            return "Error: Invalid password";
        }
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        if (request.getEmail() == null || request.getNewPassword() == null) {
            return "Error: Email and new password are required";
        }
        
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return "Error: User not found";
        }
        
        user.get().setPassword(request.getNewPassword());
        userRepository.save(user.get());
        return "Password reset successfully";
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {
        if (request.getEmail() == null || request.getOldPassword() == null || request.getNewPassword() == null) {
            return "Error: Missing required fields";
        }
        
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return "Error: User not found";
        }
        
        if (!user.get().getPassword().equals(request.getOldPassword())) {
            return "Error: Old password is incorrect";
        }
        
        user.get().setPassword(request.getNewPassword());
        userRepository.save(user.get());
        return "Password changed successfully";
    }

}
