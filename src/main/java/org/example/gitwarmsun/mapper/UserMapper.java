package org.example.gitwarmsun.mapper;

import org.example.gitwarmsun.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(User user);
    User selectUserByUsername(String username);
}