package com.ledesmalillo.labodeguitaapp.ui.productos;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.Modelos.RealPathUtil;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearProductoViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> uriMutableLiveData;
    private Uri uri;
    private MutableLiveData<Bitmap> mFoto;

    private Context context;
    public CrearProductoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public MutableLiveData<Uri> getUriMutableLiveData() {
        if (uriMutableLiveData == null) {
            uriMutableLiveData = new MutableLiveData<>();
        }
        return uriMutableLiveData;
    }
    public MutableLiveData<Bitmap> getmFoto() {
        if (mFoto == null) {
            mFoto = new MutableLiveData<>();
        }
        return mFoto;
    }
    public void guardarProducto(String nombreProducto, String precioProducto,
                                String descripcionProducto, boolean estadoProducto, Uri uriImagen){
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        String rutaArchivo = RealPathUtil.getRealPath(context, uriImagen);
        File archivo = new File(rutaArchivo);

        RequestBody nombre = RequestBody.create(MediaType.parse("application/json"),nombreProducto);
        RequestBody descripcion = RequestBody.create(MediaType.parse("application/json"), precioProducto);
        RequestBody precio = RequestBody.create(MediaType.parse("application/json"), descripcionProducto);
        RequestBody estado = RequestBody.create(MediaType.parse("application/json"), String.valueOf(estadoProducto));
        RequestBody imagenBody = RequestBody.create(MediaType.parse("multipart/form-data"), archivo);
        MultipartBody.Part imagenFile = MultipartBody.Part.createFormData("imagen", archivo.getName(), imagenBody);



        Call<Producto> productoCall = ApiClient.getEndPoints().altaProducto(token,  imagenFile,
                nombre, descripcion, precio, estado);
        productoCall.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                Toast.makeText(context, "Producto creado con Extio", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable throwable) {
                Toast.makeText(context, "Error al guardar Producto"+ throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("salida",throwable.getMessage());
            }
        });

    }
    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            uri = data.getData();
            uriMutableLiveData.setValue(uri);
        }
    }

}