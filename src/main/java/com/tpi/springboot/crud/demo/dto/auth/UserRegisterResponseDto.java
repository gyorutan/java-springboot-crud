package com.tpi.springboot.crud.demo.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponseDto {
    private Long userId;        // 등록된 사용자의 고유 ID (식별자)
    private String username;    // 등록된 사용자의 사용자 이름
    private String message;     // 성공 메시지 (예: "회원가입이 성공적으로 완료되었습니다.")
}
