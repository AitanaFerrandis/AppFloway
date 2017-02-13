package paisdeyann.floway.FragmentsTabs;

import android.app.DownloadManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import paisdeyann.floway.Manifest;
import paisdeyann.floway.Menu_Principal;
import paisdeyann.floway.Objetos.Usuario;
import paisdeyann.floway.R;

import static com.google.android.gms.R.id.url;
import static paisdeyann.floway.R.id.container;

/**
 * Created by Dario on 11/02/2017.
 */

public class MapViewFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    MapView mMapView;
    private GoogleMap mgoogleMap;
    ArrayList<Usuario>usuarios;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

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

    public void getUsers(){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("url")
                    .build();
            Response responses = null;

            try {
                responses = client.newCall(request).execute();

                String jsonData = responses.body().string();
                JSONObject Jobject = new JSONObject(jsonData);
                JSONArray Jarray = Jobject.getJSONArray("employees");

                for (int i = 0; i < Jarray.length(); i++) {
                    JSONObject object = Jarray.getJSONObject(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMarkersPasajeros(){
        mgoogleMap.clear();
        addInfo();
        // For dropping a marker at a point on the Map
        LatLng marker = new LatLng(-34, 151);
        mgoogleMap.addMarker(new MarkerOptions()
                .position(marker)
                .title("Marker Title")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.android_pasajero))
                .snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
        mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

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
}



