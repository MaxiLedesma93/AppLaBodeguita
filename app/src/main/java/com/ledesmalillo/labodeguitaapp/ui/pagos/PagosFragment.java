package com.ledesmalillo.labodeguitaapp.ui.pagos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledesmalillo.labodeguitaapp.databinding.FragmentPagosBinding;

import java.util.ArrayList;

public class PagosFragment extends Fragment {

    private PagosViewModel mViewModel;
    private FragmentPagosBinding binding;
    private PagosAdapter adapter;

    public static PagosFragment newInstance() {
        return new PagosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        binding = FragmentPagosBinding.inflate(getLayoutInflater());

        // Configurar RecyclerView
        adapter = new PagosAdapter(new ArrayList<>());
        binding.rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPagos.setAdapter(adapter);

        // Observers
        mViewModel.getTotalMercadoPago().observe(getViewLifecycleOwner(), total -> {
            binding.tvTotalMercadoPago.setText(String.format("$ %.2f", total));
        });
        mViewModel.getTotalEfectivo().observe(getViewLifecycleOwner(), total -> {
            binding.tvTotalEfectivo.setText(String.format("$ %.2f", total));
        });
        mViewModel.getTotalGeneral().observe(getViewLifecycleOwner(), total -> {
            binding.tvTotalGeneral.setText(String.format("$ %.2f", total));
        });
        mViewModel.getListaPagoDetalle().observe(getViewLifecycleOwner(), lista -> {

            adapter.actualizarLista(lista);
        });

        // Manejar el clic en "Modificar fecha"
        binding.btnModificarFecha.setOnClickListener(v -> {
            if (binding.calendarViewPagos.getVisibility() == View.GONE) {
                binding.calendarViewPagos.setVisibility(View.VISIBLE);
            } else {
                binding.calendarViewPagos.setVisibility(View.GONE);
            }
        });

        // Manejar la selección de fecha en el calendario
        binding.calendarViewPagos.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Formato dd-MM-yyyy con ceros a la izquierda
            String fecha = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year);
            binding.tvFechaSeleccionada.setText(fecha);

            // Ocultar el calendario tras elegir
            binding.calendarViewPagos.setVisibility(View.GONE);

            // Cargar datos en el ViewModel
            mViewModel.cargarCierreCaja(fecha);
        });

        return binding.getRoot();
    }
}
