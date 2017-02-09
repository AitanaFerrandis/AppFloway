package paisdeyann.floway.Conexion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

/**
 * Created by caboc on 20/01/2017.
 */

public class Conexion {

   //public static final String SERVER = "http://192.168.1.128";
    public static final String SERVER = "http://10.10.5.102";

    public static final String APIKEY="&api_key=HDRYsemQRQRPRT";



    public static boolean comprobarConexion(View view){

        boolean respuesta = false;

        ConnectivityManager connMgr = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            respuesta = true;

        } else {
            respuesta = false;
        }

        return respuesta;
    }


}
