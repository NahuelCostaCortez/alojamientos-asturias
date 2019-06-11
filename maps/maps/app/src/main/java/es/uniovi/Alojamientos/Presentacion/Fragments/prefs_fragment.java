package es.uniovi.Alojamientos.Presentacion.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.ConfigurationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;
import es.uniovi.Alojamientos.Presentacion.ViewModels.DatosCasasViewModel;
import es.uniovi.Alojamientos.R;

public class prefs_fragment extends PreferenceFragmentCompat {

    private static final String PREFERENCES = "PREFERENCES";
    private ListPreference listPreference;
    private SwitchPreferenceCompat switchPreferenceCompat;
    private Preference reset;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences prefs;
    private String value;
    private DatosCasasViewModel alojamientosViewModel;
    private String APP_STATUS = String.valueOf(MainActivity.AppStatus.UPDATED);
    private Locale locale;

    public prefs_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        listPreference = (ListPreference) getPreferenceManager().findPreference("update_language");
        switchPreferenceCompat = (SwitchPreferenceCompat) getPreferenceManager().findPreference("night_mode");
        reset = getPreferenceManager().findPreference("reset_defaults");

        getActivity().setTitle(R.string.configuration);

        if(listPreference.getSummary().equals("Español")){
            listPreference.setValueIndex(0);
            value = "0";
        }
        else if(listPreference.getSummary().equals("English")){
            listPreference.setValueIndex(1);
            value = "1";
        }

        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(value.equals(newValue))
                    return false;
                else if(newValue.equals("0")){
                    Resources res = getContext().getResources();
                    Configuration config = new Configuration(res.getConfiguration());
                    config.locale = new Locale("es", "ES");
                    res.updateConfiguration(config, res.getDisplayMetrics());
                    prefsEditor.putString("IDIOMA", "SPANISH");
                    prefsEditor.commit();
                    listPreference.setSummary("Español");
                    value = (String) newValue;
                }
                else if(newValue.equals("1")){
                    Resources res = getContext().getResources();
                    Configuration config = new Configuration(res.getConfiguration());
                    config.locale = new Locale("en","GB");
                    res.updateConfiguration(config, res.getDisplayMetrics());
                    prefsEditor.putString("IDIOMA", "ENGLISH");
                    prefsEditor.commit();
                    listPreference.setSummary("English");
                    value = (String) newValue;
                }

                getActivity().recreate();

                if (prefs.getString("THEME", "").equals("LIGHT") ) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (prefs.getString("THEME", "").equals("DARK")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                return true;
            }
        });

        switchPreferenceCompat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(!switchPreferenceCompat.isChecked()){
                    AlertDialog ad = new AlertDialog.Builder(getActivity())
                            .create();
                    ad.setCancelable(false);
                    ad.setTitle(getActivity().getString(R.string.attention));
                    ad.setMessage(getActivity().getString(R.string.message_theme));
                    ad.setButton(Dialog.BUTTON_POSITIVE, getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            prefsEditor.putString("THEME", "DARK");
                            prefsEditor.commit();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            getActivity().recreate();
                        }
                    });
                    ad.setButton(Dialog.BUTTON_NEGATIVE, getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            switchPreferenceCompat.setChecked(false);
                        }
                    });
                    ad.show();
                }
                else {
                    AlertDialog ad = new AlertDialog.Builder(getActivity())
                            .create();
                    ad.setCancelable(false);
                    ad.setTitle(getActivity().getString(R.string.attention));
                    ad.setMessage(getActivity().getString(R.string.message_theme));
                    ad.setButton(Dialog.BUTTON_POSITIVE, getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            prefsEditor.putString("THEME", "LIGHT");
                            prefsEditor.commit();
                            /*getActivity().finish();
                            Intent intentMain = new Intent(getActivity(), MainActivity.class);
                            intentMain.putExtra("FRAGMENT", "CASAS");
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentMain);*/
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            getActivity().recreate();
                        }
                    });
                    ad.setButton(Dialog.BUTTON_NEGATIVE, getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            switchPreferenceCompat.setChecked(true);
                        }
                    });
                    ad.show();
                }
                return true;
            }
        });

        reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog ad = new AlertDialog.Builder(getActivity())
                        .create();
                ad.setCancelable(false);
                ad.setTitle(getActivity().getString(R.string.attention));
                ad.setMessage(getActivity().getString(R.string.r_u_sure));
                ad.setButton(Dialog.BUTTON_POSITIVE, getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        prefsEditor.putString("IDIOMA", "-");
                        prefsEditor.putString("THEME", "LIGHT");
                        prefsEditor.commit();
                        switchPreferenceCompat.setChecked(false);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                        getActivity().recreate();
                    }
                });
                ad.setButton(Dialog.BUTTON_NEGATIVE, getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ad.hide();
                    }
                });
                ad.show();
                return true;
            }
        });

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

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        prefs = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();

        if(prefs.getString("IDIOMA","").equals("SPANISH")){
            //listPreference.setSummary("Español");
            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = new Locale("es", "ES");
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        else if (prefs.getString("IDIOMA","").equals("ENGLISH")){
            //listPreference.setSummary("English");
            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = new Locale("en", "GB");
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        else{
            locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }

        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}