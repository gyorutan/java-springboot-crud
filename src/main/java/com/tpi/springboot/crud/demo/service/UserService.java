package com.tpi.springboot.crud.demo.service;

import com.tpi.springboot.crud.demo.domain.User;
import com.tpi.springboot.crud.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userMapper.insertUser(user);
    }

    public User loginUser(User user) {
        User existUser = userMapper.selectUserByUsername(user.getUsername());

        if (existUser == null) {
            return null;
        }

        if (passwordEncoder.matches(user.getPassword(), existUser.getPassword())) {
            existUser.setPassword(null);
            return existUser;
        } else {
            return null;
        }
    }

    public User getUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    public List<User> getAllUsers() {
        return  userMapper.selectAllUsers();
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(Long id) {
        userMapper.deleteUser(id);
    }

    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }
}
