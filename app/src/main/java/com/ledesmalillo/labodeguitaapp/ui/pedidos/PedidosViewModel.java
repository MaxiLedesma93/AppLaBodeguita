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
import com.ledesmalillo.labodeguitaapp.Modelos.Estado;
import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pedido>> listaPedidos;
    private MutableLiveData<List<Detalle>> listaDetalle;

    private MutableLiveData<List<Estado>> listaEstados;



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
    public LiveData<List<Estado>> getListaEstados() {
        if (listaEstados == null) {
            listaEstados = new MutableLiveData<>();
            cargarEstados();
        }
        return listaEstados;
    }
    private void cargarEstados() {
        // Aquí puedes hardcodear los estados o traerlos de la API si tienes el endpoint
        SharedPreferences sp = ApiClient.conectar(context);
        String t = sp.getString("token", "vacio");
        List<Estado> estados = new ArrayList<>();
        estados.add(new Estado(-1, "Recibido/En preparacion"));
        Call <List<Estado>> lista = ApiClient.getEndPoints().listarEstados(t);
        lista.enqueue(new Callback<List<Estado>>() {
            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {
                if (response.isSuccessful()) {
                    estados.addAll(response.body());
                    listaEstados.setValue(estados);
                } else {
                    Toast.makeText(context, "No se encontraron Estados(On response)", Toast.LENGTH_LONG).show();
                }
                }
            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Toast.makeText(context, "Se produjo un error(failure)", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void mostrarPedidos(int idEstado){
        SharedPreferences sp = ApiClient.conectar(context);
        String t = sp.getString("token", "vacio");
        String rol = sp.getString("rol", "vacio");
        //llamada a la Api con el rol Cliente.
        if(rol.equals("Cliente")){
            Call<List<Pedido>> lista = ApiClient.getEndPoints().listarPedidosPorUsuario(t);
            lista.enqueue(new Callback<List<Pedido>>(){
                @Override
                public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                    if(response.isSuccessful()){
                        listaPedidos.setValue(response.body());
                        if(response.body().isEmpty()){
                            Toast.makeText(context, "No se encontraron Pedidos", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "No se encontraron Pedidos", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Pedido>> call, Throwable t) {
                    Toast.makeText(context, "Se produjo un error", Toast.LENGTH_LONG).show();
                }
            });
        }
        //llamado a la API con el rol Recepcionista.
        if(rol.equals("Recepcionista")){
            Call<List<Pedido>> lista = ApiClient.getEndPoints().listarPedidosPorEstado(t, idEstado);
            lista.enqueue(new Callback<List<Pedido>>(){
                @Override
                public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                    if(response.isSuccessful()){
                        listaPedidos.setValue(response.body());
                        if(response.body().isEmpty()){
                            Toast.makeText(context, "No se encontraron Pedidos", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "No se encontraron Pedidos", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<List<Pedido>> call, Throwable t) {
                    Toast.makeText(context, "Se produjo un error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}