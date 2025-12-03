package com.ledesmalillo.labodeguitaapp.ui.usuario;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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

    @SuppressLint("WrongConstant")
    //SE AGREGA REQUIREACTIVITY() en new ViewModelProvider() xq este fragment esta siendo compartido.
    //(RegistroActivity y usuarioFragment lo utilizan.)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        uvm = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class);
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
        uvm.getEstado().observe(getViewLifecycleOwner(), estado -> {
            Usuario usuario = estado.getUsuario();
            Log.d("UsuarioFragment", "Usuario: " + usuario);
            Log.d("isCamposHabilitados", String.valueOf(estado.isCamposHabilitados()));
            binding.etNombre.setText(usuario.getNombre());
            binding.etApellido.setText(usuario.getApellido());
            binding.etMail.setText(usuario.getEmail());
            binding.etTel.setText(usuario.getTelefono());
            binding.etDireccion.setText(usuario.getDireccion());
            binding.etNombre.setEnabled(estado.isCamposHabilitados());
            binding.etApellido.setEnabled(estado.isCamposHabilitados());
            binding.etMail.setEnabled(estado.isCamposHabilitados());
            binding.etTel.setEnabled(estado.isCamposHabilitados());
            binding.etDireccion.setEnabled(estado.isCamposHabilitados());
            binding.btnEditar.setVisibility(estado.getVisibilidadBotonEditar());
            binding.btnGuardar.setVisibility(estado.getVisibilidadBotonGuardar());
               /* Glide.with(getContext())
                        .load("http://192.168.0.104:5001/"+propietario.getAvatarUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //cada vez q cargues imagen de propietario queda en cache
                        .into(ivProp); */

        });
        uvm.getEstadoNuevo().observe(getViewLifecycleOwner(), estadoNuevo -> {
            Usuario usuario = estadoNuevo.getUsuario();
            Log.d("UsuarioFragment", "Usuario: " + usuario);
            Log.d("isCamposHabilitados", String.valueOf(estadoNuevo.isCamposHabilitados()));
            binding.etNombre.setText(usuario.getNombre());
            binding.etApellido.setText(usuario.getApellido());
            binding.etMail.setText(usuario.getEmail());
            binding.etTel.setText(usuario.getTelefono());
            binding.etDireccion.setText(usuario.getDireccion());
            binding.etNombre.setEnabled(estadoNuevo.isCamposHabilitados());
            binding.etApellido.setEnabled(estadoNuevo.isCamposHabilitados());
            binding.etMail.setEnabled(estadoNuevo.isCamposHabilitados());
            binding.etTel.setEnabled(estadoNuevo.isCamposHabilitados());
            binding.etDireccion.setEnabled(estadoNuevo.isCamposHabilitados());
            binding.btnEditar.setVisibility(estadoNuevo.getVisibilidadBotonEditar());
            binding.btnGuardar.setVisibility(estadoNuevo.getVisibilidadBotonGuardar());
               /* Glide.with(getContext())
                        .load("http://192.168.0.104:5001/"+propietario.getAvatarUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //cada vez q cargues imagen de propietario queda en cache
                        .into(ivProp); */

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
                //estos campos no son requeridos pero hay q mandarlos igual en vacio, sino falla el model state
                usuario.setClave(binding.etClave.getText().toString());
                usuario.setEstado(true);
                binding.btnEditar.setVisibility(View.VISIBLE);
                binding.btnGuardar.setVisibility(View.GONE);
                uvm.guardarCambios(usuario);

            }
        });


        //uvm.cargarUsuarioExistente();
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
