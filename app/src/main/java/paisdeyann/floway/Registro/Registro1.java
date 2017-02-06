package paisdeyann.floway.Registro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import paisdeyann.floway.R;




public class Registro1 extends AppCompatActivity {

    Button continuar;
    EditText nombre,apellidos,usuario,contraseña;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_1);

        nombre = (EditText)findViewById(R.id.editNombre);
        apellidos = (EditText)findViewById(R.id.editApellidos);
        usuario = (EditText)findViewById(R.id.editUser);
        contraseña = (EditText)findViewById(R.id.editTextPass);

        continuar =(Button) findViewById(R.id.ButtonContinuarprimerReg);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registro2.class);
                startActivity(intent);
            }
        });

    }


}
