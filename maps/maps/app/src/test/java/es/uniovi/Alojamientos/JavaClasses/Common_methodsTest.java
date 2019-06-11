package es.uniovi.Alojamientos.JavaClasses;

import org.junit.Before;
import org.junit.Test;

import es.uniovi.Alojamientos.Negocio.Common_methods;

import static org.junit.Assert.*;

public class Common_methodsTest {

    private Common_methods alojMethods;

    @Before
    public void setUp(){
        alojMethods = new Common_methods();
    }

    @Test
    public void isPriceLess() {
        assertEquals(true, alojMethods.isPriceLess("<p>\n\tAlojamiento:<br />\n\tDesde 20€ hasta 45€</p>", 80));
        assertEquals(false, alojMethods.isPriceLess("<p>\n\tAlojamiento:<br />\n\tDesde 20€ hasta 45€</p>", 10));
        assertEquals(false, alojMethods.isPriceLess("<p>\n\tAlojamiento:<br />\n\tDesde 20€ hasta 45€</p>", 30));
        assertEquals(false, alojMethods.isPriceLess("<p>\n\tAlojamiento:<br />\n\t</p>", 30));
        assertEquals(true, alojMethods.isPriceLess("<p> Tarifa:<br /> Desde: 99 € Hasta: 99 €</p>", 400));
    }

    @Test
    public void isStarLess() {
        assertEquals(true, alojMethods.isStarLess( "Pensiones;1 Estrella;Bajo Nalón;Pravia;Televisión en habitación;Dónde dormir;Alojamientos;Pensión;Bar/Cafetería;Calefacción", 1));
        assertEquals(false, alojMethods.isStarLess( "Comarca de la Sidra;Colunga;Dónde dormir;Alojamientos;Hotel;Acceso a Internet;Admite Animales;Admite Tarjetas de Crédito;Aparcamiento;Bar/Cafetería;Calefacción;Jardín;Restaurante;Salón TV;Servicio de Lavandería;Servicio de Recepción;Teléfono;Transporte Público < 500 m;Wifi;Hoteles;Televisión;2 Estrellas", 3));
        assertEquals(false, alojMethods.isStarLess( "Pensiones;Bajo Nalón;Pravia;Televisión en habitación;Dónde dormir;Alojamientos;Pensión;Bar/Cafetería;Calefacción", 3));
        assertEquals(true, alojMethods.isStarLess( "Pensiones;5 Estrellas;Bajo Nalón;Pravia;Televisión en habitación;Dónde dormir;Alojamientos;Pensión;Bar/Cafetería;Calefacción", 3));
    }

    @Test
    public void restoreList() {
    }

    @Test
    public void saveList() {
    }
}