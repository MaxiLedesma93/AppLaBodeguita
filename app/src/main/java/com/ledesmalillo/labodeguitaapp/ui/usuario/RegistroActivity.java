package com.ledesmalillo.labodeguitaapp.ui.usuario;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.ledesmalillo.labodeguitaapp.Modelos.Usuario;
import com.ledesmalillo.labodeguitaapp.R;
import com.ledesmalillo.labodeguitaapp.databinding.FragmentUsuarioBinding;

public class RegistroActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // 4. Aloja el UsuarioFragment en el contenedor (solo si es la primera vez que se crea)
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view_usuario, UsuarioFragment.class, null)
                    .commit();
        }

        // 5. (Opcional, pero recomendado por la pregunta anterior)
        // Comunícale al ViewModel que estamos en modo "Creación"
        UsuarioViewModel viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        // Asumiendo que sigues el patrón avanzado de la pregunta anterior
        Log.d("RegistroActivity", "Creando el ViewModel");

        viewModel.prepararNuevoUsuario();
    }



}