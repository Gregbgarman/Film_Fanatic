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
    GoogleApiClient client;
    public static final int REQUEST_LOCATION_CODE=99;
    LocationRequest locationRequest;
    Location LastLocation;
    Marker CurrentLocationMarker;
    double latitude;
    double longitude;
    public static RecyclerView rvTheaters;
    public static Context mycontext;
    public static LatLng HomeLatLng;


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
                   marker.showInfoWindow();
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


    public synchronized void buildGoogleAPIClient(){         //doubt this will work
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
        markerOptions.title("Your Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        CurrentLocationMarker=TheMap.addMarker(markerOptions);

        TheMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

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
       // String SearchString="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
        //        +Latitude+","+Longitude+"&radius=30000&name=theater&keyword=Regal&keyword=CMX&keyword=" +
         //       "Cineplex&keyword=IMAX&keyword=Cinemark&key=AIzaSyDMg3b4aByb4RcbZM8a0siyx0z31IHjDT8";

        String SearchString="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                +Latitude+","+Longitude+"&radius=30000&name=theater&key=AIzaSyDMg3b4aByb4RcbZM8a0siyx0z31IHjDT8";


        dataTransfer[1]=SearchString;
        LocalMovieTheaters object=new LocalMovieTheaters();
        object.execute(dataTransfer);

        //String SearchString="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=30.192532,-81.564995&radius=30000&name=theater&keyword=Regal&keyword=CMX&keyword=Cineplex&keyword=IMAX&keyword=Cinemark&key=AIzaSyDMg3b4aByb4RcbZM8a0siyx0z31IHjDT8";
      //  https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=30.192532,-81.564995&radius=30000&name=theater&keyword=Regal&keyword=CMX&keyword=Cineplex&keyword=IMAX&keyword=Cinemark&key=AIzaSyDMg3b4aByb4RcbZM8a0siyx0z31IHjDT8";

    }

    public static void BindAdapter(List<HashMap<String,String>> AllPlaces){                 //idk if another context will work
        TheaterAdapter theaterAdapter=new TheaterAdapter(mycontext,AllPlaces);
        rvTheaters.setAdapter(theaterAdapter);
        rvTheaters.setLayoutManager(new LinearLayoutManager(mycontext));
        theaterAdapter.notifyDataSetChanged();

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