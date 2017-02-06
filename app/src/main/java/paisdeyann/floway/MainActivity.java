package paisdeyann.floway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import paisdeyann.floway.Threads.Loguearse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText username;
    EditText password;
    TextView linkRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.User);
        password = (EditText)findViewById(R.id.con);
        linkRegistrarse = (TextView) findViewById(R.id.link);
        linkRegistrarse.setOnClickListener(this);


    }

    public void login(View view){
        if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){


            Object[] objetos = new Object[3];
            objetos[0] = username;
            objetos[1] = username.getText().toString();
            objetos[2] = password.getText().toString();
            Loguearse loguearse = new Loguearse();
            loguearse.execute(objetos);

        }else if(username.getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "Usuario no válido", Toast.LENGTH_SHORT).show();
            password.setText("");
        }else{
            Toast.makeText(MainActivity.this, "Contraseña no válida", Toast.LENGTH_SHORT).show();
            username.setText("");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.link :
                Intent intent = new Intent(v.getContext(),RegistroActivity.class);
                v.getContext().startActivity(intent);
                break;
        }

    }
}
