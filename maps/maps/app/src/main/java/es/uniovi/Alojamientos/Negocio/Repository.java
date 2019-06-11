package es.uniovi.Alojamientos.Negocio;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import es.uniovi.Alojamientos.Presentacion.Activities.MainActivity;
import es.uniovi.Alojamientos.Datos.WebService;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;

public class Repository {

    private WebService webService;
    private LiveData<List<Alojamiento>> alojamientos;
    private LiveData<MainActivity.AppStatus> appStatus;
    private Alojamiento alojamiento;

    public Repository(){
        webService = new WebService();
        updateAlojamientosData();
        updateAppStatus();
    }

    public void updateAppStatus() {
        appStatus = webService.getAppStatus();
    }

    public LiveData<MainActivity.AppStatus> getAppStatus(){
        return appStatus;
    }

    public void updateAlojamientosData(){
        alojamientos = webService.getAlojamientos();
    }

    public LiveData<List<Alojamiento>> getAlojamientosList(){
        return alojamientos;
    }

    public List<Alojamiento> getCasasList(){
        List<Alojamiento> casas = new ArrayList<Alojamiento>();
        for (Alojamiento aloj: alojamientos.getValue()) {
            if ((aloj.getTipo().equals("Casa de aldea;Apartamento rural") ||
                    aloj.getTipo().equals("Casa de aldea;Apartamento tur\u00edstico") ||
                    aloj.getTipo().equals("Casa de aldea;Hoteles-albergue") ||
                    aloj.getTipo().equals("Casa de aldea") ||
                    aloj.getTipo().equals("Casa de aldea;Albergue tur\u00edstico") ||
                    aloj.getTipo().equals("Casa de aldea;Casa de aldea") ||
                    aloj.getTipo().equals("Casa de Aldea \u00cdntegra") ||
                    aloj.getTipo().equals("Casa de aldea;Vivienda vacacional") ||
                    aloj.getTipo().equals("Hotel rural;Casa de aldea") ||
                    aloj.getTipo().equals("Casa de Aldea") ||
                    aloj.getTipo().equals("Vivienda vacacional;Casa de aldea"))
                    && !aloj.getNombre().equals("Casa Ángela")
                    && !aloj.getNombre().equals("")
                    && !aloj.getNombre().equals("Larrionda 31")
                    && !aloj.getZona().equals("")
                    && !aloj.getConcejo().equals("")
                    && !aloj.getLocalidad().equals("")
                    && !aloj.getTitulo().equals("")
                    && !aloj.getTitulo().equals(";")
                    && !aloj.getTitulo().contains(";")
                    && !aloj.getDescripcionLarga().equals(";")
                    && !aloj.getDescripcionLarga().equals("")) {
                aloj.setNombre(aloj.getNombre().replace('ń', 'ñ'));
                aloj.setDescripcionLarga(aloj.getDescripcionLarga().replace('ń', 'ñ'));
                casas.add(aloj);
            }
        }
        return casas;
    }

    public List<Alojamiento> getApartamentosList(){
        List<Alojamiento> apartamentos = new ArrayList<Alojamiento>();
        for (Alojamiento aloj: alojamientos.getValue()) {
            if((aloj.getTipo().equals("Apartamentos rurales") ||
                    aloj.getTipo().equals("Apartamento rural") ||
                    aloj.getTipo().equals("Apartamento rural;Camping") ||
                    aloj.getTipo().equals("Apartamento rural;Hotel") ||
                    aloj.getTipo().equals("Apartamento rural;Vivienda vacacional") ||
                    aloj.getTipo().equals("Apartamento tur\u00edstico") ||
                    aloj.getTipo().equals("Apartamento tur\u00edstico;Apartamento tur\u00edstico") ||
                    aloj.getTipo().equals("albergue tur\u00edstico") ||
                    aloj.getTipo().equals("Hoteles-albergue") ||
                    aloj.getTipo().equals("Hotel;Apartamento rural") ||
                    aloj.getTipo().equals("Apartamento Rural") ||
                    aloj.getTipo().equals("Apartamento Tur\u00edstico"))
                    && !aloj.getNombre().equals("")
                    && !aloj.getNombre().equals("El Mayorazo")
                    && !aloj.getZona().equals("")
                    && !aloj.getConcejo().equals("")
                    && !aloj.getLocalidad().equals("")
                    && !aloj.getTitulo().equals("")
                    && !aloj.getDescripcionLarga().equals(";")
                    && !aloj.getDescripcionLarga().equals("")
                    && !aloj.getTitulo().contains(";")
                    && !aloj.getTitulo().equals(";")){
                aloj.setNombre(aloj.getNombre().replace('ń', 'ñ'));
                aloj.setDescripcionLarga(aloj.getDescripcionLarga().replace('ń', 'ñ'));
                aloj.setConcejo(aloj.getConcejo().replace('ń', 'ñ'));
                apartamentos.add(aloj);
            }
        }
        return apartamentos;
    }

    public List<Alojamiento> getHotelesList(){
        List<Alojamiento> hoteles = new ArrayList<Alojamiento>();
        for (Alojamiento aloj: alojamientos.getValue()) {
            if((aloj.getTipo().equals("Apartamento rural;Hotel") ||
                    aloj.getTipo().equals("Apartamento rural;Hotel rural") ||
                    aloj.getTipo().equals("Casa de aldea;Hoteles-albergue")||
                    aloj.getTipo().equals("Hotel") ||
                    aloj.getTipo().equals("Hoteles-albergue") ||
                    aloj.getTipo().equals("Hotel;Apartamento rural") ||
                    aloj.getTipo().equals("Hotel rural;Casa de aldea") ||
                    aloj.getTipo().equals("Hotel;Vivienda vacacional") ||
                    aloj.getTipo().equals("Vivienda vacacional;Hotel rural") ||
                    aloj.getCategories().indexOf("Estrellas") != -1 )
                    && aloj.getNombre()!="El Habana Llanes"
                    && !aloj.getNombre().equals("La Trepe") && !aloj.getNombre().equals("The Island")
                    && !aloj.getNombre().equals("Vega del Sella") && !aloj.getNombre().equals("Sebreńu")
                    && !aloj.getNombre().equals("La Quinta Esencia") && !aloj.getNombre().equals("Peńa Castil (Sotres)")
                    && !aloj.getNombre().equals("Covadonga de Panes")
                    && !aloj.getNombre().equals("")
                    && !aloj.getArticleId().equals(247823)
                    && !aloj.getZona().equals("")
                    && !aloj.getLocalidad().equals("")
                    && !aloj.getTitulo().equals("")
                    && !aloj.getConcejo().equals("")
                    && !aloj.getDescripcionLarga().equals("")
                    && !aloj.getDescripcionLarga().equals(";")
                    && !aloj.getTitulo().equals(";")
                    && !aloj.getTitulo().contains(";")
                    && (aloj.getCategories().contains("Estrellas") || aloj.getCategories().contains("Estrella")) ){
                aloj.setNombre(aloj.getNombre().replace('ń', 'ñ'));
                aloj.setDescripcionLarga(aloj.getDescripcionLarga().replace('ń', 'ñ'));
                aloj.setConcejo(aloj.getConcejo().replace('ń', 'ñ'));
                hoteles.add(aloj);
            }
        }
        return hoteles;
    }

    public List<Alojamiento> getAlberguesList(){
        List<Alojamiento> albergues = new ArrayList<Alojamiento>();
        for (Alojamiento aloj: alojamientos.getValue()) {
            if((aloj.getTipo().equals("Albergue de peregrinos") ||
                    aloj.getTipo().equals("Albergue Juvenil") ||
                    aloj.getTipo().equals("Albergue tur\u00edstico") ||
                    aloj.getTipo().equals("Albergue Tur\u00edstico;Camping") ||
                    aloj.getTipo().equals("Casa de aldea;Albergue tur\u00edstico") ||
                    aloj.getTipo().equals("Albergue Tur\u00edstico") ||
                    aloj.getTipo().equals("Refugio de monta\u0144a") ||
                    aloj.getTipo().equals("Refugio de Monta\u0144a"))
                    && !aloj.getNombre().equals("La Covadonga")
                    && !aloj.getNombre().equals("")
                    && !aloj.getZona().equals("")
                    && !aloj.getLocalidad().equals("")
                    && !aloj.getTitulo().equals("")
                    && !aloj.getConcejo().equals("")
                    && !aloj.getTitulo().contains(";")
                    && !aloj.getDescripcionLarga().equals("")
                    && !aloj.getDescripcionLarga().equals(";")
                    && !aloj.getTitulo().equals(";")){
                aloj.setNombre(aloj.getNombre().replace('ń', 'ñ'));
                aloj.setDescripcionLarga(aloj.getDescripcionLarga().replace('ń', 'ñ'));
                aloj.setConcejo(aloj.getConcejo().replace('ń', 'ñ'));
                albergues.add(aloj);
            }
        }
        return albergues;
    }

    public List<Alojamiento> getCampingsList(){
        List<Alojamiento> campings = new ArrayList<Alojamiento>();
        for (Alojamiento aloj: alojamientos.getValue()) {
            if ((aloj.getTipo().equals("Albergue Tur\u00edstico;Camping") ||
                    aloj.getTipo().equals("Apartamento rural;Camping") ||
                    aloj.getTipo().equals("Camping"))
                    && !aloj.getNombre().equals("")
                    && !aloj.getNombre().equals("La Lastra I;Cudillero")
                    && !aloj.getNombre().equals("Los Novales;Camping El Carbayín")
                    && !aloj.getZona().equals("")
                    && !aloj.getLocalidad().equals("")
                    && !aloj.getTitulo().equals("")
                    && !aloj.getTitulo().contains(";")
                    && !aloj.getConcejo().equals("")
                    && !aloj.getDescripcionLarga().equals("")
                    && !aloj.getDescripcionLarga().equals(";")
                    && !aloj.getTitulo().equals(";")) {
                aloj.setNombre(aloj.getNombre().replace('ń', 'ñ'));
                aloj.setDescripcionLarga(aloj.getDescripcionLarga().replace('ń', 'ñ'));
                aloj.setConcejo(aloj.getConcejo().replace('ń', 'ñ'));
                campings.add(aloj);
            }
        }
        return campings;
    }

    public List<Alojamiento> getHostalesList(){
        List<Alojamiento> hostales = new ArrayList<Alojamiento>();
        for (Alojamiento aloj: alojamientos.getValue()) {
            if ((aloj.getTipo().equals("Hostal") ||
                    aloj.getTipo().equals("Pensi\u00f3n"))
                    && !aloj.getNombre().equals("")
                    && !aloj.getNombre().equals("Vegadeo")
                    && !aloj.getZona().equals("")
                    && !aloj.getLocalidad().equals("")
                    && !aloj.getTitulo().equals("")
                    && !aloj.getTitulo().contains(";")
                    && !aloj.getConcejo().equals("")
                    && !aloj.getDescripcionLarga().equals("")
                    && !aloj.getDescripcionLarga().equals(";")
                    && !aloj.getTitulo().equals(";")) {
                aloj.setNombre(aloj.getNombre().replace('ń', 'ñ'));
                aloj.setDescripcionLarga(aloj.getDescripcionLarga().replace('ń', 'ñ'));
                aloj.setConcejo(aloj.getConcejo().replace('ń', 'ñ'));
                hostales.add(aloj);
            }
        }
        return hostales;
    }


    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public Alojamiento getCurrentAlojamiento() {
        return this.alojamiento;
    }
}
