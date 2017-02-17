package paisdeyann.floway.FragmentsTabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;


import paisdeyann.floway.Conexion.Conexion;
import paisdeyann.floway.Mensajes;
import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.Objetos.Conversacion;
import paisdeyann.floway.Objetos.Usuario;
import paisdeyann.floway.R;
import paisdeyann.floway.Threads.CambiaUbic;
import paisdeyann.floway.Threads.ConseguirUsuariosPorRadio;
import paisdeyann.floway.Threads.Desconecta;
import paisdeyann.floway.Threads.DesconectarseCambiarPasajero;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Dario on 11/02/2017.
 */

public class MapViewFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    private GoogleMap mgoogleMap;
    ArrayList<Usuario>usuarios = new ArrayList<Usuario>();
    Context context;

    double tuLatitud;
    double tuLongitud;

    public String getChatUsado() {
        return chatUsado;
    }

    public void setChatUsado(String chatUsado) {
        this.chatUsado = chatUsado;
    }

    public boolean isChatRepetido() {
        return chatRepetido;
    }

    public void setChatRepetido(boolean chatRepetido) {
        this.chatRepetido = chatRepetido;
    }

    MapViewFragment esteActivity;
    LocationManager locationManager;
    Menu_Principal activity;
    ProgressBar mProgressBar;
    double radio = 10;
    int conductor = 1;
    public int conectado = 1;

    boolean chatRepetido = false;
    int idMarcaPuntoMarcado;
    String chatUsado;
    //ArrayList<Conversacion> conversaciones = new ArrayList<Conversacion>();


    //Listener de la ubicacion
    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

           if(conectado==1) {
               tuLatitud = location.getLatitude();
               tuLongitud = location.getLongitude();
               zoom();

               pintaUsuarios(tuLatitud, tuLongitud, radio, conductor, conectado);

               //String POST_PARAMS = "olduser=" + objetos[0] + "&longitud=" + objetos[1] + "&latitud=" + objetos[2];
               CambiaUbic c = new CambiaUbic();
               Object[] objetos = new Object[3];
               objetos[0] = Conexion.usuarioActivo.getId_usuario();      // radio   double
               objetos[1] = tuLongitud;       // latitud  atributo clase principal  double
               objetos[2] = tuLatitud;     //longitud   atributo clase principal   double
               c.execute(objetos);



               Log.v("syso", "latitud y longitud " + tuLatitud + " , " + tuLongitud);
           }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        esteActivity = this;

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediate


        mProgressBar = (ProgressBar)  rootView.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        ImageView mRefreshImageView = (ImageView)  rootView.findViewById(R.id.refreshImageView);
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conectado==1) {
                    getUbicacion();
                    getLastUbic();
                    zoom();
                    pintaUsuarios(tuLatitud,tuLongitud,radio,conductor,conectado);}
    else {
        Toast.makeText(activity, "Estas desconectado, conectate para realizar esta acción", Toast.LENGTH_SHORT).show();
        }

            }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mgoogleMap = mMap;
               if(!isLocationEnabled()){
                    activeGPS();
                }else {
                   getUbicacion();
                   getLastUbic();
                   zoom();
                   pintaUsuarios(tuLatitud, tuLongitud, radio, conductor, conectado);
               }
            }

        });



        return rootView;
    }


    public void setContext(Context con, Menu_Principal me){
        context = con;
        activity = me;
    }

    public void getLastUbic(){
        if(checkLocationPermission()){

            locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       if(loc!=null) {
           tuLatitud = loc.getLatitude();
           tuLongitud = loc.getLongitude();
       }
            Log.v("syso","Las nuevas ubicaciones son:"+ tuLatitud+" , "+tuLongitud);
        }
    }

    public void getUbicacion(){

        long minTime=10;
        float minDistance=100;
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        if(checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        }
    }


    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            else
            {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        2);
            }
            return false;
        }
        else
        {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {

            }

            return true;
        }
    }
    protected boolean isLocationEnabled(){
        String le = LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(le);
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return false;
        } else {
            return true;
        }
    }
    public void activeGPS(){

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Activa la ubicacion")
                    .setMessage("")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("CANCELAR",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    getUbicacion();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        //mgoogleMap.clear();

    }

    public void zoom(){
        // For dropping a marker at a point on the Map
        LatLng marker = new LatLng(this.tuLatitud, this.tuLongitud);

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
        mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void pintaPasajeros(){
        conductor=1;
        if(conectado==1) {
        getLastUbic();

            pintaUsuarios(tuLatitud, tuLongitud, radio, conductor, conectado);}
        else {
            Toast.makeText(activity, "Estas desconectado, conectate para realizar esta acción", Toast.LENGTH_SHORT).show();
        }
    }
    public void pintaConductores(){

        conductor=0;
        if(conectado==1) {
        getLastUbic();

            pintaUsuarios(tuLatitud, tuLongitud, radio, conductor, conectado);}
        else {
            Toast.makeText(activity, "Estas desconectado, conectate para realizar esta acción", Toast.LENGTH_SHORT).show();
        }
    }

    public void conecta(){
        mgoogleMap.clear();
        conectado=1;


        Desconecta d = new Desconecta();
        Object[] objetos1 = new Object[2];
        objetos1[0] = Conexion.usuarioActivo.getId_usuario();
        objetos1[1] = conectado;
        d.execute(objetos1);

        if(conductor==0){
            pintaConductores();
        }else {
            pintaPasajeros();
        }

        //falta lanzar la peticion para aparecer como desconectado en la api
    }
    public void desconecta(){
        mgoogleMap.clear();
        conectado=0;


        Desconecta d = new Desconecta();
        Object[] objetos1 = new Object[2];
        objetos1[0] = Conexion.usuarioActivo.getId_usuario();
        objetos1[1] = conectado;
        d.execute(objetos1);


        //falta lanzar la peticion para aparecer como desconectado en la api


    }

    public void pintaUsuarios(double la,double lo,double ra,int con, int pas){
        if(conectado==1) {
        mgoogleMap.clear();
        //cambia tu situacion
            //  String POST_PARAMS = "olduser=" + objetos[0] + "&conectado=" + objetos[1] + "&conductor=" + objetos[2] ;
            DesconectarseCambiarPasajero d = new DesconectarseCambiarPasajero();
            Object[] objetos1 = new Object[2];
            objetos1[0] = Conexion.usuarioActivo.getId_usuario();
            objetos1[1] = conductor;
            d.execute(objetos1);



            ConseguirUsuariosPorRadio c = new ConseguirUsuariosPorRadio();
            Object[] objetos = new Object[6];
            objetos[0] = ra;      // radio   double
            objetos[1] = la;       // latitud  atributo clase principal  double
            objetos[2] = lo;     //longitud   atributo clase principal   double
            objetos[3] = con;             // conductor    int 1 conductor 0 pasajero
            objetos[4] = pas;             // conectado    int 1 conectado 0 desconectado
            objetos[5] = esteActivity;
            c.execute(objetos);}
        else {
            Toast.makeText(activity, "Estas desconectado, conectate para realizar esta acción", Toast.LENGTH_SHORT).show();
        }

    }

    public void insertMarca(double latitud, double longitud, String titulo, int id){
        LatLng marker2 = new LatLng(latitud, longitud);

        MarkerOptions markerMaps = new MarkerOptions()
                .position(marker2)
                .title(titulo)
                .snippet("id:"+id);
        if(conductor==0){

        markerMaps.icon(BitmapDescriptorFactory.fromResource(R.drawable.pasajero_android));
        }else {
         markerMaps.icon(BitmapDescriptorFactory.fromResource(R.drawable.coche));
        }




        mgoogleMap.addMarker(markerMaps);
        mgoogleMap.setOnMarkerClickListener(this);
    }



    //-----------------------------PURRIA---------------------------------------------------------
    @Override
    public void onConnected(@Nullable Bundle bundle) {
    getUbicacion();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    public boolean onMarkerClick(final Marker marker) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(marker.getSnippet()).setTitle(marker.getTitle()).setPositiveButton("ENVIAR MENSAJE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String idMarca = marker.getSnippet();
                idMarca = idMarca.replace("id:","");
                idMarcaPuntoMarcado = Integer.parseInt(idMarca);



                for(Conversacion conversacion: activity.getConversaciones()){

                    Log.d("prueba","recorriendo el array "+conversacion.getChat());

                    if(conversacion.getId1() == idMarcaPuntoMarcado || conversacion.getId2() == idMarcaPuntoMarcado){
                        chatRepetido = true;
                        chatUsado = conversacion.getChat();
                    }

                }

                Toast.makeText(context, "HAS DADO A ENVIAR MENSAJE", Toast.LENGTH_SHORT).show();


                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String mensajeFecha = ""+timestamp;
                String mensaje = mensajeFecha.replace(" ","");
                mensaje = mensaje.replace("-","");
                mensaje = mensaje.replace(":","");
                mensaje = mensaje.replace(".","");


              //  Log.d("prueba","datos de salida "+chatUsado);

                if(chatRepetido){

                    Bundle bundle = new Bundle();
                    bundle.putString("chat",chatUsado);
                    Log.d("prueba","chat usadoooooo:   "+chatUsado);
                    chatUsado = "";

                    Intent intent = new Intent(getContext(),Mensajes.class);
                    intent.putExtra("bundle",bundle);
                    getContext().startActivity(intent);

                    chatRepetido = false;

                }else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRefChat = database.getReference("chats");
                    myRefChat.child(mensaje+"chat").child("id1").setValue(Conexion.usuarioActivo.getId_usuario());
                    myRefChat.child(mensaje+"chat").child("id2").setValue(Integer.parseInt(idMarca));
                    myRefChat.child(mensaje+"chat").child("nombre1").setValue(Conexion.usuarioActivo.getNombre()+ " "+Conexion.usuarioActivo.getApellidos());
                    myRefChat.child(mensaje+"chat").child("nombre2").setValue(marker.getTitle());

                    //FirebaseDatabase database = FirebaseDatabase.getInstance();
                    /*
                    DatabaseReference myRefChat2 = database.getReference("Conversaciones");
                    myRefChat2.child(mensaje+"conversacion").child("id1").setValue(Integer.parseInt(idMarca));
                    myRefChat2.child(mensaje+"conversacion").child("id2").setValue(Conexion.usuarioActivo.getId_usuario());
                    myRefChat2.child(mensaje+"conversacion").child("chat").setValue(mensaje+"chat");
                    myRefChat2.child(mensaje+"conversacion").child("fecha").setValue(""+timestamp);
                    */

                    Firebase mFirebase;
                    mFirebase = new Firebase("https://flowaychatviajes.firebaseio.com").child("Conversaciones");

                    Conversacion conversacion = new Conversacion(Integer.parseInt(idMarca),Conexion.usuarioActivo.getId_usuario(),mensaje+"chat",""+timestamp);
                    mFirebase.push().setValue(conversacion);

                    Bundle bundle = new Bundle();
                    bundle.putString("chat",mensaje+"chat");

                    activity.addConversacion(new Conversacion(Conexion.usuarioActivo.getId_usuario(), idMarcaPuntoMarcado,mensaje+"chat",""+timestamp ));

                    Intent intent = new Intent(getContext(),Mensajes.class);
                    intent.putExtra("bundle",bundle);
                    getContext().startActivity(intent);

                }















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


}



