package com.sistemasTarija.dunno.venta.domain.exception;

public class InventarioFailedExeption extends RuntimeException {

    public InventarioFailedExeption(String message) {
        super(message);
    }


    public InventarioFailedExeption(String message, Throwable cause) {
        super(message, cause);
    }
}