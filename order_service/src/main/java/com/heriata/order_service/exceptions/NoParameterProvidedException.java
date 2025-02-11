package com.heriata.order_service.exceptions;

public class NoParameterProvidedException extends RuntimeException {

    public NoParameterProvidedException(final String message) {
        super(message);
    }
}
