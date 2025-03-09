package org.example.gitwarmsun.service;

import org.example.gitwarmsun.model.User;

public interface UserService {
    User registerUser(String username, String password, String email);
    User loginUser(String username, String password);
    void updateUser(String email, String password);
}
