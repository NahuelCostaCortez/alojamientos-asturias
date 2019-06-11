
package es.uniovi.Alojamientos.Datos.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class Alojamiento implements Parcelable, Serializable {

    @NonNull
    @SerializedName("ArticleId")
    @Expose
    private Integer articleId;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("CodigoDGT")
    @Expose
    private String codigoDGT;
    @SerializedName("Nombre")
    @Expose
    private String nombre;
    @SerializedName("Tipo")
    @Expose
    private String tipo;
    @SerializedName("Cadena")
    @Expose
    private String cadena;
    @SerializedName("Zona")
    @Expose
    private String zona;
    @SerializedName("Comarca")
    @Expose
    private String comarca;
    @SerializedName("Concejo")
    @Expose
    private String concejo;
    @SerializedName("Localidad")
    @Expose
    private String localidad;
    @SerializedName("Tarifas")
    @Expose
    private String tarifas;
    @SerializedName("DesayunoIncluido")
    @Expose
    private String desayunoIncluido;
    @SerializedName("LimpiezaIncluida")
    @Expose
    private String limpiezaIncluida;
    @SerializedName("SabanasIncluidas")
    @Expose
    private String sabanasIncluidas;
    @SerializedName("CP")
    @Expose
    private String cP;
    @SerializedName("Direccion")
    @Expose
    private String direccion;
    @SerializedName("Telefono")
    @Expose
    private String telefono;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Fax")
    @Expose
    private String fax;
    @SerializedName("Facebook")
    @Expose
    private String facebook;
    @SerializedName("Twitter")
    @Expose
    private String twitter;
    @SerializedName("GooglePlus")
    @Expose
    private String googlePlus;
    @SerializedName("Instagram")
    @Expose
    private String instagram;
    @SerializedName("Pinterest")
    @Expose
    private String pinterest;
    @SerializedName("Youtube")
    @Expose
    private String youtube;
    @SerializedName("Web")
    @Expose
    private String web;
    @SerializedName("AbiertoTodoAno")
    @Expose
    private String abiertoTodoAno;
    @SerializedName("FechasCierre")
    @Expose
    private String fechasCierre;
    @SerializedName("Titulo")
    @Expose
    private String titulo;
    @SerializedName("DescripcionLarga")
    @Expose
    private String descripcionLarga;
    @SerializedName("TemporadaAlta")
    @Expose
    private String temporadaAlta;
    @SerializedName("TemporadaMedia")
    @Expose
    private String temporadaMedia;
    @SerializedName("TemporadaBaja")
    @Expose
    private String temporadaBaja;
    @SerializedName("EFQM")
    @Expose
    private String eFQM;
    @SerializedName("QdeCalidad")
    @Expose
    private String qdeCalidad;
    @SerializedName("AldeasCalidad")
    @Expose
    private String aldeasCalidad;
    @SerializedName("CalidadCasonasAsturianas")
    @Expose
    private String calidadCasonasAsturianas;
    @SerializedName("SistemaCalidadISO")
    @Expose
    private String sistemaCalidadISO;
    @SerializedName("Accesibilidad")
    @Expose
    private String accesibilidad;
    @SerializedName("Plazas")
    @Expose
    private String plazas;
    @SerializedName("PlazasFijas")
    @Expose
    private String plazasFijas;
    @SerializedName("PlazasSupletorias")
    @Expose
    private String plazasSupletorias;
    @SerializedName("NHabitaciones")
    @Expose
    private String nHabitaciones;
    @SerializedName("NApartamentos")
    @Expose
    private String nApartamentos;
    @SerializedName("CapacidadApartamentos")
    @Expose
    private String capacidadApartamentos;
    @SerializedName("BuscadorNGlobal")
    @Expose
    private String buscadorNGlobal;
    @SerializedName("BuscadorNHabitaciones")
    @Expose
    private String buscadorNHabitaciones;
    @SerializedName("NParcelas")
    @Expose
    private String nParcelas;
    @SerializedName("IconoCategoria")
    @Expose
    private String iconoCategoria;
    @SerializedName("TipoAlquiler")
    @Expose
    private String tipoAlquiler;
    @SerializedName("ServiciosEstablecimiento")
    @Expose
    private String serviciosEstablecimiento;
    @SerializedName("ServiciosHabitacion")
    @Expose
    private String serviciosHabitacion;
    @SerializedName("ServiciosComplementarios")
    @Expose
    private String serviciosComplementarios;
    @SerializedName("SeguridadYSanidad")
    @Expose
    private String seguridadYSanidad;
    @SerializedName("Slide")
    @Expose
    private String slide;
    @SerializedName("Observacion")
    @Expose
    private String observacion;
    @SerializedName("MasInformacion")
    @Expose
    private String masInformacion;
    @SerializedName("Coordenadas")
    @Expose
    private String coordenadas;
    @SerializedName("DatosFacilitadosPor")
    @Expose
    private String datosFacilitadosPor;
    @SerializedName("Categories")
    @Expose
    private String categories;
    @SerializedName("Tags")
    @Expose
    private String tags;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodigoDGT() {
        return codigoDGT;
    }

    public void setCodigoDGT(String codigoDGT) {
        this.codigoDGT = codigoDGT;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getComarca() {
        return comarca;
    }

    public void setComarca(String comarca) {
        this.comarca = comarca;
    }

    public String getConcejo() {
        return concejo;
    }

    public void setConcejo(String concejo) {
        this.concejo = concejo;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getTarifas() {
        return tarifas;
    }

    public void setTarifas(String tarifas) {
        this.tarifas = tarifas;
    }

    public String getDesayunoIncluido() {
        return desayunoIncluido;
    }

    public void setDesayunoIncluido(String desayunoIncluido) {
        this.desayunoIncluido = desayunoIncluido;
    }

    public String getLimpiezaIncluida() {
        return limpiezaIncluida;
    }

    public void setLimpiezaIncluida(String limpiezaIncluida) {
        this.limpiezaIncluida = limpiezaIncluida;
    }

    public String getSabanasIncluidas() {
        return sabanasIncluidas;
    }

    public void setSabanasIncluidas(String sabanasIncluidas) {
        this.sabanasIncluidas = sabanasIncluidas;
    }

    public String getCP() {
        return cP;
    }

    public void setCP(String cP) {
        this.cP = cP;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getPinterest() {
        return pinterest;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getAbiertoTodoAno() {
        return abiertoTodoAno;
    }

    public void setAbiertoTodoAno(String abiertoTodoAno) {
        this.abiertoTodoAno = abiertoTodoAno;
    }

    public String getFechasCierre() {
        return fechasCierre;
    }

    public void setFechasCierre(String fechasCierre) {
        this.fechasCierre = fechasCierre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcionLarga() {
        return descripcionLarga;
    }

    public void setDescripcionLarga(String descripcionLarga) {
        this.descripcionLarga = descripcionLarga;
    }

    public String getTemporadaAlta() {
        return temporadaAlta;
    }

    public void setTemporadaAlta(String temporadaAlta) {
        this.temporadaAlta = temporadaAlta;
    }

    public String getTemporadaMedia() {
        return temporadaMedia;
    }

    public void setTemporadaMedia(String temporadaMedia) {
        this.temporadaMedia = temporadaMedia;
    }

    public String getTemporadaBaja() {
        return temporadaBaja;
    }

    public void setTemporadaBaja(String temporadaBaja) {
        this.temporadaBaja = temporadaBaja;
    }

    public String getEFQM() {
        return eFQM;
    }

    public void setEFQM(String eFQM) {
        this.eFQM = eFQM;
    }

    public String getQdeCalidad() {
        return qdeCalidad;
    }

    public void setQdeCalidad(String qdeCalidad) {
        this.qdeCalidad = qdeCalidad;
    }

    public String getAldeasCalidad() {
        return aldeasCalidad;
    }

    public void setAldeasCalidad(String aldeasCalidad) {
        this.aldeasCalidad = aldeasCalidad;
    }

    public String getCalidadCasonasAsturianas() {
        return calidadCasonasAsturianas;
    }

    public void setCalidadCasonasAsturianas(String calidadCasonasAsturianas) {
        this.calidadCasonasAsturianas = calidadCasonasAsturianas;
    }

    public String getSistemaCalidadISO() {
        return sistemaCalidadISO;
    }

    public void setSistemaCalidadISO(String sistemaCalidadISO) {
        this.sistemaCalidadISO = sistemaCalidadISO;
    }

    public String getAccesibilidad() {
        return accesibilidad;
    }

    public void setAccesibilidad(String accesibilidad) {
        this.accesibilidad = accesibilidad;
    }

    public String getPlazas() {
        return plazas;
    }

    public void setPlazas(String plazas) {
        this.plazas = plazas;
    }

    public String getPlazasFijas() {
        return plazasFijas;
    }

    public void setPlazasFijas(String plazasFijas) {
        this.plazasFijas = plazasFijas;
    }

    public String getPlazasSupletorias() {
        return plazasSupletorias;
    }

    public void setPlazasSupletorias(String plazasSupletorias) {
        this.plazasSupletorias = plazasSupletorias;
    }

    public String getNHabitaciones() {
        return nHabitaciones;
    }

    public void setNHabitaciones(String nHabitaciones) {
        this.nHabitaciones = nHabitaciones;
    }

    public String getNApartamentos() {
        return nApartamentos;
    }

    public void setNApartamentos(String nApartamentos) {
        this.nApartamentos = nApartamentos;
    }

    public String getCapacidadApartamentos() {
        return capacidadApartamentos;
    }

    public void setCapacidadApartamentos(String capacidadApartamentos) {
        this.capacidadApartamentos = capacidadApartamentos;
    }

    public String getBuscadorNGlobal() {
        return buscadorNGlobal;
    }

    public void setBuscadorNGlobal(String buscadorNGlobal) {
        this.buscadorNGlobal = buscadorNGlobal;
    }

    public String getBuscadorNHabitaciones() {
        return buscadorNHabitaciones;
    }

    public void setBuscadorNHabitaciones(String buscadorNHabitaciones) {
        this.buscadorNHabitaciones = buscadorNHabitaciones;
    }

    public String getNParcelas() {
        return nParcelas;
    }

    public void setNParcelas(String nParcelas) {
        this.nParcelas = nParcelas;
    }

    public String getIconoCategoria() {
        return iconoCategoria;
    }

    public void setIconoCategoria(String iconoCategoria) {
        this.iconoCategoria = iconoCategoria;
    }

    public String getTipoAlquiler() {
        return tipoAlquiler;
    }

    public void setTipoAlquiler(String tipoAlquiler) {
        this.tipoAlquiler = tipoAlquiler;
    }

    public String getServiciosEstablecimiento() {
        return serviciosEstablecimiento;
    }

    public void setServiciosEstablecimiento(String serviciosEstablecimiento) {
        this.serviciosEstablecimiento = serviciosEstablecimiento;
    }

    public String getServiciosHabitacion() {
        return serviciosHabitacion;
    }

    public void setServiciosHabitacion(String serviciosHabitacion) {
        this.serviciosHabitacion = serviciosHabitacion;
    }

    public String getServiciosComplementarios() {
        return serviciosComplementarios;
    }

    public void setServiciosComplementarios(String serviciosComplementarios) {
        this.serviciosComplementarios = serviciosComplementarios;
    }

    public String getSeguridadYSanidad() {
        return seguridadYSanidad;
    }

    public void setSeguridadYSanidad(String seguridadYSanidad) {
        this.seguridadYSanidad = seguridadYSanidad;
    }

    public String getSlide() {
        return slide;
    }

    public void setSlide(String slide) {
        this.slide = slide;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getMasInformacion() {
        return masInformacion;
    }

    public void setMasInformacion(String masInformacion) {
        this.masInformacion = masInformacion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getDatosFacilitadosPor() {
        return datosFacilitadosPor;
    }

    public void setDatosFacilitadosPor(String datosFacilitadosPor) {
        this.datosFacilitadosPor = datosFacilitadosPor;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.articleId);
        dest.writeString(this.description);
        dest.writeString(this.codigoDGT);
        dest.writeString(this.nombre);
        dest.writeString(this.tipo);
        dest.writeString(this.cadena);
        dest.writeString(this.zona);
        dest.writeString(this.comarca);
        dest.writeString(this.concejo);
        dest.writeString(this.localidad);
        dest.writeString(this.tarifas);
        dest.writeString(this.desayunoIncluido);
        dest.writeString(this.limpiezaIncluida);
        dest.writeString(this.sabanasIncluidas);
        dest.writeString(this.cP);
        dest.writeString(this.direccion);
        dest.writeString(this.telefono);
        dest.writeString(this.email);
        dest.writeString(this.fax);
        dest.writeString(this.facebook);
        dest.writeString(this.twitter);
        dest.writeString(this.googlePlus);
        dest.writeString(this.instagram);
        dest.writeString(this.pinterest);
        dest.writeString(this.youtube);
        dest.writeString(this.web);
        dest.writeString(this.abiertoTodoAno);
        dest.writeString(this.fechasCierre);
        dest.writeString(this.titulo);
        dest.writeString(this.descripcionLarga);
        dest.writeString(this.temporadaAlta);
        dest.writeString(this.temporadaMedia);
        dest.writeString(this.temporadaBaja);
        dest.writeString(this.eFQM);
        dest.writeString(this.qdeCalidad);
        dest.writeString(this.aldeasCalidad);
        dest.writeString(this.calidadCasonasAsturianas);
        dest.writeString(this.sistemaCalidadISO);
        dest.writeString(this.accesibilidad);
        dest.writeString(this.plazas);
        dest.writeString(this.plazasFijas);
        dest.writeString(this.plazasSupletorias);
        dest.writeString(this.nHabitaciones);
        dest.writeString(this.nApartamentos);
        dest.writeString(this.capacidadApartamentos);
        dest.writeString(this.buscadorNGlobal);
        dest.writeString(this.buscadorNHabitaciones);
        dest.writeString(this.nParcelas);
        dest.writeString(this.iconoCategoria);
        dest.writeString(this.tipoAlquiler);
        dest.writeString(this.serviciosEstablecimiento);
        dest.writeString(this.serviciosHabitacion);
        dest.writeString(this.serviciosComplementarios);
        dest.writeString(this.seguridadYSanidad);
        dest.writeString(this.slide);
        dest.writeString(this.observacion);
        dest.writeString(this.masInformacion);
        dest.writeString(this.coordenadas);
        dest.writeString(this.datosFacilitadosPor);
        dest.writeString(this.categories);
        dest.writeString(this.tags);
    }

    public Alojamiento() {
    }

    protected Alojamiento(Parcel in) {
        this.articleId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.codigoDGT = in.readString();
        this.nombre = in.readString();
        this.tipo = in.readString();
        this.cadena = in.readString();
        this.zona = in.readString();
        this.comarca = in.readString();
        this.concejo = in.readString();
        this.localidad = in.readString();
        this.tarifas = in.readString();
        this.desayunoIncluido = in.readString();
        this.limpiezaIncluida = in.readString();
        this.sabanasIncluidas = in.readString();
        this.cP = in.readString();
        this.direccion = in.readString();
        this.telefono = in.readString();
        this.email = in.readString();
        this.fax = in.readString();
        this.facebook = in.readString();
        this.twitter = in.readString();
        this.googlePlus = in.readString();
        this.instagram = in.readString();
        this.pinterest = in.readString();
        this.youtube = in.readString();
        this.web = in.readString();
        this.abiertoTodoAno = in.readString();
        this.fechasCierre = in.readString();
        this.titulo = in.readString();
        this.descripcionLarga = in.readString();
        this.temporadaAlta = in.readString();
        this.temporadaMedia = in.readString();
        this.temporadaBaja = in.readString();
        this.eFQM = in.readString();
        this.qdeCalidad = in.readString();
        this.aldeasCalidad = in.readString();
        this.calidadCasonasAsturianas = in.readString();
        this.sistemaCalidadISO = in.readString();
        this.accesibilidad = in.readString();
        this.plazas = in.readString();
        this.plazasFijas = in.readString();
        this.plazasSupletorias = in.readString();
        this.nHabitaciones = in.readString();
        this.nApartamentos = in.readString();
        this.capacidadApartamentos = in.readString();
        this.buscadorNGlobal = in.readString();
        this.buscadorNHabitaciones = in.readString();
        this.nParcelas = in.readString();
        this.iconoCategoria = in.readString();
        this.tipoAlquiler = in.readString();
        this.serviciosEstablecimiento = in.readString();
        this.serviciosHabitacion = in.readString();
        this.serviciosComplementarios = in.readString();
        this.seguridadYSanidad = in.readString();
        this.slide = in.readString();
        this.observacion = in.readString();
        this.masInformacion = in.readString();
        this.coordenadas = in.readString();
        this.datosFacilitadosPor = in.readString();
        this.categories = in.readString();
        this.tags = in.readString();
    }

    public static final Parcelable.Creator<Alojamiento> CREATOR = new Parcelable.Creator<Alojamiento>() {
        @Override
        public Alojamiento createFromParcel(Parcel source) {
            return new Alojamiento(source);
        }

        @Override
        public Alojamiento[] newArray(int size) {
            return new Alojamiento[size];
        }
    };
}
