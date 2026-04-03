package com.ledesmalillo.labodeguitaapp.ui.productos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentProductoDetalleBinding;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;

import java.util.Locale;

public class ProductoDetalleFragment extends Fragment {

    private ProductoDetalleViewModel mViewModel;
    private CarritoViewModel carritoViewModel;
    private FragmentProductoDetalleBinding binding;
    private int cantidad = 1;
    private Double precioUnitario = 0.0;

    public static ProductoDetalleFragment newInstance() {
        return new ProductoDetalleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ProductoDetalleViewModel.class);
        carritoViewModel = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);
        binding = FragmentProductoDetalleBinding.inflate(getLayoutInflater());
        //View root = inflater.inflate(R.layout.fragment_producto_detalle, container, false);

        mViewModel.getProducto().observe(getViewLifecycleOwner(), new Observer<Producto>() {
            @Override
            public void onChanged(Producto producto){
                String URL = "http://192.168.1.35:5000/";
                binding.tvDetProductoNombre.setText(producto.getNombre());
                binding.tvDetProductoDescripcion.setText("Ingredientes: "+producto.getDescripcion());
                binding.tvDetProductoPrecio.setText(producto.getPrecio() != null ? "Precio: $ " + producto.getPrecio().toString() : "-" );
                Glide.with(binding.getRoot().getContext())
                        .load(URL + producto.getFoto())
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // No usar caché de disco
                        .skipMemoryCache(true)
                        .into(binding.ivDetProductoFoto);

                precioUnitario = producto.getPrecio();
                // Actualizamos la UI
                binding.bottomBar.tvPrecioTotal.setText(String.format(Locale.getDefault(),
                        "$ %.2f", mViewModel.calcularPrecioTotal(precioUnitario, cantidad)));

            }
        });

        binding.bottomBar.btnAgregarAlCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto p = mViewModel.getProducto().getValue();

                // Agregamos el item a la lista del carrito en el carritoViewModel
                carritoViewModel.agregarAlCarrito(p, cantidad);

                Toast.makeText(getContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show();

                //Para navegar hacia atras
                Navigation.findNavController(v).popBackStack();

            }
        });
        binding.btnEditarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("producto_para_editar", mViewModel.getProducto().getValue());
                Navigation.findNavController(view).navigate(R.id.action_productoDetalleFragment_to_crearProductoFragment, bundle);
            }
        });
        binding.bottomBar.btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidad++;
                binding.bottomBar.tvCantidad.setText(String.valueOf(cantidad));
                binding.bottomBar.tvPrecioTotal.setText(String.format(Locale.getDefault(),
                        "$ %.2f", mViewModel.calcularPrecioTotal(precioUnitario, cantidad)));
                binding.bottomBar.btnMenos.setEnabled(cantidad > 1);

            }
        });

        // ver como enviar los datos al carrito, o armar la lista aca en el detalle fragment para
        //enviarla al carrito.
        binding.bottomBar.btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidad--;
                binding.bottomBar.tvCantidad.setText(String.valueOf(cantidad));
                binding.bottomBar.tvPrecioTotal.setText(String.format(Locale.getDefault(),
                        "$ %.2f", mViewModel.calcularPrecioTotal(precioUnitario, cantidad)));

                binding.bottomBar.btnMenos.setEnabled(cantidad > 1);


            }
        });
        binding.bottomBar.imgBtnEliminar.setVisibility(View.GONE);



        mViewModel.setProducto(getArguments());

        return binding.getRoot();
    }





}