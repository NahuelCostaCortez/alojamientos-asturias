package es.uniovi.Alojamientos.Datos;import java.util.List;import es.uniovi.Alojamientos.Datos.Model.Alojamiento;import retrofit2.Call;import retrofit2.http.GET;public interface RestService {    @GET("~arias/json/DondeDormir.json")    Call<List<Alojamiento>> getInfo();}