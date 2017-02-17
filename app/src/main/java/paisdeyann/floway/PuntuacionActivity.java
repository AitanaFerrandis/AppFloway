package paisdeyann.floway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PuntuacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerv);
        LinearLayoutManager lmanager = new LinearLayoutManager(this);
        rv.setLayoutManager(lmanager);


        ArrayList<Puntuacion> puntuacion = new ArrayList<>();

        for (int i=0;i<5;i++){
            puntuacion.add(new Puntuacion("Manel","Viel","90"));
            puntuacion.add(new Puntuacion("Sr.","Cuesta","40"));
            puntuacion.add(new Puntuacion("Baptista","Basset","70"));
        }



        Adaptador_Puntuaciones adaptador;
        adaptador = new Adaptador_Puntuaciones(puntuacion);
        rv.setAdapter(adaptador);


    }
}
