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
public class HotelesDetailsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private View view;
    private Alojamiento hotel;
    private TextView nombre;
    private TextView titulo;
    private TextView descripcion_larga;
    private TextView num_habitaciones;
    private TextView q_calidad;
    private TextView num_estrellas_1;
    private TextView num_estrellas_2;
    private TextView num_estrellas_3;
    private TextView num_estrellas_4;
    private TextView num_estrellas_5;
    private TextView fechas_cierre;
    private TextView abierto_todo_anio;
    private TextView temporada_alta;
    private TextView temporada_media;
    private TextView temporada_baja;
    private TextView tarifas;
    private TextView desayuno_incluido;
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

    public HotelesDetailsFragment() {
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
        view = inflater.inflate(R.layout.hoteles_layout, container, false);

        nombre = view.findViewById(R.id.nombre_hotel);
        titulo = view.findViewById(R.id.titulo_hotel);
        descripcion_larga = view.findViewById(R.id.descripcion_larga_hotel);
        num_habitaciones = view.findViewById(R.id.num_habitaciones_hotel);
        q_calidad = view.findViewById(R.id.q_calidad_hotel);
        num_estrellas_1 = view.findViewById(R.id.num_estrellas_hotel_1);
        num_estrellas_2 = view.findViewById(R.id.num_estrellas_hotel_2);
        num_estrellas_3 = view.findViewById(R.id.num_estrellas_hotel_3);
        num_estrellas_4 = view.findViewById(R.id.num_estrellas_hotel_4);
        num_estrellas_5 = view.findViewById(R.id.num_estrellas_hotel_5);
        fechas_cierre = view.findViewById(R.id.fechas_cierre_text_hotel);
        abierto_todo_anio = view.findViewById(R.id.abierto_todo_anio_hotel);
        temporada_alta = view.findViewById(R.id.temporada_alta_hotel);
        temporada_media = view.findViewById(R.id.temporada_media_hotel);
        temporada_baja = view.findViewById(R.id.temporada_baja_hotel);
        tarifas = view.findViewById(R.id.tarifas_hotel);
        desayuno_incluido = view.findViewById(R.id.desayuno_incluido_hotel);

        direccion = view.findViewById(R.id.texto_direccion_hotel);
        telefono = view.findViewById(R.id.texto_telefono_hotel);
        email = view.findViewById(R.id.texto_email_hotel);
        web = view.findViewById(R.id.texto_web_hotel);
        redes_sociales = view.findViewById(R.id.redes_sociales_hotel);
        widgets = view.findViewById(R.id.widgets_hotel);
        mRecyclerView = view.findViewById(R.id.recycler_view_hotel_details) ;
        mLayoutManager = new LinearLayoutManager(view.getContext());
        servicios = view.findViewById(R.id.servicios_hotel);
        servicios_complementarios = view.findViewById(R.id.servicios_complementarios_hotel);
        servicios_habitacion = view.findViewById(R.id.servicios_habitacion_hotel);
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.HORIZONTAL);

        DatosAlojamientoViewModel alojamientoViewModel = ViewModelProviders.of(this).get(DatosAlojamientoViewModel.class);
        hotel = alojamientoViewModel.getAlojamiento();
        renderFragment(hotel);
        getActivity().setTitle(hotel.getNombre());

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
            if(alojamiento.getNombre().equals(hotel.getNombre())){
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
                        if(!alojamiento.getNombre().equals(hotel.getNombre())){
                            favs.add(alojamiento);
                        }
                    }
                    favoritos = favs;
                    Common_methods.saveList(favs, getActivity());
                }
                else{
                    item.setChecked(true);
                    item.setIcon(R.drawable.icono_favorito);
                    favoritos.add(hotel);
                    Common_methods.saveList(favoritos, getActivity());
                }
                return true;
            default:
                return false;
        }
    }

    public void renderFragment(final Alojamiento hotel){
        nombre.setText(hotel.getNombre() + " - " + hotel.getConcejo() + "("+hotel.getZona()+")");
        titulo.setText(!hotel.getTitulo().equals(";") ? Html.fromHtml(hotel.getTitulo()) : "");

        // NUEVO
        mAdapter = new myAdapterImagenes(R.layout.recycler_view_item_for_images, hotel.getSlide().split(";"));
        mRecyclerView.setHasFixedSize(true);
        // Animaciones para el recyclerView
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        descripcion_larga.setText(!hotel.getDescripcionLarga().equals("") ? Html.fromHtml(hotel.getDescripcionLarga()): "No hay descripción disponible...");
        num_habitaciones.setText(!hotel.getPlazas().equals("") ? num_habitaciones.getText()+" "+hotel.getPlazas() : num_habitaciones.getText()+" "+"-");
        q_calidad.setText(!hotel.getQdeCalidad().equals(true) ? q_calidad.getText()+" "+getResources().getString(R.string.yes) : q_calidad.getText()+" No");
        int indice = hotel.getCategories().indexOf("Estrella");
        if(indice != -1){
            String estrellas = hotel.getCategories().substring(indice-2, indice-1);
            int num_estrellas = Integer.valueOf(estrellas);
            switch (num_estrellas){
                case 1:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    break;
                case 2:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    break;
                case 3:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_3.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    break;
                case 4:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_3.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_4.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    break;
                case 5:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_3.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_4.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        num_estrellas_5.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.icono_favorito));
                    }
                    break;
            }
        }

        fechas_cierre.setText(!hotel.getFechasCierre().equals("") && !hotel.getFechasCierre().equals(";") ? hotel.getFechasCierre() : " -");
        abierto_todo_anio.setText(!hotel.getAbiertoTodoAno().equals("") ? abierto_todo_anio.getText() + " " + hotel.getAbiertoTodoAno() : abierto_todo_anio.getText() + "-");
        desayuno_incluido.setText(!hotel.getDesayunoIncluido().equals("") && hotel.getDesayunoIncluido().equals("Desayuno incluido") ? desayuno_incluido.getText()+ ": " + getString(R.string.yes) : desayuno_incluido.getText() + ": No");
        temporada_alta.setText(!hotel.getTemporadaAlta().equals("") ? temporada_alta.getText() + " " +hotel.getTemporadaAlta() : temporada_alta.getText() + " " + "-");
        temporada_media.setText(!hotel.getTemporadaMedia().equals("") ? temporada_media.getText() + " " +hotel.getTemporadaAlta() : temporada_media.getText() + " " + "-");
        temporada_baja.setText(!hotel.getTemporadaBaja().equals("") ? temporada_baja.getText() + " " +hotel.getTemporadaAlta() : temporada_baja.getText() + " " + "-");
        // TARIFAS
        // Coger el string entre -tipo- y €
        tarifas.setText(!hotel.getTarifas().equals("") ? Html.fromHtml(hotel.getTarifas()) : " -");
        //servicios.setText(!hotel.getServiciosEstablecimiento().equals("") ? Html.fromHtml(hotel.getServiciosEstablecimiento()) : "-");
        //servicios_complementarios.setText(!hotel.getServiciosComplementarios().equals("") ? Html.fromHtml(hotel.getServiciosComplementarios()) : "-");
        //servicios_habitacion.setText(!hotel.getServiciosHabitacion().equals("") ? Html.fromHtml(hotel.getServiciosHabitacion()) : "-");

        if (prefs.getString("THEME", "").equals("LIGHT") ) {
            servicios.loadData(!hotel.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!hotel.getServiciosComplementarios().equals("") && !hotel.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!hotel.getServiciosHabitacion().equals("") && !hotel.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosHabitacion()+ "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }
        else if (prefs.getString("THEME", "").equals("DARK")){
            servicios.loadData(!hotel.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!hotel.getServiciosComplementarios().equals("") && !hotel.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!hotel.getServiciosHabitacion().equals("") && !hotel.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosHabitacion() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }
        else{
            servicios.loadData(!hotel.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!hotel.getServiciosComplementarios().equals("") && !hotel.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_habitacion.loadData(!hotel.getServiciosHabitacion().equals("") && !hotel.getServiciosHabitacion().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + hotel.getServiciosHabitacion()+ "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }

        direccion.setText(!hotel.getDireccion().equals("") ? hotel.getDireccion() : "-");

        final String[] num_telefono = hotel.getTelefono().split("/");
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
        email.setText(!hotel.getEmail().equals("") && !hotel.getFacebook().equals(";") ? Html.fromHtml("<a href=''>"+hotel.getEmail()+"</a>") : "-");
        email.setClickable(!hotel.getEmail().equals("") ? true : false);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email.getText().toString()));
                startActivity(intent);
            }
        });
        web.setText(!hotel.getWeb().equals("") ? Html.fromHtml("<a href="+hotel.getWeb()+">"+hotel.getWeb()+"</a>") : "-");
        web.setClickable(!hotel.getWeb().equals("") ? true : false);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pagina_web =  (hotel.getWeb().contains("http://") || hotel.getWeb().contains("https://")) ? hotel.getWeb() : "http://"+hotel.getWeb();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina_web));
                startActivity(intent);
            }
        });
        redes_sociales.setVisibility(hotel.getYoutube().equals("")&hotel.getPinterest().equals("")&hotel.getInstagram().equals("")&hotel.getGooglePlus().equals("")&
                hotel.getTwitter().equals("")&hotel.getFacebook().equals("") ? View.INVISIBLE : View.VISIBLE);
        if(!hotel.getFacebook().equals("") && !hotel.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+hotel.getFacebook()+"'> http://wwww.facebook.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String facebook =  (hotel.getFacebook().contains("http://") || hotel.getFacebook().contains("https://")) ? hotel.getFacebook() : "http://"+hotel.getFacebook();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook));
                    startActivity(intent);
                }
            });
            linearLayout.addView(facebook);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!hotel.getTwitter().equals("") && !hotel.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+hotel.getTwitter()+"'> http://wwww.twitter.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String twitter =  (hotel.getTwitter()).contains("http://") || hotel.getTwitter().contains("https://") ? hotel.getTwitter() : "http://"+hotel.getTwitter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                    startActivity(intent);
                }
            });
            linearLayout.addView(twitter);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!hotel.getGooglePlus().equals("") && !hotel.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+hotel.getGooglePlus()+"'> http://wwww.googleplus.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String googleplus =  (hotel.getGooglePlus()).contains("http://") || hotel.getGooglePlus().contains("https://") ? hotel.getGooglePlus() : "http://"+hotel.getGooglePlus();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleplus ));
                    startActivity(intent);
                }
            });
            linearLayout.addView(google_plus);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!hotel.getInstagram().equals("") && !hotel.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+hotel.getInstagram()+"'> http://wwww.instagram.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String instagram =  (hotel.getInstagram()).contains("http://") || hotel.getInstagram().contains("https://") ? hotel.getInstagram() : "http://"+hotel.getInstagram();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagram));
                    startActivity(intent);
                }
            });
            linearLayout.addView(instagram);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!hotel.getPinterest().equals("") && !hotel.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+hotel.getPinterest()+"'> http://wwww.pinterest.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pinterest =  (hotel.getPinterest()).contains("http://") || hotel.getPinterest().contains("https://") ? hotel.getPinterest() : "http://"+hotel.getPinterest();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pinterest));
                    startActivity(intent);
                }
            });
            linearLayout.addView(pinterest);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!hotel.getYoutube().equals("") && !hotel.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+hotel.getYoutube()+"'> http://wwww.youtube.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String youtube =  (hotel.getYoutube()).contains("http://") || hotel.getYoutube().contains("https://") ? hotel.getYoutube() : "http://"+hotel.getYoutube();
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
        mapView = (CustomMapView) view.findViewById(R.id.map_hotel);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        String[] coordenadas = hotel.getCoordenadas().split(",");
        if(coordenadas.length == 3){
            LatLng asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[2]));
            gmap.addMarker(new MarkerOptions().position(asturias).title(hotel.getNombre()).draggable(true));
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
        intent1.putExtra("ALOJAMIENTO_TIPO", "HOTELES");
        intent1.putExtra("COORDENADAS", hotel.getCoordenadas());
        intent1.putExtra("ALOJAMIENTO_NOMBRE", hotel.getNombre());
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
