package paisdeyann.floway.Threads;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import paisdeyann.floway.Conexion.Conexion;

/**
 * Created by caboc on 19/02/2017.
 */

public class incrementarPuntuacion extends AsyncTask<Object, Object, Object> {

    Context contexto;

    @Override
    protected Object doInBackground(Object... params) {

        //params[0] contexto
        //params[1] puntuacion
        //params[2] id usuario a modificar

        contexto = (Context)params[0];
        Log.d("prueba","llego al thread incremento en puntuacion");
        if(conectarAlaRed()){
            Log.d("prueba","conecto en incremento en puntuacion");
            incrementarPuntuacion(params);

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    public boolean conectarAlaRed(){
        boolean respuesta = false;

        ConnectivityManager connMgr = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            respuesta = true;

        }

        return respuesta;
    }

    public void incrementarPuntuacion(Object[] objetos){

        String cadenaConexion = Conexion.SERVER+"/APIFLOWAY-PHP/apiNueva.php"+"?"+Conexion.APIKEY+"&puntuacion=afsdaf";
        Log.d("prueba","LA CADENAD DE CONEXION EN INCREMENTO PUNTUACION ES "+cadenaConexion);
        String POST_PARAMS = "olduser=" + ((int)objetos[2]) +"&puntuacion="+((int)objetos[1]);
        Log.d("prueba","Los parametros en incremento puntuacion son "+POST_PARAMS);

        URL obj = null;
        try {
            obj = new URL(cadenaConexion);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);

            Log.d("prueba","aqui creo q llego puntuacion");
            DataOutputStream dStream = new DataOutputStream(con.getOutputStream());
            dStream.writeBytes(POST_PARAMS); //Writes out the string to the underlying output stream as a sequence of bytes<br />
            dStream.flush(); // Flushes the data output stream.<br />
            dStream.close(); // Closing the output stream.<br />
            // For POST only - START
            Log.d("prueba","sigo por aki puntuacion");
            InputStream is = con.getInputStream();
            String respuesta = readStream(is);

            int responseCode = con.getResponseCode();
            Log.d("prueba","llego al final puntuacion");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

}
