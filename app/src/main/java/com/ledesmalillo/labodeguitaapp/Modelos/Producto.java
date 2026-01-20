package com.ledesmalillo.labodeguitaapp.Modelos;

import java.io.Serializable;

public class Producto implements Serializable {
    private int id;
    private String nombre;
    private Double precio;
    private String foto;
    private Boolean estado;
    private byte[] imagen;
    private String descripcion;
    private int idTipo;

    public Producto() {
    }

    public Producto(int id, String nombre, Double precio, String foto, Boolean estado, byte[] imagen,
                    String descripcion, int idTipo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.foto = foto;
        this.estado = estado;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.idTipo = idTipo;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getIdTipo() {
        return idTipo;
    }

    public void setTipo(int idTipo) {
        this.idTipo = idTipo;
    }
}
