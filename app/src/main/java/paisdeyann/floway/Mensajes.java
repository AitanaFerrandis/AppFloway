package paisdeyann.floway;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Timestamp;
import java.util.ArrayList;

import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.Objetos.Mensaje;



public class Mensajes extends AppCompatActivity {
    ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
    AdaptadorMensajes adaptador;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager rvLM;
    private FirebaseAuth firebaseAuth;
    private Firebase mFirebase;


    Button añadirMensaje;
    EditText editTextMensajes;

    private String mId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        Log.d("prueba","llego a mensajes");

        mId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
      //  Log.d("prueba","el mid es :"+mId);

        firebaseAuth = FirebaseAuth.getInstance();

        String chat = getIntent().getBundleExtra("bundle").getString("chat");
        //Log.d("prueba","el chat es: ");



        añadirMensaje = (Button) findViewById(R.id.buttonMensajes);
        editTextMensajes = (EditText) findViewById(R.id.editTextMensajes);

        recyclerView = (RecyclerView) findViewById(R.id.elMeuRecyclerView);
        rvLM = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLM);

        adaptador = new AdaptadorMensajes(mensajes);
        recyclerView.setAdapter(adaptador);
        Firebase.setAndroidContext(this);

        mFirebase = new Firebase("https://flowaychatviajes.firebaseio.com/chats").child(chat+"/mensajes");

        añadirMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextMensajes.getText().toString().equals("")) {
                    // creara una instacia hacia firebase insertando el mensaje sobre la clave del autor (email)
                    Mensaje mensaje = new Mensaje(editTextMensajes.getText().toString(), "" + new Timestamp(System.currentTimeMillis()), Conexion.usuarioActivo.getId_usuario());
                    mFirebase.push().setValue(mensaje);
                    // vaciar texto
                    editTextMensajes.setText("");
                }
            }
        });

        mFirebase.addChildEventListener(new ChildEventListener() {

            @Override

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("prueba","llego al listener de firebase");
                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    try {
                        Mensaje model = dataSnapshot.getValue(Mensaje.class);
                        Log.d("prueba","he recuperado el mensaje"+model.getContenido()+" "+model.getFecha()+" "+model.getEmisor());
                        mensajes.add(model);
                        recyclerView.scrollToPosition(mensajes.size()-1);
                        adaptador.notifyItemInserted(mensajes.size()-1);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




    }
}
