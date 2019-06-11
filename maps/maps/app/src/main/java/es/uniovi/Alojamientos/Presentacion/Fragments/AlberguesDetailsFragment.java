package es.uniovi.Alojamientos.Presentacion.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.uniovi.Alojamientos.Presentacion.Activities.SplashActivity;
import es.uniovi.Alojamientos.Presentacion.Activities.mapsActivity;
import es.uniovi.Alojamientos.Negocio.Adapters.myAdapterImagenes;
import es.uniovi.Alojamientos.Negocio.Common_methods;
import es.uniovi.Alojamientos.Presentacion.CustomMapView;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosAlojamientoViewModel;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.R;
import static es.uniovi.Alojamientos.Negocio.Common_methods.favoritos;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlberguesDetailsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private View view;
    private Alojamiento albergue;
    private TextView nombre;
    private TextView titulo;
    private TextView descripcion_larga;
    private TextView num_plazas;
    private TextView plazas_fijas;
    private TextView num_supletorias;
    private TextView fechas_cierre;
    private TextView abierto_todo_anio;
    private TextView temporada_alta;
    private TextView temporada_media;
    private TextView temporada_baja;
    private TextView tarifas;
    private TextView tipo_alquiler;
    private TextView desayuno_incluido;
    private TextView sabanas_incluido;
    private TextView limpieza_incluido;
    private TextView direccion;
    private TextView telefono;
    private TextView email;
    private TextView web;
    private TextView redes_sociales;
    private WebView servicios;
    private WebView servicios_complementarios;
    private WebView servicios_habitacion;
    private LinearLayout widgets;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private final static int PHONE_CALL_CODE = 100;
    static SharedPreferences prefs;
    private static final String PREFERENCES = "PREFERENCES";

    // NUEVO
    GoogleMap gmap;
    // El que va a capturar el layout
    MapView mapView;
    // Coordenadas de todos los alojamientos
    List<LatLng> coordenadas;

    public AlberguesDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if (prefs.getString("THEME", "").equals("LIGHT") ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (prefs.getString("THEME", "").equals("DARK")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.albergues_layout, container, false);

        nombre = view.findViewById(R.id.nombre_albergue);
        titulo = view.findViewById(R.id.titulo_albergue);
        descripcion_larga = view.findViewById(R.id.descripcion_larga_albergue);
        num_plazas = view.findViewById(R.id.num_habitaciones_albergue);
        fechas_cierre = view.findViewById(R.id.fechas_cierre_text_albergue);
        abierto_todo_anio = view.findViewById(R.id.abierto_todo_anio_albergue);
        tarifas = view.findViewById(R.id.tarifas_albergue);

        direccion = view.findViewById(R.id.texto_direccion_albergue);
        telefono = view.findViewById(R.id.texto_telefono_albergue);
        email = view.findViewById(R.id.texto_email_albergue);
        web = view.findViewById(R.id.texto_web_albergue);
        redes_sociales = view.findViewById(R.id.redes_sociales_albergue);
        widgets = view.findViewById(R.id.widgets_albergue);
        mRecyclerView = view.findViewById(R.id.recycler_view_albergues_details) ;
        mLayoutManager = new LinearLayoutManager(view.getContext());
        servicios = view.findViewById(R.id.servicios_albergue);
        servicios_complementarios = view.findViewById(R.id.servicios_complementarios_albergue);
        servicios_habitacion = view.findViewById(R.id.servicios_habitacion_albergue);
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.HORIZONTAL);

        DatosAlojamientoViewModel alojamientoViewModel = ViewModelProviders.of(this).get(DatosAlojamientoViewModel.class);
        albergue = alojamientoViewModel.getAlojamiento();
        renderFragment(albergue);
        getActivity().setTitle(albergue.getNombre());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Lo inflamos con el layout que creamos en res/menu
        inflater.inflate(R.menu.menu_favourite,menu);
        MenuItem item = menu.findItem(R.id.add_favourite);
        favoritos = Common_methods.restoreList(getActivity());
        Boolean favorito = false;
        for (Alojamiento alojamiento : favoritos){
            if(alojamiento.getNombre().equals(albergue.getNombre())){
                favorito = true;
            }
        }
        if(favorito==true){
            item.setIcon(R.drawable.icono_favorito);
            item.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_favourite:
                if(item.isChecked()){
                    item.setChecked(false);
                    item.setIcon(R.drawable.icono_no_favorito);
                    favoritos = Common_methods.restoreList(getActivity());
                    ArrayList<Alojamiento> favs = new ArrayList<>();
                    for (Alojamiento alojamiento: favoritos){
                        if(!alojamiento.getNombre().equals(albergue.getNombre())){
                            favs.add(alojamiento);
                        }
                    }
                    favoritos = favs;
                    Common_methods.saveList(favs, getActivity());
                }
                else{
                    item.setChecked(true);
                    item.setIcon(R.drawable.icono_favorito);
                    favoritos.add(albergue);
                    Common_methods.saveList(favoritos, getActivity());
                }
                return true;
            default:
                return false;
        }
    }

    public void renderFragment(final Alojamiento albergue){
        nombre.setText(albergue.getNombre() + " - " + albergue.getConcejo() + "("+albergue.getZona()+")");
        titulo.setText(!albergue.getTitulo().equals(";") ? Html.fromHtml(albergue.getTitulo()) : "");

        // NUEVO
        mAdapter = new myAdapterImagenes(R.layout.recycler_view_item_for_images, albergue.getSlide().split(";"));
        mRecyclerView.setHasFixedSize(true);
        // Animaciones para el recyclerView
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        descripcion_larga.setText(!albergue.getDescripcionLarga().equals("") && !albergue.getDescripcionLarga().equals(";") ? Html.fromHtml(albergue.getDescripcionLarga()): "No hay descripción disponible...");
        num_plazas.setText(!albergue.getPlazas().equals("") ? num_plazas.getText()+": "+albergue.getPlazas() : num_plazas.getText()+": "+"-");
        fechas_cierre.setText(!albergue.getFechasCierre().equals("") && !albergue.getFechasCierre().equals(";") ? albergue.getFechasCierre() : " -");
        abierto_todo_anio.setText(!albergue.getAbiertoTodoAno().equals("") ? abierto_todo_anio.getText() + " " + albergue.getAbiertoTodoAno() : abierto_todo_anio.getText() + "-");
        // TARIFAS
        // Coger el string entre -tipo- y €
        tarifas.setText(!albergue.getTarifas().equals("") && !albergue.getTarifas().equals(";")? Html.fromHtml(albergue.getTarifas()) : " -");
        //servicios.setText(!albergue.getServiciosEstablecimiento().equals("") ? Html.fromHtml(albergue.getServiciosEstablecimiento()) : "-");
        //servicios_complementarios.setText(!albergue.getServiciosComplementarios().equals("") && !albergue.getServiciosComplementarios().equals(";") ? Html.fromHtml(albergue.getServiciosComplementarios()) : "-");
        //servicios_habitacion.setText(!albergue.getServiciosHabitacion().equals("") && !albergue.getServiciosHabitacion().equals(";") ? Html.fromHtml(albergue.getServiciosHabitacion()) : "-");

        if (prefs.getString("THEME", "").equals("LIGHT") ) {
            servicios.loadData(!albergue.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!albergue.getServiciosComplementarios().equals("") && !albergue.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!albergue.getServiciosHabitacion().equals("") && !albergue.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosHabitacion()+ "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }
        else if (prefs.getString("THEME", "").equals("DARK")){
            servicios.loadData(!albergue.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!albergue.getServiciosComplementarios().equals("") && !albergue.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!albergue.getServiciosHabitacion().equals("") && !albergue.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosHabitacion() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }
        else{
            servicios.loadData(!albergue.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!albergue.getServiciosComplementarios().equals("") && !albergue.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!albergue.getServiciosHabitacion().equals("") && !albergue.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + albergue.getServiciosHabitacion()+ "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }

        direccion.setText(!albergue.getDireccion().equals("") ? albergue.getDireccion() : "-");

        final String[] num_telefono = albergue.getTelefono().split("/");
        if(num_telefono[0].equals(""))
            telefono.setText("-");
        else if (num_telefono.length>1)
            telefono.setText(Html.fromHtml("<a href=''>"+num_telefono[0]+"</a>"+" / "+"<a href=''>"+num_telefono[1]+"</a>"));
        else
            telefono.setText(Html.fromHtml("<a href=''>"+num_telefono[0]+"</a>"));

        if(telefono.getText().toString() != null) {
            telefono.setClickable(true);
            telefono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Comprobar versión de Android que se está corriendo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Comprobar si ha aceptado
                        if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                            Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num_telefono[0]));
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                                return;
                            startActivity(intentCall);
                        } else {
                            // Nunca se le ha preguntado
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            }
                        }
                    }
                    else
                        OlderVersions(telefono.getText().toString());
                }
            });
        }
        email.setText(!albergue.getEmail().equals("") && !albergue.getFacebook().equals(";") ? Html.fromHtml("<a href=''>"+albergue.getEmail()+"</a>") : "-");
        email.setClickable(!albergue.getEmail().equals("") ? true : false);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email.getText().toString()));
                startActivity(intent);
            }
        });
        web.setText(!albergue.getWeb().equals("") ? Html.fromHtml("<a href="+albergue.getWeb()+">"+albergue.getWeb()+"</a>") : "-");
        web.setClickable(!albergue.getWeb().equals("") ? true : false);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pagina_web =  (albergue.getWeb().contains("http://") || albergue.getWeb().contains("https://")) ? albergue.getWeb() : "http://"+albergue.getWeb();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina_web));
                startActivity(intent);
            }
        });
        redes_sociales.setVisibility(albergue.getYoutube().equals("")&albergue.getPinterest().equals("")&albergue.getInstagram().equals("")&albergue.getGooglePlus().equals("")&
                albergue.getTwitter().equals("")&albergue.getFacebook().equals("") ? View.INVISIBLE : View.VISIBLE);
        if(!albergue.getFacebook().equals("") && !albergue.getFacebook().equals(";")){
            LinearLayout linearLayout = new LinearLayout(widgets.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageButton facebook = new ImageButton(linearLayout.getContext());
            facebook.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                facebook.setBackground(ContextCompat.getDrawable(linearLayout.getContext(),R.drawable.icono_facebook));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(25, 0, 0, 20);
            final TextView textView = new TextView(linearLayout.getContext());
            textView.setText(Html.fromHtml("<a href='"+albergue.getFacebook()+"'> http://wwww.facebook.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String facebook =  (albergue.getFacebook().contains("http://") || albergue.getFacebook().contains("https://")) ? albergue.getFacebook() : "http://"+albergue.getFacebook();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook));
                    startActivity(intent);
                }
            });
            linearLayout.addView(facebook);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!albergue.getTwitter().equals("") && !albergue.getFacebook().equals(";")){
            LinearLayout linearLayout = new LinearLayout(widgets.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageButton twitter = new ImageButton(linearLayout.getContext());
            twitter.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                twitter.setBackground(ContextCompat.getDrawable(linearLayout.getContext(),R.drawable.icono_twitter));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(25, 0, 0, 20);
            final TextView textView = new TextView(linearLayout.getContext());
            textView.setText(Html.fromHtml("<a href='"+albergue.getTwitter()+"'> http://wwww.twitter.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String twitter =  (albergue.getTwitter()).contains("http://") || albergue.getTwitter().contains("https://") ? albergue.getTwitter() : "http://"+albergue.getTwitter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                    startActivity(intent);
                }
            });
            linearLayout.addView(twitter);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!albergue.getGooglePlus().equals("") && !albergue.getFacebook().equals(";")){
            LinearLayout linearLayout = new LinearLayout(widgets.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageButton google_plus = new ImageButton(linearLayout.getContext());
            google_plus.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                google_plus.setBackground(ContextCompat.getDrawable(linearLayout.getContext(),R.drawable.icono_google_plus));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(25, 0, 0, 20);
            final TextView textView = new TextView(linearLayout.getContext());
            textView.setText(Html.fromHtml("<a href='"+albergue.getGooglePlus()+"'> http://wwww.googleplus.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String googleplus =  (albergue.getGooglePlus()).contains("http://") || albergue.getGooglePlus().contains("https://") ? albergue.getGooglePlus() : "http://"+albergue.getGooglePlus();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleplus ));
                    startActivity(intent);
                }
            });
            linearLayout.addView(google_plus);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!albergue.getInstagram().equals("") && !albergue.getFacebook().equals(";")){
            LinearLayout linearLayout = new LinearLayout(widgets.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageButton instagram = new ImageButton(linearLayout.getContext());
            instagram.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                instagram.setBackground(ContextCompat.getDrawable(linearLayout.getContext(),R.drawable.icono_instagram));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(25, 0, 0, 20);
            final TextView textView = new TextView(linearLayout.getContext());
            textView.setText(Html.fromHtml("<a href='"+albergue.getInstagram()+"'> http://wwww.instagram.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String instagram =  (albergue.getInstagram()).contains("http://") || albergue.getInstagram().contains("https://") ? albergue.getInstagram() : "http://"+albergue.getInstagram();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagram));
                    startActivity(intent);
                }
            });
            linearLayout.addView(instagram);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!albergue.getPinterest().equals("") && !albergue.getFacebook().equals(";")){
            LinearLayout linearLayout = new LinearLayout(widgets.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageButton pinterest = new ImageButton(linearLayout.getContext());
            pinterest.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                pinterest.setBackground(ContextCompat.getDrawable(linearLayout.getContext(),R.drawable.icono_pinterest));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(25, 0, 0, 20);
            final TextView textView = new TextView(linearLayout.getContext());
            textView.setText(Html.fromHtml("<a href='"+albergue.getPinterest()+"'> http://wwww.pinterest.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pinterest =  (albergue.getPinterest()).contains("http://") || albergue.getPinterest().contains("https://") ? albergue.getPinterest() : "http://"+albergue.getPinterest();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pinterest));
                    startActivity(intent);
                }
            });
            linearLayout.addView(pinterest);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!albergue.getYoutube().equals("") && !albergue.getFacebook().equals(";")){
            LinearLayout linearLayout = new LinearLayout(widgets.getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageButton youtube = new ImageButton(linearLayout.getContext());
            youtube.setClickable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                youtube.setBackground(ContextCompat.getDrawable(linearLayout.getContext(),R.drawable.icono_youtube));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(25, 0, 0, 20);
            final TextView textView = new TextView(linearLayout.getContext());
            textView.setText(Html.fromHtml("<a href='"+albergue.getYoutube()+"'> http://wwww.youtube.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String youtube =  (albergue.getYoutube()).contains("http://") || albergue.getYoutube().contains("https://") ? albergue.getYoutube() : "http://"+albergue.getYoutube();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube));
                    startActivity(intent);
                }
            });
            linearLayout.addView(youtube);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
    }

    // Cuando el fragmento ya cargó la vista
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (CustomMapView) view.findViewById(R.id.map_albergue);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        String[] coordenadas = albergue.getCoordenadas().split(",");
        if(coordenadas.length == 3){
            LatLng asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[2]));
            gmap.addMarker(new MarkerOptions().position(asturias).title(albergue.getNombre()).draggable(true));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(asturias));

            // Para limitarlo a una zona concreta
            gmap.setMinZoomPreference(5);
            gmap.setMaxZoomPreference(15);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(asturias)
                    .zoom(10)       // limite -> 21
                    .build();
            gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else
        if(coordenadas.length > 1) {
            LatLng asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[1]));
            gmap.addMarker(new MarkerOptions().position(asturias).title(getString(R.string.see_map)).draggable(true));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(asturias));

            // Para limitarlo a una zona concreta
            gmap.setMinZoomPreference(5);
            gmap.setMaxZoomPreference(15);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(asturias)
                    .zoom(10)       // limite -> 21
                    .build();
            gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        gmap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent1 = new Intent(getActivity(), mapsActivity.class);
        intent1.putExtra("ALOJAMIENTO_TIPO", "ALBERGUES");
        intent1.putExtra("COORDENADAS", albergue.getCoordenadas());
        intent1.putExtra("ALOJAMIENTO_NOMBRE", albergue.getNombre());
        startActivity(intent1);
    }

    public boolean CheckPermission(String permission){
        int result = getActivity().checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void OlderVersions(String number){
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
        if(getActivity().checkCallingPermission(Manifest.permission.CALL_PHONE)>0)
            startActivity(intentCall);
        else
            Toast.makeText(getContext(), "No hay sufientes permisos", Toast.LENGTH_SHORT).show();
    }

}
