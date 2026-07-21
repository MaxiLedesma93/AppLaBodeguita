package com.ledesmalillo.labodeguitaapp.utils;


public class Constantes {

    // Constructor privado para evitar que alguien instancie esta clase
    private Constantes() {}

    // --- CONFIGURACIÓN DE RED ---

    //ip maxi 192.168.1.35
    //ip lula 192.168.100.9
    public static final String URL_BASE = "http://192.168.1.35:5000/";
    public static final String URL_LOGO = URL_BASE + "uploads/logo_la_bodeguita.jpeg";


    // --- CLAVES PARA BUNDLES / ARGUMENTOS ---
    public static final String KEY_PRODUCTO_EDITAR = "producto_para_editar";
    public static final String KEY_EDITAR_PEDIDO = "editar_pedido";
    public static final String KEY_ID_PEDIDO = "id_pedido";

    // --- VALORES DE NEGOCIO ---
    public static final String URL_MERCADO_PAGO = "https://link.mercadopago.com.ar/labodeguitasl";
    public static final int COSTO_ENVIO = 1500;
    public static final String DIRECCION_LOCAL = "B. Pinar del Norte, mz C, casa 2277";
    public static final String NOMBRE_LOCAL = "La Bodeguita";
    public static final String TELEFONO_LOCAL = "2664-976780";
    public static final String METODO_PAGO_MERCADO_PAGO = "Mercado Pago";
    public static final String METODO_PAGO_EFECTIVO = "Efectivo";
}
