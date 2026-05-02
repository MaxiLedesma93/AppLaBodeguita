package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentPedidosBinding;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;

import java.util.ArrayList;
import java.util.List;

public class PedidosFragment extends Fragment {

    private PedidosViewModel pedidosViewModel;
    //private CarritoViewModel carritoViewModel;
    private FragmentPedidosBinding binding;
    private RecyclerView rvPedidos;
    private PedidoAdapter pedidoAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pedidosViewModel =
                new ViewModelProvider(this).get(PedidosViewModel.class);
        binding = FragmentPedidosBinding.inflate(inflater, container, false);
        rvPedidos = binding.rvPedido;
        rvPedidos.setLayoutManager(new LinearLayoutManager(getContext()));
        pedidoAdapter = new PedidoAdapter( binding.getRoot(), new ArrayList<>(), getLayoutInflater());
        rvPedidos.setAdapter(pedidoAdapter);
        pedidosViewModel.getListaPedidos().observe(getViewLifecycleOwner(), new Observer<List<Pedido>>() {
            @Override
            public void onChanged(List<Pedido> pedidos) {
                pedidoAdapter.actualizarPedidos(pedidos);
            }
        });
        pedidosViewModel.mostrarPedidos();
        return binding.getRoot();
    }
}