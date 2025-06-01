package com.tpi.springboot.crud.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 Bad Request 관련 에러 코드
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "AUTH-001", "이미 존재하는 사용자 이름입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "AUTH-002", "유효하지 않은 이메일 형식입니다."),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "AUTH-003", "비밀번호는 최소 8자 이상이어야 합니다."),

    // 401 Unauthorized 관련 에러 코드 (인증)
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "AUTH-004", "인증에 실패했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-005", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-006", "토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-007", "리프레시 토큰을 찾을 수 없습니다."),

    // 404 Not Found 관련 에러 코드
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "사용자를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-001", "요청하신 리소스를 찾을 수 없습니다."),

    // 500 Internal Server Error 관련 에러 코드
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-002", "서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code; // 고유한 에러 코드 (프론트엔드에서 사용할 키)
    private final String message; // 기본 에러 메시지 (개발/디버깅용 또는 Fallback)
}