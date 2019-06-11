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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.ConfigurationCompat;
import es.uniovi.Alojamientos.R;

public class ApartamentosFilters extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCES = "PREFERENCES";
    private Spinner spinner_ubicaciones;
    private CheckBox checkBoxDesayuno;
    private CheckBox checkBoxWifi;
    private CheckBox checkBoxChimenea;
    private CheckBox checkBoxCalefaccion;
    private CheckBox checkBoxLavadora;
    private CheckBox checkBoxAparcamiento;
    private CheckBox checkBoxCocina;
    private CheckBox checkBoxLavavajillas;
    private CheckBox checkBoxLimpieza;
    private Button boton_aceptar;
    private Button boton_limpiar;
    private SeekBar precio;
    private TextView precio_seekbar;

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

        setContentView(R.layout.activity_apartamento_filters);
        // Activar flecha ir atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.filters);

        spinner_ubicaciones = findViewById(R.id.spinner_ubicaciones);
        boton_aceptar = findViewById(R.id.button_aceptar);
        boton_limpiar = findViewById(R.id.button_limpiar);
        checkBoxWifi = findViewById(R.id.checkBox_wifi);
        checkBoxChimenea = findViewById(R.id.checkBox_chimenea);
        checkBoxCalefaccion = findViewById(R.id.checkBox_calefaccion);
        checkBoxLavadora = findViewById(R.id.checkBox_lavadora);
        checkBoxAparcamiento = findViewById(R.id.checkBox_parking);
        checkBoxCocina = findViewById(R.id.checkBox_cocina);
        checkBoxLavavajillas = findViewById(R.id.checkBox_lavavajillas);
        checkBoxLimpieza = findViewById(R.id.checkBox_servicio_limpieza);
        precio = findViewById(R.id.seekBar);
        precio_seekbar = findViewById(R.id.precio);
        precio.setMax(400);
        precio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                precio_seekbar.setText("Max: "+ progress + "€");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
                precio_seekbar.setText("400€");
                precio.setProgress(400);
                checkBoxWifi.setChecked(false);
                checkBoxChimenea.setChecked(false);
                checkBoxCalefaccion.setChecked(false);
                checkBoxLavadora.setChecked(false);
                checkBoxAparcamiento.setChecked(false);
                checkBoxCocina.setChecked(false);
                checkBoxLavavajillas.setChecked(false);
                checkBoxLimpieza.setChecked(false);
                // Actualizar preferencias
                prefsEditor.putString("UBICACION_APARTAMENTO", getResources().getString(R.string.Any));
                prefsEditor.putInt("MAX_PRECIO_APARTAMENTO", 400);
                //prefsEditor.putBoolean("DESAYUNO_APARTAMENTO", false);
                prefsEditor.putBoolean("WIFI_APARTAMENTO", false);
                prefsEditor.putBoolean("CHIMENEA_APARTAMENTO", false);
                prefsEditor.putBoolean("CALEFACCION_APARTAMENTO",false);
                prefsEditor.putBoolean("LAVADORA_APARTAMENTO",false);
                prefsEditor.putBoolean("APARCAMIENTO_APARTAMENTO",false);
                prefsEditor.putBoolean("COCINA_APARTAMENTO",false);
                prefsEditor.putBoolean("LAVAVAJILLAS_APARTAMENTO",false);
                prefsEditor.putBoolean("LIMPIEZA_APARTAMENTO",false);

                prefsEditor.commit();
            }
        });

        String ubicacion = prefs.getString("UBICACION_APARTAMENTO", getResources().getString(R.string.Any));
        if(ubicacion.equals("Asturias West") || ubicacion.equals("Occidente de Asturias"))
            spinner_ubicaciones.setSelection(0);
        else if(ubicacion.equals("Asturias East") || ubicacion.equals("Oriente de Asturias"))
            spinner_ubicaciones.setSelection(1);
        else if(ubicacion.equals("Asturias Center") || ubicacion.equals("Centro de Asturias"))
            spinner_ubicaciones.setSelection(2);
        else
            spinner_ubicaciones.setSelection(3);

        precio.setProgress(prefs.getInt("MAX_PRECIO_APARTAMENTO", 400));
        precio_seekbar.setText(String.valueOf(precio.getProgress()));
        checkBoxWifi.setChecked(prefs.getBoolean("WIFI_APARTAMENTO", false));
        checkBoxChimenea.setChecked(prefs.getBoolean("CHIMENEA_APARTAMENTO", false));
        checkBoxCalefaccion.setChecked(prefs.getBoolean("CALEFACCION_APARTAMENTO", false));
        checkBoxLavadora.setChecked(prefs.getBoolean("LAVADORA_APARTAMENTO", false));
        checkBoxAparcamiento.setChecked(prefs.getBoolean("APARCAMIENTO_APARTAMENTO", false));
        checkBoxCocina.setChecked(prefs.getBoolean("COCINA_APARTAMENTO", false));
        checkBoxLavavajillas.setChecked(prefs.getBoolean("LAVAVAJILLAS_APARTAMENTO", false));
        checkBoxLimpieza.setChecked(prefs.getBoolean("LIMPIEZA_APARTAMENTO", false));
    }

    @Override
    public void onClick(View v) {
        prefsEditor.putString("UBICACION_APARTAMENTO", spinner_ubicaciones.getSelectedItem().toString());
        prefsEditor.putInt("MAX_PRECIO_APARTAMENTO", precio.getProgress());
        prefsEditor.putBoolean("WIFI_APARTAMENTO", checkBoxWifi.isChecked());
        prefsEditor.putBoolean("CHIMENEA_APARTAMENTO", checkBoxChimenea.isChecked());
        prefsEditor.putBoolean("CALEFACCION_APARTAMENTO", checkBoxCalefaccion.isChecked());
        prefsEditor.putBoolean("LAVADORA_APARTAMENTO", checkBoxLavadora.isChecked());
        prefsEditor.putBoolean("APARCAMIENTO_APARTAMENTO", checkBoxAparcamiento.isChecked());
        prefsEditor.putBoolean("COCINA_APARTAMENTO", checkBoxCocina.isChecked());
        prefsEditor.putBoolean("LAVAVAJILLAS_APARTAMENTO", checkBoxLavavajillas.isChecked());
        prefsEditor.putBoolean("LIMPIEZA_APARTAMENTO", checkBoxLimpieza.isChecked());
        prefsEditor.commit();
        Intent intent = new Intent(this, es.uniovi.Alojamientos.Presentacion.Activities.MainActivity.class);
        intent.putExtra("FRAGMENT", "APARTAMENTOS");
        startActivity(intent);
    }
}
