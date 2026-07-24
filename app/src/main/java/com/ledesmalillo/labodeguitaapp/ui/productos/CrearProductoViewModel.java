package com.ledesmalillo.labodeguitaapp.ui.productos;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.utils.Constantes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearProductoViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ProductoViewState> estado = new MutableLiveData<>();
    private MutableLiveData<Boolean> productoGuardadoFlag = new MutableLiveData<>();
    private Producto productoOriginal;
    private Context context;

    public CrearProductoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Uri> getUriMutableLiveData() { return uriMutableLiveData; }
    public LiveData<ProductoViewState> getEstado() { return estado; }
    public LiveData<Boolean> getProductoGuardadoFlag() { return productoGuardadoFlag; }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            uriMutableLiveData.setValue(result.getData().getData());
        }
    }

    public void iniciar(Bundle arguments) {
        if (arguments != null && arguments.containsKey(Constantes.KEY_PRODUCTO_EDITAR)) {
            this.productoOriginal = (Producto) arguments.getSerializable(Constantes.KEY_PRODUCTO_EDITAR);
            String descTipo = (productoOriginal.getIdTipo() == 1) ? "Comida" : "Bebida";
            estado.setValue(new ProductoViewState(
                    productoOriginal.getNombre(),
                    productoOriginal.getDescripcion(),
                    productoOriginal.getPrecio() != null ? productoOriginal.getPrecio().toString() : "",
                    Constantes.URL_BASE + productoOriginal.getFoto(),
                    descTipo
            ));
        } else {
            this.productoOriginal = null;
            estado.setValue(new ProductoViewState("", "", "", "", "Comida"));
        }
    }

    public void guardarProducto(String nombreProducto, String descripcionProducto,
                                boolean estadoProducto, Uri uriImagen, Double precioProducto,
                                String tipoProductoDesc) {
        
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        
        // Convertimos el precio para el backend
        String precioStr = String.valueOf(precioProducto).replace('.', ',');

        // Preparamos los RequestBody comunes
        RequestBody nombre = RequestBody.create(MediaType.parse("text/plain"), nombreProducto);
        RequestBody descripcion = RequestBody.create(MediaType.parse("text/plain"), descripcionProducto);
        RequestBody precio = RequestBody.create(MediaType.parse("text/plain"), precioStr);
        RequestBody estadoReq = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(estadoProducto));
        RequestBody tipoproducto = RequestBody.create(MediaType.parse("text/plain"), tipoProductoDesc);

        // Procesamos la imagen de forma SEGURA para evitar EACCES
        MultipartBody.Part imagenFile = null;
        if (uriImagen != null) {
            File file = uriToTempFile(uriImagen);
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                imagenFile = MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);
            }
        }

        if (productoOriginal != null) {
            // MODO EDICION
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(productoOriginal.getId()));
            RequestBody fotoOriginal = RequestBody.create(MediaType.parse("text/plain"), productoOriginal.getFoto());

            ApiClient.getEndPoints().editarProducto(token, imagenFile, nombre, descripcion, precio, fotoOriginal, estadoReq, tipoproducto, id)
                    .enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Producto Editado", Toast.LENGTH_SHORT).show();
                                productoGuardadoFlag.setValue(true);
                            }
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Log.e("API_ERROR", t.getMessage());
                        }
                    });
        } else {
            // MODO ALTA
            if (imagenFile == null) {
                Toast.makeText(context, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
                return;
            }
            ApiClient.getEndPoints().altaProducto(token, imagenFile, nombre, descripcion, precio, tipoproducto, estadoReq)
                    .enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Producto Guardado", Toast.LENGTH_SHORT).show();
                                productoGuardadoFlag.setValue(true);
                            }
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Log.e("API_ERROR", t.getMessage());
                        }
                    });
        }
    }

    // --- EL FIX CLAVE PARA EL ERROR EACCES ---
    private File uriToTempFile(Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File tempFile = new File(context.getCacheDir(), "IMG_" + timeStamp + ".jpg");
            OutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            Log.e("FILE_ERROR", "Error al crear archivo temporal: " + e.getMessage());
            return null;
        }
    }
}