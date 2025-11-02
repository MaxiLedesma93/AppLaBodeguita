package com.ledesmalillo.labodeguitaapp.Modelos;

import java.io.Serializable;

public class Detalle implements Serializable {
    private int id;
    private int pedidoId;
    private int productoId;
    private int cantidad;
    private Producto producto;
}
