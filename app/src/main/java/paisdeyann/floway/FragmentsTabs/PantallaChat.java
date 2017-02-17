package paisdeyann.floway.FragmentsTabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import paisdeyann.floway.AdaptadorGrupoMensajes;
import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.Objetos.Conversacion;
import paisdeyann.floway.Objetos.Mensaje;
import paisdeyann.floway.R;



public class PantallaChat extends Fragment {

    Menu_Principal activity;
    Context context;



    RecyclerView rv;
    RecyclerView.LayoutManager rvLM;
    AdaptadorGrupoMensajes adaptador;

    private Firebase mFirebase;
    private FirebaseAuth firebaseAuth;

    ArrayList<Conversacion> conversaciones = new ArrayList<Conversacion>();



    public PantallaChat() {
        // Required empty public constructor
    }
    public void setContext(Context cont,Menu_Principal act){
        activity=act;
        context=cont;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pantalla_chat, container, false);

        Log.d("prueba","llego al fragment chat");

        firebaseAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(context);


/*
        for (Conversacion conversacion:activity.getConversaciones()) {

            Log.d("prueba","desde pantalla chat "+conversacion.getChat());

        }
        Log.d("prueba","hiiiii salgo");
*/
        rv = (RecyclerView) rootView.findViewById(R.id.recyclerViewMensajes);
        rvLM = new LinearLayoutManager(context);
        rv.setLayoutManager(rvLM);



        adaptador = new AdaptadorGrupoMensajes(conversaciones, context);

        rv.setAdapter(adaptador);



        mFirebase = new Firebase("https://flowaychatviajes.firebaseio.com").child("Conversaciones");
        mFirebase.addChildEventListener(new ChildEventListener() {

            @Override

            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    try {

                        Conversacion model = dataSnapshot.getValue(Conversacion.class);

                        Log.d("prueba","voy a añadir una nueva conversacion "+model.getFecha()+" "+model.getId1()+" "+model.getChat());

                        conversaciones.add(model);

                        rv.scrollToPosition(conversaciones.size()-1);
                        adaptador.notifyItemInserted(conversaciones.size()-1);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;
    }

    public interface OnFragmentInteractionListener {
        public void onNavFragmentInteraction(String string);
    }
    public RecyclerView getRv() {
        return rv;
    }

    public void setRv(RecyclerView rv) {
        this.rv = rv;
    }


}
