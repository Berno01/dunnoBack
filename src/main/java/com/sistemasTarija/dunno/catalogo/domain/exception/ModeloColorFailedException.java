package com.sistemasTarija.dunno.catalogo.domain.exception;

public class ModeloColorFailedException extends RuntimeException {
    public ModeloColorFailedException(String message) {
        super(message);
    }
    public ModeloColorFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
