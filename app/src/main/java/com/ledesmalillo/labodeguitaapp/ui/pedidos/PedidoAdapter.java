package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ledesmalillo.labodeguitaapp.Modelos.Detalle;
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private View root;
    private List<Pedido> lista;
    private LayoutInflater layoutInflater;
    private CarritoViewModel carritoViewModel;

    public PedidoAdapter(View root, List<Pedido> lista, LayoutInflater layoutInflater) {
        this.root = root;
        this.lista = lista;
        this.layoutInflater = layoutInflater;
        this.carritoViewModel = new ViewModelProvider((ViewModelStoreOwner) root.getContext()).get(CarritoViewModel.class);
    }

    @NonNull
    @Override
    public PedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull PedidoAdapter.ViewHolder holder, int position) {
        Pedido p = lista.get(holder.getAdapterPosition());
        int posicion = holder.getAdapterPosition() + 1;
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date fechaObjeto = formatoEntrada.parse(p.getFecha());
            String fechaFormateada = formatoSalida.format(fechaObjeto);
            holder.tvFechaPedido.setText(fechaFormateada);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.tvTituloCPedido.setText("Pedido " + p.getId());
        holder.tvCantProductosPedido.setText(obtenerCantProductos(p.getDetalles()));
        holder.tvPrecioPedido.setText("$"+ String.valueOf(p.getImporteTotal()));
        holder.tvEstadoPedido.setText(p.getEstado().getDescripcion());



        holder.btnVerDetallePedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Pedido", p);
                Navigation.findNavController(root).navigate(R.id.pedidoDetalleFragment, bundle);
            }
        });



    }
    public String obtenerCantProductos(List<Detalle> detalles){
        int CantProductos = 0;
        for (Detalle detalle : detalles) {
            CantProductos += detalle.getCantidad();
        }
          return " Cant: " + CantProductos + " productos.";
    }


    public void actualizarPedidos(List<Pedido> nuevosPedidos) {
        this.lista = nuevosPedidos;
        // Notifica al RecyclerView que los datos han cambiado y debe redibujarse
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  tvPrecioPedido , tvTituloCPedido, tvEstadoPedido,
                tvFechaPedido, tvCantProductosPedido;

        private Button btnVerDetallePedido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrecioPedido = itemView.findViewById(R.id.tvPrecioPedido);
            tvEstadoPedido = itemView.findViewById(R.id.tvEstadoPedido);
            tvFechaPedido = itemView.findViewById(R.id.tvFecha);
            tvCantProductosPedido = itemView.findViewById(R.id.tvCantProductosPedido);
            tvTituloCPedido = itemView.findViewById(R.id.tvTituloCPedido);

            btnVerDetallePedido = itemView.findViewById(R.id.btnVerDetallePedido);

        }
    }

}
