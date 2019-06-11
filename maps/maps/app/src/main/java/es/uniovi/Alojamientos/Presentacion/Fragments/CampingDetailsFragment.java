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
public class CampingDetailsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String ACCOMODATION_LIST_FILENAME = "FAVOURITES_LIST";
    private View view;
    private Alojamiento camping;
    private TextView nombre;
    private TextView titulo;
    private TextView descripcion_larga;
    private TextView num_plazas;
    private TextView plazas_fijas;
    private TextView num_parcelas;
    private TextView fechas_cierre;
    private TextView abierto_todo_anio;
    private TextView adulto;
    private TextView tienda_individual;
    private TextView coche;
    private TextView moto;
    private TextView autocar;
    private TextView ninio;
    private TextView tienda_familiar;
    private TextView caravana;
    private TextView autocaravana;
    private TextView bungalow;
    private TextView direccion;
    private TextView telefono;
    private TextView email;
    private TextView web;
    private TextView redes_sociales;
    private WebView servicios;
    private WebView servicios_complementarios;
    private WebView seguridad_y_sanidad;
    private LinearLayout widgets;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Alojamiento> favoritos;
    private final static int PHONE_CALL_CODE = 100;
    static SharedPreferences prefs;
    private static final String PREFERENCES = "PREFERENCES";

    // NUEVO
    GoogleMap gmap;
    // El que va a capturar el layout
    MapView mapView;
    // Coordenadas de todos los alojamientos
    List<LatLng> coordenadas;

    public CampingDetailsFragment() {
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
        view = inflater.inflate(R.layout.camping_layout, container, false);

        nombre = view.findViewById(R.id.nombre_camping);
        titulo = view.findViewById(R.id.titulo);
        descripcion_larga = view.findViewById(R.id.descripcion_larga);
        num_plazas = view.findViewById(R.id.num_plazas);
        plazas_fijas = view.findViewById(R.id.num_plazas_fijas);
        num_parcelas = view.findViewById(R.id.num_parcelas);
        fechas_cierre = view.findViewById(R.id.fechas_cierre_text);
        abierto_todo_anio = view.findViewById(R.id.abierto_todo_anio);
        adulto = view.findViewById(R.id.adulto);
        tienda_individual = view.findViewById(R.id.tienda_individual);
        coche = view.findViewById(R.id.coche);
        moto = view.findViewById(R.id.moto);
        autocar = view.findViewById(R.id.autocar);
        ninio = view.findViewById(R.id.ninio);
        tienda_familiar = view.findViewById(R.id.tienda_familiar);
        caravana = view.findViewById(R.id.caravana);
        autocaravana = view.findViewById(R.id.autocaravana);
        bungalow = view.findViewById(R.id.bungalow);
        direccion = view.findViewById(R.id.texto_direccion);
        telefono = view.findViewById(R.id.texto_telefono);
        email = view.findViewById(R.id.texto_email);
        web = view.findViewById(R.id.texto_web);
        redes_sociales = view.findViewById(R.id.redes_sociales);
        widgets = view.findViewById(R.id.widgets);
        mRecyclerView = view.findViewById(R.id.recycler_view_campings_details) ;
        mLayoutManager = new LinearLayoutManager(view.getContext());
        servicios = view.findViewById(R.id.servicios_camping);
        servicios_complementarios = view.findViewById(R.id.servicios_complementarios);
        seguridad_y_sanidad = view.findViewById(R.id.seguridad_sanidad);
        ((LinearLayoutManager) mLayoutManager).setOrientation(RecyclerView.HORIZONTAL);

        DatosAlojamientoViewModel alojamientoViewModel = ViewModelProviders.of(this).get(DatosAlojamientoViewModel.class);
        camping = alojamientoViewModel.getAlojamiento();
        renderFragment(camping);
        getActivity().setTitle(camping.getNombre());

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
            if(alojamiento.getNombre().equals(camping.getNombre())){
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
                        if(!alojamiento.getNombre().equals(camping.getNombre())){
                            favs.add(alojamiento);
                        }
                    }
                    favoritos = favs;
                    Common_methods.saveList(favs, getActivity());
                }
                else{
                    item.setChecked(true);
                    item.setIcon(R.drawable.icono_favorito);
                    favoritos.add(camping);
                    Common_methods.saveList(favoritos, getActivity());
                }
                return true;
            default:
                return false;
        }
    }

    public void renderFragment(final Alojamiento camping){
        nombre.setText(camping.getNombre() + " - " + camping.getConcejo() + "("+camping.getZona()+")");
        titulo.setText(!camping.getTitulo().equals(";") ? Html.fromHtml(camping.getTitulo()) : "");

        // NUEVO
        mAdapter = new myAdapterImagenes(R.layout.recycler_view_item_for_images, camping.getSlide().split(";"));
        mRecyclerView.setHasFixedSize(true);
        // Animaciones para el recyclerView
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        descripcion_larga.setText(!camping.getDescripcionLarga().equals("") ? Html.fromHtml(camping.getDescripcionLarga()): "No hay descripción disponible...");
        num_plazas.setText(!camping.getPlazas().equals("") ? num_plazas.getText()+" "+camping.getPlazas() : num_plazas.getText()+" "+"-");
        plazas_fijas.setText(!camping.getPlazasFijas().equals("") ? plazas_fijas.getText()+" "+camping.getPlazasFijas() : plazas_fijas.getText()+" "+" -");
        num_parcelas.setText(!camping.getNParcelas().equals("") ? num_parcelas.getText()+" "+camping.getNParcelas() : num_parcelas.getText()+" "+"-");
        fechas_cierre.setText(!camping.getFechasCierre().equals("") && !camping.getFechasCierre().equals(";") ? camping.getFechasCierre() : " -");
        abierto_todo_anio.setText(!camping.getAbiertoTodoAno().equals("") ? abierto_todo_anio.getText() + " " + camping.getAbiertoTodoAno() : abierto_todo_anio.getText() + " " + "-");
        // TARIFAS
        // Coger el string entre -tipo- y €
        String[] tarifas = camping.getTarifas().split("€");
        adulto.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Adulto")!=-1 ? getResources().getString(R.string.adult) + " " + camping.getTarifas().substring(camping.getTarifas().indexOf("Adulto")+7,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Adulto"))+1): getResources().getString(R.string.adult) +" -") : getResources().getString(R.string.adult) +" -");
        ninio.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Nińo")!=-1 ? getResources().getString(R.string.children) + " "+camping.getTarifas().substring(camping.getTarifas().indexOf("Nińo")+5,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Nińo"))+1): getResources().getString(R.string.children) + " -") : getResources().getString(R.string.children) + " -");
        tienda_individual.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Tienda individual")!=-1 ? getResources().getString(R.string.individual_tent) + " "+camping.getTarifas().substring(camping.getTarifas().indexOf("Tienda individual")+18,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Tienda individual"))+1): getResources().getString(R.string.children) + " -") : getResources().getString(R.string.children) + " -");
        coche.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Coche")!=-1 ? getResources().getString(R.string.car) + " "+camping.getTarifas().substring(camping.getTarifas().indexOf("Coche")+6,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Coche"))+1): getResources().getString(R.string.individual_tent) + " -") : getResources().getString(R.string.individual_tent) + " -");
        moto.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Moto")!=-1 ? getResources().getString(R.string.motorcycle) + " "+camping.getTarifas().substring(camping.getTarifas().indexOf("Moto")+5,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Moto"))+1): getResources().getString(R.string.car) + " -") : getResources().getString(R.string.car) + " -");
        autocar.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Autocar")!=-1 ? getResources().getString(R.string.coach) + " "+ camping.getTarifas().substring(camping.getTarifas().indexOf("Autocar")+8,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Autocar"))+1) : getResources().getString(R.string.coach)+" -") : getResources().getString(R.string.coach)+" -");
        tienda_familiar.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Tienda familia")!=-1 ? getResources().getString(R.string.family_tent)+ " "+camping.getTarifas().substring(camping.getTarifas().indexOf("Tienda familia")+16,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Tienda familia"))+1): getResources().getString(R.string.family_tent)+" -") : getResources().getString(R.string.family_tent)+" -");
        caravana.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Caravana")!=-1 ? getResources().getString(R.string.van)+ " "+camping.getTarifas().substring(camping.getTarifas().indexOf("Caravana")+9,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Caravana"))+1) : getResources().getString(R.string.van)+" -") : getResources().getString(R.string.van)+" -");
        autocaravana.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Autocaravana")!=-1 ? getResources().getString(R.string.camper)+ " "+camping.getTarifas().substring(camping.getTarifas().indexOf("Autocaravana")+13,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Autocaravana"))+1) : getResources().getString(R.string.camper)+" -") : getResources().getString(R.string.camper)+" -");
        bungalow.setText(!camping.getTarifas().equals("") ? (camping.getTarifas().indexOf("Bungalow")!=-1 ? getResources().getString(R.string.bungalow)+ " "+camping.getTarifas().substring(camping.getTarifas().indexOf("Bungalow")+9,camping.getTarifas().indexOf("€",camping.getTarifas().indexOf("Bungalow"))+1): getResources().getString(R.string.bungalow)+" -") : getResources().getString(R.string.bungalow)+" -");
        //servicios.setText(!camping.getServiciosEstablecimiento().equals("") ? Html.fromHtml(camping.getServiciosEstablecimiento()) : "-");
        //servicios_complementarios.setText(!camping.getServiciosComplementarios().equals("") ? Html.fromHtml(camping.getServiciosComplementarios()) : "-");
        //seguridad_y_sanidad.setText(!camping.getSeguridadYSanidad().equals("") ? Html.fromHtml(camping.getSeguridadYSanidad()) : "-");

        if (prefs.getString("THEME", "").equals("LIGHT") ) {
            servicios.loadData(!camping.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!camping.getServiciosComplementarios().equals("") && !camping.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            seguridad_y_sanidad.loadData(!camping.getSeguridadYSanidad().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosHabitacion()+ "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }
        else if (prefs.getString("THEME", "").equals("DARK")){
            servicios.loadData(!camping.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!camping.getServiciosComplementarios().equals("") && !camping.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            seguridad_y_sanidad.loadData(!camping.getSeguridadYSanidad().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosHabitacion() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #C0C0C0; background-color: #313131;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }
        else{
            servicios.loadData(!camping.getServiciosEstablecimiento().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosEstablecimiento() + "</body></html>"  : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            servicios_complementarios.loadData(!camping.getServiciosComplementarios().equals("") && !camping.getServiciosComplementarios().equals(";") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosComplementarios() + "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>","text/html","UTF-8");
            seguridad_y_sanidad.loadData(!camping.getSeguridadYSanidad().equals("") ? "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + camping.getServiciosHabitacion()+ "</body></html>" : "<html><head>"
                    + "<style type=\"text/css\">body{color: #8D8D8D; background-color: #FBFBFB;}"
                    + "</style></head>"
                    + "<body>" + "-" + "</body></html>" ,"text/html","UTF-8");
        }

        direccion.setText(!camping.getDireccion().equals("") ? camping.getDireccion() : "-");

        final String[] num_telefono = camping.getTelefono().split("/");
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
        email.setText(!camping.getEmail().equals("") && !camping.getFacebook().equals(";") ? Html.fromHtml("<a href=''>"+camping.getEmail()+"</a>") : "-");
        email.setClickable(!camping.getEmail().equals("") ? true : false);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email.getText().toString()));
                startActivity(intent);
            }
        });
        web.setText(!camping.getWeb().equals("") ? Html.fromHtml("<a href="+camping.getWeb()+">"+camping.getWeb()+"</a>") : "-");
        web.setClickable(!camping.getWeb().equals("") ? true : false);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pagina_web =  (camping.getWeb().contains("http://") || camping.getWeb().contains("https://")) ? camping.getWeb() : "http://"+camping.getWeb();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina_web));
                startActivity(intent);
            }
        });
        redes_sociales.setVisibility(camping.getYoutube().equals("")&camping.getPinterest().equals("")&camping.getInstagram().equals("")&camping.getGooglePlus().equals("")&
        camping.getTwitter().equals("")&camping.getFacebook().equals("") ? View.INVISIBLE : View.VISIBLE);
        if(!camping.getFacebook().equals("") && !camping.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+camping.getFacebook()+"'> http://wwww.facebook.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String facebook =  (camping.getFacebook().contains("http://") || camping.getFacebook().contains("https://")) ? camping.getFacebook() : "http://"+camping.getFacebook();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook));
                    startActivity(intent);
                }
            });
            linearLayout.addView(facebook);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!camping.getTwitter().equals("") && !camping.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+camping.getTwitter()+"'> http://wwww.twitter.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String twitter =  (camping.getTwitter()).contains("http://") || camping.getTwitter().contains("https://") ? camping.getTwitter() : "http://"+camping.getTwitter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter));
                    startActivity(intent);
                }
            });
            linearLayout.addView(twitter);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!camping.getGooglePlus().equals("") && !camping.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+camping.getGooglePlus()+"'> http://wwww.googleplus.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String googleplus =  (camping.getGooglePlus()).contains("http://") || camping.getGooglePlus().contains("https://") ? camping.getGooglePlus() : "http://"+camping.getGooglePlus();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleplus ));
                    startActivity(intent);
                }
            });
            linearLayout.addView(google_plus);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!camping.getInstagram().equals("") && !camping.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+camping.getInstagram()+"'> http://wwww.instagram.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String instagram =  (camping.getInstagram()).contains("http://") || camping.getInstagram().contains("https://") ? camping.getInstagram() : "http://"+camping.getInstagram();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagram));
                    startActivity(intent);
                }
            });
            linearLayout.addView(instagram);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!camping.getPinterest().equals("") && !camping.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+camping.getPinterest()+"'> http://wwww.pinterest.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pinterest =  (camping.getPinterest()).contains("http://") || camping.getPinterest().contains("https://") ? camping.getPinterest() : "http://"+camping.getPinterest();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pinterest));
                    startActivity(intent);
                }
            });
            linearLayout.addView(pinterest);
            linearLayout.addView(textView, layoutParams);
            widgets.addView(linearLayout);
        }
        if(!camping.getYoutube().equals("") && !camping.getFacebook().equals(";")){
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
            textView.setText(Html.fromHtml("<a href='"+camping.getYoutube()+"'> http://wwww.youtube.com</a>"));
            textView.setTextSize(16);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String youtube =  (camping.getYoutube()).contains("http://") || camping.getYoutube().contains("https://") ? camping.getYoutube() : "http://"+camping.getYoutube();
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
        mapView = (CustomMapView) view.findViewById(R.id.map_camping);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        String[] coordenadas = camping.getCoordenadas().split(",");
        if(coordenadas.length == 3){
            LatLng asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[2]));
            gmap.addMarker(new MarkerOptions().position(asturias).title(camping.getNombre()).draggable(true));
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

            // Orientación de la cámara. Niveles de zoom:
            // 1 - mundo
            // 5 - continentes
            // 10 - ciudades
            // 15 - calles
            // 20 - edificios

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
        intent1.putExtra("ALOJAMIENTO_TIPO", "CAMPINGS");
        intent1.putExtra("COORDENADAS", camping.getCoordenadas());
        intent1.putExtra("ALOJAMIENTO_NOMBRE", camping.getNombre());
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
