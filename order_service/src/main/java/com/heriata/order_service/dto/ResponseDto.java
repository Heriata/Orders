package com.heriata.order_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ResponseDto<T> {
    private int code;

    //todo transient?
    private T content;

    public ResponseDto(int code, T content) {
        this.code = code;
        this.content = content;
    }

    public ResponseDto(T content) {
        this.content = content;
    }
}
