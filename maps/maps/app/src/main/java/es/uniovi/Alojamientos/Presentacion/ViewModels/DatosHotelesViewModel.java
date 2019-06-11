package es.uniovi.Alojamientos.Presentacion.ViewModels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import es.uniovi.Alojamientos.Negocio.RepositoryCreate;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Negocio.Repository;

public class DatosHotelesViewModel extends ViewModel {

    private Repository repository;
    private List<Alojamiento> mAlojamientos;
    private Alojamiento currentAlojamiento;
    private LiveData<MainActivity.AppStatus> appStatus;

    public DatosHotelesViewModel(){
      repository = RepositoryCreate.getRepository();
      mAlojamientos = repository.getHotelesList();
      appStatus = repository.getAppStatus();
    }

    public List<Alojamiento> getHotelesList() {
        return mAlojamientos;
    }

    public void updateAlojamientos(){
      repository.updateAlojamientosData();
    }

    public Alojamiento getAlojamiento() {
      return currentAlojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
      currentAlojamiento = alojamiento;
    }

    public LiveData<MainActivity.AppStatus> getAppStatus() {
      return appStatus;
    }

    public void updateAppStatus(){
      repository.updateAppStatus();
    }

}
