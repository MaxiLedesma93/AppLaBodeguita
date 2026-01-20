package com.ledesmalillo.labodeguitaapp.ui.pedidos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentPedidosBinding;

public class PedidosFragment extends Fragment {

    private PedidosViewModel pedidosViewModel;
    private FragmentPedidosBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pedidosViewModel =
                new ViewModelProvider(this).get(PedidosViewModel.class);
        binding = FragmentPedidosBinding.inflate(inflater, container, false);





        return binding.getRoot();
    }
}