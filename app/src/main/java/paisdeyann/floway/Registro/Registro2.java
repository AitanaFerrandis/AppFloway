package paisdeyann.floway.Registro;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import paisdeyann.floway.R;

public class Registro2 extends AppCompatActivity {

    Button continuar;
    EditText poblacion,cp;
    Switch horario;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_2);

        poblacion = (EditText)findViewById(R.id.editPoblacion);
        cp = (EditText)findViewById(R.id.editCP);
        horario=(Switch)findViewById(R.id.switch1);

        continuar =(Button) findViewById(R.id.ButtonContinuarSegundoReg);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (poblacion.getText().toString().equals("") ||  cp.getText().toString().equals("") ){

                    Toast.makeText(Registro2.this, "Rellena todos los campos antes de continuar, por favor.", Toast.LENGTH_SHORT).show();

                } else if (cp.getText().toString().length()<5 || cp.getText().toString().length()>5){

                    Toast.makeText(Registro2.this, "El CP es incorrecto, introduzcalo de nuevo por favor.", Toast.LENGTH_SHORT).show();

                } else{

                    guardaPreferencias();
                    Intent intent = new Intent(getApplicationContext(), Registro3.class);
                    startActivity(intent);
                }
            }
        });


    }
    //el método guardaPreferencias crea el sharedPreferences y le pasa el archivo donde guardarlo y lo pone en modo privado
    public void guardaPreferencias(){
        SharedPreferences mySharedPreferences = getSharedPreferences(Registro1.PREFS, Registro1.MODE_APPEND);
        //guardamos todas las preferencias con el editor
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("Población",poblacion.getText().toString());
        editor.putString("CP", cp.getText().toString());

        if (horario.isChecked()){
            editor.putString("Horario", "tarde");
        }else{
            editor.putString("Horario", "mañana");
        }

        //actualizamos el fichero y con el intent vamos al subActivity
        editor.commit();

    }

}