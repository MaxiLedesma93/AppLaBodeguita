package com.ledesmalillo.labodeguitaapp.Modelos;

import java.io.Serializable;

public class Producto implements Serializable {
    private int id;
    private String nombre;
    private Double precio;
    private String foto;
    private Boolean estado;
    private byte[] imagen;

}
