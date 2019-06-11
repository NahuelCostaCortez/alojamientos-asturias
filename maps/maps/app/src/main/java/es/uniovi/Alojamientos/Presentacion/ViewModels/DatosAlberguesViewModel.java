package es.uniovi.Alojamientos.Presentacion.ViewModels;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Negocio.Repository;
import es.uniovi.Alojamientos.Negocio.RepositoryCreate;

public class DatosAlberguesViewModel extends ViewModel {

    private Repository repository;
    private List<Alojamiento> mAlojamientos;
    private Alojamiento currentAlojamiento;
    private LiveData<MainActivity.AppStatus> appStatus;

    public DatosAlberguesViewModel(){
      repository = RepositoryCreate.getRepository();
      mAlojamientos = repository.getAlberguesList();
      appStatus = repository.getAppStatus();
    }

    public List<Alojamiento> getAlberguesList() {
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
