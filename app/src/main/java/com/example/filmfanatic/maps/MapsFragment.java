package com.example.filmfanatic.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.filmfanatic.MainActivity;
import com.example.filmfanatic.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//import android.location.LocationListener;
import com.google.android.gms.location.LocationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static GoogleMap TheMap;
    private GoogleApiClient client;
    public static final int REQUEST_LOCATION_CODE=99;
    private LocationRequest locationRequest;
    private Location LastLocation;
    private Marker CurrentLocationMarker;
    private double latitude;
    private double longitude;
    public static RecyclerView rvTheaters;
    public static Context mycontext;
    public static LatLng HomeLatLng;
    public static List<HashMap<String,String>> AllLocations;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            TheMap = googleMap;
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                buildGoogleAPIClient();
                TheMap.setMyLocationEnabled(true);
            }

            TheMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    UpdateMap(marker);
                    return true;
                }
            });
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTheaters=view.findViewById(R.id.rvTheaters);
        mycontext=this.getContext();

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            CheckLocationPermission();
        }


            SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    public synchronized void buildGoogleAPIClient(){
        client=new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        client.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        HomeLatLng=new LatLng(latitude,longitude);

        LastLocation=location;

        if (CurrentLocationMarker!=null)
            CurrentLocationMarker.remove();

        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();        //used to set colors,titles etc
        markerOptions.position(latLng);
        markerOptions.title("Your Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        Marker marker=TheMap.addMarker(markerOptions);
        marker.showInfoWindow();
        CurrentLocationMarker=marker;

        TheMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

        final Handler handler = new Handler();      //brief delay so user can see the marker label that indcates position
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                marker.hideInfoWindow();

            }
        }, 2000);



        if (client!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
            FindTheaters();

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                    //permission is granted
                    if(ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        if (client==null){
                            buildGoogleAPIClient();
                        }
                        TheMap.setMyLocationEnabled(true);
                    }
                    else{
                        Toast.makeText(this.getContext(),"Permission denied",Toast.LENGTH_LONG).show();

                    }
                    return;
                }
        }

    }

    public boolean CheckLocationPermission(){

        if (ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }

            else{
                ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            return false;
        }

        else
            return true;
    }

    public void FindTheaters(){
        String Latitude=String.valueOf(latitude);
        String Longitude=String.valueOf(longitude);
        Object dataTransfer[]=new Object[2];
        dataTransfer[0]=TheMap;

        String SearchString="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                +Latitude+","+Longitude+"&radius=50000&name=theater&key="+getResources().getString(R.string.google_maps_key);


        dataTransfer[1]=SearchString;
        LocalMovieTheaters object=new LocalMovieTheaters();
        object.execute(dataTransfer);


    }

    public static void BindAdapter(List<HashMap<String,String>> AllPlaces){
        TheaterAdapter theaterAdapter=new TheaterAdapter(mycontext,AllPlaces);
        rvTheaters.setAdapter(theaterAdapter);
        rvTheaters.setLayoutManager(new LinearLayoutManager(mycontext));
        theaterAdapter.notifyDataSetChanged();

        AllLocations=AllPlaces;
    }

    public void UpdateMap(Marker markerr){
        TheMap.clear();
        LatLng CurrentLatLng=markerr.getPosition();

        for (int i=0;i<AllLocations.size();i++){

            LatLng latLng=new LatLng(Double.parseDouble(AllLocations.get(i).get("lat")), Double.parseDouble(AllLocations.get(i).get("lng")));

            if (latLng.equals(CurrentLatLng)){
                Marker marker = MapsFragment.TheMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(AllLocations.get(i).get("place_name").toString() )
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                marker.showInfoWindow();

            }

            else{
                Marker marker = MapsFragment.TheMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(AllLocations.get(i).get("place_name").toString() )
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            }
        }

        Marker marker = MapsFragment.TheMap.addMarker(new MarkerOptions()
                .position(MapsFragment.HomeLatLng)
                .title(("Your Location"))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        if (CurrentLatLng.equals(HomeLatLng))
            marker.showInfoWindow();

        MapsFragment.TheMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CurrentLatLng,11));


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


}