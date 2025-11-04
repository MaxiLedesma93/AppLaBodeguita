package com.ledesmalillo.labodeguitaapp.ui.usuario;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsuarioViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UsuarioViewModel() {
        super();
        mText = new MutableLiveData<>();
        mText.setValue("This is usuario fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}