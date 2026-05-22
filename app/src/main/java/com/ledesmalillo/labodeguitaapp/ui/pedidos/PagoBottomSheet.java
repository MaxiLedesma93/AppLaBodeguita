package com.ledesmalillo.labodeguitaapp.ui.pedidos;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;

import java.util.Locale;

public class PagoBottomSheet extends BottomSheetDialogFragment {
    private CarritoViewModel carritoViewModel;
    private String direccion;
    private boolean esDelivery;

    public PagoBottomSheet(String direccion, boolean esDelivery) {
        this.direccion = direccion;
        this.esDelivery = esDelivery;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_pago_modal, container, false);

        // Usamos requireActivity para conectar con el mismo ViewModel del carrito
        carritoViewModel = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);

        TextView tvTotal = v.findViewById(R.id.tvTotalModal);

        // Mostrar el total actual
        carritoViewModel.getTotal().observe(getViewLifecycleOwner(), total -> {
            tvTotal.setText(String.format(Locale.getDefault(), "Total a pagar: $ %.2f", total));
        });

        v.findViewById(R.id.btnMercadoPago).setOnClickListener(view -> {
            Toast.makeText(getContext(), "Redirigiendo a Mercado Pago...", Toast.LENGTH_SHORT).show();
            // Aquí llamarías a la lógica de Mercado Pago y luego a guardarPedido
            carritoViewModel.guardarPedido(direccion, esDelivery);
            dismiss(); // Cierra el modal
        });

        v.findViewById(R.id.btnEfectivo).setOnClickListener(view -> {
            // Guardamos el pedido indicando que se paga en efectivo
            carritoViewModel.guardarPedido(direccion, esDelivery);
            dismiss();
        });

        return v;
    }
}
