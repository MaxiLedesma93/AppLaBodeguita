package com.ledesmalillo.labodeguitaapp.ui.productos;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;
import com.ledesmalillo.labodeguitaapp.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter <ProductoAdapter.ViewHolder> {
    private List<Producto> lista;
    private View root;

    private LayoutInflater layoutInflater;
    public ProductoAdapter(List<Producto> lista, View root, LayoutInflater layoutInflater) {
        this.lista = lista;
        this.root = root;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ViewHolder holder, int position) {
        Producto p = lista.get(position);
        //URL maxi String URL = "http://192.168.1.35:5000/";
        //URL lula String URL = "http://192.168.100.9:5000/";
        String URL = "http://192.168.1.35:5000/";
        holder.tvNombreProducto.setText(p.getNombre());
        holder.tvPrecioProducto.setText(p.getPrecio() != null ? "$ " + p.getPrecio().toString() : "-" );
        holder.tvDescripcionProducto.setText(p.getDescripcion());
        Glide.with(root.getContext())
                .load(URL + lista.get(position).getFoto())
                .diskCacheStrategy(DiskCacheStrategy.NONE) // No usar caché de disco
                .skipMemoryCache(true)
                .into(holder.ivFotoProducto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("producto", p);
                Navigation.findNavController(root).navigate(R.id.productoDetalleFragment, bundle);

            }
        });
    }
    public void actualizarProductos(List<Producto> nuevosProductos) {
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoProducto = itemView.findViewById(R.id.ivFotoProducto);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioProducto = itemView.findViewById(R.id.tvPrecioProducto);
            tvDescripcionProducto = itemView.findViewById(R.id.tvDescripcionProducto);
        }
    }
}
