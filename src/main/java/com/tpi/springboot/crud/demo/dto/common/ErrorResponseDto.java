package com.tpi.springboot.crud.demo.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private String code; // 에러 코드 (예: "AUTH-001")
    private String message; // 사용자에게 보여줄 메시지 (프론트엔드에서 번역 키로 사용)
    private String detail; // 개발자용 상세 오류 메시지 (선택 사항)
}