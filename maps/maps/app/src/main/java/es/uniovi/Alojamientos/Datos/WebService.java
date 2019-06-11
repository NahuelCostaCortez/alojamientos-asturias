package es.uniovi.Alojamientos.Datos;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebService {

    private RestService restService;
    private MutableLiveData<List<Alojamiento>> alojamientosMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<MainActivity.AppStatus> appStatus = new MutableLiveData<>();

    public WebService(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://orion.edv.uniovi.es/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.restService = retrofit.create(RestService.class);
    }

    public MutableLiveData<List<Alojamiento>> getAlojamientos(){
        System.out.println("HAGO PETICION");
        appStatus.setValue(MainActivity.AppStatus.DOWNLOADING);
        updateAlojamientos();
        return alojamientosMutableLiveData;
    }

    public MutableLiveData<MainActivity.AppStatus> getAppStatus(){
        return appStatus;
    }

    private void updateAlojamientos(){
        this.restService.getInfo().enqueue(new Callback<List<Alojamiento>>() {
            @Override
            public void onResponse(Call<List<Alojamiento>> call, Response<List<Alojamiento>> response) {
                System.out.println("RECIBO PETICION");
                appStatus.setValue(MainActivity.AppStatus.UPDATED);
                List<Alojamiento> alojamientos = response.body();
                alojamientosMutableLiveData.setValue(alojamientos);
            }

            @Override
            public void onFailure(Call<List<Alojamiento>> call, Throwable t) {
                appStatus.setValue(MainActivity.AppStatus.ERROR);
                Log.e("ERROR FAILURE", t.toString());
            }
        });

    }

}
