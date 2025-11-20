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
import com.ledesmalillo.labodeguitaapp.databinding.FragmentUsuarioBinding;

public class UsuarioFragment extends Fragment {

    private UsuarioViewModel uvm;
    //private EditText etId, etNombre, etApellido, etMail, etDireccion, etTel;
    //private Button btGuardar, btEditar;
    private FragmentUsuarioBinding binding;

    public static UsuarioFragment newInstance() {
        return new UsuarioFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        uvm = new ViewModelProvider(this).get(UsuarioViewModel.class);
        binding = FragmentUsuarioBinding.inflate(inflater, container, false);

        //View root = inflater.inflate(R.layout.fragment_usuario, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
       /* uvm.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;*/
        uvm.getUsuario().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.etNombre.setText(usuario.getNombre());
                binding.etApellido.setText(usuario.getApellido());
                binding.etMail.setText(usuario.getEmail());
                binding.etTel.setText(usuario.getTelefono());
                binding.etDireccion.setText(usuario.getDireccion());

               /* Glide.with(getContext())
                        .load("http://192.168.0.104:5001/"+propietario.getAvatarUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //cada vez q cargues imagen de propietario queda en cache
                        .into(ivProp); */
            }
        });

        binding.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etNombre.setEnabled(true);
                binding.etApellido.setEnabled(true);
                binding.etMail.setEnabled(true);
                binding.etTel.setEnabled(true);
                binding.etDireccion.setEnabled(true);
                binding.btnEditar.setVisibility(View.GONE);
                binding.btnGuardar.setVisibility(View.VISIBLE);
            }
        });
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = new Usuario();
                usuario.setNombre(binding.etNombre.getText().toString());
                usuario.setApellido(binding.etApellido.getText().toString());
                usuario.setEmail(binding.etMail.getText().toString());
                usuario.setTelefono(binding.etTel.getText().toString());
                usuario.setDireccion(binding.etDireccion.getText().toString());
                binding.btnEditar.setVisibility(View.VISIBLE);
                binding.btnGuardar.setVisibility(View.GONE);
                uvm.editarDatos(usuario);

            }
        });


        uvm.obtenerUsuario();
        return binding.getRoot();

    }
    /*private void inicializarVista(View vistaPerfil) {
        etNombre = vistaPerfil.findViewById(R.id.etNombre);
        etApellido = vistaPerfil.findViewById(R.id.etApellido);
        etMail = vistaPerfil.findViewById(R.id.etMail);
        etTel = vistaPerfil.findViewById(R.id.etTel);
        etDireccion = vistaPerfil.findViewById(R.id.etDireccion);
        btGuardar = vistaPerfil.findViewById(R.id.btnEditar);
        //editar habilita la edicion
            btEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pvm.guardarDatos();

                }
            });
    */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
