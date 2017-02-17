package paisdeyann.floway.Threads;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import paisdeyann.floway.Conexion.Conexion;

/**
 * Created by Dario on 17/02/2017.
 */

public class Desconecta extends AsyncTask<Object, Object, TextView> {

    @Override
    protected TextView doInBackground(Object... params) {
        //  String POST_PARAMS = "olduser=" + objetos[0] + "&conectado=" + objetos[1] + "&conductor=" + objetos[2] ;
        String respuesta =  cambiarEstado(params);

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

    }

    @Override
    protected void onCancelled(TextView textView) {
        super.onCancelled(textView);
    }

    public String cambiarEstado(Object[] objetos) {
        String cadenaConexion = Conexion.SERVER + "/APIFLOWAY-PHP/apiNueva.php" + "?" + Conexion.APIKEY+"&conectado=f";


        String POST_PARAMS = "olduser=" + objetos[0] + "&conectado=" + objetos[1];

        URL obj = null;
        try {
            obj = new URL(cadenaConexion);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);


            DataOutputStream dStream = new DataOutputStream(con.getOutputStream());
            dStream.writeBytes(POST_PARAMS); //Writes out the string to the underlying output stream as a sequence of bytes<br />
            dStream.flush(); // Flushes the data output stream.<br />
            dStream.close(); // Closing the output stream.<br />
            // For POST only - START

            int responseCode = con.getResponseCode();


            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}


