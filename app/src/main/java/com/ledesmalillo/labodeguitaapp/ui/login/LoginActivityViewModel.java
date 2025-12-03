package com.ledesmalillo.labodeguitaapp.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ledesmalillo.labodeguitaapp.MainActivity;
import com.ledesmalillo.labodeguitaapp.Modelos.Usuario;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.ui.usuario.UsuarioFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private Context context;

    public LoginActivityViewModel(@NonNull Application application) {

        super(application);
        context = application.getApplicationContext();
    }

    public void Logueo(String email, String password) {
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        Bundle bundle = new Bundle();
        Call<String> call = api.login(email, password);
        call.enqueue(new Callback<String>() {
            //en response.body() viene el token en un String.
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    SharedPreferences sp = ApiClient.conectar(context);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("token", "Bearer " + response.body());
                    editor.apply();
                    Call<Usuario> callUsuario = api.get("Bearer " + response.body());
                    callUsuario.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            bundle.putSerializable("usuario", response.body());
                            iniciarMenu(bundle);
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable throwable) {
                            Toast.makeText(context.getApplicationContext(), "Error obteniendo el usuario para" +
                                    " header", Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    Toast.makeText(context.getApplicationContext(), "Usuario y/o Contraseña Incorrectos",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context.getApplicationContext(), "Usuario y/o Contraseña Incorrectos",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void iniciarMenu(Bundle u) {


        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("usuario", u);
        context.getApplicationContext().startActivity(intent);
    }
}
