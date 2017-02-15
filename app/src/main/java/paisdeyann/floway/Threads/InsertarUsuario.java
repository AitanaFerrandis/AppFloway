package paisdeyann.floway.Threads;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.R;
import paisdeyann.floway.Registro.Registro2;

import static paisdeyann.floway.R.id.con;
import static paisdeyann.floway.R.id.imageView;

/**
 * Created by caboc on 08/02/2017.
 */

public class InsertarUsuario extends AsyncTask<Object, Object, Object> {


    private Object View;

    @Override
    protected Object doInBackground(Object... params) {


        if(Conexion.comprobarConexion((ImageView)params[9])){
            Log.d("prueba","tengo conexion");

            insertarUsuario(params);


        }else{
            Log.d("prueba","no tengo conexion");
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



    public void insertarUsuario(Object[] parametros){

        String cadenaConexion = Conexion.SERVER+"/APIFLOWAY-PHP/apiNueva.php"+"?"+Conexion.APIKEY;


        String POST_PARAMS = "nombre="+parametros[0]+"&apellidos="+parametros[1]+"&usuario="+parametros[2]+"&password="+parametros[3]
                +"&poblacion="+parametros[4]+"&codigoPostal="+parametros[5]+"&puntuacion="+parametros[6]+"&horario="+parametros[7];


        Log.d("prueba",cadenaConexion);
        Log.d("prueba",POST_PARAMS);
        URL obj = null;
        try {
            obj = new URL(cadenaConexion);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);


            DataOutputStream dStream = new DataOutputStream(con.getOutputStream());
                    dStream.writeBytes(POST_PARAMS); //Writes out the string to the underlying output stream as a sequence of bytes<br />
            dStream.flush(); // Flushes the data output stream.<br />
            dStream.close(); // Closing the output stream.<br />
            // For POST only - START

            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                Log.d("prueba","http ok");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());

                if (response.toString().equals("Usuario insertado")){

                    ImageView v= (ImageView)parametros[9];

                    Intent resultIntent = new Intent(v.getContext(), Menu_Principal.class);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity(v.getContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(v.getContext()).setSmallIcon(R.drawable.text)
                                    .setContentTitle("BIENVENIDO")
                                    .setContentText("Ya estás registrado")
                                    .setLights(0xff00ff00, 300, 100)
                                    .setVibrate(new long[] {1000, 2000, 3000, 4000, 5000 })
                                    .setLargeIcon(BitmapFactory.decodeResource(v.getContext().getResources(), R.drawable.text));
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager = (NotificationManager) v.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());



                    Intent intent = new Intent(v.getContext(), Menu_Principal.class);
                    v.getContext().startActivity(intent);
                }


            } else {
                System.out.println("POST request not worked");
            }





        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
