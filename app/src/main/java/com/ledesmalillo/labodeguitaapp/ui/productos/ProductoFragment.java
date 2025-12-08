package com.ledesmalillo.labodeguitaapp.ui.productos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentProductoBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductoFragment extends Fragment {

    private ProductoViewModel mViewModel;
    private FragmentProductoBinding binding;
    private RecyclerView rvProductos;
    private ProductoAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        binding = FragmentProductoBinding.inflate(getLayoutInflater());
        rvProductos = binding.rvProductos;
        mViewModel.mostrarProductos();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                2, GridLayoutManager.VERTICAL, false);
        rvProductos.setLayoutManager(gridLayoutManager);
        adapter = new ProductoAdapter(new ArrayList<>(), binding.getRoot(), getLayoutInflater());
        rvProductos.setAdapter(adapter);
        mViewModel.getProductos().observe(getViewLifecycleOwner(), new Observer<List<Producto>>() {
            @Override
            public void onChanged(List<Producto> productos) {
                adapter.actualizarProductos(productos);
            }
        });




        return binding.getRoot();
    }



}