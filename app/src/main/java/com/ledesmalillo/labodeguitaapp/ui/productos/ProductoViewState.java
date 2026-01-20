package com.ledesmalillo.labodeguitaapp.ui.productos;

import com.ledesmalillo.labodeguitaapp.Modelos.Producto;

import java.io.Serializable;

public class ProductoViewState implements Serializable {
        private final String nombre;
        private final String descripcion;
        private final String precio;
        private final String fotoUrl;
        private final int idTipo;

        // Constructor
        public ProductoViewState(String nombre, String descripcion, String precio, String fotoUrl,
                                 int idTipo) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
            this.fotoUrl = fotoUrl;
            this.idTipo = idTipo;
        }

        // Getters para todos los campos
        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getPrecio() {
            return precio;
        }

        public String getFotoUrl() {
            return fotoUrl;
        }

        public int getTipo() {
            return idTipo;
        }
}
