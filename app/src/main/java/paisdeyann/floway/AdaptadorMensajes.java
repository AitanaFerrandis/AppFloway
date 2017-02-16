package paisdeyann.floway;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import paisdeyann.floway.Objetos.Mensaje;

/**
 * Created by caboc on 15/02/2017.
 */

public class AdaptadorMensajes  extends RecyclerView.Adapter<AdaptadorMensajes.AdaptadorMensajesViewHolder> {

    ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();

    public AdaptadorMensajes(ArrayList<Mensaje> mensajes){
        this.mensajes = mensajes;
    }

    @Override
    public AdaptadorMensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemmensajes,parent,false);
        return new AdaptadorMensajesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(AdaptadorMensajesViewHolder holder, int position) {

        holder.fecha.setText(mensajes.get(position).getFecha());
        holder.mensaje.setText(mensajes.get(position).getContenido());

    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }


    public class AdaptadorMensajesViewHolder extends RecyclerView.ViewHolder{

        TextView mensaje;
        TextView fecha;

        public AdaptadorMensajesViewHolder(View itemView) {
            super(itemView);

            mensaje = (TextView) itemView.findViewById(R.id.textViewItemMensajeContenido);
            fecha = (TextView) itemView.findViewById(R.id.textViewItemMensajeFecha);
        }
    }



}
