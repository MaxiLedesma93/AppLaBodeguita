package com.ledesmalillo.labodeguitaapp.ui.pedidos;


import android.content.Intent;
import android.net.Uri;
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
    private boolean esEdicion;
    private int id_pedido_editar;

    public PagoBottomSheet(String direccion, boolean esDelivery, boolean esEdicion,
                           int id_pedido_editar) {
        this.direccion = direccion;
        this.esDelivery = esDelivery;
        this.esEdicion = esEdicion;
        this.id_pedido_editar = id_pedido_editar;
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
            if(esEdicion){
                carritoViewModel.editarPedido(id_pedido_editar,direccion, esDelivery, true);
            }
            else{
                carritoViewModel.guardarPedido(direccion, esDelivery, true);
            }
            Toast.makeText(getContext(), "Redirigiendo a Mercado Pago...", Toast.LENGTH_SHORT).show();
            // 1. El link de Mercado Pago (usualmente lo genera tu API de .NET)
            // Por ahora usaremos uno de prueba, pero aquí iría la URL que te da MP
            //String urlMercadoPago = "https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=labodeguitasl";
            String urlMercadoPago = "https://link.mercadopago.com.ar/labodeguitasl";
            // 2. Crear el Intent para abrir el navegador o la App de Mercado Pago
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlMercadoPago));
            startActivity(intent);
            dismiss();
        });

        v.findViewById(R.id.btnEfectivo).setOnClickListener(view -> {
            // Guardamos el pedido indicando que se paga en efectivo
            carritoViewModel.guardarPedido(direccion, esDelivery, false);
            dismiss();
        });

        return v;
    }
}
