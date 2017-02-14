package paisdeyann.floway.FragmentsTabs;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;


import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.Objetos.Usuario;
import paisdeyann.floway.R;
import paisdeyann.floway.Threads.ConseguirUsuariosPorRadio;

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

    MapViewFragment esteActivity;
    LocationManager locationManager;
    Menu_Principal activity;

    double radio = 10;
    int conductor = 1;
    int conectado = 1;

    //Listener de la ubicacion
    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            tuLatitud = location.getLatitude();
            tuLongitud = location.getLongitude();
            zoom();
            pintaUsuarios(tuLatitud,tuLongitud,radio,conductor,conectado);


            Log.v("syso","latitud y longitud "+tuLatitud+" , "+tuLongitud);
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

        Button reload = (Button) rootView.findViewById(R.id.button5);

        reload.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              zoom();
              pintaUsuarios(tuLatitud,tuLongitud,radio,conductor,conectado);
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
                addInfo();
                getUbicacion();
                setMarkersPasajeros();
                zoom();
                pintaUsuarios(tuLatitud,tuLongitud,radio,conductor,conectado);


            }
        });




        return rootView;
    }


    public void setContext(Context con, Menu_Principal me){
        context = con;
        activity = me;
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
    public void setMarkersPasajeros(){

        if(!isLocationEnabled()) {
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
        }
        //mgoogleMap.clear();

    }

    public void zoom(){
        // For dropping a marker at a point on the Map
        LatLng marker = new LatLng(this.tuLatitud, this.tuLongitud);


        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
        mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void pintaUsuarios(double la,double lo,double ra,int con, int pas){
        mgoogleMap.clear();
        ConseguirUsuariosPorRadio c = new ConseguirUsuariosPorRadio();
        Object[] objetos = new Object[6];
        objetos[0] = ra;      // radio   double
        objetos[1] = la;       // latitud  atributo clase principal  double
        objetos[2] = lo;     //longitud   atributo clase principal   double
        objetos[3] = con;             // conductor    int 1 conductor 0 pasajero
        objetos[4] = pas;             // conectado    int 1 conectado 0 desconectado
        objetos[5] = esteActivity;
        c.execute(objetos);

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

    public void addInfo(){
        final LayoutInflater inflater = (LayoutInflater) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

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


}



