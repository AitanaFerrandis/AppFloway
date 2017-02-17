package paisdeyann.floway.FragmentsTabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.R;


public class PantallaTransacciones extends Fragment {

    Menu_Principal activity;
    Context context;


    public PantallaTransacciones() {
        // Required empty public constructor
    }
    public void setContext(Context cont,Menu_Principal act){
        activity=act;
        context=cont;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pantalla_transacciones, container, false);
        return rootView;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
