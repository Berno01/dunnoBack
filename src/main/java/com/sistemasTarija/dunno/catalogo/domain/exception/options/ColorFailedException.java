package com.sistemasTarija.dunno.catalogo.domain.exception.options;

public class ColorFailedException extends RuntimeException {
    public ColorFailedException(String message) {
        super(message);
    }
    public ColorFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
