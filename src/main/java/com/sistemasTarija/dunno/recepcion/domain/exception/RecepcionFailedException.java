package com.sistemasTarija.dunno.recepcion.domain.exception;

public class RecepcionFailedException extends RuntimeException {
    public RecepcionFailedException(String message) {
        super(message);
    }
    public RecepcionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
