package paisdeyann.floway;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import paisdeyann.floway.R;

public class Invitar_Amigos extends AppCompatActivity {

    Button btnenviar;
    EditText editxtcorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitar__amigos);
        editxtcorreo = (EditText) findViewById(R.id.editxtcorreo);
        btnenviar = (Button) findViewById(R.id.btnenviar);
        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editxtcorreo.getText().toString().equals("")){
                    Toast.makeText(Invitar_Amigos.this, "Por favor, introduzca un correo electrónico.", Toast.LENGTH_SHORT).show();
                }else{
                    String to =  editxtcorreo.getText().toString();
                    enviar(to,  "Hola", "Esto es un email enviado desde una app de Android");
                }
            }
        });
    }




    private void enviar(String to,
                        String asunto, String mensaje) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

}

