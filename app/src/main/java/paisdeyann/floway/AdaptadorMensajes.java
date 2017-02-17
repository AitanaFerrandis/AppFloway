package paisdeyann.floway;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.Objetos.Mensaje;
import paisdeyann.floway.Threads.ConseguirUsuarioPorId;

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

        if(Conexion.usuarioActivo.getId_usuario() == mensajes.get(position).getEmisor()){

            holder.itemView.setBackgroundResource(R.color.item_message_chat_emite);
            holder.usuario.setText("Tù");
        }else{
            holder.itemView.setBackgroundResource(R.color.item_message_chat_recibe);
            holder.usuario.setText("Otro");

            Object[] objetos = new Object[5];
            objetos[0] = holder.mensaje.getContext();
            objetos[1] = mensajes.get(position).getEmisor();
            objetos[2] = holder.usuario;
            objetos[3] = holder.fecha;
            objetos[4] = mensajes.get(position).getFecha();

            ConseguirUsuarioPorId myThread = new ConseguirUsuarioPorId();
            myThread.execute(objetos);
        }

    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }


    public class AdaptadorMensajesViewHolder extends RecyclerView.ViewHolder{

        TextView mensaje;
        TextView fecha;
        TextView usuario;
        View view;

        public AdaptadorMensajesViewHolder(View itemView) {
            super(itemView);

            mensaje = (TextView) itemView.findViewById(R.id.textViewItemMensajeContenido);
            fecha = (TextView) itemView.findViewById(R.id.textViewItemMensajeFecha);
            usuario = (TextView) itemView.findViewById(R.id.textViewItemMensajeUsuario);
            view = itemView;
        }
    }



}
