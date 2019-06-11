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
public class ApartamentosDetailsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private View view;
    private Alojamiento apartamento;
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

    public ApartamentosDetailsFragment() {
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
        view = inflater.inflate(R.layout.apartamentos_layout, container, false);

        nombre = view.findViewById(R.id.nombre_apartamento);
        titulo = view.findViewById(R.id.titulo_apartamento);
        descripcion_larga = view.findViewById(R.id.descripcion_larga_apartamento);
        num_plazas = view.findViewById(R.id.num_plazas_apartamento);
        plazas_fijas = view.findViewById(R.id.num_plazas_fijas_apartamento);
        num_supletorias = view.findViewById(R.id.num_plazas_supletorias_apartamento);
        fechas_cierre = view.findViewById(R.id.fechas_cierre_text_apartamento);
        abierto_todo_anio = view.findViewById(R.id.abierto_todo_anio_apartamento);
        temporada_alta = view.findViewById(R.id.temporada_alta_apartamento);
        temporada_media = view.findViewById(R.id.temporada_media_apartamento);
        temporada_baja = view.findViewById(R.id.temporada_baja_apartamento);
        tarifas = view.findViewById(R.id.tarifas_apartamento);
        tipo_alquiler = view.findViewById(R.id.tipo_alquiler_apartamento);
        desayuno_incluido = view.findViewById(R.id.desayuno_incluido_apartamento);
        sabanas_incluido = view.findViewById(R.id.sabanas_incluidas_apartamento);
        limpieza_incluido = view.findViewById(R.id.limpieza_incluida_apartamento);

        direccion = view.findViewById(R.id.texto_direccion_apartamento);
        telefono = view.findViewById(R.id.texto_telefono_apartamento);
        email = view.findViewById(R.id.texto_email_apartamento);
        web = view.findViewById(R.id.texto_web_apartamento);
        redes_sociales = view.findViewById(R.id.redes_sociales_apartamento);
        widgets = view.findViewById(R.id.widgets_apartamento);
        mRecyclerView = view.findViewById(R.id.recycler_view_apartamentos_details) ;
        mLayoutManager = new LinearLayoutManager(view.getContext());
        servicios = view.findViewById(R.id.servicios_apartamento);
        servicios_complementarios = view.findViewById(R.id.servicios_complementarios_apartamento);
        servicios_habitacion = view.findViewById(R.id.servicios_habitacion_apartamento);
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.HORIZONTAL);

        DatosAlojamientoViewModel alojamientoViewModel = ViewModelProviders.of(this).get(DatosAlojamientoViewModel.class);
        apartamento = alojamientoViewModel.getAlojamiento();
        renderFragment(apartamento);
        getActivity().setTitle(apartamento.getNombre());

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
            if(alojamiento.getNombre().equals(apartamento.getNombre())){
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
                        if(!alojamiento.getNombre().equals(apartamento.getNombre())){
                            favs.add(alojamiento);
                        }
                    }
                    favoritos = favs;
                    Common_methods.saveList(favs, getActivity());
                }
                else{
                    item.setChecked(true);
                    item.setIcon(R.drawable.icono_favorito);
                    favoritos.add(apartamento);
                    Common_methods.saveList(favoritos, getActivity());
                }
                return true;
            default:
                return false;
        }
    }

    public void renderFragment(final Alojamiento apartamento){
        nombre.setText(apartamento.getNombre() + " - " + apartamento.getConcejo() + "("+apartamento.getZona()+")");
        titulo.setText(!apartamento.getTitulo().equals(";") ? Html.fromHtml(apartamento.getTitulo()) : "");

        // NUEVO
        mAdapter = new myAdapterImagenes(R.layout.recycler_view_item_for_images, apartamento.getSlide().split(";"));
        mRecyclerView.setHasFixedSize(true);
        // Animaciones para el recyclerView
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        descripcion_larga.setText(!apartamento.getDescripcionLarga().equals("") ? Html.fromHtml(apartamento.getDescripcionLarga()): "No hay descripción disponible...");
        num_plazas.setText(!apartamento.getPlazas().equals("") ? num_plazas.getText()+" "+apartamento.getPlazas() : num_plazas.getText()+" "+"-");
        plazas_fijas.setText(!apartamento.getPlazasFijas().equals("") ? plazas_fijas.getText()+" "+apartamento.getPlazasFijas() : plazas_fijas.getText()+" "+" -");
        num_supletorias.setText(!apartamento.getPlazasSupletorias().equals("") ? num_supletorias.getText()+" "+apartamento.getPlazasSupletorias() : num_supletorias.getText()+" "+"-");
        fechas_cierre.setText(!apartamento.getFechasCierre().equals("") && !apartamento.getFechasCierre().equals(";") ? apartamento.getFechasCierre() : " -");
        abierto_todo_anio.setText(!apartamento.getAbiertoTodoAno().equals("") ? abierto_todo_anio.getText() + " " + apartamento.getAbiertoTodoAno() : abierto_todo_anio.getText() + "-");
        desayuno_incluido.setText(!apartamento.getDesayunoIncluido().equals("") && apartamento.getDesayunoIncluido().equals("Desayuno incluido") ? desayuno_incluido.getText()+ ": " + getString(R.string.yes) : desayuno_incluido.getText() + ": No");
        limpieza_incluido.setText(!apartamento.getLimpiezaIncluida().equals("") && apartamento.getLimpiezaIncluida().equals("Limpieza incluida") ? limpieza_incluido.getText()+ ": " + getString(R.string.yes) : limpieza_incluido.getText() + ": No");
        sabanas_incluido.setText(!apartamento.getSabanasIncluidas().equals("") && apartamento.getSabanasIncluidas().equals("Sábanas incluidas") ?  sabanas_incluido.getText()+ ": " + getString(R.string.yes) :  sabanas_incluido.getText() + ": No");
        temporada_alta.setText(!apartamento.getTemporadaAlta().equals("") ? temporada_alta.getText() + " " +apartamento.getTemporadaAlta() : temporada_alta.getText() + " " + "-");
        temporada_media.setText(!apartamento.getTemporadaAlta().equals("") ? temporada_media.getText() + " " +apartamento.getTemporadaAlta() : temporada_media.getText() + " " + "-");
        temporada_baja.setText(!apartamento.getTemporadaAlta().equals("") ? temporada_baja.getText() + " " +apartamento.getTemporadaAlta() : temporada_baja.getText() + " " + "-");
        // TARIFAS
        // Coger el string entre -tipo- y €
        tarifas.setText(!apartamento.getTarifas().equals("") ? Html.fromHtml(apartamento.getTarifas()) : " -");
        tipo_alquiler.setText(!apartamento.getTipoAlquiler().equals("") ? tipo_alquiler.getText() + ": " +apartamento.getTipoAlquiler() : tipo_alquiler.getText() + " -");
        //servicios.setText(!apartamento.getServiciosEstablecimiento().equals("") ? Html.fromHtml(apartamento.getServiciosEstablecimiento()) : "-");
        //servicios_complementarios.setText(!apartamento.getServiciosComplementarios().equals("") ? Html.fromHtml(apartamento.getServiciosComplementarios()) : "-");
        //servicios_habitacion.setText(!apartamento.getServiciosHabitacion().equals("") ? Html.fromHtml(apartamento.getServiciosHabitacion()) : "-");

        if (prefs.getString("THEME", "").equals("LIGHT") ) {
            servicios.loadData(!apartamento.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!apartamento.getServiciosComplementarios().equals("") && !apartamento.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!apartamento.getServiciosHabitacion().equals("") && !apartamento.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosHabitacion()+ "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }
        else if (prefs.getString("THEME", "").equals("DARK")){
            servicios.loadData(!apartamento.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!apartamento.getServiciosComplementarios().equals("") && !apartamento.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!apartamento.getServiciosHabitacion().equals("") && !apartamento.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosHabitacion() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }
        else{
            servicios.loadData(!apartamento.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!apartamento.getServiciosComplementarios().equals("") && !apartamento.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!apartamento.getServiciosHabitacion().equals("") && !apartamento.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + apartamento.getServiciosHabitacion()+ "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }

        direccion.setText(!apartamento.getDireccion().equals("") ? apartamento.getDireccion() : "-");

        final String[] num_telefono = apartamento.getTelefono().split("/");
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
        email.setText(!apartamento.getEmail().equals("") && !apartamento.getFacebook().equals(";") ? Html.fromHtml("<a href=''>"+apartamento.getEmail()+"</a>") : "-");
        email.setClickable(!apartamento.getEmail().equals("") ? true : false);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email.getText().toString()));
                startActivity(intent);
            }
        });
        web.setText(!apartamento.getWeb().equals("") ? Html.fromHtml("<a href="+apartamento.getWeb()+">"+apartamento.getWeb()+"</a>") : "-");
        web.setClickable(!apartamento.getWeb().equals("") ? true : false);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pagina_web =  (apartamento.getWeb().contains("http://") || apartamento.getWeb().contains("https://")) ? apartamento.getWeb() : "http://"+apartamento.getWeb();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina_web));
                startActivity(intent);
            }
        });
        redes_sociales.setVisibility(apartamento.getYoutube().equals("")&apartamento.getPinterest().equals("")&apartamento.getInstagram().equals("")&apartamento.getGooglePlus().equals("")&
                apartamento.getTwitter().equals("")&apartamento.getFacebook().equals("") ? View.INVISIBLE : View.VISIBLE);
        if(!apartamento.getFacebook().equals("") && !apartamento.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+apartamento.getFacebook()+"'> http://wwww.facebook.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String facebook =  (apartamento.getFacebook().contains("http://") || apartamento.getFacebook().contains("https://")) ? apartamento.getFacebook() : "http://"+apartamento.getFacebook();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook));
                    startActivity(intent);
                }
            });
            linearLayout.addView(facebook);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!apartamento.getTwitter().equals("")&& !apartamento.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+apartamento.getTwitter()+"'> http://wwww.twitter.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String twitter =  (apartamento.getTwitter()).contains("http://") || apartamento.getTwitter().contains("https://") ? apartamento.getTwitter() : "http://"+apartamento.getTwitter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                    startActivity(intent);
                }
            });
            linearLayout.addView(twitter);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!apartamento.getGooglePlus().equals("") && !apartamento.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+apartamento.getGooglePlus()+"'> http://wwww.googleplus.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String googleplus =  (apartamento.getGooglePlus()).contains("http://") || apartamento.getGooglePlus().contains("https://") ? apartamento.getGooglePlus() : "http://"+apartamento.getGooglePlus();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleplus ));
                    startActivity(intent);
                }
            });
            linearLayout.addView(google_plus);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!apartamento.getInstagram().equals("") && !apartamento.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+apartamento.getInstagram()+"'> http://wwww.instagram.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String instagram =  (apartamento.getInstagram()).contains("http://") || apartamento.getInstagram().contains("https://") ? apartamento.getInstagram() : "http://"+apartamento.getInstagram();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagram));
                    startActivity(intent);
                }
            });
            linearLayout.addView(instagram);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!apartamento.getPinterest().equals("") && !apartamento.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+apartamento.getPinterest()+"'> http://wwww.pinterest.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pinterest =  (apartamento.getPinterest()).contains("http://") || apartamento.getPinterest().contains("https://") ? apartamento.getPinterest() : "http://"+apartamento.getPinterest();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pinterest));
                    startActivity(intent);
                }
            });
            linearLayout.addView(pinterest);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!apartamento.getYoutube().equals("") && !apartamento.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+apartamento.getYoutube()+"'> http://wwww.youtube.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String youtube =  (apartamento.getYoutube()).contains("http://") || apartamento.getYoutube().contains("https://") ? apartamento.getYoutube() : "http://"+apartamento.getYoutube();
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
        mapView = (CustomMapView) view.findViewById(R.id.map_apartamento);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        String[] coordenadas = apartamento.getCoordenadas().split(",");
        if(coordenadas.length == 3){
            LatLng asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[2]));
            gmap.addMarker(new MarkerOptions().position(asturias).title(apartamento.getNombre()).draggable(true));
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
        else if(coordenadas.length > 1) {
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
        intent1.putExtra("ALOJAMIENTO_TIPO", "APARTAMENTOS");
        intent1.putExtra("COORDENADAS", apartamento.getCoordenadas());
        intent1.putExtra("ALOJAMIENTO_NOMBRE", apartamento.getNombre());
        startActivity(intent1);
    }

    public boolean CheckPermission(String permission){
        int result = getActivity().checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void OlderVersions(String number){
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
        System.out.println("COMPROBACION: checkCallingPermission(Manifest.permission.CALL_PHONE)");
        if(getActivity().checkCallingPermission(Manifest.permission.CALL_PHONE)>0)
            startActivity(intentCall);
        else
            Toast.makeText(getContext(), "No hay sufientes permisos", Toast.LENGTH_SHORT).show();
    }

}
