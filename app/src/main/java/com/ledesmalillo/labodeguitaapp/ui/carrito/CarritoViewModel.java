package com.ledesmalillo.labodeguitaapp.ui.carrito;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ledesmalillo.labodeguitaapp.Modelos.Detalle;
import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoViewModel extends AndroidViewModel {
    private MutableLiveData<List<ItemCarrito>> listaItems = new MutableLiveData<>();
    private final MutableLiveData<Double> total = new MutableLiveData<>();
    private Context context;
    private int contDetalles = 0;
    
    // PERSISTENCIA DE EDICIÓN
    private boolean esEdicion = false;
    private int idPedidoAEditar = 0;

    public CarritoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        listaItems.setValue(new ArrayList<>());
        total.setValue(0.0);
    }

    public void setEstadoEdicion(boolean editar, int idPedido) {
        this.esEdicion = editar;
        this.idPedidoAEditar = idPedido;
    }

    public boolean isEsEdicion() {
        return esEdicion;
    }

    public int getIdPedidoAEditar() {
        return idPedidoAEditar;
    }

    public LiveData<List<ItemCarrito>> getListaItems() {
        return listaItems;
    }

    public LiveData<Double> getTotal() {
        return total;
    }

    public void reiniciarMutableCarrito() {
        listaItems.setValue(new ArrayList<>());
        total.setValue(0.0);
        esEdicion = false;
        idPedidoAEditar = 0;
    }

    public void agregarAlCarrito(Producto producto, int cantidad) {
        List<ItemCarrito> listaActual = listaItems.getValue();
        if (listaActual == null) return;

        for (ItemCarrito item : listaActual) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + cantidad);
                listaItems.setValue(new ArrayList<>(listaActual));
                calcularTotal();
                return;
            }
        }
        listaActual.add(new ItemCarrito(producto, cantidad));
        listaItems.setValue(new ArrayList<>(listaActual));
        calcularTotal();
    }

    public boolean habilitarBotonRealizarPedido() {
        return total.getValue() != null && total.getValue() > 0;
    }

    public void calcularTotal() {
        List<ItemCarrito> listaActual = listaItems.getValue();
        double sumaTotal = 0;
        if (listaActual != null) {
            for (ItemCarrito item : listaActual) {
                sumaTotal += item.getProducto().getPrecio() * item.getCantidad();
            }
        }
        total.setValue(sumaTotal);
    }

    public void cambiarCantidadCarrito(Producto producto, int cantidad) {
        List<ItemCarrito> listaActual = listaItems.getValue();
        if (listaActual == null) return;
        for (ItemCarrito item : listaActual) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(cantidad);
                listaItems.setValue(listaActual);
                calcularTotal();
                return;
            }
        }
    }

    public String asignarDireccion(int idDelivery, int idRetiro, int idRbSeleccionado) {
        SharedPreferences sp = ApiClient.conectar(context);
        if (idRbSeleccionado == idDelivery) {
            return sp.getString("direccion", "Sin direccion");
        }
        if (idRbSeleccionado == idRetiro) {
            return Constantes.DIRECCION_LOCAL;
        }
        return "Local";
    }

    public void guardarPedido(String direccionPedido, boolean deliveryEstado, boolean pagadoEstado) {
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();

        String fechaFormateada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(new Date());

        Pedido pedido = new Pedido();
        pedido.setDelivery(deliveryEstado);
        pedido.setDireccionEntrega(direccionPedido);
        pedido.setFecha(fechaFormateada);
        pedido.setImporteTotal(total.getValue());

        api.altaPedido(token, pedido).enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Pedido creado con Éxito", Toast.LENGTH_LONG).show();
                    int idNuevo = response.body().getId();
                    enviarDetalles(token, idNuevo, api);
                    registrarPago(idNuevo, pagadoEstado ? Constantes.METODO_PAGO_MERCADO_PAGO : Constantes.METODO_PAGO_EFECTIVO, total.getValue());
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(context, "Error al crear Pedido", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editarPedido(int idPedido, String direccionPedido, boolean deliveryEstado, boolean pagadoEstado) {
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();

        String fechaFormateada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(new Date());

        // 1. Obtener y eliminar detalles viejos, luego subir nuevos
        api.obtenerDetallePorPedido(token, idPedido).enqueue(new Callback<List<Detalle>>() {
            @Override
            public void onResponse(Call<List<Detalle>> call, Response<List<Detalle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eliminarDetalles(response.body(), api, token);
                    enviarDetalles(token, idPedido, api);
                }
            }
            @Override
            public void onFailure(Call<List<Detalle>> call, Throwable t) {}
        });

        // 2. Actualizar cabecera del pedido
        Pedido pedido = new Pedido();
        pedido.setId(idPedido);
        pedido.setDelivery(deliveryEstado);
        pedido.setDireccionEntrega(direccionPedido);
        pedido.setFecha(fechaFormateada);
        pedido.setImporteTotal(total.getValue());

        api.editarPedido(token, pedido).enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                Toast.makeText(context, "Pedido Editado con Éxito", Toast.LENGTH_LONG).show();
                registrarPago(idPedido, pagadoEstado ? Constantes.METODO_PAGO_MERCADO_PAGO : Constantes.METODO_PAGO_EFECTIVO, total.getValue());
            }
            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {}
        });
    }

    private void enviarDetalles(String token, int pedidoIdValue, ApiClient.MisEndPoints api) {
        List<ItemCarrito> listaActual = listaItems.getValue();
        if (listaActual == null) return;
        contDetalles = 0;
        for (ItemCarrito item : listaActual) {
            Detalle detalle = new Detalle();
            detalle.setPedidoId(pedidoIdValue);
            detalle.setProductoId(item.getProducto().getId());
            detalle.setCantidad(item.getCantidad());
            api.altaDetalle(token, detalle).enqueue(new Callback<Detalle>() {
                @Override
                public void onResponse(Call<Detalle> call, Response<Detalle> response) {
                    contDetalles++;
                    if (contDetalles == listaActual.size()) reiniciarMutableCarrito();
                }
                @Override
                public void onFailure(Call<Detalle> call, Throwable t) {}
            });
        }
    }

    public void eliminarDetalles(List<Detalle> listaPedido, ApiClient.MisEndPoints api, String token) {
        for (Detalle d : listaPedido) {
            api.borrarDetalle(token, d.getId()).enqueue(new Callback<Detalle>() {
                @Override
                public void onResponse(Call<Detalle> call, Response<Detalle> response) {}
                @Override
                public void onFailure(Call<Detalle> call, Throwable t) {}
            });
        }
    }

    public void registrarPago(int pedidoId, String metDePago, double importePago) {
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        ApiClient.getEndPoints().registrarPago(token, 
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(pedidoId)),
                RequestBody.create(MediaType.parse("text/plain"), metDePago),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(importePago))
        ).enqueue(new Callback<com.ledesmalillo.labodeguitaapp.Modelos.Pago>() {
            @Override
            public void onResponse(Call<com.ledesmalillo.labodeguitaapp.Modelos.Pago> call, Response<com.ledesmalillo.labodeguitaapp.Modelos.Pago> response) {}
            @Override
            public void onFailure(Call<com.ledesmalillo.labodeguitaapp.Modelos.Pago> call, Throwable t) {}
        });
    }
}