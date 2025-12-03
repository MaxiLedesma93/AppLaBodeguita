package com.ledesmalillo.labodeguitaapp.ui.usuario;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ledesmalillo.labodeguitaapp.Modelos.Usuario;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioViewModel extends AndroidViewModel {
    private Context context;

    private  MutableLiveData<UsuarioViewState> estado = new MutableLiveData<>();
    private  MutableLiveData<UsuarioViewState> estadoNuevo = new MutableLiveData<>();
    private Usuario usuarioOriginal; // Para guardar el usuario que estamos editando
    private MutableLiveData<Usuario> usuario;
    private MutableLiveData<Integer> guardar;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<Usuario> getUsuario() {
        if (usuario == null) {
            usuario = new MutableLiveData<>();
        }
        return usuario;
    }
    public MutableLiveData<UsuarioViewState> getEstado() {
        if(estado == null){
            estado = new MutableLiveData<>();
        }
        //estado.setValue(new UsuarioViewState)
        return estado;
    }
    public MutableLiveData<UsuarioViewState> getEstadoNuevo() {
        if(estadoNuevo == null){
            estadoNuevo = new MutableLiveData<>();
        }
        //estado.setValue(new UsuarioViewState)
        return estadoNuevo;
    }



    //usuario Logueado
    //Modo Edicion
    public void cargarUsuarioExistente() {
        SharedPreferences sp = ApiClient.conectar(context);
        String t = sp.getString("token", "vacio");
        Call<Usuario> prop = ApiClient.getEndPoints().get(t);
        prop.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuarioOriginal=response.body();
                    estado.setValue(new UsuarioViewState(usuarioOriginal, false, View.VISIBLE, View.GONE));

                    //getEditable().setValue(false);
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // Creamos el estado de "vista" y lo cargamos con el usuario, deshabilitamos los campos
        // y ocultamos el boton de editar.
        /*UsuarioViewState estadoDeVista = new UsuarioViewState(
                usuarioOriginal,
                false, // Campos deshabilitados por defecto
                View.VISIBLE, // Botón Editar visible
                View.GONE     // Botón Guardar oculto
        );
        estado.setValue(estadoDeVista); */
    }
    // MODO CREACIÓN
    public void prepararNuevoUsuario() {
        usuarioOriginal = null; // No hay usuario original

        Usuario usuarioVacio = new Usuario(); // Un objeto usuario vacío
        //primer entero es la visibilidad del boton Editar(oculto) el segundo es el boton Guardar(visible)
        /*UsuarioViewState estadoDeVista = new UsuarioViewState(
                usuarioOriginal,
                true, // Campos habilitados para crear
                View.GONE,
                View.VISIBLE
        ); */
        //Log.d("UsuarioViewModel", "Cargo estado de vista: " + estadoDeVista.isCamposHabilitados()+ "");
        estadoNuevo.setValue( new UsuarioViewState(
                usuarioVacio,
                true, // Campos habilitados para crear
                View.GONE,
                View.VISIBLE
        ));
    }
    public void habilitarEdicion() {
        estado.setValue(new UsuarioViewState(usuarioOriginal, true, View.GONE, View.VISIBLE));
    }

    //metodo para guardar cambios
    public void guardarCambios(Usuario usuarioActualizado) {
        if (usuarioOriginal != null) {
            // Modo EDICIÓN
            editarDatos(usuarioActualizado);
            estado.setValue(new UsuarioViewState(usuarioActualizado, false, View.VISIBLE, View.GONE));
        } else {
            // Modo CREACIÓN
            crearUsuario(usuarioActualizado.getNombre(), usuarioActualizado.getApellido(),
                    usuarioActualizado.getEmail(), usuarioActualizado.getDireccion(),
                    usuarioActualizado.getTelefono(), usuarioActualizado.getClave(),
                    usuarioActualizado.getRol());
        }
    }

    public void editarDatos(Usuario u) {
        SharedPreferences sp = ApiClient.conectar(context);
        String t = sp.getString("token", "vacio");
        Call<Usuario> usu = ApiClient.getEndPoints().editarUsuario(t, u);
        usu.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (response.isSuccessful()) {
                    usuarioOriginal = response.body();
                    Toast.makeText(context, "Se editaron los datos con éxito", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(context, "Error al editar " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void crearUsuario(String nombreUs, String apellidoUs, String emailUs,String direccionUs,
                             String telefonoUs, String claveUs, String rolUs ){
        SharedPreferences sp = ApiClient.conectar(context);
        String t = sp.getString("token", "vacio");
        //ver como eliminar el token de sharedPreferences cuando cerramos la aplicacion o nos deslogueamos.
        RequestBody nombre = RequestBody.create(MediaType.parse("application/json"),nombreUs);
        RequestBody apellido = RequestBody.create(MediaType.parse("application/json"),apellidoUs);
        RequestBody email = RequestBody.create(MediaType.parse("application/json"),emailUs);
        RequestBody direccion = RequestBody.create(MediaType.parse("application/json"),direccionUs);
        RequestBody telefono = RequestBody.create(MediaType.parse("application/json"),telefonoUs);
        RequestBody clave = RequestBody.create(MediaType.parse("application/json"),claveUs);
        //validar si el token pertenece a un recepcionista o cliente para dar de alta recepcionista o cliente.
        RequestBody rol = RequestBody.create(MediaType.parse("application/json"), "Cliente");
        RequestBody estado = RequestBody.create(MediaType.parse("application/json"),"true");

        Call<Usuario> usuarioCall = ApiClient.getEndPoints().registrarUsuario(t, nombre, apellido, email,
                direccion, telefono, clave, rol, estado);
        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuarioOriginal = response.body();
                    Toast.makeText(context, "Usuario registrado con Exito!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(context, "Error al registrar usuario " + t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });
    }
}
