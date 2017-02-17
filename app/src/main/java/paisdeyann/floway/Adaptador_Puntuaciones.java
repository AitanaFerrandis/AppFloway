package paisdeyann.floway;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aitana on 16/02/2017.
 */

public class Adaptador_Puntuaciones extends RecyclerView.Adapter<Adaptador_Puntuaciones.ViewHolder> {
    private ArrayList<Puntuacion> listaPuntuaciones;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView nombre, apellidos, puntuacion;
        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombre);
            apellidos = (TextView) v.findViewById(R.id.apellidos);
            puntuacion = (TextView) v.findViewById(R.id.puntuacion);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adaptador_Puntuaciones(ArrayList<Puntuacion> puntuaciones) {
        listaPuntuaciones = puntuaciones;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Adaptador_Puntuaciones.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_puntuaciones, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombre.setText(this.listaPuntuaciones.get(position).getNombre());
        holder.apellidos.setText(this.listaPuntuaciones.get(position).getApellidos());
        holder.puntuacion.setText(this.listaPuntuaciones.get(position).getPuntuacion());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listaPuntuaciones.size();
    }
}
