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
import androidx.lifecycle.ViewModel;

import com.ledesmalillo.labodeguitaapp.Modelos.Detalle;
import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.Modelos.Pago;
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.utils.Constantes;

import java.text.DateFormat;
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
    private  MutableLiveData<List<ItemCarrito>> listaItems = new MutableLiveData<>();
    private final MutableLiveData<Double> total = new MutableLiveData<>();
    private Context context;
    private int contDetalles = 0;
    private int idPedido;


    public CarritoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        listaItems.setValue(new ArrayList<>());
        total.setValue(0.0);
    }

    public LiveData<List<ItemCarrito>> getListaItems() {

        return listaItems;
    }

    public LiveData<Double> getTotal() {
        return total;
    }

    public void reiniciarMutableCarrito(){
        listaItems.setValue(new ArrayList<>());
        total.setValue(0.0);
    }

    public void agregarAlCarrito(Producto producto, int cantidad) {
        List<ItemCarrito> listaActual = listaItems.getValue();
        if (listaActual == null) return;


        // Revisa si el producto ya está en el carrito
        for (ItemCarrito item : listaActual) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + cantidad); // Si ya está, suma la cantidad
                listaItems.setValue(new ArrayList<>(listaActual)); // Notifica el cambio
                calcularTotal();
                return;
            }
        }
        // Si no está, lo agrega como un nuevo item
        listaActual.add(new ItemCarrito(producto, cantidad));
        listaItems.setValue(new ArrayList<>(listaActual));
        calcularTotal();
    }
    public boolean habilitarBotonRealizarPedido() {
        if(total.getValue() > 0) {
            return true;
        }
        return false;
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
        for (ItemCarrito item : listaActual) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(cantidad); // Si ya está, suma la cantidad
                listaItems.setValue(listaActual); // Notifica el cambio
                calcularTotal();
                return;
            }
        }
    }
    public String asignarDireccion(int idDelivery, int idRetiro, int idRbSeleccionado){
        SharedPreferences sp = ApiClient.conectar(context);
        //si el id Delivery viene en 0, entonces asignamos
        if (idRbSeleccionado == idDelivery) {
            total.setValue(total.getValue() + Constantes.COSTO_ENVIO);
            return sp.getString("direccion", "Sin direccion");
        }
        if(idRbSeleccionado == idRetiro){
            total.setValue(total.getValue() - Constantes.COSTO_ENVIO);
            return Constantes.DIRECCION_LOCAL;
        }
        return "Delivery";
    }
    public void guardarPedido(String direccionPedido, boolean deliveryEstado, boolean pagadoEstado) {
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
       // String idCliente = sp.getString("idCliente", "Sin id");
       // String delEstado = deliveryEstado ? "true" : "false";
        // para evitar que al precio se le agregue un 0 que es el que esta luego del .
        // reemplazamos el . por la ,
       // String importeConPunto = String.valueOf(total.getValue()); // Ejemplo: "10.0"
       // String importeConComa = importeConPunto.replace('.', ','); // Resultado: "10,0"



        Date fechaActual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String fechaFormateada = sdf.format(fechaActual);


        Pedido pedido = new Pedido();
        pedido.setDelivery(deliveryEstado);
        pedido.setDireccionEntrega(direccionPedido);
        pedido.setFecha(fechaFormateada);
        pedido.setEstadoId(1);
        pedido.setImporteTotal(total.getValue());
        //if total.getValue() > 1500 se realiza el alta.
        Call<Pedido> pedidoCall = api.altaPedido(token, pedido );
        pedidoCall.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                Toast.makeText(context, "Pedido creado con Exito", Toast.LENGTH_LONG).show();
                idPedido = response.body().getId();
                enviarDetalles(token, idPedido, api);
                String metDePago = null;
                if(pagadoEstado){
                    metDePago = Constantes.METODO_PAGO_MERCADO_PAGO;
                    registrarPago(idPedido, metDePago, pagadoEstado, total.getValue());
                }else{
                    metDePago = Constantes.METODO_PAGO_EFECTIVO;
                    // para probar, en realidad el pago en Efectivo lo registra la persona que entrega
                    //el pedido.
                    registrarPago(idPedido, metDePago, pagadoEstado, total.getValue());
                }

            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(context, "Falla al crear Pedido", Toast.LENGTH_LONG).show();
            }
        });



    }
    private void enviarDetalles(String token, int pedidoIdValue, ApiClient.MisEndPoints api) {
        List<ItemCarrito> listaActual = listaItems.getValue();
        if (listaActual == null) return;

        contDetalles = 0;
        for (ItemCarrito item : listaActual) {
            RequestBody pedidoId = RequestBody.create(MediaType.parse("application/json"), String.valueOf(pedidoIdValue));
            RequestBody productoId = RequestBody.create(MediaType.parse("application/json"), String.valueOf(item.getProducto().getId()));
            RequestBody cantidad = RequestBody.create(MediaType.parse("application/json"), String.valueOf(item.getCantidad()));

            api.altaDetalle(token, pedidoId, productoId, cantidad).enqueue(new Callback<Detalle>() {
                @Override
                public void onResponse(Call<Detalle> call, Response<Detalle> response) {
                    // contamos para vaciar el carrito
                    contDetalles++;
                    if (contDetalles == listaActual.size()) {
                        listaItems.setValue(new ArrayList<>());
                        total.setValue(0.0);
                    }
                }

                @Override
                public void onFailure(Call<Detalle> call, Throwable t) {
                    Log.d("API_ERROR", "Error en detalle: " + t.getMessage());
                }
            });
        }
    }
    public void editarPedido(int idPedido, String direccionPedido, boolean deliveryEstado, boolean pagadoEstado) {
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        String idCliente = sp.getString("idCliente", "Sin id");
        String delEstado = deliveryEstado ? "true" : "false";
        // para evitar que al precio se le agregue un 0 que es el que esta luego del .
        // reemplazamos el . por la ,
        String importeConPunto = String.valueOf(total.getValue()); // Ejemplo: "10.0"
        String importeConComa = importeConPunto.replace('.', ','); // Resultado: "10,0"
        Date fechaActual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String fechaFormateada = sdf.format(fechaActual);


        Call<List<Detalle>> Detalles = ApiClient.getEndPoints().obtenerDetallePorPedido(token, idPedido);
            Detalles.enqueue(new Callback<List<Detalle>>(){
            @Override
            public void onResponse(Call<List<Detalle>> call, Response<List<Detalle>> response) {
                if(response.isSuccessful()){

                    List<Detalle> listaPedido = response.body();
                    eliminarDetalles(listaPedido, api, token);
                    enviarDetalles(token, idPedido, api);
                }else{
                    Toast.makeText(context, "No se encontraron Productos(On response)", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Detalle>> call, Throwable t) {
                Toast.makeText(context, "Se produjo un error(failure)", Toast.LENGTH_LONG).show();
            }
        });
        RequestBody id = RequestBody.create(MediaType.parse("application/json"), String.valueOf(idPedido));

        RequestBody clienteId = RequestBody.create(MediaType.parse("application/json"),
                idCliente);
        RequestBody fecha = RequestBody.create(MediaType.parse("application/json"),
                fechaFormateada);
        RequestBody estadoId = RequestBody.create(MediaType.parse("application/json"),
                "1");
        RequestBody delivery = RequestBody.create(MediaType.parse("application/json"),
                delEstado);
        RequestBody direccionEntrega = RequestBody.create(MediaType.parse("application/json"),
                direccionPedido);
        RequestBody importeTotal = RequestBody.create(MediaType.parse("application/json"), importeConComa);

        Call<Pedido> pedidoCall = api.editarPedido(token, id, clienteId, fecha,  estadoId,
                delivery, direccionEntrega, importeTotal);
        pedidoCall.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                Toast.makeText(context, "Pedido Editado con Exito", Toast.LENGTH_LONG).show();
                String metDePago = null;
                if(pagadoEstado){
                    metDePago = Constantes.METODO_PAGO_MERCADO_PAGO;
                }else{
                    metDePago = Constantes.METODO_PAGO_EFECTIVO;
                }
                registrarPago(idPedido, metDePago, pagadoEstado, total.getValue());
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(context, "Falla al crear Pedido", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void eliminarDetalles(List<Detalle> listaPedido,
                                  ApiClient.MisEndPoints api, String token){

        for (Detalle detallePedido : listaPedido) {

                //eliminamos los detalles antiguos del pedido
                Call<Detalle> detalleCall = api.borrarDetalle(token, detallePedido.getId());
                detalleCall.enqueue(new Callback<Detalle>() {
                    @Override
                    public void onResponse(Call<Detalle> call, Response<Detalle> response) {

                    }

                    @Override
                    public void onFailure(Call<Detalle> call, Throwable t) {
                        Toast.makeText(context, "Falla al eliminar Detalles", Toast.LENGTH_LONG).show();
                    }
                });
        }
    }

    public void registrarPago(int pedidoId, String metDePago, boolean pagadoEstado, double importePago){
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        String importeConPunto = String.valueOf(importePago); // Ejemplo: "10.0"
        String importeConComa = importeConPunto.replace('.', ','); // Resultado: "10,0"
        RequestBody importe = RequestBody.create(MediaType.parse("application/json"), importeConComa);
        RequestBody idPedido = RequestBody.create(MediaType.parse("application/json"), String.valueOf(pedidoId));
        RequestBody metodoDePago = RequestBody.create(MediaType.parse("application/json"), metDePago);
        Call<Pago> callPago = api.registrarPago(token, idPedido, metodoDePago, importe);
        callPago.enqueue(new Callback<Pago>() {
            @Override
            public void onResponse(Call<Pago> call, Response<Pago> response) {
                Toast.makeText(context, "Pago registrado con Exito", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<Pago> call, Throwable t) {
                Toast.makeText(context, "Falla al registrar Pago", Toast.LENGTH_LONG).show();
            }
        });

    }

}