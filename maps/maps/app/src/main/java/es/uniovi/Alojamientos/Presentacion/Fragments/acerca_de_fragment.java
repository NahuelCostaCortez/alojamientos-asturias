package es.uniovi.Alojamientos.Presentacion.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosCasasViewModel;
import es.uniovi.Alojamientos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class acerca_de_fragment extends Fragment {

    private TextView textView;
    private DatosCasasViewModel alojamientosViewModel;
    private String APP_STATUS = String.valueOf(MainActivity.AppStatus.UPDATED);

    public acerca_de_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.acerca_de_fragment, container, false);
        textView = view.findViewById(R.id.text_view_acerca_de);
        textView.setText(Html.fromHtml("<a href=''>"+getString(R.string.turism_web)+"</a>"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(textView.getText().toString()));
                startActivity(intent);
            }
        });

        getActivity().setTitle(R.string.about);

        alojamientosViewModel = ViewModelProviders.of(this).get(DatosCasasViewModel.class);
        alojamientosViewModel.getAppStatus().observe(this, new Observer<MainActivity.AppStatus>() {
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

        return view;
    }

}
