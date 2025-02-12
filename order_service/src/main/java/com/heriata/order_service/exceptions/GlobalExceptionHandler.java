package com.heriata.order_service.exceptions;

import com.heriata.order_service.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.heriata.controllers")
public class GlobalExceptionHandler {

    // todo
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return null;
    }

    @ExceptionHandler(value = EntityNotSavedException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotSavedException(EntityNotSavedException e) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }
}
