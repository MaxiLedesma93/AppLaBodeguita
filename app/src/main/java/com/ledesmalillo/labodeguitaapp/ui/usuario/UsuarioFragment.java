package com.ledesmalillo.labodeguitaapp.ui.usuario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ledesmalillo.labodeguitaapp.Modelos.Usuario;
import com.ledesmalillo.labodeguitaapp.R;

public class UsuarioFragment extends Fragment {

    private UsuarioViewModel uvm;
    private EditText etId, etNombre, etApellido, etMail, etDireccion, etTel;
    private Button btGuardar, btEditar;

    public static UsuarioFragment newInstance() {
        return new UsuarioFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        uvm = new ViewModelProvider(this).get(UsuarioViewModel.class);

        View root = inflater.inflate(R.layout.fragment_usuario, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
       /* uvm.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;*/

        View vistaPerfil = inflater.inflate(R.layout.fragment_usuario, container, false);
        inicializarVista(vistaPerfil);

        uvm.getUsuario().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                etNombre.setText(usuario.getNombre());
                etApellido.setText(usuario.getApellido());
                etMail.setText(usuario.getEmail());
                etTel.setText(usuario.getTelefono());
                etDireccion.setText(usuario.getDireccion());

               /* Glide.with(getContext())
                        .load("http://192.168.0.104:5001/"+propietario.getAvatarUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //cada vez q cargues imagen de propietario queda en cache
                        .into(ivProp); */
            }
        });

        uvm.obtenerUsuario();
        return vistaPerfil;

        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btGuardar.setVisibility(View.VISIBLE);
                btEditar.setVisibility(View.GONE);
            }
        });

    }
    private void inicializarVista(View vistaPerfil) {
        etNombre = vistaPerfil.findViewById(R.id.etNombre);
        etApellido = vistaPerfil.findViewById(R.id.etApellido);
        etMail = vistaPerfil.findViewById(R.id.etMail);
        etTel = vistaPerfil.findViewById(R.id.etTel);
        etDireccion = vistaPerfil.findViewById(R.id.etDireccion);
        btGuardar = vistaPerfil.findViewById(R.id.btnEditar);
        //editar habilita la edicion
           /* btEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pvm.guardarDatos();

                }
            });*/
    }
}