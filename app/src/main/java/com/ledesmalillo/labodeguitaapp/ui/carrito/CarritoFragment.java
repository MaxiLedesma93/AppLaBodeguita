package com.ledesmalillo.labodeguitaapp.ui.carrito;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentCarritoBinding;
import com.ledesmalillo.labodeguitaapp.ui.pedidos.PagoBottomSheet;

import java.util.ArrayList;

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
        // IMPORTANTE: Usamos requireActivity() para que el ViewModel sea compartido y persista el estado de edición
        carritoViewModel = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);
        binding = FragmentCarritoBinding.inflate(inflater, container, false);
        
        rvProductosCarrito = binding.rvProductosPedido;
        rvProductosCarrito.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductoCarritoAdapter(new ArrayList<>(), binding.getRoot(), getLayoutInflater(), carritoViewModel);
        rvProductosCarrito.setAdapter(adapter);

        // Observers
        carritoViewModel.getListaItems().observe(getViewLifecycleOwner(), itemCarritos -> {
            adapter.actualizarProductos(itemCarritos);
            carritoViewModel.calcularTotal();
        });

        carritoViewModel.getTotal().observe(getViewLifecycleOwner(), total -> {
            binding.tvPrecioTotal.setText(String.format("$ %.2f", total));
            binding.btnRealizarPedido.setEnabled(carritoViewModel.habilitarBotonRealizarPedido());
        });

        // Configuración inicial de entrega
        binding.radioGroupEntrega.check(binding.rbRetiroLocal.getId());
        binding.etDireccionEntrega.setText(carritoViewModel.asignarDireccion(
                binding.rbDelivery.getId(), binding.rbRetiroLocal.getId(),
                binding.rbRetiroLocal.getId()));

        binding.radioGroupEntrega.setOnCheckedChangeListener((radioGroup, idSeleccionado) -> {
            binding.etDireccionEntrega.setText(carritoViewModel.asignarDireccion(
                    binding.rbDelivery.getId(), binding.rbRetiroLocal.getId(),
                    idSeleccionado));
            binding.etDireccionEntrega.setEnabled(false);
        });

        binding.ibEditarDireccion.setOnClickListener(view -> binding.etDireccionEntrega.setEnabled(true));

        // Lógica del botón Realizar Pedido
        binding.btnRealizarPedido.setOnClickListener(v -> {
            if (carritoViewModel.habilitarBotonRealizarPedido()) {
                String direccion = binding.etDireccionEntrega.getText().toString();
                boolean deliveryChecked = binding.rbDelivery.isChecked();
                
                // --- Usamos el estado del ViewModel en lugar de getArguments() para que no cree ---
                // un nuevo pedido al elegir editar e irse a ver los productos para agregar nuevos.
                boolean esEdicion = carritoViewModel.isEsEdicion();
                int id_pedido_editar = carritoViewModel.getIdPedidoAEditar();

                PagoBottomSheet modal = new PagoBottomSheet(direccion, deliveryChecked, esEdicion, id_pedido_editar);
                modal.show(getChildFragmentManager(), "TAG_PAGO");


            } else {
                Toast.makeText(getContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
            }
        });
        binding.fabAgregarProductos.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_carrito_to_nav_producto);
        });

        return binding.getRoot();
    }
}
