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

public class HostalesFilters extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCES = "PREFERENCES";
    private Spinner spinner_ubicaciones;
    private Spinner spinner_abierto_todo_anio;
    private Spinner spinner_tipo;
    private CheckBox checkBoxInternet;
    private CheckBox checkBoxLavanderia;
    private CheckBox checkBoxCalefaccion;
    private CheckBox checkBoxRestaurante;
    private CheckBox checkBoxTelevision;
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

        setContentView(R.layout.activity_hostal_filters);
        // Activar flecha ir atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.filters);

        spinner_ubicaciones = findViewById(R.id.spinner_ubicaciones);
        spinner_abierto_todo_anio = findViewById(R.id.spinner_abierto);
        spinner_tipo = findViewById(R.id.spinner_tipo_hostal);
        boton_aceptar = findViewById(R.id.button_aceptar);
        boton_limpiar = findViewById(R.id.button_limpiar);
        checkBoxInternet = findViewById(R.id.checkBox_internet);
        checkBoxLavanderia = findViewById(R.id.checkBox_lavanderia);
        checkBoxCalefaccion = findViewById(R.id.checkBox_calefaccion);
        checkBoxRestaurante = findViewById(R.id.checkBox_restaurante);
        checkBoxTelevision = findViewById(R.id.checkBox_television);
        estrellas = findViewById(R.id.ratingBar_estrellas);

        prefsEditor = prefs.edit();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ubicaciones_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_ubicaciones.setAdapter(adapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.abierto_todo_anio_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_abierto_todo_anio.setAdapter(adapter2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.tipo2, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_tipo.setAdapter(adapter3);

        boton_aceptar.setOnClickListener(this);
        boton_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar vista
                spinner_ubicaciones.setSelection(3);
                spinner_abierto_todo_anio.setSelection(2);
                spinner_tipo.setSelection(2);
                checkBoxRestaurante.setChecked(false);
                checkBoxTelevision.setChecked(false);
                checkBoxInternet.setChecked(false);
                checkBoxLavanderia.setChecked(false);
                checkBoxCalefaccion.setChecked(false);
                estrellas.setRating(0);
                // Actualizar preferencias
                prefsEditor.putString("UBICACION_HOSTAL", getResources().getString(R.string.Any));
                prefsEditor.putString("ABIERTO_HOSTAL", getResources().getString(R.string.Any));
                prefsEditor.putString("TIPO_HOSTAL", getResources().getString(R.string.Any));
                prefsEditor.putBoolean("INTERNET_HOSTAL", false);
                prefsEditor.putBoolean("LAVANDERIA_HOSTAL", false);
                prefsEditor.putBoolean("CALEFACCION_HOSTAL", false);
                prefsEditor.putBoolean("TELEVISION_HOSTAL",false);
                prefsEditor.putBoolean("RESTAURANTE_HOSTAL",false);
                prefsEditor.commit();
            }
        });

        String ubicacion = prefs.getString("UBICACION_HOSTAL", getResources().getString(R.string.Any));
        if(ubicacion.equals("Asturias West") || ubicacion.equals("Occidente de Asturias"))
            spinner_ubicaciones.setSelection(0);
        else if(ubicacion.equals("Asturias East") || ubicacion.equals("Oriente de Asturias"))
            spinner_ubicaciones.setSelection(1);
        else if(ubicacion.equals("Asturias Center") || ubicacion.equals("Centro de Asturias"))
            spinner_ubicaciones.setSelection(2);
        else
            spinner_ubicaciones.setSelection(3);

        String abierto = prefs.getString("ABIERTO_HOSTAL", getResources().getString(R.string.Any));
        if(abierto.equals(getResources().getString(R.string.yes)))
            spinner_abierto_todo_anio.setSelection(0);
        else if(abierto.equals("No"))
            spinner_abierto_todo_anio.setSelection(1);
        else
            spinner_abierto_todo_anio.setSelection(2);

        String tipo = prefs.getString("TIPO_HOSTAL", getResources().getString(R.string.Any));
        if(tipo.equals("Hostel") || tipo.equals("Hostal"))
            spinner_tipo.setSelection(0);
        else if(tipo.equals("Pensión") || tipo.equals("Boarding House"))
            spinner_tipo.setSelection(1);
        else
            spinner_tipo.setSelection(2);

        estrellas.setRating(prefs.getFloat("ESTRELLAS_HOSTAL", 0));
        checkBoxInternet.setChecked(prefs.getBoolean("INTERNET_HOSTAL", false));
        checkBoxRestaurante.setChecked(prefs.getBoolean("RESTAURANTE_HOSTAL", false));
        checkBoxTelevision.setChecked(prefs.getBoolean("TELEVISION_HOSTAL", false));
        checkBoxCalefaccion.setChecked(prefs.getBoolean("CALEFACCION_HOSTAL", false));
        checkBoxLavanderia.setChecked(prefs.getBoolean("LAVANDERIA_HOSTAL", false));
    }

    @Override
    public void onClick(View v) {
        prefsEditor.putString("UBICACION_HOSTAL", spinner_ubicaciones.getSelectedItem().toString());
        prefsEditor.putString("ABIERTO_HOSTAL", spinner_abierto_todo_anio.getSelectedItem().toString());
        prefsEditor.putFloat("ESTRELLAS_HOSTAL", estrellas.getRating());
        prefsEditor.putString("TIPO_HOSTAL", spinner_tipo.getSelectedItem().toString());
        prefsEditor.putBoolean("INTERNET_HOSTAL", checkBoxInternet.isChecked());
        prefsEditor.putBoolean("LAVANDERIA_HOSTAL", checkBoxLavanderia.isChecked());
        prefsEditor.putBoolean("CALEFACCION_HOSTAL", checkBoxCalefaccion.isChecked());
        prefsEditor.putBoolean("RESTAURANTE_HOSTAL", checkBoxRestaurante.isChecked());
        prefsEditor.putBoolean("TELEVISION_HOSTAL", checkBoxTelevision.isChecked());
        prefsEditor.commit();
        Intent intent = new Intent(this, es.uniovi.Alojamientos.Presentacion.Activities.MainActivity.class);
        intent.putExtra("FRAGMENT", "HOSTALES");
        startActivity(intent);
    }
}
