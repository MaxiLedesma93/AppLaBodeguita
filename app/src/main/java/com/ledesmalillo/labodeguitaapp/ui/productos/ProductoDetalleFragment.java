package com.ledesmalillo.labodeguitaapp.ui.productos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentProductoDetalleBinding;

public class ProductoDetalleFragment extends Fragment {

    private ProductoDetalleViewModel mViewModel;
    private FragmentProductoDetalleBinding binding;

    public static ProductoDetalleFragment newInstance() {
        return new ProductoDetalleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ProductoDetalleViewModel.class);
        binding = FragmentProductoDetalleBinding.inflate(getLayoutInflater());
        View root = inflater.inflate(R.layout.fragment_producto_detalle, container, false);

        mViewModel.getProducto().observe(getViewLifecycleOwner(), new Observer<Producto>() {
            @Override
            public void onChanged(Producto producto){
                String URL = "http://192.168.1.35:5000/";
                binding.tvDetProductoNombre.setText(producto.getNombre());
                binding.tvDetProductoDescripcion.setText("Ingredientes: "+producto.getDescripcion());
                binding.tvDetProductoPrecio.setText(producto.getPrecio() != null ? "Precio: $ " + producto.getPrecio().toString() : "-" );
                Glide.with(root.getContext())
                        .load(URL + producto.getFoto())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.ivDetProductoFoto);
            }
        });
        mViewModel.setProducto(getArguments());

        return binding.getRoot();
    }



}