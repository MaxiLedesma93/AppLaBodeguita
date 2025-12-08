package com.ledesmalillo.labodeguitaapp.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.Modelos.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public class ApiClient {
    //ip maxi 192.168.1.35
    //ip lula 192.168.100.9
    //private static final String URL = "http://192.168.1.35:5000/";
    private static final String URL = "http://192.168.1.35:5000/";

    private static MisEndPoints mep;
    public static SharedPreferences sp;

    public static SharedPreferences conectar(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("token.dat", 0);
        }
        return sp;
    }

    public static MisEndPoints getEndPoints() {
        Gson gson = new GsonBuilder().setLenient().create();
        // Construimos un cliente HTTP utilizando OkHttpClient para manejar las solicitudes
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // Crear un interceptor para el logging
        // agregar a gradle: implementation 'com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14'
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(httpClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mep = retrofit.create(MisEndPoints.class);
        return mep;
    }

    public interface MisEndPoints {

        //Realiza el login
        @FormUrlEncoded
        @POST("usuario/login")
        Call<String> login(@Field("Email") String e, @Field("Clave") String c);

        //Accede al perfil del usuario Logueado enviando el token en el header.
        @GET("usuario/perfil")
        Call<Usuario> get(@Header("Authorization") String authorization);

        //Se usa en la vista de perfil
        @PATCH("usuario/editar")
        Call<Usuario> editarUsuario(@Header("Authorization") String token, @Body Usuario usuario);

        @Multipart
        @POST("usuario/registrar")
        Call<Usuario> registrarUsuario(@Header("Authorization") String token,
                                       @Part("nombre") RequestBody nombre,
                                       @Part("apellido") RequestBody apellido,
                                       @Part("email") RequestBody email,
                                       @Part("direccion") RequestBody direccion,
                                       @Part("telefono") RequestBody telefono,
                                       @Part("clave")RequestBody clave,
                                       @Part("rol") RequestBody rol,
                                       @Part("estado") RequestBody estado);

        @GET("producto/listar")
        Call<List<Producto>> listaProductos(@Header("Authorization") String token);

    }
}