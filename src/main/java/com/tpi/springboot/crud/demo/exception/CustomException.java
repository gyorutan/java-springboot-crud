package com.tpi.springboot.crud.demo.exception;

import lombok.Getter;

@Getter // Lombok 어노테이션으로 getter 자동 생성
public class CustomException extends RuntimeException { // RuntimeException 을 상속하여 Unchecked Exception 으로 만듭니다.

    private final ErrorCode errorCode; // 어떤 종류의 에러인지 나타내는 ErrorCode 객체

    // 1. ErrorCode 만 받는 생성자: ErrorCode 에 정의된 기본 메시지 사용
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // RuntimeException 의 메시지로 ErrorCode 의 기본 메시지를 전달
        this.errorCode = errorCode;
    }

    // 2. ErrorCode 와 추가 메시지를 받는 생성자: 기본 메시지 외에 상세 메시지를 전달하고 싶을 때 사용
    public CustomException(ErrorCode errorCode, String message) {
        super(message); // RuntimeException 의 메시지를 직접 지정
        this.errorCode = errorCode;
    }
}