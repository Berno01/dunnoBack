package com.sistemasTarija.dunno.venta.domain.exception;

public class UserFailedException extends RuntimeException {
    public UserFailedException(String message) {
        super(message);
    }
    public UserFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
