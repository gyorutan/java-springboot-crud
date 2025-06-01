package com.tpi.springboot.crud.demo.mapper;

import com.tpi.springboot.crud.demo.domain.RefreshToken;
import com.tpi.springboot.crud.demo.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {
    // 유저 회원가입
    void insertUser(User user);

    // 유저이름으로 유저를 가져오기
    User selectUserByUsername(String username);

    // 새로운 refreshToken 생성
    void insertRefreshToken(RefreshToken refreshToken);

    // userId 로 refreshToken 가져오기
    RefreshToken selectTokenByUserId(Long userId);

    // Token 으로 refreshToken 가져오기
    RefreshToken selectTokenByToken(String token);

    // refreshToken 업데이트
    void updateRefreshToken(RefreshToken refreshToken);

    // refreshToken 상태를 업데이트
    void updateRefreshTokenStatus(@Param("token") String token, @Param("revoked") boolean revoked);

    // token 으로 refreshToken 삭제
    void deleteRefreshTokenByToken(String token);

    // userId 로 모든 refreshToken 삭제
    void deleteAllRefreshTokenByUserId(Long userId);
}
