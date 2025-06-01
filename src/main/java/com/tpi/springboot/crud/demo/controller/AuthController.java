package com.tpi.springboot.crud.demo.controller;

import com.tpi.springboot.crud.demo.dto.auth.*;
import com.tpi.springboot.crud.demo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> createUser(
            @RequestBody UserRegisterRequestDto userRegisterRequestDto
    ) {
        UserRegisterResponseDto responseDto = authService.createUser(userRegisterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> userLogin(
            @RequestBody UserLoginRequestDto userLoginRequestDto
    ) {
        Map<String, Object> loginResult = authService.userLogin(userLoginRequestDto);

        UserLoginResponseDto responseDto = (UserLoginResponseDto) loginResult.get("userLoginResponseDto");
        ResponseCookie refreshTokenCookie = (ResponseCookie) loginResult.get("refreshTokenCookie");

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(responseDto);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<UserLogoutResponseDto> userLogout(
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        if (refreshToken == null) {
            UserLogoutResponseDto userLogoutResponseDto = new UserLogoutResponseDto("이미 로그아웃된 상태입니다.");

            return ResponseEntity
                    .ok()
                    .body(userLogoutResponseDto);
        }

        authService.logout(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        UserLogoutResponseDto userLogoutResponseDto = new UserLogoutResponseDto("로그아웃 되었습니다.");

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(userLogoutResponseDto);
    }

    // accessToken 재발급
    @PostMapping("/reissue-token")
    public ResponseEntity<ReissueAccessTokenResponseDto> reissueAccessToken(
            @CookieValue(name = "refreshToken" , required = false) String refreshToken
    ) {
        ReissueAccessTokenResponseDto reissueAccessTokenResponseDto = new ReissueAccessTokenResponseDto(
                authService.reissueAccessToken(refreshToken)
        );

        return ResponseEntity.ok().body(reissueAccessTokenResponseDto);
    }
}
