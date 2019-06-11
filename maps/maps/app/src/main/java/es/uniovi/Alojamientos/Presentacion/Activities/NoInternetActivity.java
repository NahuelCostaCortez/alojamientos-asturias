package es.uniovi.Alojamientos.Presentacion.Activities;

import androidx.appcompat.app.AppCompatActivity;
import es.uniovi.Alojamientos.R;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoInternetActivity extends AppCompatActivity {

    private Button retry;
    int LOAD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        retry = findViewById(R.id.button_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    Intent intent = new Intent(NoInternetActivity.this, LoadingActivity.class);
                    LOAD = 1;
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // No hacer nada, obligar al usuario a que pulse el boton retry
    }

    boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
