package es.uniovi.Alojamientos.Presentacion.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Negocio.Adapters.myAdapter;
import es.uniovi.Alojamientos.Presentacion.Activities.CasasFilters;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;
import es.uniovi.Alojamientos.Presentacion.Activities.mapsActivity;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosCasasViewModel;
import es.uniovi.Alojamientos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class casas_fragment extends Fragment {

    private static final String CASAS_LIST_FILENAME = "LIST_CASAS";
    private static final String PREFERENCES = "PREFERENCES";
    static SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ArrayList<Alojamiento> casas_list;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatosCasasViewModel casasViewModel;
    private casas_fragment.Callbacks mCallback;
    private SearchView searchView;
    private TextView noAlojamientos;
    private String filtro_nombre = "";
    private String query_hint;
    private String APP_STATUS = String.valueOf(MainActivity.AppStatus.UPDATED);

    private View view;

    public interface Callbacks {
        public void onCasasSelected(Alojamiento alojamiento);
    }

    // Saber qué actividad lo alberga para pasarle los eventos del callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity != null) {
            mCallback = (casas_fragment.Callbacks) activity;
        }
        else {
            throw new ClassCastException(activity.toString() +
                    " must implement Callbacks");
        }
    }

    public casas_fragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prefsEditor = prefs.edit();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.casas_fragment, container, false);
        noAlojamientos = view.findViewById(R.id.text_view_no_alojamientos);
        getActivity().setTitle(R.string.housing);

        String current_search_by = prefs.getString("CURRENT_SEARCH_BY","");
        if(!current_search_by.equals("")){
            switch (current_search_by) {
                case "NOMBRE":
                    query_hint = getString(R.string.search_by_name);
                    break;
                case "LOCALIZACION":
                    query_hint = getString(R.string.search_by_location);
                    break;
                case "UBICACION":
                    query_hint = getString(R.string.search_by_ubication);
                    break;
            }
        }
        else
            query_hint = getString(R.string.search_by_name);

        mRecyclerView = view.findViewById(R.id.recycler_view_casas);
        // Es el layout sobre el que va el widget
        String layout_type = prefs.getString("LAYOUT_TYPE","");
        if(!layout_type.equals("")){
            switch (layout_type) {
                case "LINEAR":
                    mLayoutManager = new LinearLayoutManager(view.getContext());
                    break;
                case "GRID":
                    mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    break;
            }
        }
        else
            mLayoutManager = new LinearLayoutManager(view.getContext());
        // Es nuestro adaptador. R.layout.recycler_view_item es el layout con el que queremos inflar la vista
        mAdapter = new myAdapter(noAlojamientos, getContext(), R.layout.recycler_view_item, "CASAS", new myAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Alojamiento alojamiento, int position) {
                item_details(alojamiento);
            }
        });

        // Si sabemos que el layout no va a cambiar, usando este método se mejora bastante el performance al necesitar menos recursos
        mRecyclerView.setHasFixedSize(true);
        // Animaciones para el recyclerView
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        casasViewModel = ViewModelProviders.of(this).get(DatosCasasViewModel.class);
        casas_list = (ArrayList<Alojamiento>) casasViewModel.getCasasList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String current_order_by = prefs.getString("CURRENT_ORDER_BY","");
            if(!current_order_by.equals("")){
                switch (current_order_by) {
                    case "ALFABETICAMENTE":
                        casas_list.sort(new Comparator<Alojamiento>() {
                            @Override
                            public int compare(Alojamiento o1, Alojamiento o2) {
                                return o1.getNombre().compareTo(o2.getNombre());
                            }
                        });
                        break;
                    case "LOCALIZACION":
                        casas_list.sort(new Comparator<Alojamiento>() {
                            @Override
                            public int compare(Alojamiento o1, Alojamiento o2) {
                                return o1.getConcejo().compareTo(o2.getConcejo());
                            }
                        });
                        break;
                    case "ZONA":
                        casas_list.sort(new Comparator<Alojamiento>() {
                            @Override
                            public int compare(Alojamiento o1, Alojamiento o2) {
                                return o1.getZona().compareTo(o2.getZona());
                            }
                        });
                        break;
                }
            }
            else{
                casas_list.sort(new Comparator<Alojamiento>() {
                    @Override
                    public int compare(Alojamiento o1, Alojamiento o2) {
                        return o1.getNombre().compareTo(o2.getNombre());
                    }
                });
            }
        }
        ((myAdapter) mAdapter).setAlojamientos(casas_list);

        casasViewModel.getAppStatus().observe(this, new Observer<MainActivity.AppStatus>() {
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

        if(savedInstanceState != null){
            //casas_list = savedInstanceState.getParcelableArrayList(CASAS_LIST_FILENAME);
            //((myAdapter) mAdapter).setAlojamientos(casas_list);
            if(savedInstanceState.getInt("VIEW") == 0) {
                mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(mLayoutManager);
            }
            else {
                mLayoutManager = new LinearLayoutManager(view.getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
            }

            filtro_nombre = savedInstanceState.getString("FILTRO_NOMBRE");
            if(!filtro_nombre .equals("")){
                //String current_search_by = prefs.getString("CURRENT_SEARCH_BY_CASAS","");
                if(!current_search_by.equals("")){
                    switch (current_search_by) {
                        case "NOMBRE":
                            ((myAdapter) mAdapter).getFilter().filter(filtro_nombre+"&&NOMBRE");
                            break;
                        case "LOCALIZACION":
                            ((myAdapter) mAdapter).getFilter().filter(filtro_nombre+"&&LOCALIZACION");
                            break;
                        case "UBICACION":
                            ((myAdapter) mAdapter).getFilter().filter(filtro_nombre+"&&UBICACION");
                            break;
                    }
                }
            }

        }

        return view;
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mLayoutManager instanceof StaggeredGridLayoutManager) {
            outState.putInt("VIEW",0);
            prefsEditor.putString("LAYOUT_TYPE", "GRID");
            prefsEditor.commit();
        } else if(mLayoutManager instanceof LinearLayoutManager) {
            outState.putInt("VIEW",1);
            prefsEditor.putString("LAYOUT_TYPE", "LINEAR");
            prefsEditor.commit();
        }

        outState.putString("FILTRO_NOMBRE", filtro_nombre);
    }

    // PULSACION DE ELEMENTO (CAMPING)
    private void item_details(Alojamiento alojamiento) {

        // Hacer que MainActivity implemente un interfaz para pasarle los datos del camping a renderizar y que MainActivity
        // de paso a un fragmento con el detalle del camping
        mCallback.onCasasSelected(alojamiento);
    }

    // MENÚ DE OPCIONES EN EL TOOLBAR
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        // Lo inflamos con el layout que creamos en res/menu
        inflater.inflate(R.menu.menu_options,menu);
        final MenuItem item = menu.findItem(R.id.search_item);
        searchView = (SearchView) item.getActionView();
        if(!filtro_nombre.equals(getString(R.string.search_by_name)) && filtro_nombre.equals(getString(R.string.search_by_location))
                        && filtro_nombre.equals(getString(R.string.search_by_ubication))){
            searchView.setQuery(filtro_nombre, false);
        }
        else{
            searchView.setQueryHint(query_hint);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtro_nombre = newText;
                String current_search_by = prefs.getString("CURRENT_SEARCH_BY","");
                if(!current_search_by.equals("")){
                    switch (current_search_by) {
                        case "NOMBRE":
                            ((myAdapter) mAdapter).getFilter().filter(newText+"&&NOMBRE");
                            break;
                        case "LOCALIZACION":
                            ((myAdapter) mAdapter).getFilter().filter(newText+"&&LOCALIZACION");
                            break;
                        case "UBICACION":
                            ((myAdapter) mAdapter).getFilter().filter(newText+"&&UBICACION");
                            break;
                    }
                }
                else
                    ((myAdapter) mAdapter).getFilter().filter(newText+"&&NOMBRE");


                return false;
            }
        });

        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemsVisibility(menu, item, false);
            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setItemsVisibility(menu, item, true);
                return false;
            }
        });

    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    // OPCIONES DEL MENÚ DE OPCIONES DEL TOOLBAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.options_view:
                showMenu(view);
                break;
            case R.id.options_filters:
                Intent intent = new Intent(getActivity(), CasasFilters.class);
                startActivity(intent);
                break;
            case R.id.options_search_by:
                showMenuSearchBy(view);
                break;
            case R.id.options_order_by:
                showOrderByOption(view);
                break;
            case R.id.options_map:
                Intent intent1 = new Intent(getActivity(), mapsActivity.class);
                intent1.putExtra("ALOJAMIENTO_TIPO", "CASAS");
                startActivity(intent1);
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showOrderByOption(View v) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            int selectedItem = 0;
            String current_order_by = prefs.getString("CURRENT_ORDER_BY","");
            if(!current_order_by.equals("")){
                switch (current_order_by) {
                    case "ALFABETICAMENTE":
                        selectedItem = 0;
                        break;
                    case "LOCALIZACION":
                        selectedItem = 2;
                        break;
                    case "ZONA":
                        selectedItem = 1;
                        break;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.order_by)
                    .setSingleChoiceItems(R.array.ordenar_por, selectedItem, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    prefsEditor.putString("CURRENT_ORDER_BY", "ALFABETICAMENTE");
                                    prefsEditor.commit();
                                    casas_list.sort(new Comparator<Alojamiento>() {
                                        @Override
                                        public int compare(Alojamiento o1, Alojamiento o2) {
                                            return o1.getNombre().compareTo(o2.getNombre());
                                        }
                                    });
                                    ((myAdapter) mAdapter).setAlojamientos(casas_list);
                                    dialog.cancel();
                                    break;
                                case 1:
                                    prefsEditor.putString("CURRENT_ORDER_BY", "ZONA");
                                    prefsEditor.commit();
                                    casas_list.sort(new Comparator<Alojamiento>() {
                                        @Override
                                        public int compare(Alojamiento o1, Alojamiento o2) {
                                            return o1.getZona().compareTo(o2.getZona());
                                        }
                                    });
                                    ((myAdapter) mAdapter).setAlojamientos(casas_list);
                                    dialog.cancel();
                                    break;
                                case 2:
                                    prefsEditor.putString("CURRENT_ORDER_BY", "LOCALIZACION");
                                    prefsEditor.commit();
                                    casas_list.sort(new Comparator<Alojamiento>() {
                                        @Override
                                        public int compare(Alojamiento o1, Alojamiento o2) {
                                            return o1.getConcejo().compareTo(o2.getConcejo());
                                        }
                                    });
                                    ((myAdapter) mAdapter).setAlojamientos(casas_list);
                                    dialog.cancel();
                                    break;
                            }
                        }
                    });
            builder.show();
        }
        else
            Toast.makeText(view.getContext(), getString(R.string.version_error), Toast.LENGTH_LONG).show();
    }

    // MENÚ QUE APARECE TRAS PULSAR EN CAMBIAR VISTA
    public void showMenu(View v)
    {
        int selectedItem = 1;
        if(mLayoutManager instanceof StaggeredGridLayoutManager) {
            selectedItem = 0;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.change_look)
                .setSingleChoiceItems(R.array.cambiar_vista, selectedItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                                //mRecyclerView.setLayoutManager(mLayoutManager);
                                prefsEditor.putString("LAYOUT_TYPE", "GRID");
                                prefsEditor.commit();
                                // Con staggered se pueden modificar el tamaño de cada celda/rejilla
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                dialog.cancel();
                                break;
                            case 1:
                                mLayoutManager = new LinearLayoutManager(view.getContext());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                prefsEditor.putString("LAYOUT_TYPE", "LINEAR");
                                prefsEditor.commit();
                                dialog.cancel();
                                break;
                        }
                    }
                });
        builder.show();
    }

    public void showMenuSearchBy(View v) {
        String current_search_by = prefs.getString("CURRENT_SEARCH_BY","");
        int checkedItem = 0;
        if(!current_search_by.equals("")){
            switch (current_search_by) {
                case "NOMBRE":
                    checkedItem = 0;
                    break;
                case "LOCALIZACION":
                    checkedItem = 1;
                    break;
                case "UBICACION":
                    checkedItem = 2;
                    break;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.search_by)
                .setSingleChoiceItems(R.array.buscar_por, checkedItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                prefsEditor.putString("CURRENT_SEARCH_BY", "NOMBRE");
                                prefsEditor.commit();

                                query_hint =  getString(R.string.search_by_name);
                                searchView.setQueryHint(query_hint);

                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        filtro_nombre = newText;
                                        ((myAdapter) mAdapter).getFilter().filter(newText+"&&NOMBRE");
                                        return false;
                                    }
                                });
                                dialog.cancel();
                                break;
                            case 1:
                                prefsEditor.putString("CURRENT_SEARCH_BY", "LOCALIZACION");
                                prefsEditor.commit();

                                query_hint =  getString(R.string.search_by_location);
                                searchView.setQueryHint(query_hint);

                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        filtro_nombre = newText;
                                        ((myAdapter) mAdapter).getFilter().filter(newText+"&&LOCALIZACION");
                                        return false;
                                    }
                                });
                                dialog.cancel();
                                break;
                            case 2:
                                prefsEditor.putString("CURRENT_SEARCH_BY", "UBICACION");
                                prefsEditor.commit();

                                query_hint =  getString(R.string.search_by_ubication);
                                searchView.setQueryHint(query_hint);

                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        filtro_nombre = newText;
                                        ((myAdapter) mAdapter).getFilter().filter(newText+"&&UBICACION");
                                        return false;
                                    }
                                });
                                dialog.cancel();
                                break;
                        }
                    }
                });
        builder.show();
    }
}
