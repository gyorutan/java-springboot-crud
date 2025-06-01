package com.tpi.springboot.crud.demo.service;

import com.tpi.springboot.crud.demo.domain.User;
import com.tpi.springboot.crud.demo.exception.CustomException;
import com.tpi.springboot.crud.demo.exception.ErrorCode;
import com.tpi.springboot.crud.demo.mapper.AuthMapper;
import com.tpi.springboot.crud.demo.mapper.UserMapper;
import com.tpi.springboot.crud.demo.provider.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public User getUser(String accessToken) {
        jwtTokenProvider.validateToken(accessToken);

        User user = userMapper.selectUserByUsername(jwtTokenProvider.getUsername(accessToken));

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }
}
