package com.example.service;

import com.example.dto.*;

public interface AuthService {

    String register(RegisterRequest request);

    String login(LoginRequest request);

    String forgotPassword(ForgotPasswordRequest request);

    String changePassword(ChangePasswordRequest request);

}
