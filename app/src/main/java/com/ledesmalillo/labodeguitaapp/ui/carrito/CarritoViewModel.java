package com.ledesmalillo.labodeguitaapp.ui.carrito;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ledesmalillo.labodeguitaapp.Modelos.ItemCarrito;
import com.ledesmalillo.labodeguitaapp.Modelos.Producto;

import java.util.ArrayList;
import java.util.List;

public class CarritoViewModel extends ViewModel {
    private final MutableLiveData<List<ItemCarrito>> listaItems = new MutableLiveData<>();
    private final MutableLiveData<Double> total = new MutableLiveData<>();

    public CarritoViewModel() {
        listaItems.setValue(new ArrayList<>());
        total.setValue(0.0);
    }

    public LiveData<List<ItemCarrito>> getListaItems() {

        return listaItems;
    }

    public LiveData<Double> getTotal() {
        return total;
    }

    public void agregarAlCarrito(Producto producto, int cantidad) {
        List<ItemCarrito> listaActual = listaItems.getValue();
        if (listaActual == null) return;

        // Revisa si el producto ya está en el carrito
        for (ItemCarrito item : listaActual) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + cantidad); // Si ya está, suma la cantidad
                listaItems.setValue(listaActual); // Notifica el cambio
                calcularTotal();
                return;
            }
        }

        // Si no está, lo agrega como un nuevo item
        listaActual.add(new ItemCarrito(producto, cantidad));
        listaItems.setValue(listaActual);
        calcularTotal();
    }
    public boolean habilitarBotonRealizarPedido() {
        if(total.getValue() > 0) {
            return true;
        }
        return false;
    }

    public void calcularTotal() {
        List<ItemCarrito> listaActual = listaItems.getValue();
        double sumaTotal = 0;
        if (listaActual != null) {
            for (ItemCarrito item : listaActual) {
                sumaTotal += item.getProducto().getPrecio() * item.getCantidad();
            }
        }
        total.setValue(sumaTotal);
    }
    public void cambiarCantidadCarrito(Producto producto, int cantidad) {
        List<ItemCarrito> listaActual = listaItems.getValue();
        for (ItemCarrito item : listaActual) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(cantidad); // Si ya está, suma la cantidad
                listaItems.setValue(listaActual); // Notifica el cambio
                calcularTotal();
                return;
            }
        }
    }
}