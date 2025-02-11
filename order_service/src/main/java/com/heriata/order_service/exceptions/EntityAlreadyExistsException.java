package com.heriata.order_service.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(final String message) {
        super(message);
    }
}
