package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledesmalillo.labodeguitaapp.Modelos.Estado;
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentPedidosBinding;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;
import com.ledesmalillo.labodeguitaapp.utils.SessionManager;

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
        binding.spFiltroEstado.setVisibility(SessionManager.esRecepcionista(getContext()) ? View.VISIBLE : View.GONE);
        pedidosViewModel.getListaEstados().observe(getViewLifecycleOwner(), estados -> {
            ArrayAdapter<Estado> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, estados);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spFiltroEstado.setAdapter(adapter);
        });
        binding.spFiltroEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Estado estadoSeleccionado = (Estado) parent.getItemAtPosition(position);
                // Llamamos al ViewModel para filtrar por el ID del estado
                pedidosViewModel.mostrarPedidos(estadoSeleccionado.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        pedidosViewModel.mostrarPedidos(-1);
        return binding.getRoot();
    }
}