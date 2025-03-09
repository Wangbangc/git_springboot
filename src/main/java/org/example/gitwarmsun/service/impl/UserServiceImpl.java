package org.example.gitwarmsun.service.impl;

import org.example.gitwarmsun.model.User;
import org.example.gitwarmsun.service.UserService;
import org.example.gitwarmsun.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User registerUser(String username, String password, String email) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectUserByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }


        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password);

        // 保存用户到数据库
        userMapper.insertUser(user);
        return user;
    }

    @Override
    public User loginUser(String username, String password) {
        // 查找用户
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("没有这个用户名");
        }




        return user;
    }


    @Override
    public void updateUser(String email, String password) {
        // TODO: Implement user update logic
    }
}