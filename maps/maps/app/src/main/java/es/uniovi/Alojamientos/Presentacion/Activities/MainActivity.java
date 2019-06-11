package es.uniovi.Alojamientos.Presentacion.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Presentacion.Fragments.acerca_de_fragment;
import es.uniovi.Alojamientos.Presentacion.Fragments.albergues_fragment;
import es.uniovi.Alojamientos.Presentacion.Fragments.apartamentos_fragment;
import es.uniovi.Alojamientos.Presentacion.Fragments.campings_fragment;
import es.uniovi.Alojamientos.Presentacion.Fragments.casas_fragment;
import es.uniovi.Alojamientos.Presentacion.Fragments.favoritos_fragment;
import es.uniovi.Alojamientos.Presentacion.Fragments.hostales_fragment;
import es.uniovi.Alojamientos.Presentacion.Fragments.hoteles_fragment;
import es.uniovi.Alojamientos.Presentacion.Fragments.prefs_fragment;
import es.uniovi.Alojamientos.Negocio.UpdateDatosService;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosAlojamientoViewModel;
import es.uniovi.Alojamientos.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
campings_fragment.Callbacks, favoritos_fragment.Callbacks, casas_fragment.Callbacks, apartamentos_fragment.Callbacks,
hostales_fragment.Callbacks, hoteles_fragment.Callbacks, albergues_fragment.Callbacks{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private static final String PREFERENCES = "PREFERENCES";
    private DatosAlojamientoViewModel alojamientosViewModel;

    public enum AppStatus {
        ERROR, UPDATED, DOWNLOADING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();

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
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (prefs.getString("THEME", "").equals("DARK")) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navView);
        drawerLayout = findViewById(R.id.drawer_layout);

        alojamientosViewModel = ViewModelProviders.of(this).get(DatosAlojamientoViewModel.class);

        setToolbar();

        // Se accede al ultimo fragmento en el que estuvo el usuario
        Intent intent = getIntent();
        if(intent!=null && savedInstanceState==null){
            String fragmento = intent.getStringExtra("FRAGMENT");
            Fragment fragment;
            switch (fragmento){
                case "CAMPINGS":
                    navigationView.setCheckedItem(R.id.menu_camping);
                    fragment =  new campings_fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "CAMPINGS");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
                case "CASAS":
                    navigationView.setCheckedItem(R.id.menu_casas);
                    fragment =  new casas_fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "CASAS");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
                case "APARTAMENTOS":
                    navigationView.setCheckedItem(R.id.menu_apartamento);
                    fragment =  new apartamentos_fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "APARTAMENTOS");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
                case "HOTELES":
                    navigationView.setCheckedItem(R.id.menu_hotel);
                    fragment =  new hoteles_fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "HOTELES");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
                case "ALBERGUES":
                    navigationView.setCheckedItem(R.id.menu_albergue);
                    fragment =  new albergues_fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "ALBERGUES");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
                case "HOSTALES":
                    navigationView.setCheckedItem(R.id.menu_hostal);
                    fragment =  new hostales_fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "HOSTALES");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
                case "FAVORITOS":
                    navigationView.setCheckedItem(R.id.menu_favoritos);
                    fragment =  new favoritos_fragment();
                    setTitle(getString(R.string.favs));
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "FAVORITOS");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
                case "INFO":
                    navigationView.setCheckedItem(R.id.menu_acerca_de);
                    fragment =  new acerca_de_fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "INFO");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
                case "CONFIGURATION":
                    navigationView.setCheckedItem(R.id.menu_configuracion);
                    fragment =  new prefs_fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    prefsEditor.putString("CURRENT_FRAGMENT", "CONFIGURATION");
                    prefsEditor.commit();
                    drawerLayout.closeDrawers();
                    break;
            }
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        // No hacer nada, la actividad principal no tiene memoria para cambiar entre fragmentos pasados
        navigationView.setCheckedItem(R.id.menu_casas);
        Fragment fragment =  new casas_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        prefsEditor.putString("CURRENT_FRAGMENT", "CASAS");
        prefsEditor.commit();
        drawerLayout.closeDrawers();
    }

    // Pone el título de la app en el toolbar
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Selecciona el fragmento a mostrar por defecto
    private void setFragmentByDefault(){
        changeFragment(new casas_fragment(),navigationView.getMenu().getItem(0));
    }

    // Cambia el fragmento que se está viendo en pantalla
    private void changeFragment(Fragment fragment, MenuItem item){

        // POR HACER - SI EL FRAGMENTO ES EL MAPA HAY QUE PASARLE EL ARRAY CON LAS POSICIONES DE CADA LUGAR

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        // Se deja seleccionada esa opción
        item.setChecked(true);
        // Se pone como título del ActionBar el del elemento que se selecciona
        //getSupportActionBar().setTitle(item.getTitle());
        // Se cierra el drawer para dejar el foco en la opcion que se elige
        drawerLayout.closeDrawers();
    }

    // Para poder abrir el navvar con el icono hamburguesa
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                // Abrir menú lateral - le decimos que lo abra desde el START, que es el NavigationView de la izquierda
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.menu_sincronizar:
                Toast.makeText(this, getString(R.string.synchronize), Toast.LENGTH_SHORT).show();
                Intent intentServicio = new Intent(getApplicationContext(), UpdateDatosService.class);
                startService(intentServicio);
                navigationView.setCheckedItem(R.id.menu_casas);
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.menu_favoritos:
                fragment = new favoritos_fragment();
                fragmentTransaction = true;
                setTitle(R.string.favs);
                prefsEditor.putString("CURRENT_FRAGMENT", "FAVORITOS");
                prefsEditor.commit();
                break;
            case R.id.menu_casas:
                fragment = new casas_fragment();
                fragmentTransaction = true;
                prefsEditor.putString("CURRENT_FRAGMENT", "CASAS");
                prefsEditor.commit();
                break;
            case R.id.menu_apartamento:
                fragment = new apartamentos_fragment();
                fragmentTransaction = true;
                prefsEditor.putString("CURRENT_FRAGMENT", "APARTAMENTOS");
                prefsEditor.commit();
                break;
            case R.id.menu_hotel:
                fragment = new hoteles_fragment();
                fragmentTransaction = true;
                prefsEditor.putString("CURRENT_FRAGMENT", "HOTELES");
                prefsEditor.commit();
                break;
            case R.id.menu_albergue:
                fragment = new albergues_fragment();
                fragmentTransaction = true;
                prefsEditor.putString("CURRENT_FRAGMENT", "ALBERGUES");
                prefsEditor.commit();
                break;
            case R.id.menu_camping:
                fragment = new campings_fragment();
                fragmentTransaction = true;
                prefsEditor.putString("CURRENT_FRAGMENT", "CAMPINGS");
                prefsEditor.commit();
                break;
            case R.id.menu_hostal:
                fragment = new hostales_fragment();
                fragmentTransaction = true;
                prefsEditor.putString("CURRENT_FRAGMENT", "HOSTALES");
                prefsEditor.commit();
                break;
            case R.id.menu_acerca_de:
                fragment = new acerca_de_fragment();
                fragmentTransaction = true;
                setTitle(R.string.about);
                prefsEditor.putString("CURRENT_FRAGMENT", "INFO");
                prefsEditor.commit();
                break;
            case R.id.menu_configuracion:
                fragment = new prefs_fragment();
                fragmentTransaction = true;
                prefsEditor.putString("CURRENT_FRAGMENT", "CONFIGURATION");
                prefsEditor.commit();
        }

        if(fragmentTransaction){
            changeFragment(fragment,menuItem);
        }

        return true;
    }

    @Override
    public void onCampingSelected(Alojamiento alojamiento) {
        Intent intent = new Intent(this, CampingDetailsActivity.class);
        alojamientosViewModel.setAlojamiento(alojamiento);
        startActivity(intent);
    }

    @Override
    public void onCasasSelected(Alojamiento alojamiento) {
        alojamientosViewModel.setAlojamiento(alojamiento);
        Intent intent = new Intent(this, CasasDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAlbergueSelected(Alojamiento alojamiento) {
        Intent intent = new Intent(this, AlberguesDetailsActivity.class);
        alojamientosViewModel.setAlojamiento(alojamiento);
        startActivity(intent);
    }

    @Override
    public void onApartamentosSelected(Alojamiento alojamiento) {
        Intent intent = new Intent(this, ApartamentosDetailsActivity.class);
        alojamientosViewModel.setAlojamiento(alojamiento);
        startActivity(intent);
    }

    @Override
    public void onHostalSelected(Alojamiento alojamiento) {
        Intent intent = new Intent(this, HostalesDetailsActivity.class);
        alojamientosViewModel.setAlojamiento(alojamiento);
        startActivity(intent);
    }

    @Override
    public void onHotelesSelected(Alojamiento alojamiento) {
        Intent intent = new Intent(this, HotelesDetailsActivity.class);
        alojamientosViewModel.setAlojamiento(alojamiento);
        startActivity(intent);
    }

    @Override
    public void onFavoritoSelected(Alojamiento alojamiento, String tipo) {
        Intent intent;
        switch (tipo){
            case "ALBERGUE":
                intent = new Intent(this, AlberguesDetailsActivity.class);
                alojamientosViewModel.setAlojamiento(alojamiento);
                startActivity(intent);
                break;
            case "CASA":
                intent = new Intent(this, CasasDetailsActivity.class);
                alojamientosViewModel.setAlojamiento(alojamiento);
                startActivity(intent);
                break;
            case "APARTAMENTO":
                intent = new Intent(this, ApartamentosDetailsActivity.class);
                alojamientosViewModel.setAlojamiento(alojamiento);
                startActivity(intent);
                break;
            case "CAMPING":
                intent = new Intent(this, CampingDetailsActivity.class);
                alojamientosViewModel.setAlojamiento(alojamiento);
                startActivity(intent);
                break;
            case "HOSTAL":
                intent = new Intent(this, HostalesDetailsActivity.class);
                alojamientosViewModel.setAlojamiento(alojamiento);
                startActivity(intent);
                break;
            case "HOTEL":
                intent = new Intent(this, HotelesDetailsActivity.class);
                alojamientosViewModel.setAlojamiento(alojamiento);
                startActivity(intent);
                break;
        }
    }
}
