package com.sistemasTarija.dunno.catalogo.domain.exception.options;

public class CategoriaFailedException extends RuntimeException {
    public CategoriaFailedException(String message) {
        super(message);
    }
    public CategoriaFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
