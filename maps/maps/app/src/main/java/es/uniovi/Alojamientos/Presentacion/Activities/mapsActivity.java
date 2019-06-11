package es.uniovi.Alojamientos.Presentacion.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosAlberguesViewModel;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosApartamentosViewModel;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosCampingsViewModel;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosCasasViewModel;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosHostalesViewModel;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosHotelesViewModel;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosAlojamientoViewModel;
import es.uniovi.Alojamientos.R;

public class mapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private List<Alojamiento> alojamientos;
    private DatosAlojamientoViewModel alojamientosViewModel;
    private String alojamiento_tipo;
    private String alojamiento_coordenadas;
    private String alojamiento_nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        alojamientosViewModel = ViewModelProviders.of(this).get(DatosAlojamientoViewModel.class);

        Intent intent = getIntent();
        alojamiento_tipo = intent.getStringExtra("ALOJAMIENTO_TIPO");
        alojamiento_coordenadas = intent.getStringExtra("COORDENADAS");
        alojamiento_nombre = intent.getStringExtra("ALOJAMIENTO_NOMBRE");

        if(alojamiento_tipo != null){
            switch (alojamiento_tipo){
                case "CASAS":
                    DatosCasasViewModel casasViewModel = ViewModelProviders.of(this).get(DatosCasasViewModel.class);
                    alojamientos = (ArrayList<Alojamiento>) casasViewModel.getCasasList();
                    break;
                case "APARTAMENTOS":
                    DatosApartamentosViewModel apartamentosViewModel = ViewModelProviders.of(this).get(DatosApartamentosViewModel.class);
                    alojamientos = (ArrayList<Alojamiento>) apartamentosViewModel.getApartamentosList();
                    break;
                case "HOTELES":
                    DatosHotelesViewModel hotelesViewModel = ViewModelProviders.of(this).get(DatosHotelesViewModel.class);
                    alojamientos = (ArrayList<Alojamiento>) hotelesViewModel.getHotelesList();
                    break;
                case "ALBERGUES":
                    DatosAlberguesViewModel alberguesViewModel = ViewModelProviders.of(this).get(DatosAlberguesViewModel.class);
                    alojamientos = (ArrayList<Alojamiento>) alberguesViewModel.getAlberguesList();
                    break;
                case "CAMPINGS":
                    DatosCampingsViewModel campingsViewModel = ViewModelProviders.of(this).get(DatosCampingsViewModel.class);
                    alojamientos = (ArrayList<Alojamiento>) campingsViewModel.getCampingsList();
                    break;
                case "HOSTALES":
                    DatosHostalesViewModel hostalesViewModel = ViewModelProviders.of(this).get(DatosHostalesViewModel.class);
                    alojamientos = (ArrayList<Alojamiento>) hostalesViewModel.getHostalesList();
                    break;
            }
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng asturias;
        for (Alojamiento aloj: alojamientos) {
            String[] coordenadas = aloj.getCoordenadas().split(",");
            if (coordenadas.length == 3) {
                asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[2]));
                mMap.addMarker(new MarkerOptions().position(asturias).title(aloj.getNombre()).draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(asturias));
                // Para limitarlo a una zona concreta
                mMap.setMinZoomPreference(5);
                mMap.setMaxZoomPreference(15);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(asturias)
                        .zoom(10)       // limite -> 21
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else if (coordenadas.length > 1) {
                asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[1]));
                mMap.addMarker(new MarkerOptions().position(asturias).title(aloj.getNombre()).draggable(true));
            }
        }


        if(alojamiento_coordenadas != null){
            String[] coordenadas = alojamiento_coordenadas.split(",");
            if(coordenadas.length == 3){
                asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[2]));
                Marker marker = mMap.addMarker(new MarkerOptions().position(asturias).title(alojamiento_nombre).draggable(true));
                marker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(asturias));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(asturias)
                        .zoom(14)       // limite -> 21
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            else if(coordenadas.length > 1) {
                asturias = new LatLng(Double.valueOf(coordenadas[0]), Double.valueOf(coordenadas[1]));
                Marker marker = mMap.addMarker(new MarkerOptions().position(asturias).title(alojamiento_nombre).draggable(true));
                marker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(asturias));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(asturias)
                        .zoom(14)       // limite -> 21
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

        else {
            asturias = new LatLng(43.319110, -5.798872);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(asturias));

            // Para limitarlo a una zona concreta
            mMap.setMinZoomPreference(5);
            mMap.setMaxZoomPreference(15);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(asturias)
                    .zoom(10)       // limite -> 21
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        switch (alojamiento_tipo){
            case "CASAS":
                for (Alojamiento aloj: alojamientos) {
                    if(aloj.getNombre().equals(marker.getTitle())){
                        alojamientosViewModel.setAlojamiento(aloj);
                        Intent intent = new Intent(this, CasasDetailsActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                break;
            case "APARTAMENTOS":
                for (Alojamiento aloj: alojamientos) {
                    if(aloj.getNombre().equals(marker.getTitle())){
                        alojamientosViewModel.setAlojamiento(aloj);
                        Intent intent = new Intent(this, ApartamentosDetailsActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                break;
            case "HOTELES":
                for (Alojamiento aloj: alojamientos) {
                    if(aloj.getNombre().equals(marker.getTitle())){
                        alojamientosViewModel.setAlojamiento(aloj);
                        Intent intent = new Intent(this, HotelesDetailsActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                break;
            case "ALBERGUES":
                for (Alojamiento aloj: alojamientos) {
                    if(aloj.getNombre().equals(marker.getTitle())){
                        alojamientosViewModel.setAlojamiento(aloj);
                        Intent intent = new Intent(this, AlberguesDetailsActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                break;
            case "CAMPINGS":
                for (Alojamiento aloj: alojamientos) {
                    if(aloj.getNombre().equals(marker.getTitle())){
                        alojamientosViewModel.setAlojamiento(aloj);
                        Intent intent = new Intent(this, CampingDetailsActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                break;
            case "HOSTALES":
                for (Alojamiento aloj: alojamientos) {
                    if(aloj.getNombre().equals(marker.getTitle())){
                        alojamientosViewModel.setAlojamiento(aloj);
                        Intent intent = new Intent(this, HostalesDetailsActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                break;
        }
    }
}
