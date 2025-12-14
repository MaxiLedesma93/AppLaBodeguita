package com.ledesmalillo.labodeguitaapp.ui.productos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentCrearProductoBinding;

public class CrearProductoFragment extends Fragment {

    private CrearProductoViewModel mViewModel;
    private FragmentCrearProductoBinding binding;

    private Intent intent;

    private ActivityResultLauncher<Intent> arl;

    private Uri uriImagen;


    public static CrearProductoFragment newInstance() {
        return new CrearProductoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearProductoBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(CrearProductoViewModel.class);
        abrirGaleria();
        mViewModel.getUriMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivFotoProducto.setImageURI(uri);
                uriImagen =uri;
            }
        });


        binding.btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(intent);
            }
        });

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = binding.etNombreProducto.getText().toString();
                String descripcion = binding.etDescripcionProducto.getText().toString();
                String precio = binding.etPrecioProducto.getText().toString();
                Boolean estado = true;
                mViewModel.guardarProducto(nombre, descripcion, precio, estado, uriImagen);
            }
        });
        return binding.getRoot();
    }

    private void abrirGaleria(){
        intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        arl=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        mViewModel.recibirFoto(result);
                    }
                });
    }


}