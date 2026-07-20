package com.example.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

}
package com.example.dto;

public class RegisterRequest {

    private String username;
    private String email;
    private String password;

    //Getters Setters
}
package com.example.dto;

public class LoginRequest {

    private String email;
    private String password;

    //Getters Setters
}
package com.example.dto;

public class ForgotPasswordRequest {

    private String email;
    private String newPassword;

    //Getters Setters
}
package com.example.dto;

public class ChangePasswordRequest {

    private String email;
    private String oldPassword;
    private String newPassword;

    //Getters Setters
}
package com.example.service;

import com.example.dto.*;

public interface AuthService {

    String register(RegisterRequest request);

    String login(LoginRequest request);

    String forgotPassword(ForgotPasswordRequest request);

    String changePassword(ChangePasswordRequest request);

}
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
    AuthService service;

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
    AuthService service;

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
package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    // Getters and Setters
}
