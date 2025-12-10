package com.ledesmalillo.labodeguitaapp.ui.productos;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ledesmalillo.labodeguitaapp.Modelos.Producto;

public class ProductoDetalleViewModel extends AndroidViewModel {
    private MutableLiveData<Producto> mProducto;
    private Producto producto;
    private Context context;
    public ProductoDetalleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<Producto> getProducto() {
        if (mProducto == null) {
            mProducto = new MutableLiveData<>();
        }
        return mProducto;
    }
    public void setProducto(Bundle bundle) {
        if (bundle != null) {
            producto = (Producto) bundle.getSerializable("producto");
            mProducto.setValue(producto);
        }
    }


}