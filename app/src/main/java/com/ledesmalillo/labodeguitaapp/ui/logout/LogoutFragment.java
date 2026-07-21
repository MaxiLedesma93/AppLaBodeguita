package com.ledesmalillo.labodeguitaapp.ui.logout;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentLogoutBinding;
import com.ledesmalillo.labodeguitaapp.ui.login.LoginActivity;
import com.ledesmalillo.labodeguitaapp.ui.usuario.RegistroActivity;
import com.ledesmalillo.labodeguitaapp.utils.SessionManager;

public class LogoutFragment extends Fragment {

    private LogoutViewModel mViewModel;
    private FragmentLogoutBinding binding;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        binding = FragmentLogoutBinding.inflate(getLayoutInflater());

        notificacion();
        return binding.getRoot();
    }

    public void notificacion() {
        new AlertDialog.Builder(getContext())
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setCancelable(false) // Obliga al usuario a elegir una opción
                .setPositiveButton("Sí, Salir", (dialog, which) -> {

                    // 1. Limpiamos las SharedPreferences usando tu SessionManager
                    SessionManager.cerrarSesion(getContext());

                    // 2. Cerramos la actividad actual y volvemos al Login
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finishAffinity();
                })
                .setNegativeButton("Cancelar", (dialog, i) -> {
                    // Si dice que no, volvemos a la pantalla anterior (Productos)
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                })
                .show();
    }

}