package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.ledesmalillo.labodeguitaapp.Modelos.Detalle;
import com.ledesmalillo.labodeguitaapp.Modelos.Estado;
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;
import com.ledesmalillo.labodeguitaapp.utils.Constantes;
import com.ledesmalillo.labodeguitaapp.utils.SessionManager;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoDetalleViewModel extends AndroidViewModel {
    private MutableLiveData<Pedido> mPedido = new MutableLiveData<>();
    private MutableLiveData<List<Estado>> mEstados = new MutableLiveData<>();
    private Context context;

    public PedidoDetalleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Pedido> getPedido() {
        return mPedido;
    }

    public LiveData<List<Estado>> getEstados() {
        return mEstados;
    }

    public void setPedido(Bundle bundle) {
        if (bundle != null) {
            Pedido pedido = (Pedido) bundle.getSerializable("Pedido");
            mPedido.setValue(pedido);
        }
    }

    public void cargarEstados() {
        String token = SessionManager.getToken(context);
        ApiClient.getEndPoints().listarEstados(token).enqueue(new Callback<List<Estado>>() {
            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {
                if (response.isSuccessful()) {
                    mEstados.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Toast.makeText(context, "Error al cargar estados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarEstadoPedido(int idEstado) {
        Pedido actual = mPedido.getValue();
        if (actual == null) return;

        String token = SessionManager.getToken(context);
        
        Pedido pedidoEdit = new Pedido();
        pedidoEdit.setId(actual.getId());
        pedidoEdit.setClienteId(actual.getClienteId());
        pedidoEdit.setFecha(actual.getFecha());
        pedidoEdit.setEstadoId(idEstado);
        pedidoEdit.setDelivery(actual.getDelivery());
        pedidoEdit.setDireccionEntrega(actual.getDireccionEntrega());
        pedidoEdit.setImporteTotal(actual.getImporteTotal());

        ApiClient.getEndPoints().editarPedido(token, pedidoEdit)
                .enqueue(new Callback<Pedido>() {
                    @Override
                    public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mPedido.setValue(response.body());
                            Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Pedido> call, Throwable t) {
                        Toast.makeText(context, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getProductosString(Pedido pedido) {
        StringBuilder productos = new StringBuilder();
        List<Detalle> detalles = pedido.getDetalles();
        if (detalles != null) {
            for (Detalle detalle : detalles) {
                productos.append(detalle.getProducto().getNombre()).append(" x ").append(detalle.getCantidad()).append("\n");
            }
        }
        return productos.toString();
    }

    public void enviarItemsCarrito(List<Detalle> detalles, View root, CarritoViewModel carritoViewModel, boolean editar) {
        Pedido actual = mPedido.getValue();
        if (actual == null) return;
        
        carritoViewModel.reiniciarMutableCarrito();
        // PERSISTIMOS EL ESTADO EN EL VIEWMODEL PARA QUE NO SE PIERDA AL NAVEGAR
        carritoViewModel.setEstadoEdicion(editar, actual.getId());
        
        for (Detalle detalle : detalles) {
            carritoViewModel.agregarAlCarrito(detalle.getProducto(), detalle.getCantidad());
        }
        
        Navigation.findNavController(root).navigate(R.id.nav_carrito);
    }
}