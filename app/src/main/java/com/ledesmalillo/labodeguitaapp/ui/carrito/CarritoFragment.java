package com.ledesmalillo.labodeguitaapp.ui.carrito;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.ui.pedidos.PagoBottomSheet;
import com.ledesmalillo.labodeguitaapp.ui.productos.CrearProductoFragment;
import com.ledesmalillo.labodeguitaapp.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

public class CarritoFragment extends Fragment {

    private CarritoViewModel carritoViewModel;
    private FragmentCarritoBinding binding;
    private RecyclerView rvProductosCarrito;

    private ProductoCarritoAdapter adapter;

    public static CarritoFragment newInstance() {
        return new CarritoFragment();
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        carritoViewModel =
                new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);
        binding = FragmentCarritoBinding.inflate(inflater, container, false);
        rvProductosCarrito = binding.rvProductosPedido;
        rvProductosCarrito.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductoCarritoAdapter(new ArrayList<>(), binding.getRoot(), getLayoutInflater(), carritoViewModel);
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
                binding.btnRealizarPedido.setEnabled(carritoViewModel.habilitarBotonRealizarPedido());
            }
        });
        binding.radioGroupEntrega.check(binding.rbRetiroLocal.getId());
        binding.etDireccionEntrega.setText(carritoViewModel.asignarDireccion(
                binding.rbDelivery.getId(),binding.rbRetiroLocal.getId(),
                binding.rbRetiroLocal.getId()).toString());

        binding.radioGroupEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int idSeleccionado){
                binding.etDireccionEntrega.setText(carritoViewModel.asignarDireccion(
                        binding.rbDelivery.getId(),binding.rbRetiroLocal.getId(),
                        idSeleccionado).toString());
                binding.etDireccionEntrega.setEnabled(false);
            }
        });

        binding.btnRealizarPedido.setEnabled(carritoViewModel.habilitarBotonRealizarPedido());

        binding.ibEditarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etDireccionEntrega.setEnabled(true);
            }
        });
        binding.btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               // carritoViewModel.guardarPedido(direccion, deliveryChecked);
            }
        });
        binding.btnRealizarPedido.setOnClickListener(v -> {
            if (carritoViewModel.habilitarBotonRealizarPedido()) {

                // Obtenemos los datos necesarios (puedes sacarlos de tus RadioButtons de entrega)
                String direccion = binding.etDireccionEntrega.getText().toString();
                boolean deliveryChecked = binding.rbDelivery.isChecked();
                boolean esEdicion = false;
                int id_pedido_editar = 0;
                Bundle args = getArguments();
                if(args!=null){
                     esEdicion = args.getBoolean(Constantes.KEY_EDITAR_PEDIDO, false);
                     id_pedido_editar = args.getInt(Constantes.KEY_ID_PEDIDO, 0);
                }


                // Abrimos el modal
                PagoBottomSheet modal = new PagoBottomSheet(direccion, deliveryChecked, esEdicion, id_pedido_editar);
                modal.show(getChildFragmentManager(), "TAG_PAGO");

            } else {
                Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}