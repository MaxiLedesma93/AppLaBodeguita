package com.ledesmalillo.labodeguitaapp.ui.pagos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledesmalillo.labodeguitaapp.Modelos.PagoDetalle;
import com.ledesmalillo.labodeguitaapp.R;

import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolder> {
    private List<PagoDetalle> lista;

    public PagosAdapter(List<PagoDetalle> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago_tabla, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PagoDetalle pago = lista.get(position);
        holder.tvIdPedido.setText(String.valueOf(pago.getPedidoId()));
        holder.tvDireccion.setText(pago.getDireccion());
        holder.tvMetodoPago.setText(pago.getMetodoDePago());
        holder.tvImporte.setText(String.format("$%.2f", pago.getImporte()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<PagoDetalle> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdPedido, tvDireccion, tvMetodoPago, tvImporte;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdPedido = itemView.findViewById(R.id.tvIdPedido);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvMetodoPago = itemView.findViewById(R.id.tvMetodoPago);
            tvImporte = itemView.findViewById(R.id.tvImporte);
        }
    }
}
