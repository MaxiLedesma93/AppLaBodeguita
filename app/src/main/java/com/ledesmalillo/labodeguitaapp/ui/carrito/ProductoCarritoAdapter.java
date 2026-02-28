package com.ledesmalillo.labodeguitaapp.ui.carrito;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.R;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class ProductoCarritoAdapter extends RecyclerView.Adapter <ProductoCarritoAdapter.ViewHolder> {
    private List<ItemCarrito> lista;
    private View root;

    private LayoutInflater layoutInflater;
    public ProductoCarritoAdapter(List<ItemCarrito> lista, View root, LayoutInflater layoutInflater) {
        this.lista = lista;
        this.root = root;
        this.layoutInflater = layoutInflater;
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
        String URL = "http://192.168.1.35:5000/";
        holder.tvNombreProducto.setText(p.getNombre());
        holder.tvPrecioProducto.setText(p.getPrecio() != null ? "$ " + p.getPrecio().toString() : "-");
        holder.tvDescripcionProducto.setText(p.getDescripcion());
        Glide.with(root.getContext())
                .load(URL + p.getFoto())
                .diskCacheStrategy(DiskCacheStrategy.NONE) // No usar caché de disco
                .skipMemoryCache(true)
                .into(holder.ivFotoProducto);
        holder.btn_borrar_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), lista.size());
                Navigation.findNavController(view).navigate(R.id.nav_carrito);
                Navigation.findNavController(view).popBackStack();
            }
        });
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
        private TextView tvNombreProducto, tvPrecioProducto, tvDescripcionProducto;
        private Button btn_borrar_carrito;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoProducto = itemView.findViewById(R.id.ivFotoProducto);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioProducto = itemView.findViewById(R.id.tvPrecioProducto);
            tvDescripcionProducto = itemView.findViewById(R.id.tvDescripcionProducto);
            btn_borrar_carrito = itemView.findViewById(R.id.btn_borrar_carrito);
        }
    }
}
