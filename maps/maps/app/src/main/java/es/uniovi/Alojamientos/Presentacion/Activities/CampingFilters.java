package es.uniovi.Alojamientos.Presentacion.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.ConfigurationCompat;
import es.uniovi.Alojamientos.R;

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
import android.widget.Spinner;

import java.util.Locale;

public class CampingFilters extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCES = "PREFERENCES";
    private Spinner spinner_ubicaciones;
    private Spinner spinner_abierto_todo_anio;
    private CheckBox checkBoxInternet;
    private CheckBox checkBoxLavanderia;
    private CheckBox checkBoxSupermercado;
    private CheckBox checkBoxCafeteria;
    private CheckBox checkBoxDesague;
    private Button boton_aceptar;
    private Button boton_limpiar;

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

        setContentView(R.layout.activity_camping_filters);
        // Activar flecha ir atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.filters);

        spinner_ubicaciones = findViewById(R.id.spinner_ubicaciones);
        spinner_abierto_todo_anio = findViewById(R.id.spinner_abierto);
        boton_aceptar = findViewById(R.id.button_aceptar);
        boton_limpiar = findViewById(R.id.button_limpiar);
        checkBoxInternet = findViewById(R.id.checkBox_internet);
        checkBoxLavanderia = findViewById(R.id.checkBox_lavanderia);
        checkBoxSupermercado = findViewById(R.id.checkBox_supermercado);
        checkBoxCafeteria = findViewById(R.id.checkBox_cafeteria);
        checkBoxDesague = findViewById(R.id.checkBox_desague);

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

        boton_aceptar.setOnClickListener(this);
        boton_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar vista
                spinner_ubicaciones.setSelection(3);
                spinner_abierto_todo_anio.setSelection(2);
                checkBoxCafeteria.setChecked(false);
                checkBoxDesague.setChecked(false);
                checkBoxInternet.setChecked(false);
                checkBoxLavanderia.setChecked(false);
                checkBoxSupermercado.setChecked(false);
                // Actualizar preferencias
                prefsEditor.putString("UBICACION_CAMPING", getResources().getString(R.string.Any));
                prefsEditor.putString("ABIERTO_CAMPING", getResources().getString(R.string.Any));
                prefsEditor.putBoolean("INTERNET_CAMPING", false);
                prefsEditor.putBoolean("LAVANDERIA_CAMPING", false);
                prefsEditor.putBoolean("SUPERMERCADO_CAMPING", false);
                prefsEditor.putBoolean("CAFETERIA_CAMPING",false);
                prefsEditor.putBoolean("PARKING_CAMPING",false);
                prefsEditor.commit();
            }
        });

        String ubicacion = prefs.getString("UBICACION_CAMPING", getResources().getString(R.string.Any));
        if(ubicacion.equals("Asturias West") || ubicacion.equals("Occidente de Asturias"))
            spinner_ubicaciones.setSelection(0);
        else if(ubicacion.equals("Asturias East") || ubicacion.equals("Oriente de Asturias"))
            spinner_ubicaciones.setSelection(1);
        else if(ubicacion.equals("Asturias Center") || ubicacion.equals("Centro de Asturias"))
            spinner_ubicaciones.setSelection(2);
        else
            spinner_ubicaciones.setSelection(3);

        String abierto = prefs.getString("ABIERTO_CAMPING", getResources().getString(R.string.Any));
        if(abierto.equals(getResources().getString(R.string.yes)))
            spinner_abierto_todo_anio.setSelection(0);
        else if(abierto.equals("No"))
            spinner_abierto_todo_anio.setSelection(1);
        else
            spinner_abierto_todo_anio.setSelection(2);


        checkBoxInternet.setChecked(prefs.getBoolean("INTERNET_CAMPING", false));
        checkBoxCafeteria.setChecked(prefs.getBoolean("CAFETERIA_CAMPING", false));
        checkBoxDesague.setChecked(prefs.getBoolean("PARKING_CAMPING", false));
        checkBoxSupermercado.setChecked(prefs.getBoolean("SUPERMERCADO_CAMPING", false));
        checkBoxLavanderia.setChecked(prefs.getBoolean("LAVANDERIA_CAMPING", false));
    }

    @Override
    public void onClick(View v) {
        prefsEditor.putString("UBICACION_CAMPING", spinner_ubicaciones.getSelectedItem().toString());
        prefsEditor.putString("ABIERTO_CAMPING", spinner_abierto_todo_anio.getSelectedItem().toString());
        prefsEditor.putBoolean("INTERNET_CAMPING", checkBoxInternet.isChecked());
        prefsEditor.putBoolean("LAVANDERIA_CAMPING", checkBoxLavanderia.isChecked());
        prefsEditor.putBoolean("SUPERMERCADO_CAMPING", checkBoxSupermercado.isChecked());
        prefsEditor.putBoolean("CAFETERIA_CAMPING", checkBoxCafeteria.isChecked());
        prefsEditor.putBoolean("PARKING_CAMPING", checkBoxDesague.isChecked());
        prefsEditor.commit();
        Intent intent = new Intent(this, es.uniovi.Alojamientos.Presentacion.Activities.MainActivity.class);
        intent.putExtra("FRAGMENT", "CAMPINGS");
        startActivity(intent);
    }
}
