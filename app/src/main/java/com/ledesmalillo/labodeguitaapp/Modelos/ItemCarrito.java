package com.ledesmalillo.labodeguitaapp.Modelos;

import java.io.Serializable;

// Nuevo archivo en tu paquete Modelos
public class ItemCarrito implements Serializable {
    private Producto producto;
    private int cantidad;

    public ItemCarrito() {
    }

    public ItemCarrito(Producto producto,int cantidad) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}

