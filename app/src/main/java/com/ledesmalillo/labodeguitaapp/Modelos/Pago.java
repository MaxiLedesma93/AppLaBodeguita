package com.ledesmalillo.labodeguitaapp.Modelos;

import java.io.Serializable;

public class Pago implements Serializable {
    private int id;
    private int pedidoId;
    private String metodoDePago;
    private Double importe;

    public Pago() {
    }

    public Pago(int id, String metodoDePago, int pedidoId,  Double importe) {
        this.id = id;
        this.metodoDePago = metodoDePago;
        this.pedidoId = pedidoId;
        this.importe = importe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
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
