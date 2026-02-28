package com.ledesmalillo.labodeguitaapp.ui.carrito;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentCarritoBinding;

import java.util.ArrayList;
import java.util.List;

public class CarritoFragment extends Fragment {

    private CarritoViewModel carritoViewModel;
    private FragmentCarritoBinding binding;
    private RecyclerView rvProductosCarrito;

    private ProductoCarritoAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        carritoViewModel =
                new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);
        binding = FragmentCarritoBinding.inflate(inflater, container, false);
        rvProductosCarrito = binding.rvProductosPedido;
        rvProductosCarrito.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductoCarritoAdapter(new ArrayList<>(), binding.getRoot(), getLayoutInflater());
        rvProductosCarrito.setAdapter(adapter);

        carritoViewModel.getListaItems().observe(getViewLifecycleOwner(), new Observer<List<ItemCarrito>>() {
            @Override
            public void onChanged(List<ItemCarrito> itemCarritos) {
                adapter.actualizarProductos(itemCarritos);
                carritoViewModel.calcularTotal();

            }
        });
        carritoViewModel.getTotal().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double total) {
                binding.tvPrecioTotal.setText("$"+total.toString());
            }

        });

        return binding.getRoot();
    }
}