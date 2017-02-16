package paisdeyann.floway.Conexion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import paisdeyann.floway.Objetos.Usuario;

/**
 * Created by caboc on 20/01/2017.
 */

public class Conexion {

   //public static final String SERVER = "http://192.168.1.131";

    public static final String SERVER = "http://54.93.101.246";

    public static final String APIKEY="api_key=HDRYsemQRQRPRT";

    public static Usuario usuarioActivo;



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
