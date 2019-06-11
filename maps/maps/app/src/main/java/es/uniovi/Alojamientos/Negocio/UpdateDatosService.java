package es.uniovi.Alojamientos.Negocio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class UpdateDatosService extends Service {

    // Servicio que actualiza los datos de los alojamientos
    private static Repository repository;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Se actualizan los datos del repositorio
        repository = RepositoryCreate.getRepository();
        repository.updateAlojamientosData();
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
