package com.sistemasTarija.dunno.venta.domain.model;

import com.sistemasTarija.dunno.venta.domain.exception.InventarioFailedExeption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Inventario {
    private Integer idInventario;
    private Integer idVariante;
    private Integer stock;
    private Integer idSucursal;
    private Boolean estado;

    public Inventario(Integer idInventario, Integer idVariante, Integer stock, Integer idSucursal) {
        this.idInventario = idInventario;
        this.idVariante = idVariante;
        this.stock = stock;
        this.idSucursal = idSucursal;
        this.estado = true;
    }

    public void decreaseStock(Integer cantidad){
        if (this.stock < cantidad){
            throw new InventarioFailedExeption("Stock insuficiente para el repuesto con ID: "+this.idInventario+". Solicitando: "+cantidad+", Disponible: "+this.stock);
        } this.stock -= cantidad;
    }
    public void increaseStock(Integer cantidad){
        if (cantidad < 0) throw new InventarioFailedExeption("No puedes devolver cantidad negativa");
        this.stock += cantidad;
    }

}
