package paisdeyann.floway.Threads;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.FragmentsTabs.MapViewFragment;
import paisdeyann.floway.Objetos.Usuario;

/**
 * Created by caboc on 13/02/2017.
 */

public class ConseguirUsuariosPorRadio extends AsyncTask<Object, Object, TextView> {

    @Override
    protected TextView doInBackground(Object... params) {

        double radio = (Double)params[0];

        double latitud = (Double)params[1];

        double longitud = (Double) params[2];

        int conductor = (Integer) params[3];

        int conectado = (Integer) params[4];

        MapViewFragment fragmentMapa = (MapViewFragment)params[5];

        String respuesta =  conseguirStringUsuarios(params);

        Object[] objetos = new Object[2];
        objetos[0] = respuesta;
        objetos[1] = (MapViewFragment)params[5];

        publishProgress(objetos);

        return null;
    }

    @Override
    protected void onPreExecute() {


        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(TextView textView) {
        super.onPostExecute(textView);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        String respuesta = (String) values[0];
        MapViewFragment fragmentMapa = (MapViewFragment) values[1];

        //Log.d("prueba","respuesta progress: "+respuesta);

        Gson gson = new Gson(); // usaremos esto para pasear el jason
        JsonParser parseador = new JsonParser();    // necesitamos este objeto para conseguir el elemento raiz del String


        JsonElement raiz = parseador.parse(respuesta); // conseguimos el elemnto raiz del string
        JsonArray lista = raiz.getAsJsonArray();    // el elemento raiz es un array de objetos jason por eso nos creamos un objeto JsonArray y lo cogemos con este metodo

        for (JsonElement elemento : lista) {    // recorremos el JsonArray lista con este for each abreviado y nos creamos un nuevo usuario
            Usuario usuario = gson.fromJson(elemento, Usuario.class);
            //Log.d("prueba","usuario proximidad "+usuario.getNombre());
            //usuario.imprimir();

            fragmentMapa.addUsuario(usuario);
            //public void insertMarca(double latitud, double longitud, String titulo, String descripcion)

            fragmentMapa.insertMarca(usuario.getLatitud(),usuario.getLongitud(),usuario.getNombre()+" "+usuario.getApellidos(),usuario.getId_usuario());

        }


}

    @Override
    protected void onCancelled(TextView textView) {
        super.onCancelled(textView);
    }

    public String conseguirStringUsuarios(Object[] objetos){
        URL url = null;
        HttpURLConnection conn;

        String respuesta="";
        Log.d("prueba","llego a conseguir usuarios");


        try {
           // objetos[0] = 10000.0;  // borrar esto
            String cadenaConexion = Conexion.SERVER+"/APIFLOWAY-PHP/apiNueva.php?conectado="+(int)objetos[4]+"&conductor="+(int)objetos[3]+"&radio="+(double)objetos[0]+"&longitud="+(double)objetos[2]+"&latitud="+(double)objetos[1]+"&"+ Conexion.APIKEY;
           // String cadenaConexion = Conexion.SERVER+"/APIFLOWAY-PHP/apiNueva.php?conectado=1&conductor=1&radio=5&longitud=-0.533108&latitud=39.590381&"+ Conexion.APIKEY;
            Log.d("prueba","cadena conexion: "+cadenaConexion);
            url = new URL(cadenaConexion);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");                   // SuperGlobal de como vas a enviar los datos
            conn.setDoInput(true);

            conn.connect();

            int response = conn.getResponseCode();		// código devuelto por web Service
            InputStream is = conn.getInputStream();

            respuesta = transformarInputStremString(is);

           // Log.d("prueba","respuesta: "+respuesta);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return respuesta;
    }

    private String transformarInputStremString(InputStream is) {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String linea = "";

        try {
            while((linea = reader.readLine()) != null){
                stringBuilder.append(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


}
