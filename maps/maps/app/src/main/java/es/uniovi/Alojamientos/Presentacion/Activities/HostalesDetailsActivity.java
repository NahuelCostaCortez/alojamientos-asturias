package es.uniovi.Alojamientos.Presentacion.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import es.uniovi.Alojamientos.Presentacion.Fragments.HostalesDetailsFragment;
import es.uniovi.Alojamientos.R;

public class HostalesDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostal_details);

        // Activar flecha ir atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Existe el contenedor del fragmento?
        if (findViewById(R.id.fragment_container_hostal) != null) {

            // Si estamos restaurando desde un estado previo no hacemos nada
            if (savedInstanceState != null) {
                return;
            }

            // Crear el fragmento pasándole los parámetros necesarios
            HostalesDetailsFragment fragment = new HostalesDetailsFragment();

            // Añadir el fragmento al contenedor 'fragment_container'
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_hostal, fragment).commit();
        }
    }

}
