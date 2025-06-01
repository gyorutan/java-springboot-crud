package com.tpi.springboot.crud.demo.controller;

import com.tpi.springboot.crud.demo.domain.User;
import com.tpi.springboot.crud.demo.dto.user.UserResponseDto;
import com.tpi.springboot.crud.demo.exception.CustomException;
import com.tpi.springboot.crud.demo.exception.ErrorCode;
import com.tpi.springboot.crud.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/current-user")
    public ResponseEntity<UserResponseDto> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS); // 헤더 형식이 잘못됨
        }

        String accessToken = authorizationHeader.substring(7);

        User user = userService.getUser(accessToken);

        UserResponseDto userResponseDto = new UserResponseDto(
                user.getId(),
                user.getUsername()
        );

        return ResponseEntity.ok().body(userResponseDto);
    }
}
