package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.ledesmalillo.labodeguitaapp.Modelos.Detalle;
import com.ledesmalillo.labodeguitaapp.Modelos.Pedido;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;

import java.util.List;


public class PedidoDetalleViewModel extends AndroidViewModel {
    private MutableLiveData<Pedido> mPedido;
    private Pedido pedido;
    private Context context;

    public PedidoDetalleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<Pedido> getPedido() {
        if (mPedido == null) {
            mPedido = new MutableLiveData<>();
        }
        return mPedido;
    }

    public void setPedido(Bundle bundle) {
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("Pedido");
            mPedido.setValue(pedido);
        }
    }


    public String getProductosString(Pedido pedido) {
        String productos = "";
        List<Detalle> detalles = pedido.getDetalles();
        for (Detalle detalle : detalles) {
            productos += detalle.getProducto().getNombre() + " x " + detalle.getCantidad() + "\n";
        }

        return productos;
    }
    public void enviarItemsCarrito(List<Detalle> detalles, View root, CarritoViewModel carritoViewModel){
        carritoViewModel.reiniciarMutableCarrito();
        for (Detalle detalle : detalles) {
            carritoViewModel.agregarAlCarrito(detalle.getProducto(), detalle.getCantidad());
        }
        Navigation.findNavController(root).navigate(R.id.nav_carrito);
    }
}