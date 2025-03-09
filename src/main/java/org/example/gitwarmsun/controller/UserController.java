package org.example.gitwarmsun.controller;

import org.example.gitwarmsun.dto.ResponseDTO;
import org.example.gitwarmsun.model.User;
import org.example.gitwarmsun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseDTO<User> registerUser(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String email) {
        try {
            User user = userService.registerUser(username, password, email);
            return new ResponseDTO<>(200, "User registered successfully", user);
        } catch (RuntimeException e) {
            return new ResponseDTO<>(400, e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public ResponseDTO<User> loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            System.out.println("Login user: " + username + ", " + password);
            User user = userService.loginUser(username, password);
            return new ResponseDTO<>(200, "User logged in successfully", user);
        } catch (RuntimeException e) {
            return new ResponseDTO<>(401, e.getMessage(), null);
        }
    }

    @PutMapping("/update")
    public ResponseDTO<Void> updateUser(@RequestParam(required = false) String email, @RequestParam(required = false) String password) {
        userService.updateUser(email, password);
        return new ResponseDTO<>(200, "User updated successfully", null);
    }
}