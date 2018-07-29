package com.nayem.tourguide.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nayem.tourguide.R;
import com.nayem.tourguide.api.NearbyApi;
import com.nayem.tourguide.gpsService.GPSTracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapDirectionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleMapOptions options;
    private double currentLat = 0.0, currentLng = 0.0;
    private GPSTracker gps;
    private double desLat, desLng;
    String destination, currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_direction);

        desLat = getIntent().getDoubleExtra("lat",0.0);
        desLng = getIntent().getDoubleExtra("lng",0.0);

        gps = new GPSTracker(MapDirectionActivity.this);
        // Obtain the SupportMapFragment and getnotified when the map is ready to be used.

        options = new GoogleMapOptions();
        options.zoomControlsEnabled(true).compassEnabled(true);
        SupportMapFragment fragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment);
        transaction.commit();
     /*   SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);*/
        fragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(gps.canGetLocation()){

            currentLat = gps.getLatitude();
            currentLng = gps.getLongitude();

            // \n is for new line
            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LatLng myLocation = new LatLng(currentLat, currentLng);
        mMap.addMarker(new MarkerOptions().position(myLocation).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,17));

        currentLocation = String.format("%s,%s", currentLat,currentLng);
        destination = String.format("%s,%s", desLat,desLng);


        getDirection();
    }

    private void getDirection() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NearbyApi nearbyApi = retrofit.create(NearbyApi.class);

        String url = String.format("json?origin=%s&destination=%s&key=AIzaSyD3x85RvPG_ON1tJ2RdvM8qJikMj4whg3w",currentLocation, destination);

        Call<DirectionResponse> directionResponseCall = nearbyApi.getDirection(url);

        directionResponseCall.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {

                if (response.code()==200){
                    DirectionResponse directionResponse = response.body();
                    if (directionResponse!=null){
                        List<DirectionResponse.Step> steps = directionResponse.getRoutes().get(0).getLegs().get(0).getSteps();

                        for (int i=0; i<steps.size(); i++){
                            double sLat = steps.get(i).getStartLocation().getLat();
                            double sLng = steps.get(i).getStartLocation().getLng();
                            double eLat = steps.get(i).getEndLocation().getLat();
                            double eLng = steps.get(i).getEndLocation().getLng();

                            LatLng sLatLng = new LatLng(sLat, sLng);
                            LatLng eLatLng = new LatLng(eLat, eLng);

                            Polyline polyline = mMap.addPolyline(new PolylineOptions().add(sLatLng).add(eLatLng));
                            polyline.setColor(Color.BLUE);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {

            }
        });

    }
}
