package com.heriata.order_service.exceptions;

public class EntityNotSavedException extends RuntimeException {
    public EntityNotSavedException(String message) {
        super(message);
    }
}
