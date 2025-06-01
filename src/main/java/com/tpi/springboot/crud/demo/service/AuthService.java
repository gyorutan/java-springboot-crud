package com.tpi.springboot.crud.demo.service;

import com.tpi.springboot.crud.demo.domain.RefreshToken;
import com.tpi.springboot.crud.demo.domain.User;
import com.tpi.springboot.crud.demo.dto.auth.*;
import com.tpi.springboot.crud.demo.exception.CustomException;
import com.tpi.springboot.crud.demo.exception.ErrorCode;
import com.tpi.springboot.crud.demo.mapper.AuthMapper;
import com.tpi.springboot.crud.demo.provider.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    @Autowired
    private final AuthMapper authMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserRegisterResponseDto createUser(UserRegisterRequestDto userRegisterRequestDto) {
        // 사용자 이름 중복 확인
        if (authMapper.selectUserByUsername(userRegisterRequestDto.getUsername()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME, "이미 사용중인 Username 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userRegisterRequestDto.getPassword());

        User createdUser = new User(
                userRegisterRequestDto.getUsername(),
                encodedPassword
        );

        authMapper.insertUser(createdUser);

        return new UserRegisterResponseDto(
                createdUser.getId(),
                createdUser.getUsername(),
                "회원가입이 성공적으로 완료되었습니다."
        );
    }

    @Transactional
    public Map<String, Object> userLogin(UserLoginRequestDto userLoginRequestDto) {
        // 사용자 이름이 있는지 확인
        User user = authMapper.selectUserByUsername(userLoginRequestDto.getUsername());

        // 유저 없음
        if (user == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 불일치
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        // 기존 refreshToken 유무 확인
        RefreshToken existingRefreshToken = authMapper.selectTokenByUserId(user.getId());
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiresAt = issuedAt.plusSeconds(jwtTokenProvider.getRefreshTokenExpirationMilliseconds() / 1000);

        // 기존 refreshToken 있음
        if (existingRefreshToken != null) {
            // refreshToken 업데이트
            existingRefreshToken.setToken(refreshToken);
            existingRefreshToken.setIssuedAt(issuedAt);
            existingRefreshToken.setExpiresAt(expiresAt);
            existingRefreshToken.setRevoked(false);
            authMapper.updateRefreshToken(existingRefreshToken);
        } else {
            // 새로운 RefreshToken 저장
            RefreshToken createdRefreshToken = new RefreshToken(
                    null,
                    user.getId(),
                    refreshToken,
                    issuedAt,
                    expiresAt,
                    false
            );
            authMapper.insertRefreshToken(createdRefreshToken);
        }

        // 쿠키 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken" , refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtTokenProvider.getRefreshTokenExpirationMilliseconds() / 1000)
//                .sameSite("Lax")
                .build();

        // 컨트롤러로 전달할 응답 데이터 구성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken" , accessToken);
        responseData.put("refreshTokenCookie" , refreshTokenCookie);
        responseData.put("userLoginResponseDto" , new UserLoginResponseDto(
                accessToken,
                user.getId(),
                user.getUsername(),
                "로그인에 성공하였습니다."
        ));

        return responseData;
    }

    @Transactional
    public void logout(String refreshToken) {
        // refreshToken 검증
        jwtTokenProvider.validateToken(refreshToken);

        RefreshToken storedRefreshToken = authMapper.selectTokenByToken(refreshToken);

        // RefreshToken 무효화
        authMapper.updateRefreshTokenStatus(storedRefreshToken.getToken(), true);
    }

    @Transactional
    public String reissueAccessToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        jwtTokenProvider.validateToken(refreshToken);

        return jwtTokenProvider
                .createAccessToken(jwtTokenProvider.getUsername(refreshToken));
    }
}
