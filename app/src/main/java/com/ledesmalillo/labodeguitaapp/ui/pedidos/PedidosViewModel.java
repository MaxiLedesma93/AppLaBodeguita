package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PedidosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public  PedidosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pedidos fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}