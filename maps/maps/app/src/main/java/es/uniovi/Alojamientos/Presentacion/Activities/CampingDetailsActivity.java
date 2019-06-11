package es.uniovi.Alojamientos.Presentacion.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import es.uniovi.Alojamientos.Presentacion.Fragments.CampingDetailsFragment;
import es.uniovi.Alojamientos.R;

public class CampingDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camping_details);

        // Activar flecha ir atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Intent intent = getIntent();
        //Alojamiento camping = intent.getParcelableExtra("CAMPING");

        // Existe el contenedor del fragmento?
        if (findViewById(R.id.fragment_container) != null) {

            // Si estamos restaurando desde un estado previo no hacemos nada
            if (savedInstanceState != null) {
                return;
            }

            // Crear el fragmento pasándole los parámetros necesarios
            CampingDetailsFragment fragment = new CampingDetailsFragment();

            // Añadir el fragmento al contenedor 'fragment_container'
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

}
