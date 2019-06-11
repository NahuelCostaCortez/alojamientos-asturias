package es.uniovi.Alojamientos.Presentacion.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosAlojamientoViewModel;
import es.uniovi.Alojamientos.R;

public class LoadingActivity extends AppCompatActivity {

    public static DatosAlojamientoViewModel alojamientosViewModel;
    private static final String PREFERENCES = "PREFERENCES";
    private SharedPreferences.Editor prefsEditor;

    private String connectionType = null;
    private  SharedPreferences prefs;

    // BroadcastReceiver para controlar las pérdida de conexión
    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           //System.out.println("NETWORK: Network connectivity change");
           ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           //String loading = prefs.getString("LOADING_ACTIVITY","");
           if(cm.getActiveNetworkInfo() != null) {
            //System.out.println("NETWORK: " + cm.getActiveNetworkInfo().getTypeName());
               if (!cm.getActiveNetworkInfo().getTypeName().equals(connectionType)) {
                   // No hay Internet
                   if(prefs.getString("LOADING_ACTIVITY","").equals("YES")) {
                       //prefsEditor.putString("LOADING_ACTIVITY", "NO");
                       //prefsEditor.commit();
                       Intent intentMain = new Intent(LoadingActivity.this, es.uniovi.Alojamientos.Presentacion.Activities.NoInternetActivity.class);
                       startActivity(intentMain);
                   }
                   else{
                       Toast.makeText(context, getString(R.string.connection_lost), Toast.LENGTH_LONG).show();
                   }
               }
           }
            if(cm.getActiveNetworkInfo() == null) {
                // No hay Internet
                if(prefs.getString("LOADING_ACTIVITY","").equals("YES")) {
                    //prefsEditor.putString("LOADING_ACTIVITY", "NO");
                    //prefsEditor.commit();
                    Intent intentMain = new Intent(LoadingActivity.this, es.uniovi.Alojamientos.Presentacion.Activities.NoInternetActivity.class);
                    startActivity(intentMain);
                }
                else{
                    Toast.makeText(context, getString(R.string.connection_lost), Toast.LENGTH_LONG).show();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);

        prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
        prefsEditor.putString("LOADING_ACTIVITY", "YES");
        prefsEditor.commit();

        if(prefs.getString("IDIOMA","").equals("SPANISH")){
            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = new Locale("es", "ES");
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        else if (prefs.getString("IDIOMA","").equals("ENGLISH")){
            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = new Locale("en", "GB");
            res.updateConfiguration(config, res.getDisplayMetrics());
        }

        if (prefs.getString("THEME", "").equals("LIGHT") ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (prefs.getString("THEME", "").equals("DARK")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo  != null)
            connectionType = networkInfo.getTypeName();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

    }

    private void getData() {
        // Si está online se bajan los datos
        if(isOnline()) {
            // Carga el último fragmento accedido
            String current_fragment = prefs.getString("CURRENT_FRAGMENT", "");
            final Intent intentMain = new Intent(LoadingActivity.this, es.uniovi.Alojamientos.Presentacion.Activities.MainActivity.class);
            alojamientosViewModel = ViewModelProviders.of(this).get(DatosAlojamientoViewModel.class);
            if (!current_fragment.equals("")) {
                intentMain.putExtra("FRAGMENT", current_fragment);
                // Cuando se terminan de descargar los datos se accede a la pantalla principal
                alojamientosViewModel.getAlojamientosList().observe(this, new Observer<List<Alojamiento>>() {
                    @Override
                    public void onChanged(List<Alojamiento> alojamientos) {
                        prefsEditor.putString("LOADING_ACTIVITY", "NO");
                        prefsEditor.commit();
                        startActivity(intentMain);
                    }
                });
            }
            // Carga el fragmento por defecto
            else {
                intentMain.putExtra("FRAGMENT", "CASAS");
                // Cuando se terminan de descargar los datos se accede a la pantalla principal
                alojamientosViewModel.getAlojamientosList().observe(this, new Observer<List<Alojamiento>>() {
                    @Override
                    public void onChanged(List<Alojamiento> alojamientos) {
                        prefsEditor.putString("LOADING_ACTIVITY", "NO");
                        prefsEditor.commit();
                        startActivity(intentMain);
                    }
                });
            }
        }
        else{
            Intent intent = new Intent(LoadingActivity.this, es.uniovi.Alojamientos.Presentacion.Activities.NoInternetActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Se llama en onResume porque si el usuario ya ha accedido a la actividad principal de la aplicación y le da al
        // botón de volver, volverá a esta actividad y se tendrían que volver a obtener los datos
        getData();
    }

    @Override
    public void onBackPressed() {
        // No hacer nada, obligar al usuario a que pulse el boton retry
    }

    // Comprueba que el teléfono está conectado a Internet
    boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
