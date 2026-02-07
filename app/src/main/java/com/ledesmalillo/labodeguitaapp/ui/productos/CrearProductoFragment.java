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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
        mViewModel.iniciar(getArguments());
        mViewModel.getUriMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivFotoProducto.setImageURI(uri);
                uriImagen =uri;
                Glide.with(getContext())
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // No usar caché de disco
                        .skipMemoryCache(true)
                        .into(binding.ivFotoProducto);
            }
        });
        mViewModel.getEstado().observe(getViewLifecycleOwner(), new Observer<ProductoViewState>() {
            @Override
            public void onChanged(ProductoViewState estado) {
                // El Fragment no piensa, solo obedece al ViewState
                //en caso de ser creacion estado.getNombre() == "".
                //En caso de edicion, estado.getNombre() va a traer el dato del producto a editar.
                String URL = "http://192.168.1.35:5000/";
                binding.etNombreProducto.setText(estado.getNombre());
                binding.etDescripcionProducto.setText(estado.getDescripcion());
                binding.etPrecioProducto.setText(estado.getPrecio());
                Glide.with(getContext())
                        .load(estado.getFotoUrl())
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // No usar caché de disco
                        .skipMemoryCache(true)
                        .into(binding.ivFotoProducto);
                View rbAsociado = binding.rgTipoProducto.findViewWithTag(estado.getTipo());
                binding.rgTipoProducto.check(rbAsociado.getId());

            }
        });
        /*mViewModel.getUriMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
                    @Override
                    public void onChanged(Uri uri) {
                        binding.ivFotoProducto.setImageURI(uri);
                    }
                });

         */


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
                Double precio = Double.valueOf(binding.etPrecioProducto.getText().toString());
                Boolean estado = true;
                int idSeleccionado = binding.rgTipoProducto.getCheckedRadioButtonId();
                String tipo = binding.rgTipoProducto.findViewById(idSeleccionado).getTag().toString();

                mViewModel.guardarProducto(nombre, descripcion, estado, uriImagen, precio, tipo);
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