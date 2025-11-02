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
}
