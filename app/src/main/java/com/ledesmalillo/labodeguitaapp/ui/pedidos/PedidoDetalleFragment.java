package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentPedidoDetalleBinding;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PedidoDetalleFragment extends Fragment {

    private PedidoDetalleViewModel mViewModel;
    private CarritoViewModel carritoViewModel;
    private FragmentPedidoDetalleBinding binding;

    public static PedidoDetalleFragment newInstance() {
        return new PedidoDetalleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPedidoDetalleBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(PedidoDetalleViewModel.class);
        carritoViewModel = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);

        mViewModel.getPedido().observe(getViewLifecycleOwner(), pedido -> {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date fechaObjeto = formatoEntrada.parse(pedido.getFecha());
                String fechaFormateada = formatoSalida.format(fechaObjeto);
                binding.tvDetFechaPedido.setText(fechaFormateada);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            binding.tvDetTituloPedido.setText(pedido.getEstado().getDescripcion());
            binding.tvDetEstadoPedido.setText(pedido.getEstado().getDescripcion());
            binding.tvDetDireccionPedido.setText(pedido.getDireccionEntrega());
            binding.tvDetImportePedido.setText("$ "+ String.valueOf(pedido.getImporteTotal()));
            binding.tvDetProductosPedido.setText(mViewModel.getProductosString(pedido));
        });
        binding.btnRepetirPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.enviarItemsCarrito(mViewModel.getPedido().getValue().getDetalles(),
                        view, carritoViewModel, false);
            }
        });
        binding.btnEditarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.enviarItemsCarrito(mViewModel.getPedido().getValue().getDetalles(),
                        view, carritoViewModel, true);
            }
        });
        mViewModel.setPedido(getArguments());

        return binding.getRoot();
    }



}