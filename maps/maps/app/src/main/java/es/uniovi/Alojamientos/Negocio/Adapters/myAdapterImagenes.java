package es.uniovi.Alojamientos.Negocio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.uniovi.Alojamientos.R;

public class myAdapterImagenes extends RecyclerView.Adapter<myAdapterImagenes.ViewHolder> {

  private String[] imagenes;
  private int layout;
  private Context context;

  public myAdapterImagenes(int layout, String[] imagenes) {
    this.layout = layout;
    this.imagenes = imagenes;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

    // Cogemos el contexto del padre e inflamos
    View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    // Aqui podemos inicializar el context, que lo usaremos para picasso (en la nueva version de Picasso no hace falta)
    context = parent.getContext();
    // Le pasamos el viewHolder esa vista inflada
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  // Volcar los datos del modelo en nuestros atributos del ViewHolder
  // Este método solo se llama cuando se crea el RecyclerView, luego no se vuelve a renderizar por cada uno de los objetos,
  // por eso tenemos el  itemView.setOnClickListener para que se llame cada vez que se haga click en algun elemento
  // este listener se sobrescribirá en la clase en la que se cree el adaptador
  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    viewHolder.bind(imagenes, i);
  }

  // Retorna el número de items que vamos a tener
  @Override
  public int getItemCount() {
    return imagenes.length;
  }

  // Ahora en vez de hacer pasarle el Holder a la vista con setTag, le pasamos al Holder la vista y a partir de ahí extraemos la
  // referencia/referencias que queremos
  public class ViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageViewPoster;

    public ViewHolder(View v){
      super(v);
      this.imageViewPoster = v.findViewById(R.id.card_view_image_details);
    }

    public void bind(final String[] imagenes, int pos){


      // NUEVO - PICASSO (para centrar imagen al card)
      //Picasso.get().load(R.drawable.imagen_default).fit().into(imageViewPoster);
      String url = "https://www.turismoasturias.es"+imagenes[pos];
      Picasso.get().load(url).fit().into(imageViewPoster);
    }
  }
}
