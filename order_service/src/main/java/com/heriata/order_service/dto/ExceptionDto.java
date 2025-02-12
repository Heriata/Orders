package com.heriata.order_service.dto;

import lombok.Getter;

@Getter
public class ExceptionDto {
    private final int code;
    private final String message;
    public ExceptionDto(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
