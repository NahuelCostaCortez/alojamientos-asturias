package es.uniovi.Alojamientos.Negocio.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Negocio.Common_methods;
import es.uniovi.Alojamientos.R;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> implements Filterable {

    private static final String PREFERENCES = "PREFERENCES";
    private List<Alojamiento> alojamientos;
    private List<Alojamiento> alojamientos_filtered;
    private int layout;
    private TextView textViewNoAlojamientos;
    private String type;
    private myAdapter.OnItemClickListener itemClickListener;
    protected Context context;
    private SharedPreferences prefs;

    public myAdapter(TextView noAlojamientos, Context context, int layout, String type, myAdapter.OnItemClickListener listener) {
        this.layout = layout;
        this.itemClickListener = listener;
        this.context = context;
        this.type = type;
        this.textViewNoAlojamientos = noAlojamientos;
        prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setAlojamientos(List<Alojamiento> mAlojamientos) {

        // Primero filtrar alojamientos
        array_with_filters(type, mAlojamientos);

        this.alojamientos_filtered = this.alojamientos;
        // Notificar si no existen alojamientos que no cumplan los criterios de busqueda/filtro
        if(this.textViewNoAlojamientos!= null) {
            if (alojamientos_filtered.size() == 0) {
                this.textViewNoAlojamientos.setText(R.string.no_alojamientos);
                //Toast.makeText(context, R.string.no_alojamientos, Toast.LENGTH_LONG).show();
            } else {
                this.textViewNoAlojamientos.setText("");
            }
        }

        // Notificar cambios
        notifyDataSetChanged();
    }

    // Método que filtra los alojamientos que tiene que mostrar en funcion de las preferencias
    private void array_with_filters(String type, List<Alojamiento> alojamientos) {
        List<Alojamiento> filteredList = new ArrayList<>();
        String ubicacion;
        String abierto;
        switch (type){
            case "FAVORITOS":
                this.alojamientos = alojamientos;
                break;
            case "APARTAMENTOS":
                ubicacion = prefs.getString("UBICACION_APARTAMENTO", context.getResources().getString(R.string.Any));

                if (ubicacion.equals(context.getResources().getString(R.string.asturias_west))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Occidente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_east))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Oriente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_center))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Centro de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if(prefs.getInt("MAX_PRECIO_APARTAMENTO", -1)!=-1) {
                    Common_methods alojMethods = new Common_methods();
                    for (Alojamiento aloj : alojamientos) {
                        if(alojMethods.isPriceLess(aloj.getTarifas(), prefs.getInt("MAX_PRECIO_APARTAMENTO", -1))){
                            filteredList.add(aloj);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("DESAYUNO_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getDesayunoIncluido().equals("Desayuno incluido")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("WIFI_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Wifi") || row.getServiciosEstablecimiento().contains("wifi")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CHIMENEA_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Chimenea")){
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CALEFACCION_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Calefacción")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LAVADORA_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Lavadora") || row.getServiciosEstablecimiento().contains("lavadora")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("APARCAMIENTO_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Parking") || row.getServiciosEstablecimiento().contains("Aparcamiento")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("COCINA_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Cocina")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LAVAVAJILLAS_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Lavavajillas")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LIMPIEZA_APARTAMENTO", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Servicio de Limpieza")  || row.getLimpiezaIncluida().equals("Limpieza incluida")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                this.alojamientos = alojamientos;
                break;
            case "HOTELES":
                ubicacion = prefs.getString("UBICACION_HOTEL", context.getResources().getString(R.string.Any));

                if (ubicacion.equals(context.getResources().getString(R.string.asturias_west))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Occidente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_east))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Oriente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_center))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Centro de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if(prefs.getFloat("ESTRELLAS_HOTEL", -1)!=-1) {
                    for (Alojamiento aloj : alojamientos) {
                        Common_methods alojMethods = new Common_methods();
                        if(alojMethods.isStarLess(aloj.getCategories(), prefs.getFloat("ESTRELLAS_HOTEL", -1))){
                            filteredList.add(aloj);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("AIRE_HOTEL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Aire") ||
                                row.getServiciosHabitacion().contains("Aire") ||
                                row.getServiciosComplementarios().contains("Aire")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("WIFI_HOTEL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Wifi") || row.getServiciosEstablecimiento().contains("wifi")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("PISCINA_HOTEL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().toLowerCase().contains("piscina") || row.getServiciosComplementarios().toLowerCase().contains("piscina")){
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CAFETERIA_HOTEL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().toLowerCase().contains("cafeteria") || row.getServiciosComplementarios().toLowerCase().contains("cafeteria")
                            || row.getServiciosEstablecimiento().toLowerCase().contains("cafetería") || row.getServiciosComplementarios().toLowerCase().contains("cafetería")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("TELEVISION_HOTEL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().toLowerCase().contains("television") || row.getServiciosEstablecimiento().contains("TV")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("APARCAMIENTO_HOTEL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Parking") || row.getServiciosEstablecimiento().contains("Aparcamiento")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("SECADOR_HOTEL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosComplementarios().toLowerCase().contains("secador") || row.getServiciosHabitacion().toLowerCase().contains("secador")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CAJA_HOTEL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosHabitacion().toLowerCase().contains("caja")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                this.alojamientos = alojamientos;
                break;
            case "ALBERGUES":
                abierto = prefs.getString("TIPO_ALBERGUE", context.getResources().getString(R.string.Any));
                ubicacion = prefs.getString("UBICACION_ALBERGUE", context.getResources().getString(R.string.Any));

                if (abierto.equals(context.getResources().getString(R.string.hostel))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getTipo().equals("Albergue de peregrinos") ||
                                row.getTipo().equals("Albergue Juvenil") ||
                                row.getTipo().equals("Albergue tur\u00edstico") ||
                                row.getTipo().equals("Albergue Tur\u00edstico;Camping") ||
                                row.getTipo().equals("Casa de aldea;Albergue tur\u00edstico") ||
                                row.getTipo().equals("Albergue Tur\u00edstico")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (abierto.equals(context.getResources().getString(R.string.shelter))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getTipo().equals("Refugio de monta\u0144a") ||
                                row.getTipo().equals("Refugio de Monta\u0144a")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (ubicacion.equals(context.getResources().getString(R.string.asturias_west))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Occidente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_east))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Oriente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_center))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Centro de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("INTERNET_ALBERGUE", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Internet") || row.getServiciosEstablecimiento().contains("Wifi")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CAFETERIA_ALBERGUE", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosComplementarios().contains("Cafetería") || row.getServiciosComplementarios().contains("Restaurante")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("PARKING_ALBERGUE", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Parking") ||
                                row.getServiciosEstablecimiento().contains("Aparcamiento") ||
                                row.getServiciosComplementarios().contains("Parking") || row.getServiciosComplementarios().contains("Aparcamiento")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CALEFACCION_ALBERGUE", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosComplementarios().contains("Calefacción") || row.getServiciosEstablecimiento().contains("Calefacción")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LAVADORA_ALBERGUE", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().toLowerCase().contains("lavadora")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CHIMENEA_ALBERGUE", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().toLowerCase().contains("chimenea")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }

                this.alojamientos = alojamientos;

                break;
            case "HOSTALES":
                ubicacion = prefs.getString("UBICACION_HOSTAL", context.getResources().getString(R.string.Any));
                abierto = prefs.getString("ABIERTO_HOSTAL", context.getResources().getString(R.string.Any));

                String tipo = prefs.getString("TIPO_HOSTAL", context.getResources().getString(R.string.Any));

                if (tipo.equals(context.getResources().getString(R.string.hostal))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getTipo().equals("Hostal")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (tipo.equals(context.getResources().getString(R.string.board))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getTipo().equals("Pensi\u00f3n")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }

                if (abierto.equals(context.getResources().getString(R.string.yes))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getAbiertoTodoAno().equals("Si")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (abierto.equals("No")) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getAbiertoTodoAno().equals("No")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }

                if (ubicacion.equals(context.getResources().getString(R.string.asturias_west))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Occidente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_east))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Oriente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_center))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Centro de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }

                if(prefs.getFloat("ESTRELLAS_HOSTAL", -1)!=-1) {
                    for (Alojamiento aloj : alojamientos) {
                        Common_methods alojMethods = new Common_methods();
                        if(alojMethods.isStarLess(aloj.getCategories(), prefs.getFloat("ESTRELLAS_HOSTAL", -1))){
                            filteredList.add(aloj);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }

                if(prefs.getBoolean("INTERNET_HOSTAL", false)) {
                    for (Alojamiento aloj : alojamientos) {

                        if(aloj.getServiciosEstablecimiento().toLowerCase().contains("wifi") ||
                                aloj.getServiciosHabitacion().toLowerCase().contains("wifi") ||
                                aloj.getServiciosComplementarios().toLowerCase().contains("wifi")) {
                            filteredList.add(aloj);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CALEFACCION_HOSTAL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Calefacción") ||
                                row.getServiciosHabitacion().contains("Calefacción") ||
                                row.getServiciosComplementarios().contains("Calefacción")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("RESTAURANTE_HOSTAL", false)){
                    for (Alojamiento row : alojamientos) {
                        if(row.getNombre().equals("Niserias")){
                            System.out.print("");
                        }
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Restaurante") || row.getServiciosComplementarios().contains("Restaurante")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("PISCINA_HOSTAL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().toLowerCase().contains("piscina")){
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LAVANDERIA_HOSTAL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().toLowerCase().contains("lavanderia") || row.getServiciosEstablecimiento().toLowerCase().contains("lavandería")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("TELEVISION_HOSTAL", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().toLowerCase().contains("television") || row.getServiciosEstablecimiento().contains("TV")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }

                this.alojamientos = alojamientos;
                break;
            case "CAMPINGS":
                abierto = prefs.getString("ABIERTO_CAMPING", context.getResources().getString(R.string.Any));
                ubicacion = prefs.getString("UBICACION_CAMPING", context.getResources().getString(R.string.Any));

                if (abierto.equals(context.getResources().getString(R.string.yes))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getAbiertoTodoAno().equals("Si")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (abierto.equals("No")) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getAbiertoTodoAno().equals("No")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (ubicacion.equals(context.getResources().getString(R.string.asturias_west))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Occidente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_east))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Oriente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_center))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Centro de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("INTERNET_CAMPING", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Internet")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CAFETERIA_CAMPING", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosComplementarios().contains("Cafetería") || row.getServiciosComplementarios().contains("Restaurante")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("PARKING_CAMPING", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosComplementarios().contains("Parking") || row.getServiciosComplementarios().contains("Aparcamiento")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("SUPERMERCADO_CAMPING", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosComplementarios().contains("Supermercado")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LAVANDERIA_CAMPING", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("lavanderia") || row.getServiciosEstablecimiento().contains("lavandería")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                this.alojamientos = alojamientos;
                break;

            case "CASAS":
                ubicacion = prefs.getString("UBICACION_CASA", context.getResources().getString(R.string.Any));

                if (ubicacion.equals(context.getResources().getString(R.string.asturias_west))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Occidente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_east))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Oriente de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                else if (ubicacion.equals(context.getResources().getString(R.string.asturias_center))) {
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getZona().equals("Centro de Asturias")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if(prefs.getInt("MAX_PRECIO_CASA", -1)!=-1) {
                    Common_methods alojMethods = new Common_methods();
                    for (Alojamiento aloj : alojamientos) {
                        if(alojMethods.isPriceLess(aloj.getTarifas(), prefs.getInt("MAX_PRECIO_CASA", -1))){
                            filteredList.add(aloj);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("DESAYUNO_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getDesayunoIncluido().equals("Desayuno incluido")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("WIFI_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Wifi") || row.getServiciosEstablecimiento().contains("wifi")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CHIMENEA_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Chimenea")){
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("CALEFACCION_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Calefacción")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LAVADORA_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Lavadora") || row.getServiciosEstablecimiento().contains("lavadora")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("APARCAMIENTO_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Parking") || row.getServiciosEstablecimiento().contains("Aparcamiento")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("COCINA_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Cocina")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LAVAVAJILLAS_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        if (row.getServiciosEstablecimiento().contains("Lavavajillas")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                if (prefs.getBoolean("LIMPIEZA_CASA", false)){
                    for (Alojamiento row : alojamientos) {
                        // name match condition.
                        if (row.getServiciosEstablecimiento().contains("Servicio de Limpieza") || row.getLimpiezaIncluida().equals("Limpieza incluida")) {
                            filteredList.add(row);
                        }
                    }
                    alojamientos = new ArrayList<Alojamiento>(filteredList);
                    filteredList = new ArrayList<>();
                }
                this.alojamientos = alojamientos;
                break;
        }
    }

    @NonNull
    @Override
    public myAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        // Cogemos el contexto del padre e inflamos
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        // Aqui podemos inicializar el context, que lo usaremos para picasso (en la nueva version de Picasso no hace falta)
        context = parent.getContext();
        // Le pasamos el viewHolder esa vista inflada
        return new myAdapter.ViewHolder(v);
    }

    // Volcar los datos del modelo en nuestros atributos del ViewHolder
    // Este método solo se llama cuando se crea el RecyclerView, luego no se vuelve a renderizar por cada uno de los objetos,
    // por eso tenemos el  itemView.setOnClickListener para que se llame cada vez que se haga click en algun elemento
    // este listener se sobrescribirá en la clase en la que se cree el adaptador
    @Override
    public void onBindViewHolder(@NonNull myAdapter.ViewHolder viewHolder, int i) {
        if(alojamientos_filtered != null) {
            viewHolder.bind(alojamientos_filtered.get(i), itemClickListener);
        }
        else{
            viewHolder.name.setText("Aún no hay información");
        }

    }

    // Retorna el número de items que vamos a tener
    @Override
    public int getItemCount() {
        if(alojamientos_filtered == null)
            return 1;
        else
            return alojamientos_filtered.size();
    }

    // Se filtran los alojamientos ya mostrados en pantalla en función de las búsquedas que haga el usuario
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = (String) charSequence;
                String[] split_filter = charString.split("&&");
                charString = split_filter[0];
                List<Alojamiento> filteredList = new ArrayList<>();

                if (charString.isEmpty()) {
                    alojamientos_filtered = alojamientos;
                } else {
                    if(split_filter[1].equals("NOMBRE")) {
                        for (Alojamiento row : alojamientos) {
                            // name match condition.
                            if (row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                    }
                    else if(split_filter[1].equals("LOCALIZACION")) {
                        for (Alojamiento row : alojamientos) {
                            // Concejo match condition.
                            if (row.getConcejo().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                    }
                    else if(split_filter[1].equals("UBICACION")) {
                        for (Alojamiento row : alojamientos) {
                            // zona match condition.
                            if (row.getZona().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                    }
                    alojamientos_filtered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = alojamientos_filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                alojamientos_filtered = (ArrayList<Alojamiento>) filterResults.values;

                if(alojamientos_filtered.size() == 0)
                    textViewNoAlojamientos.setText(R.string.no_alojamientos);
                else
                    textViewNoAlojamientos.setText("");

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    // Ahora en vez de hacer pasarle el Holder a la vista con setTag, le pasamos al Holder la vista
    // y a partir de ahí extraemos la referencia/referencias que queremos
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView imageViewPoster;
        public TextView location;

        public ViewHolder(View v){
            super(v);
            // El nombre de nuestro ViewHolder es el del textView que tenemos (en recycler_view_item)
            this.name = v.findViewById(R.id.card_view_image_title);
            this.imageViewPoster = v.findViewById(R.id.card_view_image);
            this.location = v.findViewById(R.id.card_view_image_button_title);
        }

        public void bind(final Alojamiento alojamiento, final myAdapter.OnItemClickListener listener){

            // Volcado de datos
            this.name.setText(alojamiento.getNombre());
            this.location.setText(alojamiento.getConcejo()+ " - " + alojamiento.getZona());

            // NUEVO - PICASSO (para centrar imagen al card)
            //Picasso.get().load(R.drawable.imagen_default).fit().into(imageViewPoster);
            String url = "https://www.turismoasturias.es"+alojamiento.getSlide().split(";")[0];
            //Picasso.get().load(url).placeholder(R.drawable.loading).into(imageViewPoster);
            Picasso.get().load(url).into(imageViewPoster);
            //this.imageViewPoster.setImageResource(movie.poster); -> esto ya no se necesita, se especifica con picasso
            // Definimos como trabaja el click listener
            // El itemView viene de la clase ViewHolder heredada del RecycledView (public static class ViewHolder extends RecyclerView.ViewHolder)
            // y se refiere al actual itemView. Es decir, cada elemento (cada item de la lista) es una vista en sí
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // getAdapterPosition() consulta la posicion cada vez que hagamos el click
                    listener.onItemClick(alojamiento, getAdapterPosition());
                }
            });
        }
    }

    // Vamos a tener que implementar nuestro propio onItemClickListener
    public interface OnItemClickListener{
        void onItemClick(Alojamiento alojamiento, int position);
    }
}
