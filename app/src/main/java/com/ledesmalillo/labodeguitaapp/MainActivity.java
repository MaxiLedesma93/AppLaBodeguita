package com.ledesmalillo.labodeguitaapp;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.ledesmalillo.labodeguitaapp.Modelos.Usuario;
import com.ledesmalillo.labodeguitaapp.databinding.ActivityMainBinding;
import com.ledesmalillo.labodeguitaapp.ui.carrito.CarritoViewModel;
import com.ledesmalillo.labodeguitaapp.ui.usuario.UsuarioViewModel;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private CarritoViewModel carritoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        iniciarHeader(navigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph())
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        // Obtienes una instancia del ViewModel cuyo ciclo de vida está atado a la Activity
        UsuarioViewModel usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        carritoViewModel = new ViewModelProvider(this).get(CarritoViewModel.class);

        // Le ordenas al ViewModel que cargue los datos del usuario.
        // Esto emitirá un estado que el UsuarioFragment (cuando esté visible) recibirá.
        usuarioViewModel.cargarUsuarioExistente();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.main_nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void iniciarHeader(NavigationView navigationView){
        View header = navigationView.getHeaderView(0);

        TextView nombre = header.findViewById(R.id.tvNombreHeader);
        TextView direccion = header.findViewById(R.id.tvDireccionHeader);
        TextView telefono = header.findViewById(R.id.tvTelefonoHeader);
        //ImageView imageView = header.findViewById(R.id.imageView);

        Usuario u = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            u = this.getIntent().getBundleExtra("usuario").getSerializable("usuario", Usuario.class);
        }
        nombre.setText(u.getNombre()+ " " + u.getApellido());
        direccion.setText(u.getEmail()+"");
        telefono.setText(u.getTelefono());
    }
}