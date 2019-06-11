package es.uniovi.Alojamientos.Presentacion.ViewModels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import es.uniovi.Alojamientos.Negocio.RepositoryCreate;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import es.uniovi.Alojamientos.Negocio.Repository;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;

public class DatosAlojamientoViewModel extends ViewModel {

  private Repository repository;
  private LiveData<List<Alojamiento>> mAlojamientos;
  private List<Alojamiento> todos_alojamientos;
  private LiveData<MainActivity.AppStatus> appStatus;

  public DatosAlojamientoViewModel(){
    repository = RepositoryCreate.getRepository();//new Repository();
    mAlojamientos = repository.getAlojamientosList();
    appStatus = repository.getAppStatus();
  }

  public LiveData<List<Alojamiento>> getAlojamientosList() {
    return mAlojamientos;
  }

  public void updateAlojamientos(){
    repository.updateAlojamientosData();
  }

  public void setTodos_alojamientos(List<Alojamiento> todos_alojamientos) {
    this.todos_alojamientos = todos_alojamientos;
  }

  public List<Alojamiento> getTodos_alojamientos() {
    return todos_alojamientos;
  }

  public Alojamiento getAlojamiento() {
    return repository.getCurrentAlojamiento();
  }

  public void setAlojamiento(Alojamiento alojamiento) {
    repository.setAlojamiento(alojamiento);
  }

  public LiveData<MainActivity.AppStatus> getAppStatus() {
    return appStatus;
  }

}
