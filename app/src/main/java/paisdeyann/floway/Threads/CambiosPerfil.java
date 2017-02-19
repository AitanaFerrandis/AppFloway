package paisdeyann.floway.Threads;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;

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
import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.R;

/**
 * Created by caboc on 18/02/2017.
 */

public class CambiosPerfil extends AsyncTask<Object, Object, Object> {

    Context contexto;

    @Override
    protected Object doInBackground(Object... params) {

        //params[0] contexto
        //params[1] indice campo
        //params[2] valorNuevo
        //params[3] indice olduser
        //params[4] valor idUsuario cambiar

        contexto = (Context) params[0];


        if(conectarAlaRed()){

            modificarUsuario(params);

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


    public void modificarUsuario(Object[] objetos){

        Log.d("prueba","llego a modificar usuario");

        String cadenaConexion = Conexion.SERVER+"/APIFLOWAY-PHP/apiNueva.php"+"?"+Conexion.APIKEY;
        String POST_PARAMS = ((String)objetos[1]) + "=" + ((String)objetos[2]) +"&"+((String)objetos[3])+"="+((String)objetos[4]);

        Log.d("prueba","parametros son "+POST_PARAMS);

        URL obj = null;
        try {
            obj = new URL(cadenaConexion);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);

            Log.d("prueba","aqui creo q llego");
            DataOutputStream dStream = new DataOutputStream(con.getOutputStream());
            dStream.writeBytes(POST_PARAMS); //Writes out the string to the underlying output stream as a sequence of bytes<br />
            dStream.flush(); // Flushes the data output stream.<br />
            dStream.close(); // Closing the output stream.<br />
            // For POST only - START
            Log.d("prueba","sigo por aki");
            InputStream is = con.getInputStream();
            String respuesta = readStream(is);

            int responseCode = con.getResponseCode();
            Log.d("prueba","llego al final");

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
