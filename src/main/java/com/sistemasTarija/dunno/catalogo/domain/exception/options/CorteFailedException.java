package com.sistemasTarija.dunno.catalogo.domain.exception.options;

public class CorteFailedException extends RuntimeException {
    public CorteFailedException(String message) {
        super(message);
    }
    public CorteFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
