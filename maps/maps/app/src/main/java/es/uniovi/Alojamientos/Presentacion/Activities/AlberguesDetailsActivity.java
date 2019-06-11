package es.uniovi.Alojamientos.Presentacion.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import es.uniovi.Alojamientos.Presentacion.Fragments.AlberguesDetailsFragment;
import es.uniovi.Alojamientos.R;

public class AlberguesDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albergue_details);

        // Activar flecha ir atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Existe el contenedor del fragmento?
        if (findViewById(R.id.fragment_container_albergue) != null) {

            // Si estamos restaurando desde un estado previo no hacemos nada
            if (savedInstanceState != null) {
                return;
            }

            // Crear el fragmento pasándole los parámetros necesarios
            AlberguesDetailsFragment fragment = new AlberguesDetailsFragment();

            // Añadir el fragmento al contenedor 'fragment_container'
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_albergue, fragment).commit();
        }
    }

}
