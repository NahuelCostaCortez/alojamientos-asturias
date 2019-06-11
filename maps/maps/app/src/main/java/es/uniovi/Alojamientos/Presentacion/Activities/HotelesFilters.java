package es.uniovi.Alojamientos.Presentacion.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.ConfigurationCompat;
import es.uniovi.Alojamientos.R;

public class HotelesFilters extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCES = "PREFERENCES";
    private Spinner spinner_ubicaciones;
    private CheckBox checkBoxWifi;
    private CheckBox checkBoxAire;
    private CheckBox checkBoxPiscina;
    private CheckBox checkBoxCafeteria;
    private CheckBox checkBoxAparcamiento;
    private CheckBox checkBoxTelevision;
    private CheckBox checkBoxSecador;
    private CheckBox checkBoxCajaFuerte;
    private Button boton_aceptar;
    private Button boton_limpiar;
    private RatingBar estrellas;

    // PREFERNCIAS
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        if (prefs.getString("THEME", "").equals("LIGHT") ) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (prefs.getString("THEME", "").equals("DARK")) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if(prefs.getString("IDIOMA","").equals("SPANISH")){
            //listPreference.setSummary("Español");
            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = new Locale("es", "ES");
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        else if (prefs.getString("IDIOMA","").equals("ENGLISH")){
            //listPreference.setSummary("English");
            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = new Locale("en", "GB");
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        else{
            Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }

        setContentView(R.layout.activity_hotel_filters);
        // Activar flecha ir atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.filters);

        spinner_ubicaciones = findViewById(R.id.spinner_ubicaciones);
        boton_aceptar = findViewById(R.id.button_aceptar);
        boton_limpiar = findViewById(R.id.button_limpiar);
        checkBoxWifi = findViewById(R.id.checkBox_wifi);
        checkBoxAire = findViewById(R.id.checkBox_aire);
        checkBoxPiscina = findViewById(R.id.checkBox_piscina);
        checkBoxCafeteria = findViewById(R.id.checkBox_cafeteria);
        checkBoxAparcamiento = findViewById(R.id.checkBox_parking);
        checkBoxTelevision = findViewById(R.id.checkBox_television);
        checkBoxSecador = findViewById(R.id.checkBox_secador);
        checkBoxCajaFuerte = findViewById(R.id.checkBox_caja_fuerte);
        estrellas = findViewById(R.id.ratingBar_estrellas);
        //estrellas.setRating(0);

        prefsEditor = prefs.edit();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ubicaciones_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_ubicaciones.setAdapter(adapter);

        boton_aceptar.setOnClickListener(this);
        boton_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar vista
                spinner_ubicaciones.setSelection(3);
                checkBoxWifi.setChecked(false);
                checkBoxAire.setChecked(false);
                checkBoxPiscina.setChecked(false);
                checkBoxCafeteria.setChecked(false);
                checkBoxAparcamiento.setChecked(false);
                checkBoxTelevision.setChecked(false);
                checkBoxSecador.setChecked(false);
                checkBoxCajaFuerte.setChecked(false);
                estrellas.setRating(0);
                // Actualizar preferencias
                prefsEditor.putString("UBICACION_HOTEL", getResources().getString(R.string.Any));
                prefsEditor.putInt("MAX_PRECIO_HOTEL", 400);
                prefsEditor.putBoolean("DESAYUNO_HOTEL", false);
                prefsEditor.putBoolean("WIFI_HOTEL", false);
                prefsEditor.putBoolean("AIRE_HOTEL", false);
                prefsEditor.putBoolean("PISCINA_HOTEL",false);
                prefsEditor.putBoolean("CAFETERIA_HOTEL",false);
                prefsEditor.putBoolean("APARCAMIENTO_HOTEL",false);
                prefsEditor.putBoolean("TELEVISION_HOTEL",false);
                prefsEditor.putBoolean("SECADOR_HOTEL",false);
                prefsEditor.putBoolean("CAJA_HOTEL",false);

                prefsEditor.commit();
            }
        });

        String ubicacion = prefs.getString("UBICACION_HOTEL", getResources().getString(R.string.Any));
        if(ubicacion.equals("Asturias West") || ubicacion.equals("Occidente de Asturias"))
            spinner_ubicaciones.setSelection(0);
        else if(ubicacion.equals("Asturias East") || ubicacion.equals("Oriente de Asturias"))
            spinner_ubicaciones.setSelection(1);
        else if(ubicacion.equals("Asturias Center") || ubicacion.equals("Centro de Asturias"))
            spinner_ubicaciones.setSelection(2);
        else
            spinner_ubicaciones.setSelection(3);

        estrellas.setRating(prefs.getFloat("ESTRELLAS_HOTEL", 0));
        checkBoxWifi.setChecked(prefs.getBoolean("WIFI_HOTEL", false));
        checkBoxAire.setChecked(prefs.getBoolean("AIRE_HOTEL", false));
        checkBoxPiscina.setChecked(prefs.getBoolean("PISCINA_HOTEL", false));
        checkBoxCafeteria.setChecked(prefs.getBoolean("CAFETERIA_HOTEL", false));
        checkBoxAparcamiento.setChecked(prefs.getBoolean("APARCAMIENTO_HOTEL", false));
        checkBoxTelevision.setChecked(prefs.getBoolean("TELEVISION_HOTEL", false));
        checkBoxSecador.setChecked(prefs.getBoolean("SECADOR_HOTEL", false));
        checkBoxCajaFuerte.setChecked(prefs.getBoolean("CAJA_HOTEL", false));
    }

    @Override
    public void onClick(View v) {
        prefsEditor.putString("UBICACION_HOTEL", spinner_ubicaciones.getSelectedItem().toString());
        prefsEditor.putFloat("ESTRELLAS_HOTEL", estrellas.getRating());
        prefsEditor.putBoolean("WIFI_HOTEL", checkBoxWifi.isChecked());
        prefsEditor.putBoolean("AIRE_HOTEL", checkBoxAire.isChecked());
        prefsEditor.putBoolean("PISCINA_HOTEL", checkBoxPiscina.isChecked());
        prefsEditor.putBoolean("CAFETERIA_HOTEL", checkBoxCafeteria.isChecked());
        prefsEditor.putBoolean("APARCAMIENTO_HOTEL", checkBoxAparcamiento.isChecked());
        prefsEditor.putBoolean("TELEVISION_HOTEL", checkBoxTelevision.isChecked());
        prefsEditor.putBoolean("SECADOR_HOTEL", checkBoxSecador.isChecked());
        prefsEditor.putBoolean("CAJA_HOTEL", checkBoxCajaFuerte.isChecked());
        prefsEditor.commit();
        Intent intent = new Intent(this, es.uniovi.Alojamientos.Presentacion.Activities.MainActivity.class);
        intent.putExtra("FRAGMENT", "HOTELES");
        startActivity(intent);
    }
}
