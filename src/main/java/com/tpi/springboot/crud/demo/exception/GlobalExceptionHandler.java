package com.tpi.springboot.crud.demo.exception;

import com.tpi.springboot.crud.demo.dto.common.ErrorResponseDto; // ErrorResponseDto 임포트
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException; // 유효성 검사 실패 예외 처리용
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException; // 유효성 검사 실패 예외 처리용

@RestControllerAdvice // 모든 @Controller 또는 @RestController 에서 발생하는 예외를 가로챕니다.
public class GlobalExceptionHandler {

    // --- 1. CustomException 처리 ---
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        // ErrorCode 의 메시지를 기본으로 사용하되, CustomException 생성 시 상세 메시지가 있다면 그것을 사용
        String errorMessage = (ex.getMessage() != null && !ex.getMessage().equals(errorCode.getMessage()))
                ? ex.getMessage() : errorCode.getMessage();

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                errorCode.getCode(),     // 정의된 에러 코드
                errorMessage,            // 사용자에게 보여줄 메시지
                ex.getMessage()          // 예외의 상세 메시지 (개발자용, 디버깅 시 유용)
        );
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    // --- 2. @Valid/@Validated 유효성 검사 실패 예외 처리 ---
    // @RequestBody 등에서 발생하는 유효성 검사 실패 (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        // 첫 번째 유효성 검사 오류 메시지를 가져옴 (또는 모든 오류 메시지를 리스트로 반환 가능)
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        String errorCode = "VALIDATION-001"; // 유효성 검사 실패에 대한 공통 에러 코드

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                errorCode,
                errorMessage,
                "입력 값 유효성 검사에 실패했습니다."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    // --- 3. 그 외 모든 예상치 못한 일반 예외 처리 ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        // 예상치 못한 모든 예외는 INTERNAL_SERVER_ERROR 로 처리합니다.
        // 실제 배포 환경에서는 ex.getMessage()를 그대로 노출하지 않고 로깅만 하는 것이 좋습니다.
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                ex.getMessage() // 개발자용 상세 메시지 (운영 환경에서는 로그로만 남기고 노출 X)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}