package com.sistemasTarija.dunno.catalogo.domain.exception;

public class VarianteFailedException extends RuntimeException {
    public VarianteFailedException(String message) {
        super(message);
    }
    public VarianteFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
