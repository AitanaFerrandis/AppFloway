package paisdeyann.floway.Registro;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.R;

import static paisdeyann.floway.R.id.editNombre;


public class Registro1 extends AppCompatActivity {

    Button continuar;
    EditText nombre,apellidos,usuario,contraseña;
    boolean respuesta;
    //creamos el archivo donde vamos a guardar las preferencias
    static final String PREFS = "My preferences";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_1);

        nombre = (EditText)findViewById(editNombre);
        apellidos = (EditText)findViewById(R.id.editApellidos);
        usuario = (EditText)findViewById(R.id.editUser);
        contraseña = (EditText)findViewById(R.id.editTextPass);

        continuar =(Button) findViewById(R.id.ButtonContinuarprimerReg);

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //comprobamos que todos los campos estén rellenos, si es así, guardamos preferencias
                //y hacemos un intent a la siguiente pantalla de registro
                //de lo contrario mostramos por pantalla un mensaje para que los rellene
                if (nombre.getText().toString().equals("") || apellidos.getText().toString().equals("") ||
                        usuario.getText().toString().equals("") || contraseña.getText().toString().equals("")) {
                    Toast.makeText(Registro1.this, "Rellena todos los campos antes de continuar, por favor.", Toast.LENGTH_SHORT).show();

                } else if (!consultaUsuario() && respuesta == false) {

                    Log.d("syso","entro pq el usuario no esta registrado");
                    guardaPreferencias();

                    Intent intent = new Intent(getApplicationContext(), Registro2.class);
                    startActivity(intent);
                }

            }


        });

    }


    public boolean consultaUsuario() {

        //nos devuelve un JSon el cual parseamos en un boleano que nos dice si existe ese usuario o no
        OkHttpClient client = new OkHttpClient();
        Request Usuario = new Request.Builder()
                .url(Conexion.SERVER+"/APIFLOWAY-PHP/apiNueva.php"+ "?usuario=" + usuario.getText() +"&" +Conexion.APIKEY)
                .build();
        Log.v("syso", "Peticion: " + Usuario.toString());

        Call callUsuario = client.newCall(Usuario);
        Log.d("syso","empiezo toda la movida");
        callUsuario.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.v("syso", "Fallo de peticion");
                respuesta = false;


            }

            //
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                {
                    Log.d("syso","entro en el response");
                    try {
                        String jsonData = response.body().string();
                        Log.v("sysoImg", jsonData);

                        if (response.isSuccessful()) {
                            JSONObject json = new JSONObject(jsonData);
                            respuesta = json.getBoolean("existe");
                            Log.d("syso", "el booleano es: "+ respuesta);

                        }


                    } catch (IOException e) {
                        Log.e("syso", "Exception caught: ", e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(respuesta){
            Toast.makeText(this, "El nombre de usuario ya existe, por favor inserte otro", Toast.LENGTH_SHORT).show();
        }

        return respuesta;

    }

    //el método guardaPreferencias crea el sharedPreferences y le pasa el archivo donde guardarlo y lo pone en modo privado
    public void guardaPreferencias(){
        SharedPreferences mySharedPreferences = getSharedPreferences(PREFS ,Registro1.MODE_PRIVATE);
        //guardamos todas las preferencias con el editor
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("Nombre",nombre.getText().toString());
        editor.putString("Apellidos", apellidos.getText().toString());
        editor.putString("Usuario", usuario.getText().toString());
        editor.putString("Contraseña", contraseña.getText().toString());

        //actualizamos el fichero y con el intent vamos al subActivity
        editor.commit();

    }


}