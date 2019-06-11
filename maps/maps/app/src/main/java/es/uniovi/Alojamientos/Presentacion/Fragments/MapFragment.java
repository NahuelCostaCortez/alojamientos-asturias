package es.uniovi.Alojamientos.Presentacion.Fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import es.uniovi.Alojamientos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;
    private GoogleMap gmap;
    // El que va a capturar el layout
    private MapView mapView;
    // Coordenadas de todos los alojamientos
    private List<LatLng> coordenadas;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    // Cuando el fragmento ya cargó la vista
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) rootView.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    public void getCoordinates(List<LatLng> coordinates){
        this.coordenadas = coordinates;
    }

    public void drawCoordinates(List<LatLng> coordinates, GoogleMap map){
        for(int i=0; i < coordinates.size(); i++){
            map.addMarker(new MarkerOptions().position(coordinates.get(i)));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng asturias = new LatLng(43.32, -5.84);
        //gmap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
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

        /*if(coordenadas!= null){
            drawCoordinates(coordenadas, gmap);
        }*/

        // Bearing - orientación hacia el este
        // Tilt - inclinación para ver la altura de los edificios
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(asturias)
                .zoom(8)       // limite -> 21
                //.bearing(90)    // limite -> 0 - 365 grados
                //.tilt(30)       // limite -> 90
                .build();
        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }


}
