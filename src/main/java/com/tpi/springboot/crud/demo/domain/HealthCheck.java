package com.tpi.springboot.crud.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HealthCheck {
    private String message;

    public HealthCheck(String message) {
        this.message = message;
    }
}
