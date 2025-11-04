package com.ledesmalillo.labodeguitaapp.Modelos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Pedido implements Serializable {
    private int id;
    private int clienteId;
    private String fecha;
    private int estadoId;
    private Boolean pagado;
    private Usuario cliente;
    private Estado estado;
    private List<Detalle> detalles;

    public Pedido(int id, int clienteId, String fecha, int estadoId, Boolean pagado, Usuario cliente, Estado estado, List<Detalle> detalles) {
        this.id = id;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.estadoId = estadoId;
        this.pagado = pagado;
        this.cliente = cliente;
        this.estado = estado;
        this.detalles = detalles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }
}
