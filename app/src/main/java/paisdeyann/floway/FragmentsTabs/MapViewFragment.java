package paisdeyann.floway.FragmentsTabs;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.MainActivity;
import paisdeyann.floway.Manifest;
import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.Objetos.Usuario;
import paisdeyann.floway.R;
import paisdeyann.floway.Threads.ConseguirUsuariosPorRadio;

import static com.google.android.gms.R.id.url;
import static paisdeyann.floway.R.id.container;

/**
 * Created by Dario on 11/02/2017.
 */

public class MapViewFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    private GoogleMap mgoogleMap;
    ArrayList<Usuario>usuarios = new ArrayList<Usuario>();
    Context context;

    double tuLatitud;
    double tuLongitud;

    MapViewFragment esteActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        esteActivity = this;
        tuLatitud =39.590381;
        tuLongitud= -0.533108;

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mgoogleMap = mMap;
                addInfo();


                double radio = 10;
               // double latitud = 39.590381 ;
               // double longitud = -0.533108;
                int conductor = 1;
                int conectado = 1;

                Object[] objetos = new Object[6];
                objetos[0] = radio;      // radio   double
                objetos[1] = tuLatitud;       // latitud  atributo clase principal  double
                objetos[2] = tuLongitud;     //longitud   atributo clase principal   double
                objetos[3] = conductor;             // conductor    int 1 conductor 0 pasajero
                objetos[4] = conectado;             // conectado    int 1 conectado 0 desconectado
                objetos[5] = esteActivity;          // este fragment


                ConseguirUsuariosPorRadio threadConseguirUsuarios = new ConseguirUsuariosPorRadio();

                threadConseguirUsuarios.execute(objetos);

                // For showing a move to my location button
                /*
                * Esto le dara permisos a el fragment para obtener su ubicacion pero falta darle un contexto

                if (ContextCompat.checkSelfPermission(Menu_Principal.class,
                        Manifest.permission.MAPS_RECEIVE){
                    googleMap.setMyLocationEnabled(true);

                }
                */




                setMarkersPasajeros();
            }
        });

        return rootView;
    }
    public void setContext(Context con){
        context = con;
    }
/*
    public void getUsers(){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://54.93.101.246/APIFLOWAY-PHP/apiNueva.php?conectado=1&conductor=1&radio=5&longitud=-0.533108&latitud=39.590381&"+ Conexion.APIKEY)
                    .build();
            Response responses = null;

            Log.d("prueba","llego getUsers");
            Log.d("prueba","http://54.93.101.246/APIFLOWAY-PHP/apiNueva.php?conectado=1&conductor=1&radio=5&longitud=-0.533108&latitud=39.590381&"+ Conexion.APIKEY);

                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                Log.d("prueba",jsonData);
                Gson gson = new Gson();

                JsonParser parseador = new JsonParser();
                JsonElement raiz = parseador.parse(jsonData);
                JsonArray listaUsuarios = raiz.getAsJsonArray();

                for (JsonElement elemento: listaUsuarios) {
                    Usuario u = gson.fromJson(elemento,Usuario.class);
                    u.imprimir();
                }


        }catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    public void setMarkersPasajeros(){
        mgoogleMap.clear();
        addInfo();
        // For dropping a marker at a point on the Map
        LatLng marker = new LatLng(this.tuLatitud, this.tuLongitud);



        MarkerOptions markerMaps = new MarkerOptions()
                .position(marker)
                .title("Yo")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.android_pasajero))
                .snippet("Mi posicion");



        mgoogleMap.addMarker(markerMaps);
        mgoogleMap.setOnMarkerClickListener(this);




        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
        mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    public void insertMarca(double latitud, double longitud, String titulo, String descripcion){
        LatLng marker2 = new LatLng(latitud, longitud);

        MarkerOptions markerMaps = new MarkerOptions()
                .position(marker2)
                .title(titulo)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.android_pasajero))
                .snippet(descripcion);

        mgoogleMap.addMarker(markerMaps);
        mgoogleMap.setOnMarkerClickListener(this);
    }
    /*
    public void setMarkersPasajeros(){
        mgoogleMap.clear();
        addInfo();
        // For dropping a marker at a point on the Map
        LatLng marker = new LatLng(-34, 151);
        Log.d("prueba","estoy en la marca pasajero");
        mgoogleMap.addMarker(new MarkerOptions()
                .position(marker)
                .title("Marker Title")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.android_pasajero))
                .snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
        mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }
*/
    public void addInfo(){
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        if (mgoogleMap != null) {
            mgoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {


                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = inflater.inflate(R.layout.info_google_window, null);

                    ImageView imag = (ImageView) v.findViewById(R.id.imageView1);
                    TextView nom = (TextView) v.findViewById(R.id.tv_locality);
                    TextView lat = (TextView) v.findViewById(R.id.tv_lat);
                    TextView lon = (TextView) v.findViewById(R.id.tv_lng);
                    Button btn = (Button) v.findViewById(R.id.button4);

                    return null;
                }
            });

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(marker.getSnippet()).setTitle(marker.getTitle()).setPositiveButton("ENVIAR MENSAJE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(context, "HAS DADO A ENVIAR MENSAJE", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(context, "HAS DADO CANCELAR", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();

        return false;
    }

    public void addUsuario(Usuario usuario){
        this.usuarios.add(usuario);
    }

/*
    public void cogerUsuarios(){

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            Log.d("prueba","conecto en el maps");
            getUsers();

        } else {
            Log.d("prueba","no conecto en el maps");
        }



    }

    public void añadirUsuarios(){
        URL url = null;
        HttpURLConnection conn;
        try {
            String cadenaConexion = "http://54.93.101.246/APIFLOWAY-PHP/apiNueva.php?conectado=1&conductor=1&radio=5&longitud=-0.533108&latitud=39.590381&"+ Conexion.APIKEY;
            Log.d("prueba","cadena conexion: "+cadenaConexion);
            url = new URL(cadenaConexion);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");                   // SuperGlobal de como vas a enviar los datos
            conn.setDoInput(true);
            Log.d("prueba","llego 1");
            conn.connect();
            Log.d("prueba","llego 2");
            int response = conn.getResponseCode();		// código devuelto por web Service
            InputStream is = conn.getInputStream();

            String respuesta = transformarInputStremString(is);

            Log.d("prueba","respuesta: "+respuesta);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
*/
}



