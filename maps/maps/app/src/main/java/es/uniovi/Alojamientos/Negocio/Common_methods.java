package es.uniovi.Alojamientos.Negocio;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import es.uniovi.Alojamientos.Datos.Model.Alojamiento;

public class Common_methods {

    private static final String ACCOMODATION_LIST_FILENAME = "FAVOURITES_LIST";
    public static ArrayList<Alojamiento> favoritos;

    public Common_methods(){

    }

    // Devuelve True si el precio que se el pasa como parámetro es menor que el precio
    // establecido como máximo por el usuario
    public boolean isPriceLess(String tarifas, int tarifaBaseComparacion){

        int precio_casa = 10000;
        String[] precio = tarifas.split("€");

        if ((precio.length > 1 && precio != null) && (!tarifas.contains("Desayuno"))) {
            precio = precio[precio.length - 2].toLowerCase().split("hasta");
            precio = precio[precio.length - 1].split(":");
            try {
                if (precio.length > 1)
                    precio_casa = Integer.valueOf(precio[1].replace(',', '.').replaceAll("\\s",""));
                else
                    precio_casa = Integer.valueOf(precio[0].replace(',', '.').replaceAll("\\s",""));
            } catch (NumberFormatException e) {
            }
        }
        else{
            return false;
        }

        return (Math.abs(precio_casa) < tarifaBaseComparacion);
    }

    // Devuelve True si las estrellas del hotel/hostal que se el pasa como parámetro
    // es menor que las estrellas establecidas como máximo por el usuario
    public boolean isStarLess(String categories, float estrelllasComparacion){

        int indice = categories.indexOf("Estrella");
        int num_estrellas = 0;
        try{num_estrellas = Integer.valueOf(categories.substring(indice-2, indice-1));}
        catch(IndexOutOfBoundsException e){
            return false;
        }

        return num_estrellas >= estrelllasComparacion;
    }

    // Restauracion de lista de favoritos
    public static ArrayList<Alojamiento> restoreList (FragmentActivity activity) {
        InputStream buffer = null;
        ObjectInput input = null;
        ArrayList<Alojamiento> listaAlojamientos = null;

        try {
            buffer = new BufferedInputStream(
                    activity.openFileInput(ACCOMODATION_LIST_FILENAME));

            input = new ObjectInputStream(buffer);
            listaAlojamientos = (ArrayList<Alojamiento>)input.readObject();
            return listaAlojamientos;
        } catch (Exception ex) {

        } finally {
            try {
                input.close();
            } catch (Exception e) {

            }
        }

        return new ArrayList<Alojamiento>();
    }

    // Guardado de lista de favoritos
    public static void saveList (List<Alojamiento> alojamientos, FragmentActivity activity) {
        FileOutputStream file = null;
        OutputStream buffer = null;
        ObjectOutput output = null;

        try {
            file = activity.openFileOutput(ACCOMODATION_LIST_FILENAME, Context.MODE_PRIVATE);
            buffer = new BufferedOutputStream(file);
            output = new ObjectOutputStream(buffer);
            output.writeObject(alojamientos);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
    }

}
