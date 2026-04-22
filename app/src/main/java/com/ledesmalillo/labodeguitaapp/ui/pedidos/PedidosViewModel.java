package com.ledesmalillo.labodeguitaapp.ui.pedidos;

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
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pedido>> listaPedidos;
    private MutableLiveData<List<Detalle>> listaDetalle;

    private Context context;

    public PedidosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

    }
    public LiveData<List<Pedido>> getListaPedidos() {
        if(listaPedidos == null){
            listaPedidos = new MutableLiveData<List<Pedido>>();
        }
        return listaPedidos;
    }
    public LiveData<List<Detalle>> getListaDetalle(){
        if(listaDetalle == null){
            listaDetalle = new MutableLiveData<List<Detalle>>();
        }
        return listaDetalle;
    }

    public void mostrarPedidos(){
        SharedPreferences sp = ApiClient.conectar(context);
        String t = sp.getString("token", "vacio");
        Call<List<Pedido>> lista = ApiClient.getEndPoints().listarPedidosPorUsuario(t);
        lista.enqueue(new Callback<List<Pedido>>(){
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if(response.isSuccessful()){
                    listaPedidos.setValue(response.body());
                }else{
                    Toast.makeText(context, "No se encontraron Productos(On response)", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(context, "Se produjo un error(failure)", Toast.LENGTH_LONG).show();
            }
        });
    }


    /*
    public void calcularTotal(int idPedido) {

        List<Detalle> listaActual = listaDetalle.getValue();
        double sumaTotal = 0;
        SharedPreferences sp = ApiClient.conectar(context);
        String t = sp.getString("token", "vacio");
        Call<List<Detalle>> lista = ApiClient.getEndPoints().obtenerDetallePorPedido(t, idPedido);
        lista.enqueue(new Callback<List<Detalle>>() {
            @Override
            public void onResponse(Call<List<Detalle>> call, Response<List<Detalle>> response) {
                if(response.isSuccessful()){
                    listaDetalle.setValue(response.body());
                }else{
                    Toast.makeText(context, "No se encontraron Productos(On response)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Detalle>> call, Throwable t) {
                Toast.makeText(context, "Se produjo un error(failure)", Toast.LENGTH_LONG).show();
                Log.d("salida", t.getMessage());
            }
        });
        if (listaActual != null) {
            for (Detalle detalle : listaActual) {
                //sumaTotal += pedido.getProducto().getPrecio() * ite.getCantidad();


            }
        }
        total.setValue(sumaTotal);
    } */
}