package es.uniovi.Alojamientos.Negocio;

import java.util.List;

import androidx.lifecycle.LiveData;
import es.uniovi.Alojamientos.Datos.WebService;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;

public abstract class RepositoryCreate {

    // En esta clase se crea el singleton del repositorio
    private static Repository INSTANCE;
    WebService webService;
    private LiveData<List<Alojamiento>> alojamientos;

    public static Repository getRepository() {
        if (INSTANCE == null) {
            synchronized (RepositoryCreate.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = new Repository();
                }
            }
        }
        return INSTANCE;
    }

    public RepositoryCreate(){
        webService = new WebService();
        updateAlojamientosData();
    }


    public void updateAlojamientosData(){
        alojamientos = webService.getAlojamientos();
    }

    public LiveData<List<Alojamiento>> getAlojamientosList(){
        return alojamientos;
    }
}
