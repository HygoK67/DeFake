package com.group6.defakelogibackend.mapper;

import com.group6.defakelogibackend.model.Group;
import com.group6.defakelogibackend.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    // 这些查找函数如果不提供 password_hash 时，那么就不检查密码是否相等
    public User findUserByEmail(String email);

    public List<User> findUserByUsername(String username);

    public User findUserById(long id);

    public User findUserByPhone(String phone);

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUser(long id);

    public void updateUserPassword(User user);

    public List<User> getAllUsers();

}
