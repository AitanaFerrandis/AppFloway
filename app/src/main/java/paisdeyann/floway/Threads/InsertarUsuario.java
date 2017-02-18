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
import paisdeyann.floway.Objetos.Usuario;
import paisdeyann.floway.R;
import paisdeyann.floway.Registro.Registro2;

import static paisdeyann.floway.R.id.con;
import static paisdeyann.floway.R.id.imageView;

/**
 * Created by caboc on 08/02/2017.
 */

public class InsertarUsuario extends AsyncTask<Object, Object, Object> {

    Usuario usuario=null;


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
                +"&poblacion="+parametros[4]+"&codigoPostal="+parametros[5]+"&puntuacion="+parametros[6]+"&horario="+parametros[7]+"&data="+parametros[8];



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

            InputStream is = con.getInputStream();
            String respuesta = readStream(is);
            respuesta = respuesta.replaceAll("\n","");
            Log.d("prueba","el usuario que acabo de insertar es id "+respuesta+ " sin salto");


            usuario = new Usuario(Integer.parseInt(respuesta),(String)parametros[0],(String)parametros[1],(String)parametros[2],(String)parametros[3],(String)parametros[4],(String)parametros[5], (String)parametros[7],(int)parametros[6],(String)parametros[8],1,0,0,0);
            Conexion.usuarioActivo = usuario;
            Log.d("prueba","usuario activo es abajo: ");
            usuario.imprimir();

            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                Log.d("prueba","http ok");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                Log.d("prueba","llego");
                String inputLine;
                StringBuffer response = new StringBuffer();
                Log.d("prueba","llego1");
                /*
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    Log.d("prueba","llego2");
                }
                */
                in.close();
                Log.d("prueba","llego3");
                // print result
                System.out.println(response.toString());
                Log.d("prueba","estoy imprimiendo un salto de linea "+respuesta+" creo q si");

                if (!respuesta.equals("")){

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
