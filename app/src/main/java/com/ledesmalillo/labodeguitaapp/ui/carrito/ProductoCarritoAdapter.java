package com.ledesmalillo.labodeguitaapp.ui.carrito;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.R;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class ProductoCarritoAdapter extends RecyclerView.Adapter <ProductoCarritoAdapter.ViewHolder> {
    private List<ItemCarrito> lista;
    private View root;
    private CarritoViewModel carritoViewModel;

    private LayoutInflater layoutInflater;
    public ProductoCarritoAdapter(List<ItemCarrito> lista, View root, LayoutInflater layoutInflater,
                                  CarritoViewModel carritoViewModel) {
        this.lista = lista;
        this.root = root;
        this.layoutInflater = layoutInflater;
        this.carritoViewModel = carritoViewModel;
    }

    @NonNull
    @Override
    public ProductoCarritoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_producto_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoCarritoAdapter.ViewHolder holder, int position) {
        Producto p = lista.get(holder.getAdapterPosition()).getProducto();
        //URL maxi String URL = "http://192.168.1.35:5000/";
        //URL lula String URL = "http://192.168.100.9:5000/";
        //carritoViewModel = new ViewModelProvider((ViewModelStoreOwner) root.getContext()).get(CarritoViewModel.class);
        String URL = "http://192.168.1.35:5000/";
        holder.tvNombreProducto.setText(p.getNombre());
        holder.tvPrecioProducto.setText(p.getPrecio() != null ? "$ " + p.getPrecio().toString() : "-");
        holder.tvDescripcionProducto.setText(p.getDescripcion());
        Glide.with(root.getContext())
                .load(URL + p.getFoto())
                .diskCacheStrategy(DiskCacheStrategy.NONE) // No usar caché de disco
                .skipMemoryCache(true)
                .into(holder.ivFotoProducto);

        holder.imgBtn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), lista.size());
                carritoViewModel.calcularTotal();
                //Navigation.findNavController(view).navigate(R.id.nav_carrito);
                //Navigation.findNavController(view).popBackStack();
            }
        });
        holder.btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = holder.tvCantidad.getText().toString().equals("") ? 0 : Integer.parseInt(holder.tvCantidad.getText().toString());
                cantidad--;
                carritoViewModel.cambiarCantidadCarrito(p, cantidad);
               holder.btnMenos.setEnabled(cantidad > 1);

            }
        });
        holder.btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = holder.tvCantidad.getText().toString().equals("") ? 0 : Integer.parseInt(holder.tvCantidad.getText().toString());
                cantidad++;
                carritoViewModel.cambiarCantidadCarrito(p, cantidad);
                holder.btnMenos.setEnabled(cantidad > 1);


            }
        });
        holder.btnAgregarAlCarrito.setVisibility(View.GONE);
        holder.tvPrecioTotal.setVisibility(View.GONE);
        holder.btnMas.getLayoutParams().width = 80;
        holder.btnMenos.getLayoutParams().width = 80;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.layoutStepper.getLayoutParams();
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // Esto lo centra al no haber más elementos
        holder.layoutStepper.setLayoutParams(params);
        holder.bottom_bar.setPadding(0, 4, 0, 4);
        holder.btnMas.getLayoutParams().width = 70;  // Bajamos un poco más de 80 si es necesario
        holder.btnMenos.getLayoutParams().width = 70;
        holder.btnMas.getLayoutParams().height = 70;
        holder.btnMenos.getLayoutParams().height = 70;

        holder.tvCantidad.setText(String.valueOf(lista.get(holder.getAdapterPosition()).getCantidad()));



    }

    public void actualizarProductos(List<ItemCarrito> nuevosProductos) {
        this.lista = nuevosProductos;
        // Notifica al RecyclerView que los datos han cambiado y debe redibujarse
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFotoProducto;
        private TextView tvNombreProducto, tvPrecioProducto, tvDescripcionProducto, tvPrecioTotal,
        tvCantidad;
        private View bottom_bar, layoutStepper;
        private ImageButton imgBtn_eliminar;
        private Button btnAgregarAlCarrito, btnMenos, btnMas;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoProducto = itemView.findViewById(R.id.ivFotoProducto);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioProducto = itemView.findViewById(R.id.tvPrecioProducto);
            tvDescripcionProducto = itemView.findViewById(R.id.tvDescripcionProducto);
            imgBtn_eliminar = itemView.findViewById(R.id.imgBtn_eliminar);
            btnAgregarAlCarrito = itemView.findViewById(R.id.btnAgregarAlCarrito);
            btnMas = itemView.findViewById(R.id.btnMas);
            btnMenos = itemView.findViewById(R.id.btnMenos);
            tvPrecioTotal = itemView.findViewById(R.id.tvPrecioTotal);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            bottom_bar = itemView.findViewById(R.id.bottomBarItemProdCarrito);
            layoutStepper = itemView.findViewById(R.id.layoutStepper);


        }
    }
}
