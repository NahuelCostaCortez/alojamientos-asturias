package es.uniovi.Alojamientos.Presentacion.Fragments;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Negocio.Adapters.myAdapter;
import es.uniovi.Alojamientos.Negocio.Common_methods;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosAlberguesViewModel;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosAlojamientoViewModel;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosCasasViewModel;
import es.uniovi.Alojamientos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class favoritos_fragment extends Fragment {

    private ArrayList<Alojamiento> alojamientos;
    private RecyclerView mRecyclerView;
    private TextView no_favoritos;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatosCasasViewModel alojamientosViewModel;
    private favoritos_fragment.Callbacks mCallback;
    private static final String ACCOMODATION_LIST_FILENAME = "FAVOURITES_LIST";
    static SharedPreferences prefs;
    private static final String PREFERENCES = "PREFERENCES";
    private String APP_STATUS = String.valueOf(MainActivity.AppStatus.UPDATED);

    public interface Callbacks {
        public void onFavoritoSelected(Alojamiento alojamiento, String tipo);
    }

    // Saber qué actividad lo alberga para pasarle los eventos del callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity != null) {
            mCallback = (favoritos_fragment.Callbacks) activity;
        }
        else {
            throw new ClassCastException(activity.toString() +
                    " must implement Callbacks");
        }
    }

    public favoritos_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter != null) {
            alojamientos = Common_methods.restoreList(getActivity());
            ((myAdapter) mAdapter).setAlojamientos(alojamientos);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if (prefs.getString("THEME", "").equals("LIGHT") ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (prefs.getString("THEME", "").equals("DARK")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Lista guardada en fichero?
        alojamientos = Common_methods.restoreList(getActivity());

        getActivity().setTitle(R.string.favs);

        // Inflar el layout
        View view;
        if(alojamientos!=null && alojamientos.size()>0){
            view = inflater.inflate(R.layout.favoritos_fragment, container, false);
        }
        else{
            view = inflater.inflate(R.layout.favoritos_fragment_empty, container, false);
            return view;
        }

        // Inicializar el ListView
        mRecyclerView = view.findViewById(R.id.recycler_view_favoritos);
        // Es el layout sobre el que va el widget
        mLayoutManager = new LinearLayoutManager(view.getContext());
        // Es nuestro adaptador. R.layout.recycler_view_item es el layout con el que queremos inflar la vista
        mAdapter = new myAdapter(null, getContext(), R.layout.recycler_view_item, "FAVORITOS", new myAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Alojamiento alojamiento, int position) {
                item_details(alojamiento);
            }
        });
        ((myAdapter) mAdapter).setAlojamientos(alojamientos);
        // Si sabemos que el layout no va a cambiar, usando este método se mejora bastante el performance al necesitar menos recursos
        mRecyclerView.setHasFixedSize(true);
        // Animaciones para el recyclerView
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        alojamientosViewModel = ViewModelProviders.of(this).get(DatosCasasViewModel.class);
        alojamientosViewModel.getAppStatus().observe(this, new Observer<MainActivity.AppStatus>() {
            @Override
            public void onChanged(MainActivity.AppStatus status) {
                // Cuando terminan de actualizarse los datos se retira la barra de progreso
                if(status.equals(MainActivity.AppStatus.UPDATED) && APP_STATUS.equals(String.valueOf(MainActivity.AppStatus.DOWNLOADING))){
                    Toast.makeText(getActivity(), getString(R.string.completed_sync), Toast.LENGTH_SHORT).show();
                }
                if(status.equals(MainActivity.AppStatus.DOWNLOADING)){
                    APP_STATUS = String.valueOf(MainActivity.AppStatus.DOWNLOADING);
                }
                if(status.equals(MainActivity.AppStatus.ERROR)) {
                    Toast.makeText(getActivity(), getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Retornar el layout
        return view;
    }

    // PULSACION DE ELEMENTO
    private void item_details(Alojamiento alojamiento) {

        // Hacer que MainActivity implemente un interfaz para pasarle los datos del camping a renderizar y que MainActivity
        // de paso a un fragmento con el detalle del camping
        mCallback.onFavoritoSelected(alojamiento, getType(alojamiento.getTipo()));

    }


    private String getType(String tipo) {
        if(tipo.equals("Albergue de peregrinos") ||
                tipo.equals("Albergue Juvenil") ||
                tipo.equals("Albergue tur\u00edstico") ||
                tipo.equals("Albergue Tur\u00edstico;Camping") ||
                tipo.equals("Casa de aldea;Albergue tur\u00edstico") ||
                tipo.equals("Albergue Tur\u00edstico") || tipo.equals("Refugio de monta\u0144a") ||
                tipo.equals("Refugio de Monta\u0144a")){
            return "ALBERGUE";
        }
        else if(tipo.equals("Casa de aldea;Apartamento rural") ||
                tipo.equals("Casa de aldea;Apartamento tur\u00edstico") ||
                tipo.equals("Casa de aldea;Hoteles-albergue") ||
                tipo.equals("Casa de aldea") ||
                tipo.equals("Casa de aldea;Albergue tur\u00edstico") ||
                tipo.equals("Casa de aldea;Casa de aldea") ||
                tipo.equals("Casa de Aldea \u00cdntegra") ||
                tipo.equals("Casa de aldea;Vivienda vacacional") ||
                tipo.equals("Hotel rural;Casa de aldea") ||
                tipo.equals("Casa de Aldea") ||
                tipo.equals("Vivienda vacacional;Casa de aldea")){
            return "CASA";
        }
        else if(tipo.equals("Apartamentos rurales") ||
                tipo.equals("Apartamento rural") ||
                tipo.equals("Apartamento rural;Camping") ||
                tipo.equals("Apartamento rural;Hotel") ||
                tipo.equals("Apartamento rural;Vivienda vacacional") ||
                tipo.equals("Apartamento tur\u00edstico") ||
                tipo.equals("Apartamento tur\u00edstico;Apartamento tur\u00edstico") ||
                tipo.equals("albergue tur\u00edstico") ||
                tipo.equals("Hoteles-albergue") ||
                tipo.equals("Hotel;Apartamento rural") ||
                tipo.equals("Apartamento Rural") ||
                tipo.equals("Apartamento Tur\u00edstico")){
            return "APARTAMENTO";
        }
        else if(tipo.equals("Albergue Tur\u00edstico;Camping") ||
                tipo.equals("Apartamento rural;Camping") ||
                tipo.equals("Camping")){
            return "CAMPING";
        }
        else if(tipo.equals("Hostal") || tipo.equals("Pensi\u00f3n")){
            return "HOSTAL";
        }
        else if(tipo.equals("Apartamento rural;Hotel") ||
                tipo.equals("Apartamento rural;Hotel rural") ||
                tipo.equals("Casa de aldea;Hoteles-albergue")||
                tipo.equals("Hotel") ||
                tipo.equals("Hotel rural") ||
                tipo.equals("Hoteles-albergue") ||
                tipo.equals("Hotel;Apartamento rural") ||
                tipo.equals("Hotel rural;Casa de aldea") ||
                tipo.equals("Hotel;Vivienda vacacional") ||
                tipo.equals("Vivienda vacacional;Hotel rural")){
            return "HOTEL";
        }
        return null;
    }

}
