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
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.Modelos.RealPathUtil;

import com.ledesmalillo.labodeguitaapp.Modelos.Usuario;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.ui.usuario.UsuarioViewState;


import java.io.File;
import java.util.Locale;

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
    private  MutableLiveData<ProductoViewState> estado = new MutableLiveData<>();
    private  MutableLiveData<ProductoViewState> estadoNuevo = new MutableLiveData<>();
    private Producto productoOriginal; // Para guardar el producto que estamos editando

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

    public MutableLiveData<ProductoViewState> getEstado() {
        if(estado == null){
            estado = new MutableLiveData<>();
        }
        return estado;
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            uri = data.getData();
            uriMutableLiveData.setValue(uri);
        }
    }

    public void iniciar(Bundle arguments) {
        if (arguments != null && arguments.containsKey("producto_para_editar")) {
            // MODO EDITAR
            this.productoOriginal = (Producto) arguments.getSerializable("producto_para_editar");
            // Creamos un ViewState a partir del producto existente
            ProductoViewState estadoDeEdicion = new ProductoViewState(
                    productoOriginal.getNombre(),
                    productoOriginal.getDescripcion(),
                    productoOriginal.getPrecio() != null ? productoOriginal.getPrecio().toString() : "",
                    "http://192.168.1.35:5000/" + productoOriginal.getFoto(),
                    productoOriginal.getIdTipo()
            );
            //Log.d("URL FOTO " , "http://192.168.1.35:5000/" + productoOriginal.getFoto());
            arguments.clear();
            estado.setValue(estadoDeEdicion);
        } else {
            // MODO CREAR
            this.productoOriginal = null;
            // Creamos un ViewState con valores por defecto (vacíos)
            ProductoViewState estadoDeCreacion = new ProductoViewState("", "", "","", 0);
            estado.setValue(estadoDeCreacion);
        }
    }
    public void guardarProducto(String nombreProducto, String descripcionProducto,
                                boolean estadoProducto, Uri uriImagen, Double precioProducto,
                                String tipoProducto) {
        //buscar en la tabla tipo de la bd la descripcion que coincida con el string tipo
        //asignarle el id del registro encontrado al idTipoProducto
        int idTipoProducto = 0;
        if (productoOriginal != null) {
            SharedPreferences sp = ApiClient.conectar(context);
            String token = sp.getString("token", "no token");
            String rutaArchivo = "";
            // para evitar el error de null en uriImagen cuando no cambia la uriImagen,
            // creamos los parametros en null. esto sucedia si no editabamos la imagen.
            // y solo los seteamos si uriImagen != null
            RequestBody imagenBody = null;
            MultipartBody.Part imagenFile = null;
            // para evitar que al precio se le agregue un 0 que es el que esta luego del .
            // reemplazamos el . por la ,
            String precioConPunto = String.valueOf(precioProducto); // Ejemplo: "10.0"
            String precioConComa = precioConPunto.replace('.', ','); // Resultado: "10,0"


            if(uriImagen != null){
                rutaArchivo = RealPathUtil.getRealPath(context, uriImagen);
                File archivo = new File(rutaArchivo);
                imagenBody = RequestBody.create(MediaType.parse("multipart/form-data"), archivo);
                imagenFile = MultipartBody.Part.createFormData("imagen", archivo.getName(), imagenBody);


            }

            RequestBody id = RequestBody.create(MediaType.parse("application/json"), String.valueOf(productoOriginal.getId()));
            RequestBody nombre = RequestBody.create(MediaType.parse("application/json"),nombreProducto);
            RequestBody precio = RequestBody.create(MediaType.parse("application/json"), precioConComa);
            RequestBody descripcion = RequestBody.create(MediaType.parse("application/json"), descripcionProducto);
            RequestBody foto = RequestBody.create(MediaType.parse("application/json"), rutaArchivo);
            RequestBody estado = RequestBody.create(MediaType.parse("application/json"), String.valueOf(estadoProducto));
            RequestBody tipo = RequestBody.create(MediaType.parse("application/json"), String.valueOf(idTipoProducto));




            Call<Producto> productoCall = ApiClient.getEndPoints().editarProducto(token,  imagenFile,
                    nombre, descripcion, precio, foto, estado,tipo, id);
            productoCall.enqueue(new Callback<Producto>() {
                @Override
                public void onResponse(Call<Producto> call, Response<Producto> response) {
                    Toast.makeText(context, "Producto Editado con Exito", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Producto> call, Throwable throwable) {
                    Toast.makeText(context, "Error al editar Producto"+ throwable.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("salida",throwable.getMessage());
                }
            });

            // Log.d("ViewModel", "Actualizando producto: " + productoOriginal.getId());
        } else {
            SharedPreferences sp = ApiClient.conectar(context);
            String token = sp.getString("token", "no token");
            String rutaArchivo = RealPathUtil.getRealPath(context, uriImagen);
            File archivo = new File(rutaArchivo);

            RequestBody nombre = RequestBody.create(MediaType.parse("application/json"),nombreProducto);
            RequestBody descripcion = RequestBody.create(MediaType.parse("application/json"), descripcionProducto);
            RequestBody precio = RequestBody.create(MediaType.parse("application/json"), String.valueOf(precioProducto));
            RequestBody estado = RequestBody.create(MediaType.parse("application/json"), String.valueOf(estadoProducto));
            RequestBody imagenBody = RequestBody.create(MediaType.parse("multipart/form-data"), archivo);
            MultipartBody.Part imagenFile = MultipartBody.Part.createFormData("imagen", archivo.getName(), imagenBody);
            RequestBody tipo = RequestBody.create(MediaType.parse("application/json"), String.valueOf(idTipoProducto));



            Call<Producto> productoCall = ApiClient.getEndPoints().altaProducto(token,  imagenFile,
                    nombre, descripcion, precio, tipo, estado);
            productoCall.enqueue(new Callback<Producto>() {
                @Override
                public void onResponse(Call<Producto> call, Response<Producto> response) {
                    Toast.makeText(context, "Producto Guardado con Exito", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Producto> call, Throwable throwable) {
                    Toast.makeText(context, "Error al guardar Producto"+ throwable.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("salida",throwable.getMessage());
                }
            });
        }
        // Aquí podrías emitir un nuevo estado para indicar "Guardado con éxito" o navegar hacia atrás.
    }

}