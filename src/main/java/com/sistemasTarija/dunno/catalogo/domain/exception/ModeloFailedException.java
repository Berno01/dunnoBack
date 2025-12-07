package com.sistemasTarija.dunno.catalogo.domain.exception;

public class ModeloFailedException extends RuntimeException {
    public ModeloFailedException(String message) {
        super(message);
    }
    public ModeloFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
