package paisdeyann.floway.Threads;
import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.Objetos.Usuario;
import paisdeyann.floway.Registro.Registro1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class Loguearse extends AsyncTask<Object, Object, TextView> implements View.OnClickListener{

    EditText userName;
    String usuarioComprobar;
    String passwordComprobar;
    Usuario usuario = null; // aqui guardaremos el usuario para usarlo en toda la aplicacion

    String logueado = "false";
    Context contexto;


    @Override

    protected void onPreExecute() {  }


    @Override
    protected TextView doInBackground(Object... params) {

        contexto = (Context) params[3];
        userName = (EditText)params[0];
        usuarioComprobar = (String)params[1];
        passwordComprobar = (String)params[2];

        conectarseALaRed((EditText)params[0]);


        return (EditText) params[0];
    }

    @Override

    protected void onProgressUpdate(Object... progress) {




    }

    @Override
    protected void onPostExecute(TextView t) {
        if(logueado.equals("true")) {
            Toast.makeText(contexto, "Logueado", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(t.getContext(),Menu_Principal.class);
            t.getContext().startActivity(i);

        }else if(logueado.equals("false")) {
            Toast.makeText(t.getContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(t.getContext(),"Clave Key Incorrecta",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled(TextView t) {

    }


    public void conectarseALaRed(EditText view) {


            ConnectivityManager connMgr = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

            // podem fer la connexió a alguna URL
                //Toast.makeText(view.getContext(), "CONECTO", Toast.LENGTH_SHORT).show();

                String cadenaConexion = Conexion.SERVER+"/apiFloway/apiNueva.php?where="+usuarioComprobar+Conexion.APIKEY;
                Log.d("prueba",cadenaConexion);
                URL url;
                InputStream is = null;  // esto lo usaremos para conseguir un String de lo que venga

                String respuestaJason = null;       // aqui guardaremos un string del jason que venga

                try {

                    url = new URL(cadenaConexion);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();


                    conn.setReadTimeout(10000);

                    conn.setConnectTimeout(15000);

                    conn.setRequestMethod("GET");   // la manera con la que mandamos la uri

                    conn.setDoInput(true);  // esto en true significa que vamos a recibir datos

                    conn.connect();

                    int response = conn.getResponseCode();  // codigo de respuesta que manda el servidor para saber como ha ido todo


                    // aqui estamos recibiendo la respuesta jason de la api y convirtiendola en String
                   // is = new BufferedInputStream(conn.getInputStream());
                    is = conn.getInputStream();
                    respuestaJason = readStream(is);

                    /*
                    try {
                        objetoJason = new JSONArray(respuestaJason);
                        String temp = objetoJason.getJSONObject(0).getString("nombre");
                        Log.d("prueba",temp);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }*/

                   // Log.d("prueba",respuestaJason);


                    // aqui vamos a parsear el JASON a un objeto usuario

                    if(respuestaJason.toString().equals("fallo api key\n")){
                        Log.d("prueba","key incorrecta");
                        logueado = "key incorrecta";
                    }else {
                        Gson gson = new Gson(); // usaremos esto para pasear el jason
                        JsonParser parseador = new JsonParser();    // necesitamos este objeto para conseguir el elemento raiz del String


                        JsonElement raiz = parseador.parse(respuestaJason); // conseguimos el elemnto raiz del string
                        JsonArray lista = raiz.getAsJsonArray();    // el elemento raiz es un array de objetos jason por eso nos creamos un objeto JsonArray y lo cogemos con este metodo

                        for (JsonElement elemento : lista) {    // recorremos el JsonArray lista con este for each abreviado y nos creamos un nuevo usuario
                            usuario = gson.fromJson(elemento, Usuario.class);
                            // System.out.println(u.getNombre());
                        }


                        if (usuario != null) {

                            if (usuario.getPassword().equals(passwordComprobar)) {
                                Log.d("prueba", "logueado");
                                // Log.d("prueba","nombre"+usuario.getNombre()+" apellido "+usuario.getApellidos()+" pass "+usuario.getPassword()+" usuario "+usuario.getUsuario()+" id "+usuario.getId_usuario()+" cp "+usuario.getCp()+" horario "+usuario.getHorario()+" poblacion "+usuario.getPoblacion()+" puntuacion "+usuario.getPuntuacion()+" foto "+usuario.getFoto());
                                logueado = "true";

                                Object[] objetos = new Object[1];
                                objetos[0] = view;

                                publishProgress(objetos);

                                //Toast.makeText(view.getContext(), "Usuario y contraseña correctos", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("prueba", "usuario no coincide con contraseña");
                                // Toast.makeText(view.getContext(), "Usuario y contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d("prueba", "no existe usuario");
                            // Toast.makeText(view.getContext(), "No hay ningun usuario con ese nombre", Toast.LENGTH_SHORT).show();
                        }
                    }

                   /* List <Usuario> usuarios =
                            new ArrayList<Usuario>();
                   usuarios = gson.fromJson(respuestaJason,Usuario.class);
                    */



                } catch (MalformedURLException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }finally {

                    if(is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

               // Log.d("prueba",stringBuilder.toString()); // esto mostraba el stringBuilder para ver que nos estaba llegando
            } else {
                Log.d("prueba","no conecto");
                Snackbar.make(view,"No hay conexion",Snackbar.LENGTH_SHORT).setAction("REINTENTAR", this).show();
            // mostrem un error indicant que no dispossem de connexió a la Xarxa
                //Toast.makeText(view.getContext(), "NO CONECTO", Toast.LENGTH_SHORT).show();


            }

        }



    public String readStream(InputStream in){
        //llegim de l’InputStream i ho conver5m a un String
        StringBuilder stringBuilder = new StringBuilder();  // cadena q iremos añadiendo los datos del documento JSON
        BufferedReader reader = new BufferedReader(new InputStreamReader(in)); // buffer donde iremos leyendo el documento json que nose envie el servidor
        String line;

        try {
            while ((line = reader.readLine()) != null) {

                stringBuilder.append(line).append('\n');

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }


    @Override
    public void onClick(View v) {
        Object[] objetos = new Object[3];
        objetos[0] = userName;
        objetos[1] = usuarioComprobar;
        objetos[2] = passwordComprobar;
        Loguearse loguearse = new Loguearse();
        loguearse.execute(objetos);
    }
}
