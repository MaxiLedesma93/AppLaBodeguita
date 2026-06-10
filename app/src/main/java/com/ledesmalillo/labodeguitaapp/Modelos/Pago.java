package com.ledesmalillo.labodeguitaapp.Modelos;

import java.io.Serializable;

public class Pago implements Serializable {
    private int id;
    private int idPedido;
    private String metodoDePago;
    private Double importe;

    public Pago() {
    }

    public Pago(int id, String metodoDePago, int idPedido,  Double importe) {
        this.id = id;
        this.metodoDePago = metodoDePago;
        this.idPedido = idPedido;
        this.importe = importe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(String metodoDePago) {
        this.metodoDePago = metodoDePago;
    }


    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }
}
