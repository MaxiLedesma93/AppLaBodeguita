package com.ledesmalillo.labodeguitaapp.ui.productos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;

import java.io.Closeable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoViewModel extends ViewModel {
    private MutableLiveData<List<Producto>> productos;
    private Context context;

    public ProductoViewModel(@NonNull Application application) {
        super((Closeable) application);
        context = application.getApplicationContext();

    }
    public LiveData<List<Producto>> getProductos() {
        if (productos == null) {
            productos = new MutableLiveData<>();
        }
        return productos;

    }

    public void mostrarProductos() {

        SharedPreferences sp = ApiClient.conectar(context);
        String t = sp.getString("token", "vacio");
        Call<List<Producto>> lista = ApiClient.getEndPoints().listaProductos(t);
        lista.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if(response.isSuccessful()){
                    productos.postValue(response.body());
                }else{
                    Toast.makeText(context, "No se encontraron Inmuebles(On response)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(context, "Se produjo un error(failure)", Toast.LENGTH_LONG).show();
                Log.d("salida", t.getMessage());
            }
        });
    }
}