package com.ledesmalillo.labodeguitaapp.ui.pedidos;

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
import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private View root;
    private List<Pedido> lista;
    private LayoutInflater layoutInflater;
    private CarritoViewModel carritoViewModel;




    public PedidoAdapter(View root, List<Pedido> lista, LayoutInflater layoutInflater) {
        this.root = root;
        this.lista = lista;
        this.layoutInflater = layoutInflater;

    }

    @NonNull
    @Override
    public PedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_pedido, parent, false);
        carritoViewModel = new ViewModelProvider((ViewModelStoreOwner) root.getContext()).get(CarritoViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapter.ViewHolder holder, int position) {
        Pedido p = lista.get(holder.getAdapterPosition());
        int posicion = holder.getAdapterPosition() + 1;
        holder.tvTituloCPedido.setText("Pedido: " + posicion);
        holder.tvProductos.setText(obtenerProductosDePedido(p.getDetalles()));
        holder.btnRepetirPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarItemsCarrito(p.getDetalles());
            }
        });

    }
    public String obtenerProductosDePedido(List<Detalle> detalles){
        String productos = "";
        for (Detalle detalle : detalles) {
            productos += detalle.getProducto().getNombre() + " Cant: " + detalle.getCantidad() + "\n";
        }
        return productos;
    }
    public void enviarItemsCarrito(List<Detalle> detalles){
       // List<ItemCarrito> itemsCarrito = new ArrayList<ItemCarrito>();
        carritoViewModel.reiniciarMutableCarrito();
        for (Detalle detalle : detalles) {
            //itemsCarrito.add(new ItemCarrito(detalle.getProducto(), detalle.getCantidad()));
            carritoViewModel.agregarAlCarrito(detalle.getProducto(), detalle.getCantidad());
        }
        Navigation.findNavController(root).navigate(R.id.nav_carrito);
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
        private TextView  tvProductos , tvTituloCPedido;

        private Button btnRepetirPedido;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductos = itemView.findViewById(R.id.tvProductos);
            tvTituloCPedido = itemView.findViewById(R.id.tvTituloCPedido);
            btnRepetirPedido = itemView.findViewById(R.id.btnRepetirPedido);

        }
    }

}
