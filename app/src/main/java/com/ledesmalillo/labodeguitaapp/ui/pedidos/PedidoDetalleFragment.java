package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ledesmalillo.labodeguitaapp.Modelos.Estado;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentPedidoDetalleBinding;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;
import com.ledesmalillo.labodeguitaapp.utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

        // Control de visibilidad del botón para Recepcionista
        //if (SessionManager.esRecepcionista(getContext())) {
        //    binding.btnCambiarEstado.setVisibility(View.VISIBLE);
        //}

        mViewModel.getPedido().observe(getViewLifecycleOwner(), pedido -> {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date fechaObjeto = formatoEntrada.parse(pedido.getFecha());
                String fechaFormateada = formatoSalida.format(fechaObjeto);
                binding.tvDetFechaPedido.setText(fechaFormateada);
            } catch (ParseException e) {
                binding.tvDetFechaPedido.setText(pedido.getFecha());
            }
            binding.tvDetTituloPedido.setText("Pedido #" + pedido.getId());
            binding.tvDetEstadoPedido.setText(pedido.getEstado().getDescripcion());
            binding.tvDetDireccionPedido.setText(pedido.getDireccionEntrega());
            binding.tvDetImportePedido.setText("$ " + String.format(Locale.getDefault(), "%.2f", pedido.getImporteTotal()));
            binding.tvDetProductosPedido.setText(mViewModel.getProductosString(pedido));
        });

        // Observamos los estados para mostrar el diálogo
        mViewModel.getEstados().observe(getViewLifecycleOwner(), estados -> {
            if (estados != null && !estados.isEmpty()) {
                mostrarDialogoEstados(estados);
            }
        });
        binding.btnCambiarEstado.setVisibility(SessionManager.esRecepcionista(getContext()) ? View.VISIBLE : View.GONE);
        binding.btnCambiarEstado.setOnClickListener(v -> mViewModel.cargarEstados());

        binding.btnRepetirPedido.setOnClickListener(v -> 
            mViewModel.enviarItemsCarrito(mViewModel.getPedido().getValue().getDetalles(), v, carritoViewModel, false)
        );

        binding.btnEditarPedido.setOnClickListener(v -> 
            mViewModel.enviarItemsCarrito(mViewModel.getPedido().getValue().getDetalles(), v, carritoViewModel, true)
        );

        mViewModel.setPedido(getArguments());
        return binding.getRoot();
    }

    private void mostrarDialogoEstados(List<Estado> estados) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleccione el nuevo estado");

        ArrayAdapter<Estado> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, estados);

        builder.setAdapter(adapter, (dialog, which) -> {
            Estado seleccionado = estados.get(which);
            mViewModel.actualizarEstadoPedido(seleccionado.getId());
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}