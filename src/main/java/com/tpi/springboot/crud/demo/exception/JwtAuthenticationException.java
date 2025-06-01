package com.tpi.springboot.crud.demo.exception;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
