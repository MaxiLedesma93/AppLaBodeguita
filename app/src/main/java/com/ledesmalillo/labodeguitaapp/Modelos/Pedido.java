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

    private String direccionEntrega;
    private Boolean delivery;
    private Double importeTotal;
    private String metodoDePago;

    public Pedido() {
    }

    public Pedido(int id, int clienteId, String fecha, int estadoId,
                  Boolean pagado, Usuario cliente, Estado estado,
                  List<Detalle> detalles, String direccionEntrega,
                  Boolean delivery, Double importeTotal, String metodoDePago) {
        this.id = id;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.estadoId = estadoId;
        this.pagado = pagado;
        this.cliente = cliente;
        this.estado = estado;
        this.detalles = detalles;
        this.direccionEntrega = direccionEntrega;
        this.delivery = delivery;
        this.importeTotal = importeTotal;
        this.metodoDePago = metodoDePago;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }
    public boolean getDelivery() {
        return delivery;
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

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(String metodoDePago) {
        this.metodoDePago = metodoDePago;
    }
}

