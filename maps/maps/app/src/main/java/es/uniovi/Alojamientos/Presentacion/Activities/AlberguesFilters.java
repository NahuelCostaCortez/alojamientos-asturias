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
import android.widget.Spinner;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.ConfigurationCompat;
import es.uniovi.Alojamientos.R;

public class AlberguesFilters extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCES = "PREFERENCES";
    private Spinner spinner_ubicaciones;
    private Spinner spinner_tipo;
    private CheckBox checkBoxInternet;
    private CheckBox checkBoxChimenea;
    private CheckBox checkBoxCalefaccion;
    private CheckBox checkBoxCafeteria;
    private CheckBox checkBoxLavadora;
    private CheckBox checkBoxParking;
    private Button boton_aceptar;
    private Button boton_limpiar;

    // PREFERENCIAS
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

        setContentView(R.layout.activity_albergue_filters);
        // Activar flecha ir atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.filters);

        spinner_ubicaciones = findViewById(R.id.spinner_ubicaciones);
        spinner_tipo = findViewById(R.id.spinner_tipo);
        boton_aceptar = findViewById(R.id.button_aceptar);
        boton_limpiar = findViewById(R.id.button_limpiar);
        checkBoxInternet = findViewById(R.id.checkBox_wifi);
        checkBoxChimenea = findViewById(R.id.checkBox_chimenea);
        checkBoxCalefaccion = findViewById(R.id.checkBox_calefaccion);
        checkBoxCafeteria = findViewById(R.id.checkBox_cafeteria);
        checkBoxLavadora = findViewById(R.id.checkBox_lavadora);
        checkBoxParking = findViewById(R.id.checkBox_parking);

        prefsEditor = prefs.edit();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ubicaciones_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_ubicaciones.setAdapter(adapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.tipo, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_tipo.setAdapter(adapter2);

        boton_aceptar.setOnClickListener(this);
        boton_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar vista
                spinner_ubicaciones.setSelection(3);
                spinner_tipo.setSelection(2);
                checkBoxCafeteria.setChecked(false);
                checkBoxLavadora.setChecked(false);
                checkBoxInternet.setChecked(false);
                checkBoxParking.setChecked(false);
                checkBoxChimenea.setChecked(false);
                checkBoxCalefaccion.setChecked(false);
                // Actualizar preferencias
                prefsEditor.putString("UBICACION_ALBERGUE", getResources().getString(R.string.Any));
                prefsEditor.putString("TIPO_ALBERGUE", getResources().getString(R.string.Any));
                prefsEditor.putBoolean("INTERNET_ALBERGUE", false);
                prefsEditor.putBoolean("LAVADORA_ALBERGUE", false);
                prefsEditor.putBoolean("CHIMENEA_ALBERGUE", false);
                prefsEditor.putBoolean("CALEFACCION_ALBERGUE", false);
                prefsEditor.putBoolean("CAFETERIA_ALBERGUE",false);
                prefsEditor.putBoolean("PARKING_ALBERGUE",false);
                prefsEditor.commit();
            }
        });

        String ubicacion = prefs.getString("UBICACION_ALBERGUE", getResources().getString(R.string.Any));
        if(ubicacion.equals("Asturias West") || ubicacion.equals("Occidente de Asturias"))
            spinner_ubicaciones.setSelection(0);
        else if(ubicacion.equals("Asturias East") || ubicacion.equals("Oriente de Asturias"))
            spinner_ubicaciones.setSelection(1);
        else if(ubicacion.equals("Asturias Center") || ubicacion.equals("Centro de Asturias"))
            spinner_ubicaciones.setSelection(2);
        else
            spinner_ubicaciones.setSelection(3);

        String abierto = prefs.getString("TIPO_ALBERGUE", getResources().getString(R.string.Any));
        if(abierto.equals("Hostel") || abierto.equals("Hostel"))
            spinner_tipo.setSelection(1);
        else if(abierto.equals("Shelter") || abierto.equals("Refugio"))
            spinner_tipo.setSelection(0);
        else
            spinner_tipo.setSelection(2);


        checkBoxInternet.setChecked(prefs.getBoolean("INTERNET_ALBERGUE", false));
        checkBoxCafeteria.setChecked(prefs.getBoolean("CAFETERIA_ALBERGUE", false));
        checkBoxParking.setChecked(prefs.getBoolean("PARKING_ALBERGUE", false));
        checkBoxCalefaccion.setChecked(prefs.getBoolean("CALEFACCION_ALBERGUE", false));
        checkBoxLavadora.setChecked(prefs.getBoolean("LAVADORA_ALBERGUE", false));
        checkBoxChimenea.setChecked(prefs.getBoolean("CHIMENEA_ALBERGUE", false));
    }

    @Override
    public void onClick(View v) {
        prefsEditor.putString("UBICACION_ALBERGUE", spinner_ubicaciones.getSelectedItem().toString());
        prefsEditor.putString("TIPO_ALBERGUE", spinner_tipo.getSelectedItem().toString());
        prefsEditor.putBoolean("INTERNET_ALBERGUE", checkBoxInternet.isChecked());
        prefsEditor.putBoolean("CHIMENEA_ALBERGUE", checkBoxChimenea.isChecked());
        prefsEditor.putBoolean("CALEFACCION_ALBERGUE", checkBoxCalefaccion.isChecked());
        prefsEditor.putBoolean("CAFETERIA_ALBERGUE", checkBoxCafeteria.isChecked());
        prefsEditor.putBoolean("PARKING_ALBERGUE", checkBoxParking.isChecked());
        prefsEditor.putBoolean("LAVADORA_ALBERGUE", checkBoxLavadora.isChecked());
        prefsEditor.commit();
        Intent intent = new Intent(this, es.uniovi.Alojamientos.Presentacion.Activities.MainActivity.class);
        intent.putExtra("FRAGMENT", "ALBERGUES");
        startActivity(intent);
    }
}
