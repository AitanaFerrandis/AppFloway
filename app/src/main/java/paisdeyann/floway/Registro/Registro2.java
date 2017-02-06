package paisdeyann.floway.Registro;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

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
                Intent intent = new Intent(getApplicationContext(), Registro3.class);
                startActivity(intent);
            }
        });


    }


}