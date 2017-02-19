package paisdeyann.floway.Threads;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import paisdeyann.floway.Objetos.Publicacion;
import paisdeyann.floway.R;

/**
 * Created by caboc on 19/02/2017.
 */

public class AdaptadorPublicaciones extends RecyclerView.Adapter<AdaptadorPublicaciones.AdaptadorPublicacionesViewHolder>{

    ArrayList<Publicacion> publicaciones;

    public AdaptadorPublicaciones( ArrayList<Publicacion> publicaciones){
        this.publicaciones = publicaciones;
    }

    @Override
    public AdaptadorPublicacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itempublicacion,parent,false);
        return new AdaptadorPublicacionesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(AdaptadorPublicacionesViewHolder holder, int position) {

        holder.fecha.setText(publicaciones.get(position).getFecha());
        holder.mensaje.setText(publicaciones.get(position).getMensaje());
        holder.nombre.setText(publicaciones.get(position).getNombreUsuarioEmisor());
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }

    public class AdaptadorPublicacionesViewHolder extends RecyclerView.ViewHolder{

        TextView fecha;
        TextView nombre;
        TextView mensaje;

        public AdaptadorPublicacionesViewHolder(View itemView) {
            super(itemView);
            fecha = (TextView) itemView.findViewById(R.id.textViewFechaItemPublicacion);
            nombre = (TextView)itemView.findViewById(R.id.textViewNombreItemPublicacion);
            mensaje = (TextView)itemView.findViewById(R.id.textViewMensajeItemPublicacion);
        }


    }

}
